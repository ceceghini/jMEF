package it.pointec.mef;

/**
 * Attenzione, in questo java si effettuano operazioni su file del sistema operativo
 * L'utente oracle deve avere le autorizzazioni corrette (lettura e cancellazione di file e cartelle)
 * Inoltre l'utente che esegue la storeprocedure java deve avere il seguente grant:
 * 		GRANT JAVASYSPRIV TO user;
 */


import java.io.*;
import java.util.zip.ZipEntry;
//import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

//import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.*;

/**
 * @author cesare
 *
 */
public class MefFile {
	
	private String _start_dir;
	private String _dest_dir;
	private String _extension;
	
	private String _data;
	private String _azienda;
	
	private FilenameFilter fileFilter;
	private FilenameFilter dirFilter;
	
	private MefOracle mo;
	
	private String _fileName;
	
	//private int _id_file;
	
	//DOMParser parser = new DOMParser();
	DocumentBuilderFactory dbf;
	DocumentBuilder db;
	
	public MefFile(String start_dir, String dest_dir) throws Exception {
		
		// Connessione a oracle
		this.mo = new MefOracle();
		
		this._start_dir = start_dir;
		this._dest_dir = dest_dir;
		
		File d = new File(this._dest_dir);
		if (!d.exists())
			mo.LogAppException("La directory di destinazione ["+this._dest_dir+"] non esiste.");
		
		// Parser java
		dbf = DocumentBuilderFactory.newInstance();
		db = dbf.newDocumentBuilder();
		
		//mo.TruncateXmlSa();
		//mo.TruncateCsvSa();
		
	}
	
	public void EffettuaElaborazione (String ext) throws Exception {
		
		this._extension = "."+ext;	// Estensione
		
		// Filtro utilizzato per estrarre tutte le directory
		dirFilter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return new File(dir, name).isDirectory();
			}
		};
		
		// Filtro utilizzato per estrarre i file sulla base dell'estensione
		fileFilter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				name = name.toLowerCase();
				return name.endsWith(_extension);
			}
		};
			
		try {
			
			mo.InfoLog("Elaborazione start directory. " + _start_dir);
			
			//	Riferimento alla cartella da elaborare
			File dir = new File(_start_dir);
			File subDir;
			
			String[] children = dir.list(dirFilter);	// Lista delle sottodirectory
						
			if (children != null) {
				// Loop fra le sottodirectory radici
				for (int i=0; i<children.length; i++) {
					// Riferimento alla sottodirectory
					mo.InfoLog("Elaborazione directory. " + children[i]);
					
					subDir = new File(dir, children[i]);
					
					// Reperisco azienda e data
					_data = children[i].substring(0, 8);

					
					_azienda = "";
					
					if (children[i].length()>=15) {
						if (children[i].toLowerCase().substring(9, 15).equals("infotn"))
							_azienda = "infotn";
					}
					
					if (children[i].length()>=12) {
						if (children[i].toLowerCase().substring(9, 12).equals("gpi"))
							_azienda = "gpi";
					}
					
					if (_azienda == "") {
						mo.LogAppException("Sottodirectory ["+children[i]+"] non valid. Il formato deve essere yyyymmdd_AZIENDA.");
						return;
					}
					else {
						// Elaborazione della sottodirecotry
						loadFileFromDir(subDir);
						
						// Eliminazione delle sottodirectory
						verifyDirectory(subDir);
					}
						
				}	
			}
		}
		catch (Exception e) {
						
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			
			throw new Exception(e.getMessage() + " ### " + _fileName + " ### " + sw.toString());
		}
		
	}
	
	/**
	 * Legge il contenuto di una directory e ne copia i file in uno streem xml
	 * 
	 * @param dir
	 */
	private void loadFileFromDir(File dir) throws Exception {
		
		File 		curDir;
		
		String[] children = dir.list(dirFilter);	// Lista delle sottodirectory
		if (children == null)
			return;	// Se non vi sono elementi nella directory si esce
		
		// Loop fra tutti gli elementi della directory
		for (int i=0; i<children.length; i++) {
			// Recupero la sottodirectory
			mo.InfoLog("Elaborazione directory. " + children[i]);
			curDir = new File(dir, children[i]);
			// Elaborazione della sottodirectory
			loadFileFromDir(curDir);
			
			// Verifico la cartella per eliminarla se vuota
			verifyDirectory(curDir);
		}
		
		children = dir.list(fileFilter);	// Lista dei file .csv o .xml
		
		if (children == null)
			return;	// Se non vi sono elementi nella directory si esce
		
		//	Loop fra tutti gli elementi della directory
		for (int i=0; i<children.length; i++) {
			// Recupero il file
			mo.InfoLog("Elaborazione file. " + children[i]);
			curDir = new File(dir, children[i]);
			
			_fileName = curDir.getAbsolutePath();
			
			// Elaborazione dei file
			if (this._extension.equals(".xml"))
				loadXMLFile(curDir);
			
			if (this._extension.equals(".pdf"))
				loadPDFFile();
				
			if (this._extension.equals(".csv"))
				loadCVSFile(curDir);
			
			// Compressione e cancellazione del file
			//if (_id_file>0)
			MoveFile(curDir);
			
		}
		
	} 
		
	/**
	 * Compressione del file nella cartella di archivio e eliminazione
	 * el file origine
	 * @param f	File da spostare
	 * @throws Exception
	 */
	private void MoveFile(File f) throws Exception {
		
		mo.InfoLog("Spostamento file: " + f.getAbsolutePath());
		
		System.gc();
		
		//	Directory padre
		File dirParent = f.getParentFile();
		String parentPath = dirParent.getAbsolutePath();
		
		mo.InfoLog("Directory padre: "+ parentPath);
		
		// Nome della directory di destinazione
		String destDirPath = _dest_dir + "/" + parentPath.substring(_start_dir.length()+1, parentPath.length());
		File destDir = new File(destDirPath);
		
		mo.InfoLog("Directory di destinazione: "+ destDir.getAbsolutePath());
		
		// Se la directory non esiste la creiamo
		if (!destDir.exists()) {
			mo.InfoLog("La directory di destinazione non esiste. Verrà creata.");
			destDir.mkdirs();
		}
		
		// Nome del file di destinazione
		String zip = destDir.getAbsolutePath() + "/" + f.getName() + ".zip";
		
		mo.InfoLog("Creazione zip: " + zip);
		
		// definiamo l'output previsto che sarà un file in formato zip
		ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zip)));
		
		//	definiamo il buffer per lo stream di bytes 
	    byte[] data = new byte[1000]; 
		
	    // indichiamo il nome del file che subirà la compressione
	    BufferedInputStream in = new BufferedInputStream(new FileInputStream(f.getAbsolutePath()));
	    int count;

	    // processo di compressione
	    out.putNextEntry(new ZipEntry(f.getName()));
	    while((count = in.read(data,0,1000)) != -1) {
	    	out.write(data, 0, count);
	    }
	    in.close();
	    out.flush();
	    out.close();
	    
	    // Verifica file di destinazione, se esiste si cancella
	    mo.InfoLog("Verifica esistenza file zip.");
	    File fDest = new File (zip);
	    if (fDest.exists()) {
	    	mo.InfoLog("Il file zip esiste. Si cancella il file originale." + f.canWrite());
	    	if (!f.delete())
	    		mo.InfoLog("File [" + f.getAbsolutePath() + "] non cancellato.");
	    }
		
	}
	
	/**
	 * Verifica se esistono elementi nella directory. Se non esiste niente la cancella
	 * @param dir
	 */
	private void verifyDirectory(File dir)  throws Exception {
		
		if (dir.list().length==0)
			if(!dir.delete())
				mo.LogAppException("La directory [" + dir.getAbsolutePath() + "] non è stata cancellata.");
		
	}
	
	/**
	 * Elabora il file csv e ne legge tutte le righe.
	 * @param file
	 * @throws Exception
	 */
	private void loadCVSFile(File file) throws Exception {
		
		// Lettura del file csv
		FileReader fr = new FileReader(file);
		BufferedReader input = new BufferedReader(fr);
		String line;
		
		// Prima lettura a vuoto.. corrisponde all'intestazione
		input.readLine();
		
		// Seconda lettura... corrisponde alla prima riga, solamente se c'è almeno una riga
		// si procede con la sua elaborazione
		if (( line = input.readLine()) != null) {
			// Inizializzazione del file
			
			// Elaborazione della prima riga di contenuto
			ElaboraRigaCSV(line);
			
			// Loop fra tutte le altre righe ed inserimento in sa
			while (( line = input.readLine()) != null){
				
				ElaboraRigaCSV(line);
				
			}
			
		}
		
		fr.close();
		
	}
	
	/**
	 * Elaborazione di una riga del file .csv
	 * @param linea
	 * @throws Exception
	 */
	private void ElaboraRigaCSV(String linea) throws Exception {
	
		// Recupero i vari campi della riga
		String[] aLine = MefUtil.Split(linea,";");
				
		mo.SetCsvProcParm(1, getValueCsv(aLine[5]));
		mo.SetCsvProcParm(2, getValueCsv(aLine[0]));
		mo.SetCsvProcParm(3, getValueCsv(aLine[1]));
		mo.SetCsvProcParm(4, this._fileName);
		
		mo.InsertCsvRow();
		
	}
	
	/**
	 * Parsing di un valore presente in una riga csv
	 * @param s
	 * @return
	 */
	private String getValueCsv(String s) {
		
		char c1 = '\'';
		char c2 = '\\';
		char c3 = '\"';
		
		String ret;
		ret = MefUtil.removeChar(s, c1);
		ret = MefUtil.removeChar(ret, c2);
		ret = MefUtil.removeChar(ret, c3);
		
		return ret;
		
	}
	
	/**
	 * Elaborazione fil pdf
	 * @param file
	 * @throws Exception
	 */
	private void loadPDFFile() throws Exception {
		
		mo.InfoLog("Inizio elaborazione file pdf. ["+this._fileName+"]");
		
		MefPdf mpdf = new MefPdf(this._fileName);
		
		mo.SetPdfProcParm(1, mpdf.getProtocollo());
		mo.SetPdfProcParm(2, mpdf.getNomessa());
		mo.SetPdfProcParm(3, this._fileName);
		
		mo.InsertPdfRow();
		
	}
	
	/**
	 * Elabora un file xml
	 * @param file
	 * @throws Exception
	 */
	private void loadXMLFile(File file) throws Exception {
		
		try {
			
			mo.InfoLog("Verifica se il file è già stato elaborato.");
			
			// Parsing del documento
			Document doc = db.parse(file);
			
			Element nRicette = (Element) doc.getFirstChild();
			
			mo.InfoLog("Creazione elemento testata");
			
			MefTestata t = new MefTestata((Element)nRicette.getElementsByTagName("Testata").item(0), _data, _azienda);
			
			NodeList nlRicetta = nRicette.getElementsByTagName("Ricetta");
			
			mo.InfoLog("Creazione elementi ricette");
			
			for (int i=0;i<nlRicetta.getLength();i++) {
				
				t.getRicette().add(new MefRicetta((Element)nlRicetta.item(i), i+1));
				
			}
			
			// Elaborazione del file ed inserimento dei dati in testata
			elaboraFile(t);
			
			t.getRicette().clear();
			
			mo.InfoLog("Elaborazione dei dati in sa.");
			
			//mo.elaboraXML();

		}
		catch (Exception e) {
			
			mo.LogAppException("Errore nell'elaborazione del file: [" + file.getAbsolutePath() + "] ["+ MefFile.getStackTrace(e) +"]");
			
			throw e;
		}
		
		
	}
	
	private void elaboraFile(MefTestata t) throws Exception {
		
		mo.InfoLog("Inserimento del file in sa.");
		
		MefRicetta r;
		MefPrestazione p;
		
		//mo.TruncateXmlSa();
				
		// Loop fra le ricette
		for(int i=0;i<t.getRicette().size();i++) {
			
			// Recupero la ricetta
			r = (MefRicetta) t.getRicette().get(i);
			
			// Loop fra le prestazioni
			for (int j=0;j<r.getPrestazioni().size();j++) {
				
				p = (MefPrestazione) r.getPrestazioni().get(j);
				
				mo.SetXmlProcParm(1, t.getAzienda());
				mo.SetXmlProcParm(2, t.getDataInvioFile());
				mo.SetXmlProcParm(3, t.getRegStruttura());
				mo.SetXmlProcParm(4, t.getCodAsl());
				mo.SetXmlProcParm(5, t.getCodStruttura());
				mo.SetXmlProcParm(6, t.getTotRic());
				mo.SetXmlProcParm(7, t.getTotPrest());
				mo.SetXmlProcParm(8, t.getTotImpCaricoAss());
				mo.SetXmlProcParm(9, t.getTotValRicInviate());
				mo.SetXmlProcParm(10, t.getTotImpCaricoSSN());
				mo.SetXmlProcParm(11, t.getTotRicNuove());
				mo.SetXmlProcParm(12, t.getTotRicVariaz());
				mo.SetXmlProcParm(13, t.getTotRicCanc());
				mo.SetXmlProcParm(14, t.getTotStrutture());
				mo.SetXmlProcParm(54, t.getAnnoMeseNoInvio());
		
				mo.SetXmlProcParm(15, r.getFlagOperazione());
				mo.SetXmlProcParm(16, r.getRegStrutturaRic());
				mo.SetXmlProcParm(17, r.getCodAslRic());
				mo.SetXmlProcParm(18, r.getCodStrutturaRic());
				mo.SetXmlProcParm(19, r.getCodRegione());
				mo.SetXmlProcParm(20, r.getAnnoProduzione());
				mo.SetXmlProcParm(21, r.getProgRicettaRicettario());
				mo.SetXmlProcParm(22, r.getCheckDigit());
				mo.SetXmlProcParm(23, r.getCodiceAss());
				mo.SetXmlProcParm(24, r.getProgRicettaStruttura());
				mo.SetXmlProcParm(25, r.getSiglaProvincia());
				mo.SetXmlProcParm(26, r.getASLAssistito());
				mo.SetXmlProcParm(27, r.getStatoEstero());
				mo.SetXmlProcParm(28, r.getIstituzCompetente());
				mo.SetXmlProcParm(29, r.getNumIdentPers());
				mo.SetXmlProcParm(30, r.getNumIdentTess());
				mo.SetXmlProcParm(31, r.getSuggerita());
				mo.SetXmlProcParm(32, r.getAltro());
				mo.SetXmlProcParm(33, r.getDataCompilazione());
				mo.SetXmlProcParm(34, r.getDataSpedizione());
				mo.SetXmlProcParm(35, r.getTipoRic());
				mo.SetXmlProcParm(36, r.getCodiceDiagnosi());
				//mo.SetXmlProcParm(37, r.getTipoEsenz());
				mo.SetXmlProcParm(37, r.getCodEsenzione());
				mo.SetXmlProcParm(38, r.getCodRaggrup());
				mo.SetXmlProcParm(39, r.getTotPrestazioni());
				mo.SetXmlProcParm(40, r.getTotValoreRicetta());
				mo.SetXmlProcParm(41, r.getFranchigiaCaricoAss());
				mo.SetXmlProcParm(42, r.getQuotaCaricoAss());
				mo.SetXmlProcParm(43, r.getImpCaricoSSN());
				mo.SetXmlProcParm(55, r.getDispReg());
				mo.SetXmlProcParm(56, r.getAltroRic());
				mo.SetXmlProcParm(57, r.getTipoAccesso());
				mo.SetXmlProcParm(58, r.getGaranziaTempiMassimi());
				mo.SetXmlProcParm(59, r.getAnnoMeseFatt());
				mo.SetXmlProcParm(60, r.getNonEsente());
				mo.SetXmlProcParm(61, r.getReddito());
				mo.SetXmlProcParm(62, r.getClassePriorita());
				mo.SetXmlProcParm(63, r.getTipoErogazione());
				mo.SetXmlProcParm(64, r.getDataNascitaEstero());
				mo.SetXmlProcParm(65, r.getDataScadTessera());
			
				mo.SetXmlProcParm(44, p.getCodicePresidio());
				mo.SetXmlProcParm(45, p.getCodicePrest());
				mo.SetXmlProcParm(46, p.getCodReparto());
				mo.SetXmlProcParm(47, p.getBrancaPrestazione());
				mo.SetXmlProcParm(48, p.getDataPrenotazione());
				mo.SetXmlProcParm(49, p.getDataErogInizio());
				mo.SetXmlProcParm(50, p.getDataErogFine());
				mo.SetXmlProcParm(51, p.getTipologiaPrestazione());
				mo.SetXmlProcParm(52, p.getQtaPrest());
				mo.SetXmlProcParm(53, p.getTariffaPrest());
				mo.SetXmlProcParm(66, p.getDataErogazione());
				mo.SetXmlProcParm(67, p.getTariffaPrestLab());
				
				mo.SetXmlProcParm(68, r.getIdRicetta());	// Valorizzazione del progressivo ricetta
				
				mo.SetXmlProcParm(69, this._fileName);
				
				mo.InsertXmlRow();
				
			}
			
			r.getPrestazioni().clear();
			
		}
		
	}
	
	/**
	 * Elaborazione dei file xml
	 * @param start_dir
	 * @throws Exception
	 */
	public static void elabora(String start_dir, String dest_dir) throws Exception {

		MefFile mf = new MefFile(start_dir, dest_dir);
		mf.EffettuaElaborazione("xml");
		mf.EffettuaElaborazione("csv");
		mf.EffettuaElaborazione("pdf");
	}
	
	private static String getStackTrace(Throwable throwable) {
	    Writer writer = new StringWriter();
	    PrintWriter printWriter = new PrintWriter(writer);
	    throwable.printStackTrace(printWriter);
	    return writer.toString();
	}

}
