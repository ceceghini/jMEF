package it.pointec.mef.ui;

import it.pointec.mef.MefConnection;
import it.pointec.mef.MefFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Mef {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {

		System.out.println("Attivazione caricamento dati MEF...");
		
		// Apertura del file proprietà e recupero delle informazioni necessarie per la connessione oracle
		Properties properties = new Properties();
		try {
	        properties.load(new FileInputStream("jMEF.properties"));
	    } catch (IOException e) {
	    	System.out.println(e.getMessage());
	    	return;
	    }
		
		String cs = properties.getProperty("ORA_CS");
	    String userName = properties.getProperty("ORA_USERNAME");
	    String password = properties.getProperty("ORA_PASSWORD");
	    String start_dir = properties.getProperty("START_DIR");
	    String dest_dir = properties.getProperty("DEST_DIR");
	    
		// Connessione al DB Oracle
		try {
			MefConnection.getConnection(cs, userName, password);
		}
		catch (Exception e) {
			e.printStackTrace();
			return;
		}
		
		// Elaborazione dei dati
		MefFile.elabora(start_dir, dest_dir);
		
		System.out.println("Dati MEF caricati correttamente in staging area.");
		
	}

}
