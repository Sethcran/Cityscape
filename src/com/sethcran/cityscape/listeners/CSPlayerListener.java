package com.sethcran.cityscape.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.sethcran.cityscape.City;
import com.sethcran.cityscape.Cityscape;
import com.sethcran.cityscape.Constants;
import com.sethcran.cityscape.PlayerCache;
import com.sethcran.cityscape.RankPermissions;
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
		
		String currentCityLoc = db.getCityNameAt(curLoc.getBlockX(), curLoc.getBlockZ());
		
		plugin.insertIntoPlayerCache(playerName, 
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
		String city = db.getCityNameAt(chunk.getX(), chunk.getZ());
		
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
		plugin.removeFromPlayerCache(event.getPlayer().getName());
	}
	
	@Override
	public void onPlayerBucketFill(PlayerBucketFillEvent event) {
		if(event.isCancelled())
			return;
		
		Player player = event.getPlayer();
		Block block = event.getBlockClicked();
		int x = block.getChunk().getX();
		int z = block.getChunk().getZ();
		
		City city = plugin.getDB().getCityAt(x, z);
		
		if(city == null)
			return;
		
		String cityName = plugin.getDB().getCurrentCity(player.getName());
		
		if(cityName == null) {
			if(!city.isOutsiderBuild()) {
				event.setCancelled(true);
				player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
						"You can't destroy here.");
				return;
			}
		}
		
		if(city.getName().equals(cityName)) {
			if(!city.isResidentBuild()) {
				RankPermissions rp = plugin.getDB().getPermissions(player.getName());
				if(!rp.isCityBuild()) {
					player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR + 
							"You can't destroy here.");
					event.setCancelled(true);
				}
			}
		}
		else {
			if(!city.isOutsiderBuild()) {
				event.setCancelled(true);
				player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
						"You can't destroy here.");
			}
		}
	}
	
	public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) {
		if(event.isCancelled())
			return;
		
		Player player = event.getPlayer();
		Block block = event.getBlockClicked();
		int x = block.getChunk().getX();
		int z = block.getChunk().getZ();
		
		City city = plugin.getDB().getCityAt(x, z);
		
		if(city == null)
			return;
		
		String cityName = plugin.getDB().getCurrentCity(player.getName());
		
		if(cityName == null) {
			if(!city.isOutsiderBuild()) {
				event.setCancelled(true);
				player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
						"You can't build here.");
				return;
			}
		}
		
		if(city.getName().equals(cityName)) {
			if(!city.isResidentBuild()) {
				RankPermissions rp = plugin.getDB().getPermissions(player.getName());
				if(!rp.isCityBuild()) {
					player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR + 
							"You can't build here.");
					event.setCancelled(true);
				}
			}
		}
		else {
			if(!city.isOutsiderBuild()) {
				event.setCancelled(true);
				player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
						"You can't build here.");
			}
		}
	}
}
