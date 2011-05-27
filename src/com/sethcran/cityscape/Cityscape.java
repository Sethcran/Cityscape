package com.sethcran.cityscape;

import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import com.sethcran.cityscape.database.Database;


public class Cityscape extends JavaPlugin {
	public static Logger log = null;
	
	public static Settings settings = null;
	public static Database database = null;

	@Override
	public void onDisable() {
		log.info("Cityscape unloaded.");
	}

	@Override
	public void onEnable() {
		log = Logger.getLogger("Minecraft");
		log.info("Cityscape loaded.");
		
		settings = new Settings();
		database = new Database();
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, 
			String commandLabel, String[] args) {
		if(cmd.getName().equalsIgnoreCase("test"))
			sender.sendMessage("works.");
		return true;
	}
}
