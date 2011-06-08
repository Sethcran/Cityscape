package com.sethcran.cityscape.listeners;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SnowFormEvent;

import com.sethcran.cityscape.City;
import com.sethcran.cityscape.Cityscape;
import com.sethcran.cityscape.Constants;
import com.sethcran.cityscape.Permissions;
import com.sethcran.cityscape.Plot;
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
		Chunk chunk = block.getChunk();
		
		City city = plugin.getCityAt(chunk.getX(), chunk.getZ(), chunk.getWorld().getName());
		
		if(city == null) {
			return;
		}
		
		String playerCity = plugin.getCache(player.getName()).getCity();
		
		Plot plot = city.getPlotAt(block.getX(), block.getZ());
		if(plot != null) {
			if(playerCity == null) {
				if(!plot.isOutsiderDestroy()) {
					Permissions perms = plot.getPlayerPermissions(player.getName());
					if(perms == null) {
						event.setCancelled(true);
						player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
								"You can't destroy here.");
						return;
					}
					else if(!perms.isCanDestroy()) {					
						event.setCancelled(true);
						player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
								"You can't destroy here.");
						return;
					}
				}
			}
			else if(city.getName().equals(playerCity)) {
				RankPermissions rp = plugin.getPermissions(player.getName());
				if(rp != null) {			
					if(rp.isCityDestroy()) {
						return;
					}
				}
				if(!plot.isResidentDestroy()) {
					Permissions perms = plot.getPlayerPermissions(player.getName());
					if(perms == null) {
						event.setCancelled(true);
						player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
								"You can't destroy here.");
						return;
					}
					else if(!perms.isCanDestroy()) {
						event.setCancelled(true);
						player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
								"You can't destroy here.");
						return;
					}
				}
			}
			else {
				if(!plot.isOutsiderDestroy()) {
					Permissions perms = plot.getPlayerPermissions(player.getName());
					if(perms == null) {
						Permissions cityperms = plot.getCityPermissions(playerCity);
						if(cityperms == null) {
							event.setCancelled(true);
							player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
									"You can't destroy here.");
							return;
						}
						else if(!cityperms.isCanDestroy()) {
							event.setCancelled(true);
							player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
									"You can't destroy here.");
							return;
						}
					}
					else if(!perms.isCanDestroy()) {
						event.setCancelled(true);
						player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
								"You can't destroy here.");
						return;
					}
				}
			}
			return;
		}
		
		if(playerCity == null) {
			if(!city.isOutsiderDestroy()) {
				event.setCancelled(true);
				player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
						"You can't destroy here.");
				return;
			}
		}
		
		if(city.getName().equals(playerCity)) {
			if(!city.isResidentDestroy()) {
				RankPermissions rp = plugin.getPermissions(player.getName());
				if(rp != null) {			
					if(!rp.isCityDestroy()) {
						player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR + 
								"You can't destroy here.");
						event.setCancelled(true);
						return;
					}
				}
				else {
					player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR + 
							"You can't destroy here.");
					event.setCancelled(true);
					return;
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
	public void onBlockDamage(BlockDamageEvent event) {
		Player player = event.getPlayer();
		if(!plugin.isSendingChests(player.getName()))
			return;
		
		BlockState state = event.getBlock().getState();
		Chest chest = null;
		if(state instanceof Chest) {
			chest = (Chest)state;
			plugin.getDB().addChest(chest, player.getName());
			chest.getInventory().clear();
			chest.getBlock().setType(Material.AIR);
		}
	}
	
	@Override
	public void onBlockPlace(BlockPlaceEvent event) {
		if(event.isCancelled())
			return;
		
		Player player = event.getPlayer();
		Block block = event.getBlock();
		Chunk chunk = block.getChunk();
		
		City city = plugin.getCityAt(chunk.getX(), chunk.getZ(), chunk.getWorld().getName());
		
		if(city == null)
			return;
		
		String playerCity = plugin.getCache(player.getName()).getCity();
		
		Plot plot = city.getPlotAt(block.getX(), block.getZ());
		if(plot != null) {
			if(playerCity == null) {
				if(!plot.isOutsiderBuild()) {
					Permissions perms = plot.getPlayerPermissions(player.getName());
					if(perms == null) {
						event.setCancelled(true);
						player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
								"You can't build here.");
						return;
					}
					else if(!perms.isCanBuild()) {
						event.setCancelled(true);
						player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
								"You can't build here.");
						return;
					}
				}
			}
			else if(city.getName().equals(playerCity)) {
				RankPermissions rp = plugin.getPermissions(player.getName());
				if(rp != null) {			
					if(rp.isCityBuild()) {
						return;
					}
				}
				if(!plot.isResidentBuild()) {
					Permissions perms = plot.getPlayerPermissions(player.getName());
					if(perms == null) {
						event.setCancelled(true);
						player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
								"You can't build here.");
						return;
					}
					else if(!perms.isCanDestroy()) {
						event.setCancelled(true);
						player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
								"You can't build here.");
						return;
					}
				}
			}
			else {
				if(!plot.isOutsiderBuild()) {
					Permissions perms = plot.getPlayerPermissions(player.getName());
					if(perms == null) {
						Permissions cityperms = plot.getCityPermissions(playerCity);
						if(cityperms == null) {
							event.setCancelled(true);
							player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
									"You can't destroy here.");
							return;
						}
						else if(!cityperms.isCanDestroy()) {
							event.setCancelled(true);
							player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
									"You can't destroy here.");
							return;
						}
					}
					else if(!perms.isCanBuild()) {
						event.setCancelled(true);
						player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
								"You can't build here.");
						return;
					}
				}
			}
			return;
		}
		
		if(playerCity == null) {
			if(!city.isOutsiderBuild()) {
				event.setCancelled(true);
				player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
						"You can't build here.");
				return;
			}
		}
		
		if(city.getName().equals(playerCity)) {
			if(!city.isResidentBuild()) {
				RankPermissions rp = plugin.getPermissions(player.getName());
				if(rp != null) {			
					if(!rp.isCityBuild()) {
						player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR + 
								"You can't build here.");
						event.setCancelled(true);
						return;
					}
				}
				else {
					player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR + 
							"You can't build here.");
					event.setCancelled(true);
					return;
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
	
	@Override
	public void onSnowForm(SnowFormEvent event) {
		Block block = event.getBlock();
		Chunk chunk = block.getChunk();
		
		City city = plugin.getCityAt(chunk.getX(), chunk.getZ(), chunk.getWorld().getName());
		
		if(city == null)
			return;
		
		Plot plot = city.getPlotAt(block.getX(), block.getZ());
		
		if(plot != null) {
			if(!plot.isSnow())
				event.setCancelled(true);
			return;
		}
		
		if(!city.isSnow())
			event.setCancelled(true);
	}
}
