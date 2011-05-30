package com.sethcran.cityscape;

import java.util.HashMap;
import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.iConomy.iConomy;
import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;
import com.sethcran.cityscape.commands.CommandHandler;
import com.sethcran.cityscape.database.Database;
import com.sethcran.cityscape.listeners.CSBlockListener;
import com.sethcran.cityscape.listeners.CSPlayerListener;
import com.sethcran.cityscape.listeners.CSServerListener;


public class Cityscape extends JavaPlugin {
	public static Logger log = null;
	public PermissionHandler permissionHandler = null;
	public iConomy iconomy = null;
	
	private Settings settings = null;
	private Database database = null;
	private CommandHandler commandHandler = null;
	private HashMap<String, PlayerCache> locationCache = new HashMap<String, PlayerCache>();

	@Override
	public void onDisable() {
		log.info("Cityscape unloaded.");
	}

	@Override
	public void onEnable() {
		log = Logger.getLogger("Minecraft");
		
		setupPermissions();
		
		registerEvents();
		
		settings = new Settings();
		database = new Database(this);
		commandHandler = new CommandHandler(this);
		
		log.info("Cityscape loaded.");
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, 
			String commandLabel, String[] args) {
		return commandHandler.handleCommand(sender, cmd, commandLabel, args);
	}
	
	public PlayerCache getCache(String playerName) {
		return locationCache.get(playerName);
	}
	
	public CommandHandler getCommandHandler() {
		return commandHandler;
	}
	
	public Database getDB() {
		return database;
	}
	
	public Settings getSettings() {
		return settings;
	}
	
	public void insertIntoCache(String playerName, PlayerCache playerCache) {
		locationCache.put(playerName, playerCache);
	}
	
	private void registerEvents() {
		PluginManager pm = getServer().getPluginManager();
		CSServerListener serverListener = new CSServerListener(this);
		CSPlayerListener playerListener = new CSPlayerListener(this);
		CSBlockListener blockListener = new CSBlockListener(this);
		
		pm.registerEvent(Type.PLUGIN_ENABLE, serverListener, Priority.Monitor, this);
		pm.registerEvent(Type.PLUGIN_DISABLE, serverListener, Priority.Monitor, this);
		
		pm.registerEvent(Type.PLAYER_JOIN, playerListener, Priority.Normal, this);
		pm.registerEvent(Type.PLAYER_QUIT, playerListener, Priority.Normal, this);
		pm.registerEvent(Type.PLAYER_MOVE, playerListener, Priority.Normal, this);
		pm.registerEvent(Type.PLAYER_BUCKET_EMPTY, playerListener, Priority.High, this);
		pm.registerEvent(Type.PLAYER_BUCKET_FILL, playerListener, Priority.High, this);
		
		pm.registerEvent(Type.BLOCK_BREAK, blockListener, Priority.High, this);
		pm.registerEvent(Type.BLOCK_PLACE, blockListener, Priority.High, this);
	}
	
	public void removeFromCache(String playerName) {
		locationCache.remove(playerName);
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
