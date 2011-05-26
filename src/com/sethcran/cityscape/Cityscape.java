package com.sethcran.cityscape;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;


public class Cityscape extends JavaPlugin {
	public static Logger log = null;
	private CSSettings settings = null;
	private Connection con = null;

	@Override
	public void onDisable() {
		log.info("Cityscape unloaded.");
	}

	@Override
	public void onEnable() {
		log = Logger.getLogger("Minecraft");
		log.info("Cityscape loaded.");
		
		settings = new CSSettings();
		loadDBConnection();
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, 
			String commandLabel, String[] args) {
		if(cmd.getName().equalsIgnoreCase("test"))
			sender.sendMessage("works.");
		return true;
	}
	
	private void loadDBConnection() {
		File file = new File("bukkit.yml");
		Configuration config = new Configuration(file);
		config.load();
		
		String username = config.getString("database.username");
		String password = config.getString("database.password");
		String driver = config.getString("database.driver");
		String url = config.getString("database.url");
		
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, username, password);
		} catch(ClassNotFoundException e) {
			e.printStackTrace();
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}

}
