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
	
	public void createChestsTable() {
		String sql = 	"CREATE TABLE IF NOT EXISTS cschests(" +
						"id INT AUTO_INCREMENT PRIMARY KEY, " +
						"player CHAR(" + Constants.PLAYER_MAX_NAME_LENGTH + ")," +
						"x INT, " +
						"y INT, " +
						"z INT, " +
						"FOREIGN KEY(player) REFERENCES csplayers(name) ON DELETE SET NULL) " +
						"ENGINE = InnoDB," +
						"CHARACTER SET latin1 COLLATE latin1_general_cs;";
		
		try {
			con.createStatement().executeUpdate(sql);
		} catch (SQLException e) {
			Cityscape.log.severe("There was an error creating a database table.");
			e.printStackTrace();
		}
	}
	
	public void createChestDataTable() {
		String sql = 	"CREATE TABLE IF NOT EXISTS cschestdata(" +
						"id INT, " +
						"type INT, " +
						"amount INT, " +
						"FOREIGN KEY(id) REFERENCES cschestlostandfound(id) " +
						"ON DELETE CASCADE) " +
						"ENGINE = InnoDB, " +
						"CHARACTER SET latin1 COLLATE latin1_general_cs;";
		try {
			con.createStatement().executeUpdate(sql);
		} catch (SQLException e) {
			Cityscape.log.severe("There was an error creating a database table.");
			e.printStackTrace();
		}
	}
	
	public void createChestLostAndFound() {
		String sql = 	"CREATE TABLE IF NOT EXISTS cschestlostandfound(" +
						"id INT AUTO_INCREMENT PRIMARY KEY, " +
						"player CHAR(" + Constants.PLAYER_MAX_NAME_LENGTH + "), " +
						"FOREIGN KEY(player) REFERENCES csplayers(name) ON DELETE CASCADE)" +
						"ENGINE = InnoDB, " +
						"CHARACTER SET latin1 COLLATE latin1_general_cs;";
		try {
			con.createStatement().executeUpdate(sql);
		} catch (SQLException e) {
			Cityscape.log.severe("There was an error creating a database table.");
			e.printStackTrace();
		}
	}
	
	public void createCitiesTable() {
		String sql = 	"CREATE TABLE IF NOT EXISTS cscities(" + 
						"name CHAR(" + Constants.TOWN_MAX_NAME_LENGTH + ") PRIMARY KEY," +
						"mayor CHAR(" + Constants.PLAYER_MAX_NAME_LENGTH + ")," +
						"welcome CHAR(" + Constants.WELCOME_MESSAGE_LENGTH + "), " +
						"rank INT NOT NULL," +
						"founded DATETIME NOT NULL," +
						"usedClaims INT NOT NULL," +
						"baseClaims INT NOT NULL," +
						"bonusClaims INT NOT NULL," + 
						"spawnX DOUBLE," +
						"spawnY DOUBLE," +
						"spawnZ DOUBLE," +
						"spawnPitch FLOAT, " +
						"spawnYaw FLOAT, " +
						"world CHAR(" + Constants.WORLD_MAX_NAME_LENGTH + "), " +
						"residentBuild BOOL, " +
						"residentDestroy BOOL, " +
						"residentSwitch BOOL, " +
						"outsiderBuild BOOL, " +
						"outsiderDestroy BOOL, " +
						"outsiderSwitch BOOL, " +
						"snow BOOL, " +
						"FOREIGN KEY(mayor) REFERENCES CSPlayers(name) " +
						"ON DELETE CASCADE) " +
						"ENGINE = InnoDB," +
						"CHARACTER SET latin1 COLLATE latin1_general_cs;";
		
		try {
			con.createStatement().executeUpdate(sql);
		} catch (SQLException e) {
			Cityscape.log.severe("There was an error creating a database table.");
			e.printStackTrace();
		}
	}
	
	public void createCityBanListTable() {
		String sql = 	"CREATE TABLE IF NOT EXISTS cscitybanlist(" +
						"city CHAR(" + Constants.TOWN_MAX_NAME_LENGTH + ") NOT NULL, " +
						"player CHAR(" + Constants.PLAYER_MAX_NAME_LENGTH + ") NOT NULL, " +
						"PRIMARY KEY(city, player), " +
						"FOREIGN KEY(city) REFERENCES cscities(name) " +
						"ON DELETE CASCADE ON UPDATE CASCADE, " +
						"FOREIGN KEY(player) REFERENCES csplayers(name) " +
						"ON DELETE CASCADE) " +
						"ENGINE = InnoDB, " +
						"CHARACTER SET latin1 COLLATE latin1_general_cs;";
		try {
			con.createStatement().executeUpdate(sql);
		} catch (SQLException e) {
			Cityscape.log.severe("There was an error creating a database table.");
			e.printStackTrace();
		}
	}
	
	public void createClaimsTable() {
		String sql = 	"CREATE TABLE IF NOT EXISTS csclaims(" +
						"city CHAR(" + Constants.TOWN_MAX_NAME_LENGTH + ") NOT NULL, " + 
						"world CHAR(" + Constants.WORLD_MAX_NAME_LENGTH + ") NOT NULL, " +
						"x INT, " +
						"z INT, " +
						"PRIMARY KEY(x, z), " +
						"FOREIGN KEY(city) REFERENCES cscities(name) " +
						"ON DELETE CASCADE ON UPDATE CASCADE)" +
						"ENGINE = InnoDB," +
						"CHARACTER SET latin1 COLLATE latin1_general_cs;";
		
		try {
			con.createStatement().executeUpdate(sql);
		} catch (SQLException e) {
			Cityscape.log.severe("There was an error creating a database table.");
			e.printStackTrace();
		}
		
	}
	
	public void createCSDatabase() {
		createPlayersTable();
		createCitiesTable();
		
		createChestsTable();
		createChestLostAndFound();
		createChestDataTable();
		createClaimsTable();
		
		createInvitesTable();
		
		createPlotsTable();
		createPlotPermissionsTable();
		createPlayerCityDataTable();
		createRanksTable();
		createResidentsTable();
		createCityBanListTable();
	}
	
	public void createPlayerCityDataTable() {
		String sql = 	"CREATE TABLE IF NOT EXISTS csplayercitydata(" +
						"player CHAR(" + Constants.PLAYER_MAX_NAME_LENGTH + ")," +
						"city CHAR(" + Constants.TOWN_MAX_NAME_LENGTH + ")," +
						"startingfrom DATETIME NOT NULL," +
						"PRIMARY KEY(player, startingfrom)," +
						"FOREIGN KEY(player) REFERENCES CSPlayers(name) ON DELETE CASCADE)" +
						"ENGINE = InnoDB," +
						"CHARACTER SET latin1 COLLATE latin1_general_cs;";
		
		try {
			con.createStatement().executeUpdate(sql);
		} catch (SQLException e) {
			Cityscape.log.severe("There was an error creating a database table.");
			e.printStackTrace();
		}
	}
	
	public void createInvitesTable() {
		String sql = 	"CREATE TABLE IF NOT EXISTS csinvites(" +
						"player CHAR(" + Constants.PLAYER_MAX_NAME_LENGTH + "), " +
						"city CHAR(" + Constants.TOWN_MAX_NAME_LENGTH + "), " +
						"day DATETIME, " +
						"PRIMARY KEY(player, city), " +
						"FOREIGN KEY(player) REFERENCES csplayers(name) ON DELETE CASCADE, " +
						"FOREIGN KEY(city) REFERENCES cscities(name) ON DELETE CASCADE " +
						"ON UPDATE CASCADE) " +
						"ENGINE = InnoDB," +
						"CHARACTER SET latin1 COLLATE latin1_general_cs;";
		try {
			con.createStatement().executeUpdate(sql);
		} catch (SQLException e) {
			Cityscape.log.severe("There was an error creating a database table.");
			e.printStackTrace();
		}
	}
	
	public void createPlayersTable() {
		String sql = 	"CREATE TABLE IF NOT EXISTS csplayers(" +
						"name CHAR(" + Constants.PLAYER_MAX_NAME_LENGTH + ") PRIMARY KEY," +
						"firstLogin DATETIME NOT NULL," +
						"lastLogin DATETIME NOT NULL) " +
						"ENGINE = InnoDB," +
						"CHARACTER SET latin1 COLLATE latin1_general_cs;";
		
		try {
			con.createStatement().executeUpdate(sql);
		} catch (SQLException e) {
			Cityscape.log.severe("There was an error creating a database table.");
			e.printStackTrace();
		}	
	}
	
	public void createPlotPermissionsTable() {
		String sql = 	"CREATE TABLE IF NOT EXISTS csplotpermissions(" +
						"id INT, " +
						"name CHAR(" + Constants.PLAYER_MAX_NAME_LENGTH + "), " +
						"isPlayer BOOL, " +
						"build BOOL, " +
						"destroy BOOL, " +
						"switch BOOL, " +
						"PRIMARY KEY(id, name, isPlayer), " +
						"FOREIGN KEY(id) REFERENCES csplots(id) ON DELETE CASCADE)" +
						"ENGINE = InnoDB," +
						"CHARACTER SET latin1 COLLATE latin1_general_cs;";
		try {
			con.createStatement().executeUpdate(sql);
		} catch (SQLException e) {
			Cityscape.log.severe("There was an error creating a database table.");
			e.printStackTrace();
		}
	}
	
	public void createPlotsTable() {
		String sql = 	"CREATE TABLE IF NOT EXISTS csplots(" +
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
						"cityPlot BOOL, " +
						"snow BOOL, " +
						"forSale BOOL, " +
						"price INT, " +
						"id INT AUTO_INCREMENT, " +
						"PRIMARY KEY(id), " +
						"FOREIGN KEY(city) REFERENCES cscities(name) " +
						"ON DELETE CASCADE ON UPDATE CASCADE) " +
						"ENGINE = InnoDB," +
						"CHARACTER SET latin1 COLLATE latin1_general_cs;";
		
		try {
			con.createStatement().executeUpdate(sql);
		} catch (SQLException e) {
			Cityscape.log.severe("There was an error creating a database table.");
			e.printStackTrace();
		}
	}
	
	public void createRanksTable() {
		String sql = 	"CREATE TABLE IF NOT EXISTS csranks(" +
						"city CHAR(" + Constants.TOWN_MAX_NAME_LENGTH + "), " +
						"name CHAR(" + Constants.RANK_MAX_NAME_LENGTH + "), " +
						"addResident BOOL, " +
						"removeResident BOOL, " +
						"claim BOOL, " +
						"unclaim BOOL, " +
						"promote BOOL, " +
						"demote BOOL, " +
						"withdraw BOOL, " +
						"settings BOOL, " +
						"setWelcome BOOL, " +
						"setMayor BOOL, " +
						"setWarp BOOL, " +
						"setName BOOL, " +
						"setPlotSale BOOL, " +
						"setTaxes BOOL, " +
						"createPlots BOOL, " +
						"sendChestsToLostAndFound BOOL, " +
						"cityBuild BOOL, " +
						"cityDestroy BOOL, " +
						"citySwitch BOOL, " +
						"changeCityPlotPerms BOOL, " +
						"ban BOOL, " +
						"unban BOOL, " +
						"PRIMARY KEY(city, name), " +
						"FOREIGN KEY(city) REFERENCES cscities(name) " +
						"ON DELETE CASCADE ON UPDATE CASCADE)" +
						"ENGINE = InnoDB," +
						"CHARACTER SET latin1 COLLATE latin1_general_cs;";
		try {
			con.createStatement().executeUpdate(sql);
		} catch (SQLException e) {
			Cityscape.log.severe("There was an error creating a database table.");
			e.printStackTrace();
		}
	}
	
	public void createResidentsTable() {
		String sql = 	"CREATE TABLE IF NOT EXISTS csresidents(" +
						"player CHAR(" + Constants.PLAYER_MAX_NAME_LENGTH + ") PRIMARY KEY," +
						"city CHAR(" + Constants.TOWN_MAX_NAME_LENGTH + "), " +
						"rank CHAR(" + Constants.RANK_MAX_NAME_LENGTH + "), " +
						"FOREIGN KEY(player) REFERENCES csplayers(name) ON DELETE CASCADE, " +
						"FOREIGN KEY(city) REFERENCES cscities(name) " +
						"ON DELETE SET NULL ON UPDATE CASCADE) " +
						"ENGINE = InnoDB," +
						"CHARACTER SET latin1 COLLATE latin1_general_cs;";
		try {
			con.createStatement().executeUpdate(sql);
		} catch (SQLException e) {
			Cityscape.log.severe("There was an error creating a database table.");
			e.printStackTrace();
		}
	}
}
