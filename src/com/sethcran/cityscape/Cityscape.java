package com.sethcran.cityscape;

import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class Cityscape extends JavaPlugin {

	@Override
	public void onDisable() {
		Logger log = Logger.getLogger("Minecraft");
		log.info("Cityscape unloaded.");
	}

	@Override
	public void onEnable() {
		Logger log = Logger.getLogger("Minecraft");
		log.info("Cityscape loaded.");
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, 
			String commandLabel, String[] args) {
		if( cmd.getName().equalsIgnoreCase("test"))
			sender.sendMessage("works.");
		return true;
	}

}
