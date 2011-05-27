package com.sethcran.cityscape.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.sethcran.cityscape.Cityscape;
import com.sethcran.cityscape.Settings;

public class Database {
	private Connection connection = null;
	
	public Database(Cityscape plugin) {		
		Settings settings = plugin.getSettings();
		
		try {
			Class.forName(settings.databaseDriver);
			connection = DriverManager.getConnection(settings.databaseUrl, 
					settings.databaseUsername, settings.databasePassword);
		} catch(ClassNotFoundException e) {
			e.printStackTrace();
		} catch(SQLException e) {
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
			e.printStackTrace();
		}
	}
		
	public Connection getConnection() {
		return connection;
	}
		
}
