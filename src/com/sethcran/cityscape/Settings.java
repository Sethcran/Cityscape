package com.sethcran.cityscape;

import java.io.File;

import org.bukkit.util.config.Configuration;


public class Settings {
	public boolean debug;
	
	public Settings() {
		String directory = "plugins/Cityscape";
		File dataFolder = new File(directory);
		dataFolder.mkdir();
		File file = new File(directory + File.separator + "config.yml");
		Configuration config = new Configuration(file);
		config.load();
		
		if(!file.exists())
			create(file);
		else
			debug = config.getBoolean("debug", false);
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
		config.save();
	}
}
