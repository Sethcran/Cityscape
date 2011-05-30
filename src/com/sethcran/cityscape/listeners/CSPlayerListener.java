package com.sethcran.cityscape.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.sethcran.cityscape.Cityscape;
import com.sethcran.cityscape.PlayerCache;
import com.sethcran.cityscape.database.Database;

public class CSPlayerListener extends PlayerListener {
	private Cityscape plugin = null;
	
	public CSPlayerListener(Cityscape plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public void onPlayerJoin(PlayerJoinEvent event) {
		Database db = plugin.getDB();
		Player player = event.getPlayer();
		String playerName = player.getName();
		Location curLoc = player.getLocation();
		
		if(db.doesPlayerExist(playerName)) {
			db.updatePlayerTimeStamp(playerName);
		}
		else {			
			db.insertNewPlayer(playerName);
		}
		
		String currentCityLoc = db.getCityAt(curLoc.getBlockX(), curLoc.getBlockZ());
		
		plugin.insertIntoCache(playerName, 
				new PlayerCache(curLoc, null, currentCityLoc));
	}
	
	@Override
	public void onPlayerMove(PlayerMoveEvent event) {  
		Location from = event.getFrom();
		Location to = event.getTo();
		
		if(from.getBlockX() == to.getBlockX() && from.getBlockZ() == to.getBlockZ())
			return;
		
		Player player = event.getPlayer();		
		Chunk chunk = to.getBlock().getChunk();
		Database db = plugin.getDB();
		
		PlayerCache cache = plugin.getCache(player.getName());
		String lastTown = cache.getLastTownLocation();	
		String city = db.getCityAt(chunk.getX(), chunk.getZ());
		
		if(lastTown == null) {
			if(city != null) {
				player.sendMessage(ChatColor.GREEN + "You have entered the city of " 
						+ city + ".");
				cache.setLastTownLocation(city);
			}
		}
		else {
			if(city == null) {
				player.sendMessage(ChatColor.GREEN + "You have entered the wilderness.");
				cache.setLastTownLocation(null);
			}
			else {
				if(!lastTown.equals(city)) {
					player.sendMessage(ChatColor.GREEN + "You have entered the city of " + 
							city + ".");
					cache.setLastTownLocation(city);
				}
			}
				
		}
		cache.setLastLocation(to);
	}
	
	@Override
	public void onPlayerQuit(PlayerQuitEvent event) {
		plugin.removeFromCache(event.getPlayer().getName());
	}
}
