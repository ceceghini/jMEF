package it.pointec.mef;

import java.io.IOException;

import com.lowagie.text.pdf.PRIndirectReference;
import com.lowagie.text.pdf.PRStream;
import com.lowagie.text.pdf.PdfDictionary;
import com.lowagie.text.pdf.PdfName;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.RandomAccessFileOrArray;

public class MefPdf {
	
	private String s_file;

	public MefPdf(String nomeFileCsv) throws IOException {
		
		PdfReader reader = new PdfReader(nomeFileCsv);
		PdfDictionary page = reader.getPageN(1);
		PRIndirectReference objectReference = (PRIndirectReference) page.get(PdfName.CONTENTS);
		PRStream stream = (PRStream) PdfReader.getPdfObject(objectReference); 
		RandomAccessFileOrArray rf = stream.getReader().getSafeFile();
		byte[] streamBytes = PdfReader.getStreamBytes(stream, rf); 
		
		this.s_file = removeChar(new String(streamBytes));
		
		rf.close();
		
	}
	
	/**
	 * Recupero del nome ssa a partire dal contenuto del pdf
	 * @return
	 */
	public String getNomessa() {
		
		int i;
		String s;
		
		i = s_file.indexOf(".ssa");
		s = s_file.substring(1, i+4);
		
		i = s.indexOf("hainviatoilfile");
		s = s.substring(i+15, s.length());
				
		return s;
	} 
	
	/**
	 * Recupero del protocollo a partire dal pdf
	 * @return
	 */
	public String getProtocollo() {
		
		int i;
		String s;
	
		i = s_file.indexOf("sistemacentraleconilnumero");
		s = s_file.substring(i, s_file.length());
	
		i = s.indexOf("0042");
		s = s.substring(i, i+23);
	
		return s;
	}
	
	/**
	 * Parsing del pdf e cancellazione caratteri impropri
	 * @param s
	 * @return
	 */
	private String removeChar(String s) {
		
		char c10 = (char) 10;
		char c13 = (char) 13;
				
		String ret = "";
		
		int i;
		for(i=0;i<s.length();i++) {
			
			if (s.charAt(i)!= ')' & s.charAt(i)!= '(' & s.charAt(i)!= c10 & s.charAt(i)!= c13 & s.charAt(i)!= ' ')
				ret += s.charAt(i);
				
		}
		
		ret = removeString(ret, "Tj-100Td10-18Td");
		ret = removeString(ret, "Tj-50Td5-18Td(");
		
		/*i = ret.indexOf("Tj-100Td10-18Td");
		while(i>0) {
			ret = ret.substring(1, i) + ret.substring(i+16, ret.length());
			i = ret.indexOf("Tj-100Td10-18Td");
		}
		
		i = ret.indexOf("Tj-50Td5-18Td(");
		while(i>0) {
			ret = ret.substring(1, i) + ret.substring(i+16, ret.length());
			i = ret.indexOf("Tj-100Td10-18Td");
		}*/
		
		//return ret.replaceAll("Tj-50Td5-18Td\\(", "");
		return ret;
	}
	
	private String removeString(String s, String r) {
		
		String ret = s;
		int l = r.length();
		
		int i = s.indexOf(r);
		while(i>0) {
			ret = ret.substring(1, i) + ret.substring(i+l, ret.length());
			i = ret.indexOf(r);
		}
		
		return ret;
		
	}
	
}
