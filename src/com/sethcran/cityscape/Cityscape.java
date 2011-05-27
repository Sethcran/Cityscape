package com.sethcran.cityscape;

import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.iConomy.iConomy;
import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;
import com.sethcran.cityscape.commands.CommandHandler;
import com.sethcran.cityscape.database.Database;
import com.sethcran.cityscape.listeners.CSServerListener;


public class Cityscape extends JavaPlugin {
	public static Logger log = null;
	public PermissionHandler permissionHandler = null;
	public iConomy iconomy = null;
	
	private Settings settings = null;
	private Database database = null;
	private CommandHandler commandHandler = null;

	@Override
	public void onDisable() {
		log.info("Cityscape unloaded.");
	}

	@Override
	public void onEnable() {
		log = Logger.getLogger("Minecraft");
		
		setupPermissions();
		getServer().getPluginManager().registerEvent(Type.PLUGIN_ENABLE, 
				new CSServerListener(this), Priority.Monitor, this);
		getServer().getPluginManager().registerEvent(Type.PLUGIN_DISABLE,
				new CSServerListener(this), Priority.Monitor, this);
		
		settings = new Settings();
		database = new Database(this);
		commandHandler = new CommandHandler();
		
		log.info("Cityscape loaded.");
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, 
			String commandLabel, String[] args) {
		return commandHandler.handleCommand(sender, cmd, commandLabel, args);
	}
	
	public Settings getSettings() {
		return settings;
	}
	
	public Database getDB() {
		return database;
	}
	
	public CommandHandler getCommandHandler() {
		return commandHandler;
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
