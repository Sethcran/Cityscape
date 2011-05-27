package com.sethcran.cityscape;

import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;
import com.sethcran.cityscape.database.Database;


public class Cityscape extends JavaPlugin {
	public static Logger log = null;
	public static PermissionHandler permissionHandler = null;
	
	public static Settings settings = null;
	public static Database database = null;

	@Override
	public void onDisable() {
		log.info("Cityscape unloaded.");
	}

	@Override
	public void onEnable() {
		log = Logger.getLogger("Minecraft");
		setupPermissions();
		
		settings = new Settings();
		database = new Database();
		
		log.info("Cityscape loaded.");
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, 
			String commandLabel, String[] args) {
		if(cmd.getName().equalsIgnoreCase("test"))
			sender.sendMessage("works.");
		return true;
	}
	
	private void setupPermissions() {
		Plugin permissionsPlugin = this.getServer().getPluginManager().getPlugin("Permissions");
		
		if(permissionHandler == null) {
			if(permissionsPlugin != null) {
				permissionHandler = ((Permissions)permissionsPlugin).getHandler();
			} else {
				log.info("Permission system not detected, defaulting to OP");
			}
		}
	}
}
