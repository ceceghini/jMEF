package it.pointec.mef;

import java.util.List;
import java.util.Date;
import java.util.ArrayList;
import org.w3c.dom.Element;

public class MefTestata {

	private String regStruttura;
	private String codAsl;
	private String CodStruttura;
	private int totRic;
	private int totPrest;
	private double totImpCaricoAss;
	private double totValRicInviate;
	private double totImpCaricoSSN;
	private int totRicNuove;
	private int totRicVariaz;
	private int totRicCanc;
	private int totStrutture;
	private String annoMeseNoInvio;
	
	private Date dataInvioFile;
	private String azienda;
	
	private List<MefRicetta> ricette;

	public MefTestata(Element e, String data, String azienda) {
		
		this.dataInvioFile = MefUtil.StringToDate(data, "yyyyMMdd");
		this.azienda = azienda;
		
		this.regStruttura 		= MefUtil.getNodeValue(e, "RegStruttura");
		this.codAsl 			= MefUtil.getNodeValue(e, "CodAsl");
		this.CodStruttura 		= MefUtil.getNodeValue(e, "CodStruttura");
		this.totRic 			= Integer.parseInt(MefUtil.getNodeValue(e, "TotRic"));
		this.totPrest 			= Integer.parseInt(MefUtil.getNodeValue(e, "TotPrest"));
		this.totImpCaricoAss 	= MefUtil.StringToDouble(MefUtil.getNodeValue(e, "TotImpCaricoAss"));
		this.totValRicInviate 	= MefUtil.StringToDouble(MefUtil.getNodeValue(e, "TotValRicInviate"));
		this.totImpCaricoSSN 	= MefUtil.StringToDouble(MefUtil.getNodeValue(e, "TotImpCaricoSSN"));
		this.totRicNuove 		= Integer.parseInt(MefUtil.getNodeValue(e, "TotRicNuove"));
		this.totRicVariaz 		= Integer.parseInt(MefUtil.getNodeValue(e, "TotRicVariaz"));
		this.totRicCanc 		= Integer.parseInt(MefUtil.getNodeValue(e, "TotRicCanc"));
		this.totStrutture 		= Integer.parseInt(MefUtil.getNodeValue(e, "TotStrutture"));
		
		// Nuovo tracciato
		this.annoMeseNoInvio	= MefUtil.getNodeValue(e, "AnnoMeseNoInvio");
		
		ricette = new ArrayList<MefRicetta>();
		
	}

	public List<MefRicetta> getRicette() {
		return ricette;
	}

	public String getAzienda() {
		return azienda;
	}

	public String getCodAsl() {
		return codAsl;
	}

	public String getCodStruttura() {
		return CodStruttura;
	}

	public Date getDataInvioFile() {
		return dataInvioFile;
	}

	public String getRegStruttura() {
		return regStruttura;
	}

	public double getTotImpCaricoAss() {
		return totImpCaricoAss;
	}

	public double getTotImpCaricoSSN() {
		return totImpCaricoSSN;
	}

	public int getTotPrest() {
		return totPrest;
	}

	public int getTotRic() {
		return totRic;
	}

	public int getTotRicCanc() {
		return totRicCanc;
	}

	public int getTotRicNuove() {
		return totRicNuove;
	}

	public int getTotRicVariaz() {
		return totRicVariaz;
	}

	public int getTotStrutture() {
		return totStrutture;
	}

	public double getTotValRicInviate() {
		return totValRicInviate;
	}

	public String getAnnoMeseNoInvio() {
		return annoMeseNoInvio;
	}
		
}
