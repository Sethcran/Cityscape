package com.sethcran.cityscape.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.sethcran.cityscape.Cityscape;
import com.sethcran.cityscape.Settings;

public class Database {
	private Connection connection = null;
	
	private CSCities cscities = null;
	private CSPlayers csplayers = null;
	private CSPlayerCityData csplayercitydata = null;
	private CSResidents csresidents = null;
	
	public Database(Cityscape plugin) {		
		Settings settings = plugin.getSettings();
		
		try {
			Class.forName(settings.databaseDriver);
			connection = DriverManager.getConnection(settings.databaseUrl, 
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
			ResultSet rs = connection.createStatement().executeQuery(sql);
			if(!rs.next()) {
				Schemas schemas = new Schemas(connection);
				schemas.createCSDatabase();
			}
		} catch (SQLException e) {
			if(settings.debug)
				e.printStackTrace();
		}
		
		cscities = new CSCities(connection, plugin.getSettings());
		csplayers = new CSPlayers(connection, plugin.getSettings());
		csplayercitydata = new CSPlayerCityData(connection, plugin.getSettings());
		csresidents = new CSResidents(connection, plugin.getSettings());
	}
		
	public Connection getConnection() {
		return connection;
	}
	
	public CSCities getCSCities() {
		return cscities;
	}
	
	public CSPlayers getCSPlayers() {
		return csplayers;
	}
	
	public CSPlayerCityData getCSPlayerCityData() {
		return csplayercitydata;
	}
	
	public CSResidents getCSResidents() {
		return csresidents;
	}
}
