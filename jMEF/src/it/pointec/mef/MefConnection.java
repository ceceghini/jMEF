package it.pointec.mef;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MefConnection {
	
	private static Connection conn;
	//private static boolean _nooracle = false;
	
	public static Connection getConnection(String cs, String username, String password) throws SQLException {
		
		if (conn==null) {
			DriverManager.registerDriver (new oracle.jdbc.driver.OracleDriver());
			conn = DriverManager.getConnection("jdbc:oracle:thin:@" + cs, username, password);
			conn.setAutoCommit(false);
			//CallableStatement proc = conn.prepareCall("{ call wtl_logger.resumejournal }");
			//proc.execute();
			//_nooracle = true;
		}
		return conn;
		
	}
	
	public static Connection getConnection() throws SQLException {
		
		if (conn==null) {
			System.out.println("Connessione inseistente");
			return null;
		}
		return conn;
		
	}
	
}
