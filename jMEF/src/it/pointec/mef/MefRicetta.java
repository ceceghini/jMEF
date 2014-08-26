package it.pointec.mef;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class MefRicetta {

	private String flagOperazione;
	private String regStrutturaRic;
	private String codAslRic;
	private String codStrutturaRic;
	private String codRegione;
	private String annoProduzione;
	private String progRicettaRicettario;
	private String checkDigit;
	private String codiceAss;
	private String progRicettaStruttura;
	private String siglaProvincia;
	private String aSLAssistito;
	private String statoEstero;
	private String istituzCompetente;
	private String numIdentPers;
	private String numIdentTess;
	private String suggerita;
	private String altro;
	private Date dataCompilazione;
	private Date dataSpedizione;
	private String tipoRic;
	private String codiceDiagnosi;
	//private String tipoEsenz;
	private String codEsenzione;
	private String codRaggrup;
	private int totPrestazioni;
	private double totValoreRicetta;
	private double franchigiaCaricoAss;
	private double quotaCaricoAss;
	private double impCaricoSSN;
	
	private String dispReg;
	private String altroRic;
	private String tipoAccesso;
	private String garanziaTempiMassimi;
	private String annoMeseFatt;
	private String nonEsente;
	private String reddito;
	private String classePriorita;
	private String tipoErogazione;
	private Date dataNascitaEstero;
	private Date dataScadTessera;
	
	private double idRicetta;
	
	
	private List<MefPrestazione> prestazioni;
	
	public MefRicetta(Element e, double id) {
		
		this.idRicetta = id;
		
		this.flagOperazione 		= MefUtil.getNodeValue(e, "FlagOperazione");
		this.regStrutturaRic 		= MefUtil.getNodeValue(e, "RegStrutturaRic");
		this.codAslRic 				= MefUtil.getNodeValue(e, "CodAslRic");
		this.codStrutturaRic 		= MefUtil.getNodeValue(e, "CodStrutturaRic");
		this.codRegione 			= MefUtil.getNodeValue(e, "CodRegione");
		this.annoProduzione 		= MefUtil.getNodeValue(e, "AnnoProduzione");
		this.progRicettaRicettario 	= MefUtil.getNodeValue(e, "ProgRicettaRicettario");
		this.checkDigit 			= MefUtil.getNodeValue(e, "CheckDigit");
		this.codiceAss 				= MefUtil.getNodeValue(e, "CodiceAss");
		this.progRicettaStruttura 	= MefUtil.getNodeValue(e, "ProgRicettaStruttura");
		this.siglaProvincia 		= MefUtil.getNodeValue(e, "SiglaProvincia");
		this.aSLAssistito 			= MefUtil.getNodeValue(e, "ASLAssistito");
		this.statoEstero 			= MefUtil.getNodeValue(e, "StatoEstero");
		this.istituzCompetente 		= MefUtil.getNodeValue(e, "IstituzCompetente");
		this.numIdentPers 			= MefUtil.getNodeValue(e, "NumIdentPers");
		this.numIdentTess 			= MefUtil.getNodeValue(e, "NumIdentTess");
		this.suggerita 				= MefUtil.getNodeValue(e, "Suggerita");
		this.altro 					= MefUtil.getNodeValue(e, "Altro");
		this.dataCompilazione 		= MefUtil.StringToDate(MefUtil.getNodeValue(e, "DataCompilazione"), "yyyy-MM-dd");
		this.dataSpedizione 		= MefUtil.StringToDate(MefUtil.getNodeValue(e, "DataSpedizione"), "yyyy-MM-dd");
		this.tipoRic 				= MefUtil.getNodeValue(e, "TipoRic");
		this.codiceDiagnosi 		= MefUtil.getNodeValue(e, "CodiceDiagnosi");
		//this.tipoEsenz 				= MefUtil.getNodeValue(e, "TipoEsenz");
		this.codEsenzione 			= MefUtil.getNodeValue(e, "CodEsenzione");
		this.codRaggrup 			= MefUtil.getNodeValue(e, "CodRaggrup");
		this.totPrestazioni 		= Integer.parseInt(MefUtil.getNodeValue(e, "TotPrestazioni"));
		this.totValoreRicetta 		= MefUtil.StringToDouble(MefUtil.getNodeValue(e, "TotValoreRicetta"));
		this.franchigiaCaricoAss 	= MefUtil.StringToDouble(MefUtil.getNodeValue(e, "FranchigiaCaricoAss"));
		this.quotaCaricoAss 		= MefUtil.StringToDouble(MefUtil.getNodeValue(e, "QuotaCaricoAss"));
		this.impCaricoSSN 			= MefUtil.StringToDouble(MefUtil.getNodeValue(e, "ImpCaricoSSN"));
		
		//	Nuovo tracciato
		this.dispReg				= MefUtil.getNodeValue(e, "DispReg");
		this.altroRic				= MefUtil.getNodeValue(e, "AltroRic");
		this.tipoAccesso			= MefUtil.getNodeValue(e, "TipoAccesso");
		this.garanziaTempiMassimi	= MefUtil.getNodeValue(e, "GaranziaTempiMassimi");
		this.annoMeseFatt			= MefUtil.getNodeValue(e, "AnnoMeseFatt");
		this.nonEsente				= MefUtil.getNodeValue(e, "NonEsente");
		this.reddito				= MefUtil.getNodeValue(e, "Reddito");
		this.classePriorita			= MefUtil.getNodeValue(e, "ClassePriorita");
		this.tipoErogazione			= MefUtil.getNodeValue(e, "TipoErogazione");
		this.dataNascitaEstero		= MefUtil.StringToDate(MefUtil.getNodeValue(e, "DataNascitaEstero"), "yyyy-MM-dd");
		this.dataScadTessera		= MefUtil.StringToDate(MefUtil.getNodeValue(e, "DataScadTessera"), "yyyy-MM-dd");
		
		
		// Elaborazione dei sottonodi prestazione
		prestazioni = new ArrayList<MefPrestazione>();
		NodeList nlPrestazione = e.getElementsByTagName("Prestazione");
		
		for (int i=0;i<nlPrestazione.getLength();i++) {
			
			prestazioni.add(new MefPrestazione((Element)nlPrestazione.item(i)));
			
		}
		
	}

	public List<MefPrestazione> getPrestazioni() {
		return prestazioni;
	}
	
	public double getIdRicetta() {
		return idRicetta;
	}

	public String getAltro() {
		return altro;
	}

	public String getAnnoProduzione() {
		return annoProduzione;
	}

	public String getASLAssistito() {
		return aSLAssistito;
	}

	public String getCheckDigit() {
		return checkDigit;
	}

	public String getCodAslRic() {
		return codAslRic;
	}

	public String getCodEsenzione() {
		return codEsenzione;
	}

	public String getCodiceAss() {
		return codiceAss;
	}

	public String getCodiceDiagnosi() {
		return codiceDiagnosi;
	}

	public String getCodRaggrup() {
		return codRaggrup;
	}

	public String getCodRegione() {
		return codRegione;
	}

	public String getCodStrutturaRic() {
		return codStrutturaRic;
	}

	public Date getDataCompilazione() {
		return dataCompilazione;
	}

	public Date getDataSpedizione() {
		return dataSpedizione;
	}

	public String getFlagOperazione() {
		return flagOperazione;
	}

	public double getFranchigiaCaricoAss() {
		return franchigiaCaricoAss;
	}

	public double getImpCaricoSSN() {
		return impCaricoSSN;
	}

	public String getIstituzCompetente() {
		return istituzCompetente;
	}

	public String getNumIdentPers() {
		return numIdentPers;
	}

	public String getNumIdentTess() {
		return numIdentTess;
	}

	public String getProgRicettaRicettario() {
		return progRicettaRicettario;
	}

	public String getProgRicettaStruttura() {
		return progRicettaStruttura;
	}

	public double getQuotaCaricoAss() {
		return quotaCaricoAss;
	}

	public String getRegStrutturaRic() {
		return regStrutturaRic;
	}

	public String getSiglaProvincia() {
		return siglaProvincia;
	}

	public String getStatoEstero() {
		return statoEstero;
	}

	public String getSuggerita() {
		return suggerita;
	}

	/*public String getTipoEsenz() {
		return tipoEsenz;
	}*/

	public String getTipoRic() {
		return tipoRic;
	}

	public int getTotPrestazioni() {
		return totPrestazioni;
	}

	public double getTotValoreRicetta() {
		return totValoreRicetta;
	}

	public String getAltroRic() {
		return altroRic;
	}

	public String getAnnoMeseFatt() {
		return annoMeseFatt;
	}

	public String getClassePriorita() {
		return classePriorita;
	}

	public Date getDataNascitaEstero() {
		return dataNascitaEstero;
	}

	public Date getDataScadTessera() {
		return dataScadTessera;
	}

	public String getDispReg() {
		return dispReg;
	}

	public String getGaranziaTempiMassimi() {
		return garanziaTempiMassimi;
	}

	public String getNonEsente() {
		return nonEsente;
	}

	public String getReddito() {
		return reddito;
	}

	public String getTipoAccesso() {
		return tipoAccesso;
	}

	public String getTipoErogazione() {
		return tipoErogazione;
	}
		
}
