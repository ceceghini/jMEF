package it.pointec.mef;

import java.sql.CallableStatement;

public class MefOracle {
	
	CallableStatement procCSVrow;
	CallableStatement procXMLrow;
	CallableStatement procPDFrow;
	
	public MefOracle() throws Exception {
		
		// Procedure di inserimento
		procCSVrow = MefConnection.getConnection().prepareCall("{ call insert_csv_row(?, ?, ?, ?) }");
																				 //                              1                             2                             3                             4                             5                       6                                 
																	 		     //   1  2  3  4  5  6  7  8  9  0  1  2  3  4  5  6  7  8  9  0  1  2  3  4  5  6  7  8  9  0  1  2  3  4  5  6  7  8  9  0  1  2  3  4  5  6  7  8  9  0  1  2  3  4  5  6  7  8  9  0  1  2  3  4  5  6  7  8  9
		procXMLrow = MefConnection.getConnection().prepareCall("{ call insert_xml_row(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) }");
		
		procPDFrow = MefConnection.getConnection().prepareCall("{ call insert_pdf_row(?, ?, ?) }");
		
	}
	
	public void LogAppException(String message) throws Exception {
		System.out.println("[ERRORE] "+message);
	}
	
	public void InfoLog(String message) throws Exception {
		System.out.println("[INFO] "+message);
	}
	
	public void SetCsvProcParm(int n, String s) throws Exception {
		procCSVrow.setString(n, s);
	}
	
	public void SetPdfProcParm(int n, String s) throws Exception {
		procPDFrow.setString(n, s);
	}
	
	public void SetXmlProcParm(int n, String s) throws Exception {
		procXMLrow.setString(n, s);
	}
	
	public void SetXmlProcParm(int n, java.util.Date d) throws Exception {
		procXMLrow.setDate(n, MefUtil.DateToSql(d));
	}
	
	public void SetXmlProcParm(int n, double d) throws Exception {
		procXMLrow.setDouble(n, d);
	}
	
	public void SetXmlProcParm(int n, int i) throws Exception {
		procXMLrow.setInt(n, i);
	}
	
	public void InsertCsvRow() throws Exception {
		procCSVrow.execute();
	}
	
	public void InsertXmlRow() throws Exception {
		procXMLrow.execute();		
	}
	
	public void InsertPdfRow() throws Exception {
		procPDFrow.execute();		
	}
	
}
