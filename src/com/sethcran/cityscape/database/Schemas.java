package com.sethcran.cityscape.database;

import java.sql.Connection;
import java.sql.SQLException;

import com.sethcran.cityscape.Cityscape;

public class Schemas {
	private Connection con = null;
	
	public Schemas(Connection con) {
		this.con = con;
	}
	
	public void createCSDatabase() {
		createPlayersTable();
		createCitiesTable();
		createClaimsTable();
		createPlotsTable();
		createPlayerTownDataTable();
		createChestsTable();
	}
	
	public void createPlayersTable() {
		String sql = "CREATE TABLE IF NOT EXISTS CSPlayers(" +
		"Name CHAR(32) PRIMARY KEY);";
		
		try {
			con.createStatement().executeUpdate(sql);
		} catch (SQLException e) {
			Cityscape.log.severe("There was an error creating a database table.");
			e.printStackTrace();
		}	
	}
	
	public void createCitiesTable() {
		String sql = "CREATE TABLE IF NOT EXISTS CSCities(" + 
		"Name CHAR(32) PRIMARY KEY," +
		"Mayor CHAR(32)," +
		"FOREIGN KEY(Mayor) REFERENCES CSPlayers(Name));";
		
		try {
			con.createStatement().executeUpdate(sql);
		} catch (SQLException e) {
			Cityscape.log.severe("There was an error creating a database table.");
			e.printStackTrace();
		}
	}
	
	public void createClaimsTable() {
		
	}
	
	public void createPlotsTable() {
		
	}
	
	public void createPlayerTownDataTable() {
		
	}
	
	public void createChestsTable() {
		
	}
}
