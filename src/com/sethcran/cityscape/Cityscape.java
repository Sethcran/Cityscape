package com.sethcran.cityscape;

import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import com.mysql.jdbc.Connection;

public class Cityscape extends JavaPlugin {
	static public Logger log = null;
	CSSettings settings;
	Connection con = null;

	@Override
	public void onDisable() {
		log.info("Cityscape unloaded.");
	}

	@Override
	public void onEnable() {
		log = Logger.getLogger("Minecraft");
		log.info("Cityscape loaded.");
		
		settings = new CSSettings();
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, 
			String commandLabel, String[] args) {
		if(cmd.getName().equalsIgnoreCase("test"))
			sender.sendMessage("works.");
		return true;
	}

}
