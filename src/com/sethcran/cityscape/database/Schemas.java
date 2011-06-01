package com.sethcran.cityscape.database;

import java.sql.Connection;
import java.sql.SQLException;

import com.sethcran.cityscape.Cityscape;
import com.sethcran.cityscape.Constants;

public class Schemas {
	private Connection con = null;
	
	public Schemas(Connection con) {
		this.con = con;
	}
	
	public void createCSDatabase() {
		createPlayersTable();
		createCitiesTable();
		
		createChestsTable();
		createClaimsTable();
		createPlotsTable();
		createPlotPermissionsTable();
		createPlayerCityDataTable();
		createRanksTable();
		createResidentsTable();
	}
	
	public void createChestsTable() {
		String sql = 	"CREATE TABLE IF NOT EXISTS CSChests(" +
						"id INT AUTO_INCREMENT PRIMARY KEY," +
						"player CHAR(" + Constants.PLAYER_MAX_NAME_LENGTH + ")," +
						"FOREIGN KEY(player) REFERENCES CSPlayers(name) ON DELETE SET NULL) " +
						"ENGINE = InnoDB;";
		
		try {
			con.createStatement().executeUpdate(sql);
		} catch (SQLException e) {
			Cityscape.log.severe("There was an error creating a database table.");
			e.printStackTrace();
		}
	}
	
	public void createCitiesTable() {
		String sql = 	"CREATE TABLE IF NOT EXISTS CSCities(" + 
						"name CHAR(" + Constants.TOWN_MAX_NAME_LENGTH + ") PRIMARY KEY," +
						"mayor CHAR(" + Constants.PLAYER_MAX_NAME_LENGTH + ")," +
						"rank INT NOT NULL," +
						"founded DATETIME NOT NULL," +
						"usedClaims INT NOT NULL," +
						"baseClaims INT NOT NULL," +
						"bonusClaims INT NOT NULL," + 
						"spawnX INT," +
						"spawnY INT," +
						"spawnZ INT," +
						"residentBuild BOOL, " +
						"residentDestroy BOOL, " +
						"residentSwitch BOOL, " +
						"outsiderBuild BOOL, " +
						"outsiderDestroy BOOL, " +
						"outsiderSwitch BOOL, " +
						"FOREIGN KEY(mayor) REFERENCES CSPlayers(name) ON DELETE CASCADE) " +
						"ENGINE = InnoDB;";
		
		try {
			con.createStatement().executeUpdate(sql);
		} catch (SQLException e) {
			Cityscape.log.severe("There was an error creating a database table.");
			e.printStackTrace();
		}
	}
	
	public void createClaimsTable() {
		String sql = 	"CREATE TABLE IF NOT EXISTS CSClaims(" +
						"city CHAR(" + Constants.TOWN_MAX_NAME_LENGTH + ") NOT NULL, " + 
						"world CHAR(" + Constants.WORLD_MAX_NAME_LENGTH + ") NOT NULL, " +
						"xmin INT, " +
						"zmin INT, " +
						"xmax INT, " +
						"zmax INT, " +
						"id INT AUTO_INCREMENT, " +
						"PRIMARY KEY(id), " +
						"FOREIGN KEY(city) REFERENCES cscities(name))" +
						"ENGINE = InnoDB;";
		
		try {
			con.createStatement().executeUpdate(sql);
		} catch (SQLException e) {
			Cityscape.log.severe("There was an error creating a database table.");
			e.printStackTrace();
		}
		
	}
	
	public void createPlayerCityDataTable() {
		String sql = 	"CREATE TABLE IF NOT EXISTS CSPlayerCityData(" +
						"player CHAR(" + Constants.PLAYER_MAX_NAME_LENGTH + ")," +
						"city CHAR(" + Constants.TOWN_MAX_NAME_LENGTH + ")," +
						"startingfrom DATETIME NOT NULL," +
						"PRIMARY KEY(player, startingfrom)," +
						"FOREIGN KEY(player) REFERENCES CSPlayers(name) ON DELETE CASCADE," +
						"FOREIGN KEY(city) REFERENCES CSCities(name) ON DELETE NO ACTION) " +
						"ENGINE = InnoDB;";
		
		try {
			con.createStatement().executeUpdate(sql);
		} catch (SQLException e) {
			Cityscape.log.severe("There was an error creating a database table.");
			e.printStackTrace();
		}
	}
	
	public void createPlayersTable() {
		String sql = 	"CREATE TABLE IF NOT EXISTS CSPlayers(" +
						"name CHAR(" + Constants.PLAYER_MAX_NAME_LENGTH + ") PRIMARY KEY," +
						"firstLogin DATETIME NOT NULL," +
						"lastLogin DATETIME NOT NULL) " +
						"ENGINE = InnoDB;";
		
		try {
			con.createStatement().executeUpdate(sql);
		} catch (SQLException e) {
			Cityscape.log.severe("There was an error creating a database table.");
			e.printStackTrace();
		}	
	}
	
	public void createPlotPermissionsTable() {
		String sql = 	"CREATE TABLE IF NOT EXISTS CSPlotPermissions(" +
						"xmin INT, " +
						"xmax INT, " +
						"zmin INT, " +
						"zmax INT, " +
						"name CHAR(" + Constants.PLAYER_MAX_NAME_LENGTH + "), " +
						"isPlayer BOOL, " +
						"build BOOL, " +
						"destroy BOOL, " +
						"switch BOOL, " +
						"PRIMARY KEY(xmin, xmax, zmin, zmax)) " +
						"ENGINE = InnoDB;";
		try {
			con.createStatement().executeUpdate(sql);
		} catch (SQLException e) {
			Cityscape.log.severe("There was an error creating a database table.");
			e.printStackTrace();
		}
	}
	
	public void createPlotsTable() {
		String sql = 	"CREATE TABLE IF NOT EXISTS CSPlots(" +
						"city CHAR(" + Constants.TOWN_MAX_NAME_LENGTH + ") NOT NULL," +
						"owner CHAR(" + Constants.PLAYER_MAX_NAME_LENGTH + ")," +
						"xmin INT," +
						"xmax INT," +
						"zmin INT," +
						"zmax INT," +
						"residentBuild BOOL," +
						"residentDestroy BOOL," +
						"residentSwitch BOOL," +
						"outsiderBuild BOOL," +
						"outsiderDestroy BOOL," +
						"outsiderSwitch BOOL," +
						"id INT AUTO_INCREMENT, " +
						"PRIMARY KEY(id)) " +
						"ENGINE = InnoDB;";
		
		try {
			con.createStatement().executeUpdate(sql);
		} catch (SQLException e) {
			Cityscape.log.severe("There was an error creating a database table.");
			e.printStackTrace();
		}
	}
	
	public void createRanksTable() {
		String sql = 	"CREATE TABLE IF NOT EXISTS CSRanks(" +
						"city CHAR(" + Constants.TOWN_MAX_NAME_LENGTH + "), " +
						"name CHAR(" + Constants.RANK_MAX_NAME_LENGTH + "), " +
						"addResident BOOL, " +
						"removeResident BOOL, " +
						"claim BOOL, " +
						"unclaim BOOL, " +
						"promote BOOL, " +
						"demote BOOL, " +
						"withdraw BOOL, " +
						"changeRankName BOOL, " +
						"setWelcome BOOL, " +
						"setMayor BOOL, " +
						"setWarp BOOL, " +
						"setName BOOL, " +
						"setPlotSale BOOL, " +
						"setTaxes BOOL, " +
						"setPrices BOOL, " +
						"createPlots BOOL, " +
						"sendChestsToLostAndFound BOOL, " +
						"cityBuild BOOL, " +
						"cityDestroy BOOL, " +
						"citySwitch BOOL, " +
						"PRIMARY KEY(city, name), " +
						"FOREIGN KEY(city) REFERENCES cscities(name))" +
						"ENGINE = InnoDB;";
		try {
			con.createStatement().executeUpdate(sql);
		} catch (SQLException e) {
			Cityscape.log.severe("There was an error creating a database table.");
			e.printStackTrace();
		}
	}
	
	public void createResidentsTable() {
		String sql = 	"CREATE TABLE IF NOT EXISTS CSResidents(" +
						"player CHAR(" + Constants.PLAYER_MAX_NAME_LENGTH + ") PRIMARY KEY," +
						"city CHAR(" + Constants.TOWN_MAX_NAME_LENGTH + "), " +
						"rank CHAR(" + Constants.RANK_MAX_NAME_LENGTH + "), " +
						"FOREIGN KEY(player) REFERENCES csplayers(name), " +
						"FOREIGN KEY(city) REFERENCES cscities(name)) " +
						"ENGINE = InnoDB;";
		try {
			con.createStatement().executeUpdate(sql);
		} catch (SQLException e) {
			Cityscape.log.severe("There was an error creating a database table.");
			e.printStackTrace();
		}
	}
}
