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
import com.sethcran.cityscape.database.CSClaims;
import com.sethcran.cityscape.database.CSPlayerCityData;
import com.sethcran.cityscape.database.CSPlayers;
import com.sethcran.cityscape.database.CSResidents;

public class CSPlayerListener extends PlayerListener {
	private Cityscape plugin = null;
	
	public CSPlayerListener(Cityscape plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public void onPlayerJoin(PlayerJoinEvent event) {
		CSPlayers csplayers = plugin.getDB().getCSPlayers();
		Player player = event.getPlayer();
		String playerName = player.getName();
		Location curLoc = player.getLocation();
		
		if(csplayers.doesPlayerExist(playerName)) {
			csplayers.updatePlayerTimeStamp(playerName);
		}
		else {
			CSPlayerCityData csplayercitydata = plugin.getDB().getCSPlayerCityData();
			CSResidents csresidents = plugin.getDB().getCSResidents();
			
			csplayers.insertNewPlayer(playerName);
			csresidents.insertNewPlayer(playerName);
			csplayercitydata.removePlayerFromCity(playerName);
		}
		
		CSClaims csclaims = plugin.getDB().getCSClaims();
		String currentCityLoc = csclaims.getCityAt(curLoc.getBlockX(), curLoc.getBlockZ());
		
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
		CSClaims csclaims = plugin.getDB().getCSClaims();
		
		PlayerCache cache = plugin.getCache(player.getName());
		String lastTown = cache.getLastTownLocation();	
		String city = csclaims.getCityAt(chunk.getX(), chunk.getZ());
		
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
