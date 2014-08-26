package it.pointec.mef;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.StringTokenizer;

public class MefUtil {

	/**
	 * Recupera il valore di un nodo
	 * @param e			nodo dalla quale estrarre il valore
	 * @param tag		tag del valore
	 * @return
	 */
	public static String getNodeValue(Element e, String tag) {
		
		Node n = e.getElementsByTagName(tag).item(0).getFirstChild();
		
		if (n==null)
			return "";
		else
			return n.getNodeValue();
		
	}
	
	/**
	 * Converte una stringa in data sulla base del formato
	 * @param date	stringa da convertire
	 * @param frm	formato
	 * @return
	 */
	public static java.util.Date StringToDate(String date, String frm) {
		
		try {
            if (date == null) return null;
            SimpleDateFormat df = new SimpleDateFormat(frm);        
            return df.parse(date); 
        } catch(ParseException e){
            return null;            
        }
		
	}
	
	/**
	 * Converte una data in un Data.sql
	 **/
	public static java.sql.Date DateToSql(java.util.Date d) {
		
		if (d==null)
			return null;
		else
			return new java.sql.Date(d.getTime());
		
	}
	
	/**
	 * Sostituisce una stringa con un altra
	 **/
	static String replaceAllWords1(String original, String find, String replacement) {
	    String result = "";
	    String delimiters = "+-*/(),. ";
	    StringTokenizer st = new StringTokenizer(original, delimiters, true);
	    
	    while (st.hasMoreTokens()) {
	        String w = st.nextToken();
	        if (w.equals(find)) {
	            result = result + replacement;
	        } else {
	            result = result + w;
	        }
	    }
	    return result;
	}

	/**
	 * Rimozione di un carattere da una stringa
	 * @param s
	 * @param c
	 * @return
	 */
	public static String removeChar(String s, char c) {
	   String r = "";
	   for (int i = 0; i < s.length(); i ++) {
	      if (s.charAt(i) != c) r += s.charAt(i);
	      }
	   return r;
	}
	
	/**
	 * Split di una stringa
	 **/
	public static String[] Split(String str, String delimiter) {
		
		StringTokenizer st = new StringTokenizer(str,delimiter,false);
		String[] ret = new String[st.countTokens()];
		int i = 0;
		while (st.hasMoreElements()){
			ret[i] = st.nextToken();
			i++;
		}
		return ret;
	}
	
	public static double StringToDouble(String str) {
		if (str.equals("")) str = "0";
		return Double.parseDouble(str);
	}
}
