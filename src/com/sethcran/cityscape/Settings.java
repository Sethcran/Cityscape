package com.sethcran.cityscape;

import java.io.File;

import org.bukkit.util.config.Configuration;


public class Settings {
	public boolean debug = false;
	
	public String databaseUsername = null;
	public String databasePassword = null;
	public String databaseDriver = null;
	public String databaseUrl = null;
	
	public int defaultCityRank = 1;
	public int defaultBaseClaims = 0;
	public int maxCityRanks = 0;
	
	public boolean defaultResidentBuild = false;
	public boolean defaultResidentDestroy = false;
	public boolean defaultResidentSwitch = false;
	public boolean defaultOutsiderBuild = false;
	public boolean defaultOutsiderDestroy = false;
	public boolean defaultOutsiderSwitch = false;
	public boolean taxes = false;
	public boolean cityWarps = false;
	
	public double cityCost = 0.0;
	
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
		
		defaultBaseClaims = config.getInt("cityscape.defaultbaseclaims", 0);
		maxCityRanks = config.getInt("cityscape.maxcityranks", 10);
		
		defaultResidentBuild = config.getBoolean("cityscape.defaultresidentbuild", false);
		defaultResidentDestroy = config.getBoolean("cityscape.defaultresidentdestroy", false);
		defaultResidentSwitch = config.getBoolean("cityscape.defaultresidentswitch", false);
		defaultOutsiderBuild = config.getBoolean("cityscape.defaultoutsiderbuild", false);
		defaultOutsiderDestroy = config.getBoolean("cityscape.defaultoutsiderdestroy", false);
		defaultOutsiderSwitch = config.getBoolean("cityscape.defaultoutsiderswitch", false);
		
		taxes = config.getBoolean("cityscape.taxes", false);
		cityWarps = config.getBoolean("cityscape.citywarps", false);
		
		cityCost = config.getDouble("iconomy.citycost", 0.0);
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
		
		config.setProperty("cityscape.defaultbaseclaims", 16);
		config.setProperty("cityscape.maxcityranks", 10);
		config.setProperty("cityscape.defaultresidentbuild", false);
		config.setProperty("cityscape.defaultresidentdestroy", false);
		config.setProperty("cityscape.defaultresidentswitch", false);
		config.setProperty("cityscape.defaultoutsiderbuild", false);
		config.setProperty("cityscape.defaultoutsiderdestroy", false);
		config.setProperty("cityscape.defaultoutsiderswitch", false);
		
		config.setProperty("cityscape.taxes", false);
		config.setProperty("cityscape.citywarps", false);
		
		config.setProperty("iconomy.citycost", 0.0);
		config.save();
	}
}
