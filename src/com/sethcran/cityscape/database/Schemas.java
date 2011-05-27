package com.sethcran.cityscape.database;

import java.sql.Connection;
import java.sql.SQLException;

import com.sethcran.cityscape.Cityscape;

public class Schemas {
	private Connection con = null;
	
	private final int NAME_MAX_CHARACTER_LENGTH = 32;
	
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
		"name CHAR(" + NAME_MAX_CHARACTER_LENGTH + ") PRIMARY KEY," +
		"firstLogin DATETIME NOT NULL," +
		"lastLogin DATETIME NOT NULL);";
		
		try {
			con.createStatement().executeUpdate(sql);
		} catch (SQLException e) {
			Cityscape.log.severe("There was an error creating a database table.");
			e.printStackTrace();
		}	
	}
	
	public void createCitiesTable() {
		String sql = "CREATE TABLE IF NOT EXISTS CSCities(" + 
		"name CHAR(" + NAME_MAX_CHARACTER_LENGTH + ") PRIMARY KEY," +
		"mayor CHAR(" + NAME_MAX_CHARACTER_LENGTH + ")," +
		"rank INT NOT NULL," +
		"founded DATETIME NOT NULL," +
		"baseClaims INT NOT NULL," +
		"bonusClaims INT NOT NULL," + 
		"spawn POINT," +
		"FOREIGN KEY(mayor) REFERENCES CSPlayers(name) ON DELETE CASCADE);";
		
		try {
			con.createStatement().executeUpdate(sql);
		} catch (SQLException e) {
			Cityscape.log.severe("There was an error creating a database table.");
			e.printStackTrace();
		}
	}
	
	public void createClaimsTable() {
		String sql = "CREATE TABLE IF NOT EXISTS CSClaims(" +
		"loc POINT PRIMARY KEY," +
		"town CHAR(" + NAME_MAX_CHARACTER_LENGTH + ") NOT NULL);";
		
		try {
			con.createStatement().executeUpdate(sql);
		} catch (SQLException e) {
			Cityscape.log.severe("There was an error creating a database table.");
			e.printStackTrace();
		}
		
	}
	
	public void createPlotsTable() {
		String sql = "CREATE TABLE IF NOT EXISTS CSPlots(" +
		"loc1 POINT," +
		"loc2 POINT," + 
		"town CHAR(" + NAME_MAX_CHARACTER_LENGTH + ") NOT NULL," +
		"PRIMARY KEY(loc1, loc2));";
		
		try {
			con.createStatement().executeUpdate(sql);
		} catch (SQLException e) {
			Cityscape.log.severe("There was an error creating a database table.");
			e.printStackTrace();
		}
	}
	
	public void createPlayerTownDataTable() {
		String sql = "CREATE TABLE IF NOT EXISTS CSPlayerTownData(" +
		"player CHAR(" + NAME_MAX_CHARACTER_LENGTH + ")," +
		"town CHAR(" + NAME_MAX_CHARACTER_LENGTH + ")," +
		"startingfrom DATETIME NOT NULL," +
		"PRIMARY KEY(player, town, startingfrom)," +
		"FOREIGN KEY(player) REFERENCES CSPlayers(name) ON DELETE CASCADE," +
		"FOREIGN KEY(town) REFERENCES CSCities(name) ON DELETE NO ACTION);";
		
		try {
			con.createStatement().executeUpdate(sql);
		} catch (SQLException e) {
			Cityscape.log.severe("There was an error creating a database table.");
			e.printStackTrace();
		}
	}
	
	public void createChestsTable() {
		String sql = "CREATE TABLE IF NOT EXISTS CSChests(" +
		"id INT AUTO_INCREMENT PRIMARY KEY," +
		"player CHAR(" + NAME_MAX_CHARACTER_LENGTH + ")," +
		"FOREIGN KEY(player) REFERENCES CSPlayers(name) ON DELETE SET NULL);";
		
		try {
			con.createStatement().executeUpdate(sql);
		} catch (SQLException e) {
			Cityscape.log.severe("There was an error creating a database table.");
			e.printStackTrace();
		}
	}
}
