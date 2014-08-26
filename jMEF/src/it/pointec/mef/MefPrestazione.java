package it.pointec.mef;

//import java.text.ParseException;
//import java.text.SimpleDateFormat;
import java.util.Date;
import org.w3c.dom.Element;

public class MefPrestazione {
	
	private String codicePresidio;
	private String codicePrest;
	private String codReparto;
	private String brancaPrestazione;
	private Date dataPrenotazione;
	private Date dataErogInizio;
	private Date dataErogFine;
	private String tipologiaPrestazione;
	private int qtaPrest;
	private double tariffaPrest;
	
	private Date dataErogazione;
	private double tariffaPrestLab;
	
	public MefPrestazione (Element e) {
		
		this.codicePresidio 		= MefUtil.getNodeValue(e, "CodicePresidio");
		this.codicePrest 			= MefUtil.getNodeValue(e, "CodicePrest");
		this.codReparto 			= MefUtil.getNodeValue(e, "CodReparto");
		this.brancaPrestazione 		= MefUtil.getNodeValue(e, "BrancaPrestazione");
		this.dataPrenotazione 		= MefUtil.StringToDate(MefUtil.getNodeValue(e, "DataPrenotazione"), "yyyy-MM-dd");
		this.dataErogInizio 		= MefUtil.StringToDate(MefUtil.getNodeValue(e, "DataErogInizio"), "yyyy-MM-dd");
		this.dataErogFine 			= MefUtil.StringToDate(MefUtil.getNodeValue(e, "DataErogFine"), "yyyy-MM-dd");
		this.tipologiaPrestazione 	= MefUtil.getNodeValue(e, "TipologiaPrestazione");
		this.qtaPrest 				= Integer.parseInt(MefUtil.getNodeValue(e, "QtaPrest"));
		this.tariffaPrest 			= MefUtil.StringToDouble(MefUtil.getNodeValue(e, "TariffaPrest"));
		
		// Nuovo tracciato
		this.dataErogazione			= MefUtil.StringToDate(MefUtil.getNodeValue(e, "DataErogazione"), "yyyy-MM-dd");
		this.tariffaPrestLab		= MefUtil.StringToDouble(MefUtil.getNodeValue(e, "TariffaPrestLab"));
		
	}

	public String getBrancaPrestazione() {
		return brancaPrestazione;
	}

	public String getCodicePresidio() {
		return codicePresidio;
	}

	public String getCodicePrest() {
		return codicePrest;
	}

	public String getCodReparto() {
		return codReparto;
	}

	public Date getDataErogFine() {
		return dataErogFine;
	}

	public Date getDataErogInizio() {
		return dataErogInizio;
	}

	public Date getDataPrenotazione() {
		return dataPrenotazione;
	}

	public int getQtaPrest() {
		return qtaPrest;
	}

	public double getTariffaPrest() {
		return tariffaPrest;
	}

	public String getTipologiaPrestazione() {
		return tipologiaPrestazione;
	}

	public Date getDataErogazione() {
		return dataErogazione;
	}

	public double getTariffaPrestLab() {
		return tariffaPrestLab;
	}
	
}
