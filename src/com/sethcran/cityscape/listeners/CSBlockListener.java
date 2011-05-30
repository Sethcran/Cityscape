package com.sethcran.cityscape.listeners;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlaceEvent;

import com.sethcran.cityscape.City;
import com.sethcran.cityscape.Cityscape;
import com.sethcran.cityscape.Constants;
import com.sethcran.cityscape.RankPermissions;

public class CSBlockListener extends BlockListener {
	private Cityscape plugin = null;
	
	public CSBlockListener(Cityscape plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public void onBlockBreak(BlockBreakEvent event) {
		if(event.isCancelled())
			return;
		
		Player player = event.getPlayer();
		Block block = event.getBlock();
		int x = block.getChunk().getX();
		int z = block.getChunk().getZ();
		
		City city = plugin.getDB().getCityAt(x, z);
		
		if(city == null)
			return;
		
		String cityName = plugin.getDB().getCurrentCity(player.getName());
		
		if(cityName == null) {
			if(!city.isOutsiderDestroy()) {
				event.setCancelled(true);
				player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
						"You can't destroy here.");
				return;
			}
		}
		
		if(city.getName().equals(cityName)) {
			if(!city.isResidentDestroy()) {
				RankPermissions rp = plugin.getDB().getPermissions(player.getName());
				if(!rp.isCityDestroy()) {
					player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR + 
							"You can't destroy here.");
					event.setCancelled(true);
				}
			}
		}
		else {
			if(!city.isOutsiderDestroy()) {
				event.setCancelled(true);
				player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
						"You can't destroy here.");
			}
		}
	}
	
	@Override
	public void onBlockPlace(BlockPlaceEvent event) {
		if(event.isCancelled())
			return;
		
		Player player = event.getPlayer();
		Block block = event.getBlock();
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
