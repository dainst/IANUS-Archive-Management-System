package de.ianus.ingest.core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

public class Testing {

	private static final Logger logger = Logger.getLogger(Testing.class);

	// JDBC driver name and database URL
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_CAMUNDA_URL = "jdbc:mysql://localhost/camunda";
	static final String DB_IANUS_URL = "jdbc:mysql://localhost/ianus";

	// Database credentials
	static final String USER = "root";
	static final String PASS = "admin";

	public void createCamundaDB() {
		try {
			Connection conn = getCamundaConn();
			Statement stmt = conn.createStatement();
			
			logger.info("dropping database camunda...");
			stmt.executeUpdate("drop database camunda");
			
			logger.info(" database camunda...");
			stmt.executeUpdate("create database camunda");

			stmt.close();
			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void removeAllSubmissions(){
		try {
			Connection conn = getIanusConn();
			Statement stmt = conn.createStatement();
			
			logger.info("removing all submissions...");
			stmt.executeUpdate("delete from SubmissionIP");
			

			stmt.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Connection getCamundaConn() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		Connection conn = null;
		System.out.println("Connecting to database...");
		conn = DriverManager.getConnection(DB_CAMUNDA_URL, USER, PASS);
		return conn;
	}

	private Connection getIanusConn() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		Connection conn = null;
		System.out.println("Connecting to database...");
		conn = DriverManager.getConnection(DB_IANUS_URL, USER, PASS);
		return conn;
	}
}
