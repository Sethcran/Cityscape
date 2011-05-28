package com.sethcran.cityscape.listeners;

import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;

import com.sethcran.cityscape.Cityscape;
import com.sethcran.cityscape.database.CSPlayerCityData;
import com.sethcran.cityscape.database.CSPlayers;

public class CSPlayerListener extends PlayerListener {
	private Cityscape plugin = null;
	
	public CSPlayerListener(Cityscape plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public void onPlayerJoin(PlayerJoinEvent event) {
		CSPlayers csp = new CSPlayers(plugin.getDB().getConnection(), plugin.getSettings());
		String playerName = event.getPlayer().getName();
		
		if(csp.doesPlayerExist(playerName)) {
			csp.updatePlayerTimeStamp(playerName);
		}
		else {
			csp.insertNewPlayer(playerName);
			CSPlayerCityData cspcd = new CSPlayerCityData(plugin.getDB().getConnection(),
					plugin.getSettings());
			cspcd.removePlayerFromCity(playerName);
		}
	}
}
