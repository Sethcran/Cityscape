package com.sethcran.cityscape.listeners;

import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;

import com.sethcran.cityscape.Cityscape;
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
		String playerName = event.getPlayer().getName();
		
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
	}
}
