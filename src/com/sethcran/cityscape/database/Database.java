package com.sethcran.cityscape.database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.util.config.Configuration;

public class Database {
	private Connection connection = null;
	
	public Database() {
		File file = new File("bukkit.yml");
		Configuration config = new Configuration(file);
		config.load();
		
		String username = config.getString("database.username");
		String password = config.getString("database.password");
		String driver = config.getString("database.driver");
		String url = config.getString("database.url");
		
		try {
			Class.forName(driver);
			connection = DriverManager.getConnection(url, username, password);
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
