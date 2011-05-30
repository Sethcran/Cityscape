package com.sethcran.cityscape.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.sethcran.cityscape.Cityscape;
import com.sethcran.cityscape.RankPermissions;
import com.sethcran.cityscape.Settings;

public class Database {
	private Connection con = null;
	
	private Settings settings = null;
	
	private CSCities cscities = null;
	private CSClaims csclaims = null;
	private CSPlayers csplayers = null;
	private CSPlayerCityData csplayercitydata = null;
	private CSPlots csplots = null;
	private CSRanks csranks = null;
	private CSResidents csresidents = null;
	
	public Database(Cityscape plugin) {		
		settings = plugin.getSettings();
		
		try {
			Class.forName(settings.databaseDriver);
			con = DriverManager.getConnection(settings.databaseUrl, 
					settings.databaseUsername, settings.databasePassword);
		} catch(ClassNotFoundException e) {
			if(settings.debug)
				e.printStackTrace();
		} catch(SQLException e) {
			if(settings.debug)
				e.printStackTrace();
		}
		
		String sql = "show tables like 'CSCities';";
		try {
			ResultSet rs = con.createStatement().executeQuery(sql);
			if(!rs.next()) {
				Schemas schemas = new Schemas(con);
				schemas.createCSDatabase();
			}
		} catch (SQLException e) {
			if(settings.debug)
				e.printStackTrace();
		}
		
		cscities = new CSCities(con, plugin.getSettings());
		csclaims = new CSClaims(con, plugin.getSettings());
		csplayers = new CSPlayers(con, plugin.getSettings());
		csplayercitydata = new CSPlayerCityData(con, plugin.getSettings());
		csplots = new CSPlots(con, plugin.getSettings());
		csranks = new CSRanks(con, plugin.getSettings());
		csresidents = new CSResidents(con, plugin.getSettings());
	}
	
	public void claimChunk(String cityName, int x, int z, String worldName) {
		try {
			con.setAutoCommit(false);
			csclaims.claimChunk(cityName, x, z, worldName);
			cscities.addUsedClaims(cityName, 1);
			con.commit();
			con.setAutoCommit(true);
		} catch(SQLException e) {
			if(settings.debug)
				e.printStackTrace();
		}
	}

	public void createCity(String playerName, String cityName, 
			int x, int z, String worldName) {
		try {
			con.setAutoCommit(false);
			cscities.createCity(playerName, cityName);
			csclaims.claimChunk(cityName, x, z, worldName);
			csplayercitydata.addPlayerCityHistory(playerName, cityName);
			csranks.createRank(cityName, "Mayor", new RankPermissions(true));
			csresidents.setCurrentCity(playerName, cityName, "Mayor");
			con.commit();
			con.setAutoCommit(true);
		} catch(SQLException e) {
			if(settings.debug)
				e.printStackTrace();
		}
	}
	
	public boolean doesCityExist(String cityName) {
		return cscities.doesCityExist(cityName);
	}
	
	public boolean doesPlayerExist(String playerName) {
		return csplayers.doesPlayerExist(playerName);
	}
	
	public String getCityAt(int x, int z) {
		return csclaims.getCityAt(x, z);
	}
	
	public Connection getConnection() {
		return con;
	}
	
	public String getCurrentCity(String playerName) {
		return csresidents.getCurrentCity(playerName);
	}
	
	public RankPermissions getPermissions(String townName, String rank) {
		return csranks.getPermissions(townName, rank);
	}
	
	public String getRank(String playerName) {
		return csresidents.getRank(playerName);
	}
	
	public boolean hasClaims(String cityName, int numClaims) {
		return cscities.hasClaims(cityName, numClaims);
	}
	
	public void insertNewPlayer(String playerName) {
		try {
			con.setAutoCommit(false);
			csplayers.insertNewPlayer(playerName);
			csresidents.insertNewPlayer(playerName);
			csplayercitydata.addPlayerCityHistory(playerName, null);
			con.commit();
			con.setAutoCommit(true);
		} catch (SQLException e) {
			if(settings.debug)
				e.printStackTrace();
		}
	}
	
	public boolean isChunkClaimed(int x, int z) {
		return csclaims.isChunkClaimed(x, z);
	}
	
	public void updatePlayerTimeStamp(String playerName) {
		csplayers.updatePlayerTimeStamp(playerName);
	}
}
