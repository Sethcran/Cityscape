package com.sethcran.cityscape;

import java.io.File;

import org.bukkit.util.config.Configuration;


public class Settings {
	public boolean debug = false;
	
	public String databaseUsername = null;
	public String databasePassword = null;
	public String databaseDriver = null;
	public String databaseUrl = null;
	
	public Settings() {
		String directory = "plugins/Cityscape";
		File dataFolder = new File(directory);
		dataFolder.mkdir();
		File file = new File(directory + File.separator + "config.yml");
		Configuration config = new Configuration(file);
		config.load();
		
		if(!file.exists())
			create(file);
		config.load();
		
		debug = config.getBoolean("debug", false);
			
		databaseUsername = config.getString("database.username");
		databasePassword = config.getString("database.password");
		databaseDriver = config.getString("database.driver");
		databaseUrl = config.getString("database.url");
	}
	
	public void create(File file) {
		try {
			file.createNewFile();
		} catch(Exception e) {
			e.printStackTrace();
		}
		Configuration config = new Configuration(file);
		config.load();
		
		config.setProperty("debug",	false);
		
		config.setProperty("database.username", "minecraft");
		config.setProperty("database.password", "testpass");
		config.setProperty("database.driver", "com.mysql.jdbc.Driver");
		config.setProperty("database.url", "jdbc:mysql://localhost/minecraft");
		config.save();
	}
}
