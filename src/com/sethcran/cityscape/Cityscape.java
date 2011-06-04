package com.sethcran.cityscape;

import gnu.trove.TIntObjectHashMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.iConomy.iConomy;
import com.infomatiq.jsi.Rectangle;
import com.infomatiq.jsi.rtree.RTree;
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
	private HashMap<String, Selection> selectionMap = null;
	private HashMap<String, PlayerCache> playerCache = null;
	private HashMap<String, City> cityCache = null;
	private TIntObjectHashMap<Claim> claimMap = null;
	private RTree claimTree = null;

	public void addClaim(Claim claim) {
		claimMap.put(claim.getId(), claim);
		claimTree.add(new Rectangle(claim.getXmin(), claim.getZmin(), claim.getXmax(),
				claim.getZmax()), claim.getId());
	}
	
	public void addUsedClaim(String cityName) {
		City city = getCity(cityName);
		city.setUsedClaims(city.getUsedClaims() + 1);
	}
	
	public void changePlayerCityInCache(String playerName, String cityName) {
		getCache(playerName).setCity(cityName);
	}

	public PlayerCache getCache(String playerName) {
		return playerCache.get(playerName);
	}
	
	public City getCity(String cityName) {
		return cityCache.get(cityName);
	}
	
	public City getCityAt(int x, int z, String world) {
		TreeProcedure tproc = new TreeProcedure();
		claimTree.intersects(new Rectangle(x, z, x, z), tproc);
		for(int i : tproc.getId()) {
			Claim claim = claimMap.get(i);
			if(claim.getWorld().equals(world))
				return getCity(claim.getCityName());
		}
		return null;
	}
	
	public Claim getClaimAt(int x, int z, String world) {
		TreeProcedure tproc = new TreeProcedure();
		claimTree.intersects(new Rectangle(x, z, x, z), tproc);
		for(int i : tproc.getId()) {
			Claim claim = claimMap.get(i);
			if(claim.getWorld().equals(world))
				return claim;
		}
		return null;
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
	
	public boolean isChunkClaimed(int xmin, int zmin, int xmax, int zmax, String world) {
		TreeProcedure tproc = new TreeProcedure();
		claimTree.intersects(new Rectangle(xmin, zmin, xmax, zmax), tproc);
		if(tproc.getId().size() == 0)
			return false;
		else {
			for(int i : tproc.getId()) {
				Claim claim = claimMap.get(i);
				if(claim.getWorld().equals(world))
					return true;
			}
		}
		return false;	
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
		claimMap = new TIntObjectHashMap<Claim>();
		claimTree = new RTree();
		
		claimTree.init(null);
		
		populateCityCache();
		populateClaimsCache();
		
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
		pm.registerEvent(Type.PLAYER_INTERACT, playerListener, Priority.Normal, this);
		pm.registerEvent(Type.PLAYER_BUCKET_EMPTY, playerListener, Priority.High, this);
		pm.registerEvent(Type.PLAYER_BUCKET_FILL, playerListener, Priority.High, this);
		
		pm.registerEvent(Type.BLOCK_BREAK, blockListener, Priority.High, this);
		pm.registerEvent(Type.BLOCK_PLACE, blockListener, Priority.High, this);
		pm.registerEvent(Type.SNOW_FORM, blockListener, Priority.High, this);
	}
	
	public void removeClaim(Claim claim) {
		claimMap.remove(claim.getId());
		claimTree.delete(new Rectangle(claim.getXmin(), claim.getZmin(), claim.getXmax(),
				claim.getZmax()), claim.getId());
		City city = getCity(claim.getCityName());
		city.setUsedClaims(city.getUsedClaims() - 1);
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
}
