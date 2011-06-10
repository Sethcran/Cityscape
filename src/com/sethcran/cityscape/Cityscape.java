package com.sethcran.cityscape;



import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.iConomy.iConomy;
import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;
import com.sethcran.cityscape.commands.CommandHandler;
import com.sethcran.cityscape.database.Database;
import com.sethcran.cityscape.listeners.CSBlockListener;
import com.sethcran.cityscape.listeners.CSEntityListener;
import com.sethcran.cityscape.listeners.CSPlayerListener;
import com.sethcran.cityscape.listeners.CSServerListener;

public class Cityscape extends JavaPlugin {
	public static Logger log = null;
	public PermissionHandler permissionHandler = null;
	public iConomy iconomy = null;
	
	private Settings settings = null;
	private Database database = null;
	private CommandHandler commandHandler = null;
	private HashMap<String, Selection> selectionMap = null;
	private HashMap<String, PlayerCache> playerCache = null;
	private HashMap<String, City> cityCache = null;
	private HashMap<String, Claim> claimMap = null;

	public void addClaim(Claim claim) {
		claimMap.put(claim.toString(), claim);
		com.sethcran.cityscape.Claim north = getClaimAt(
				claim.getX(), claim.getZ() + 1, claim.getWorld());
		com.sethcran.cityscape.Claim east = getClaimAt(
				claim.getX() + 1, claim.getZ(), claim.getWorld());
		com.sethcran.cityscape.Claim south = getClaimAt(
				claim.getX(), claim.getZ() - 1, claim.getWorld());
		com.sethcran.cityscape.Claim west = getClaimAt(
				claim.getX() - 1, claim.getZ(), claim.getWorld());
		
		if(north != null) {
			if(!north.getCityName().equals(claim.getCityName()))
				north = null;
		}
		if(east != null) {
			if(!east.getCityName().equals(claim.getCityName()))
				east = null;
		}
		if(south != null) {
			if(!south.getCityName().equals(claim.getCityName()))
				south = null;
		}
		if(west != null) {
			if(!west.getCityName().equals(claim.getCityName()))
				west = null;
		}
		getCity(claim.getCityName()).addClaim(claim, north, east, south, west);
	}
	
	public void addUsedClaim(String cityName) {
		City city = getCity(cityName);
		city.setUsedClaims(city.getUsedClaims() + 1);
	}
	
	public void changePlayerCityInCache(String playerName, String cityName) {
		getCache(playerName).setCity(cityName);
	}
	
	public void deleteCity(String city) {
		
		Collection<Claim> cc = claimMap.values();
		Iterator<Claim> iter = cc.iterator();
		while(iter.hasNext()) {
			Claim claim = iter.next();
			if(city.equals(claim.getCityName()))
				iter.remove();
		}
		
		cityCache.remove(city);
		
		for(PlayerCache pc : playerCache.values()) {
			if(city.equals(pc.getCity())) {
				pc.setCity(null);
				pc.setRank(null);
			}
		}
	}

	public PlayerCache getCache(String playerName) {
		return playerCache.get(playerName);
	}
	
	public City getCity(String cityName) {
		return cityCache.get(cityName);
	}
	
	public City getCityAt(int x, int z, String world) {
		Claim claim = claimMap.get(new Claim(world, x, z).toString());
		if(claim == null)
			return null;
		return cityCache.get(claim.getCityName());
	}
	
	public Claim getClaimAt(int x, int z, String world) {
		return claimMap.get(new Claim(world, x, z).toString());
	}
	
	public CommandHandler getCommandHandler() {
		return commandHandler;
	}
	
	public Database getDB() {
		return database;
	}
	
	public RankPermissions getPermissions(String player) {
		PlayerCache cache = getCache(player);
		
		if(cache.getCity() == null)
			return null;
		
		if(cache.getRank() == null)
			return null;
		
		City city = getCity(cache.getCity());
		return city.getRank(cache.getRank());
	}
	
	public Selection getSelection(String player) {
		return selectionMap.get(player);
	}
	
	public Settings getSettings() {
		return settings;
	}
	
	public void insertIntoCityCache(String cityName, City city) {
		cityCache.put(cityName, city);
	}
	
	public void insertIntoPlayerCache(String playerName, PlayerCache playerCache) {
		this.playerCache.put(playerName, playerCache);
	}
	
	public void insertSelection(String player, Selection selection) {
		selectionMap.put(player, selection);
	}
	
	public boolean isChunkClaimed(String world, int x, int z) {
			Claim claim = claimMap.get(new Claim(world, x, z).toString());
			if(claim == null)
				return false;
			else
				return true;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, 
			String commandLabel, String[] args) {
		return commandHandler.handleCommand(sender, cmd, commandLabel, args);
	}
	
	@Override
	public void onDisable() {
		log.info("Cityscape unloaded.");
	}
	
	@Override
	public void onEnable() {
		log = Logger.getLogger("Minecraft");
		
		setupPermissions();
		
		settings = new Settings();
		database = new Database(this);
		commandHandler = new CommandHandler(this);
		selectionMap = new HashMap<String, Selection>();
		playerCache = new HashMap<String, PlayerCache>();
		cityCache = new HashMap<String, City>();
		claimMap = new HashMap<String, Claim>();
		
		populateCityCache();
		populateClaimsCache();
		populatePlayerCache();
		
		registerEvents();
		
		database.deleteOldInvites();
		
		log.info("Cityscape loaded.");
	}
	
	public void populateCityCache() {
		ArrayList<City> cityArray = database.getCities();
		if(cityArray != null) {
			for(City city : cityArray) {
				cityCache.put(city.getName(), city);
			}
		}
	}
	
	public void populateClaimsCache() {
		ArrayList<Claim> claimsArray = database.getClaims();
		if(claimsArray != null) {
			for(Claim claim : claimsArray) {
				addClaim(claim);
			}
		}
	}
	
	public void populatePlayerCache() {
		Player[] players = getServer().getOnlinePlayers();
		CSPlayerListener cpl = new CSPlayerListener(this);
		for(Player p : players) {
			cpl.onPlayerJoin(new PlayerJoinEvent(p, null));
		}
	}
	
	private void registerEvents() {
		PluginManager pm = getServer().getPluginManager();
		CSServerListener serverListener = new CSServerListener(this);
		CSPlayerListener playerListener = new CSPlayerListener(this);
		CSBlockListener blockListener = new CSBlockListener(this);
		CSEntityListener entityListener = new CSEntityListener(this);
		
		pm.registerEvent(Type.PLUGIN_ENABLE, serverListener, Priority.Monitor, this);
		pm.registerEvent(Type.PLUGIN_DISABLE, serverListener, Priority.Monitor, this);
		
		pm.registerEvent(Type.PLAYER_JOIN, playerListener, Priority.Highest, this);
		pm.registerEvent(Type.PLAYER_QUIT, playerListener, Priority.Normal, this);
		pm.registerEvent(Type.PLAYER_MOVE, playerListener, Priority.Normal, this);
		pm.registerEvent(Type.PLAYER_TELEPORT, playerListener, Priority.High, this);
		pm.registerEvent(Type.PLAYER_INTERACT, playerListener, Priority.Normal, this);
		pm.registerEvent(Type.PLAYER_BUCKET_EMPTY, playerListener, Priority.High, this);
		pm.registerEvent(Type.PLAYER_BUCKET_FILL, playerListener, Priority.High, this);
		pm.registerEvent(Type.PLAYER_RESPAWN, playerListener, Priority.High, this);
		
		pm.registerEvent(Type.BLOCK_BREAK, blockListener, Priority.High, this);
		pm.registerEvent(Type.BLOCK_PLACE, blockListener, Priority.High, this);
		pm.registerEvent(Type.SNOW_FORM, blockListener, Priority.High, this);
		
		pm.registerEvent(Type.CREATURE_SPAWN, entityListener, Priority.High, this);
		pm.registerEvent(Type.ENTITY_DAMAGE, entityListener, Priority.High, this);
		pm.registerEvent(Type.ENTITY_TARGET, entityListener, Priority.High, this);
	}
	
	public void removeClaim(Claim claim) {
		claimMap.remove(claim.toString());
		City city = getCity(claim.getCityName());
		city.setUsedClaims(city.getUsedClaims() - 1);
		city.removeClaim(claim);
	}
	
	public void removeFromCityCache(String cityName) {
		cityCache.remove(cityName);
	}
	
	public void removeFromPlayerCache(String playerName) {
		playerCache.remove(playerName);
	}
	
	public void removeSelection(String player) {
		selectionMap.remove(player);
	}
	
	public void renameCity(String oldName, String newName) {
		for(Claim claim : claimMap.values()) {
			if(oldName.equals(claim.getCityName()))
					claim.setCityName(newName);
		}
		
		City city = cityCache.get(oldName);
		cityCache.remove(oldName);
		city.rename(newName);
		cityCache.put(newName, city);
		
		for(PlayerCache pc : playerCache.values()) {
			if(oldName.equals(pc.getCity()))
				pc.setCity(newName);
		}
	}
	
	public void sendMessageToCity(String message, String city) {
		String m = Constants.TOWN_COLOR + "[" + city + "] " + 
				Constants.MESSAGE_COLOR + message;
		Set<Entry<String, PlayerCache>> cache = playerCache.entrySet();
		for(Entry<String, PlayerCache> entry : cache) {
			if(city.equals(entry.getValue().getCity())) {
				Player player = getServer().getPlayer(entry.getKey());
				player.sendMessage(m);
			}
		}
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
	
	public void unclaimAll(String cityName, Claim claim) {
		City city = cityCache.get(cityName);
		
		Collection<Claim> cc = claimMap.values();
		Iterator<Claim> iter = cc.iterator();
		while(iter.hasNext()){
			Claim c = iter.next();
			if(c.getCityName().equals(cityName)) {
				iter.remove();
				city.removeClaim(c);
			}
		}
		claimMap.put(claim.toString(), claim);
		city.addClaim(claim, null, null, null, null);
		
		city.setUsedClaims(1);
	}
}
