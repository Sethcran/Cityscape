package com.sethcran.cityscape.listeners;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.sethcran.cityscape.City;
import com.sethcran.cityscape.Cityscape;
import com.sethcran.cityscape.Constants;
import com.sethcran.cityscape.Permissions;
import com.sethcran.cityscape.PlayerCache;
import com.sethcran.cityscape.Plot;
import com.sethcran.cityscape.RankPermissions;
import com.sethcran.cityscape.Selection;
import com.sethcran.cityscape.database.Database;

public class CSPlayerListener extends PlayerListener {
	private Cityscape plugin = null;
	
	public CSPlayerListener(Cityscape plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) {
		if(event.isCancelled())
			return;
		
		Player player = event.getPlayer();
		Block block = event.getBlockClicked();
		
		City city = plugin.getCityAt(block.getX(), block.getZ(), block.getWorld().getName());
		
		String playerCity = plugin.getDB().getCurrentCity(player.getName());
		
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
	public void onPlayerBucketFill(PlayerBucketFillEvent event) {
		if(event.isCancelled())
			return;
		
		Player player = event.getPlayer();
		Block block = event.getBlockClicked();
		
		City city = plugin.getCityAt(block.getX(), block.getZ(), block.getWorld().getName());
		
		String playerCity = plugin.getDB().getCurrentCity(player.getName());
		
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
	public void onPlayerInteract(PlayerInteractEvent event) {
		Selection selection = plugin.getSelection(event.getPlayer().getName());
		if(selection == null)
			return;
		
		Player player = event.getPlayer();
		
		if(event.getAction() == Action.LEFT_CLICK_BLOCK) {
			Block block = event.getClickedBlock();
			City city = plugin.getCityAt(block.getX(), block.getZ(), 
					block.getWorld().getName());
			
			if(city == null) {
				player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
						"You must select in your city.");
				return;
			}
			String playerCity = plugin.getCache(player.getName()).getCity();
			if(!city.getName().equals(playerCity)) {
				player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
						"You must select in your city.");
				return;
			}
			
			selection.setFirst(block.getX(), block.getZ(), block.getWorld().getName());
			player.sendMessage(Constants.CITYSCAPE + Constants.SUCCESS_COLOR +
					"Set first position to " + block.getX() + ", " + block.getZ() + ".");
			event.setCancelled(true);
		}
		else if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			Block block = event.getClickedBlock();
			City city = plugin.getCityAt(block.getX(), block.getZ(), 
					block.getWorld().getName());
			
			if(city == null) {
				player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
						"You must select in your city.");
				return;
			}
			String playerCity = plugin.getCache(player.getName()).getCity();
			if(!city.getName().equals(playerCity)) {
				player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
						"You must select in your city.");
				return;
			}
			
			selection.setSecond(block.getX(), block.getZ(), block.getWorld().getName());
			player.sendMessage(Constants.CITYSCAPE + Constants.SUCCESS_COLOR +
					"Set second position to " + block.getX() + ", " + block.getZ() + ".");
			event.setCancelled(true);
		}
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
		
		City city = plugin.getCityAt(curLoc.getBlockX(), curLoc.getBlockZ(), 
				curLoc.getWorld().getName());
		String currentCityLoc = null;
		if(city != null)
			currentCityLoc = city.getName();
		
		String playerCity = db.getPlayerCity(playerName);
		String rank = db.getRank(playerName);
		
		plugin.insertIntoPlayerCache(playerName, 
				new PlayerCache(curLoc, null, currentCityLoc, playerCity, rank));
		
		ArrayList<String> invites = db.getInvites(player.getName());
		if(invites.size() > 0) {
			player.sendMessage(Constants.CITYSCAPE + Constants.SUCCESS_COLOR +
					"You have invitations to join cities. Type '/c invites' to " +
					"view the list of cities who have invited you.");
		}
	}
	
	@Override
	public void onPlayerMove(PlayerMoveEvent event) {  
		Location from = event.getFrom();
		Location to = event.getTo();
		
		if(from.getBlockX() == to.getBlockX() && from.getBlockZ() == to.getBlockZ())
			return;
		
		Player player = event.getPlayer();
		
		PlayerCache cache = plugin.getCache(player.getName());
		String lastTown = cache.getLastTownLocation();	
		City city = plugin.getCityAt(to.getBlockX(), to.getBlockZ(), 
				to.getWorld().getName());
		String currentCityLoc = null;
		if(city != null)
			currentCityLoc = city.getName();
		
		if(lastTown == null) {
			if(currentCityLoc != null) {
				player.sendMessage(ChatColor.GREEN + "You have entered the city of " 
						+ currentCityLoc + ".");
				cache.setLastTownLocation(currentCityLoc);
			}
		}
		else {
			if(currentCityLoc == null) {
				player.sendMessage(ChatColor.GREEN + "You have entered the wilderness.");
				cache.setLastTownLocation(null);
				cache.setLastPlotLocation(null);
			}
			else {
				if(!lastTown.equals(currentCityLoc)) {
					player.sendMessage(ChatColor.GREEN + "You have entered the city of " + 
							currentCityLoc + ".");
					cache.setLastTownLocation(currentCityLoc);
				}
			}
				
		}
		
		cache.setLastLocation(to);
		
		if(city == null)
			return;
		
		Plot plot = city.getPlotAt(to.getBlockX(), to.getBlockZ());
		if(cache.getLastPlotLocation() == null) {
			if(plot != null) {
				player.sendMessage(ChatColor.DARK_GREEN + "You have entered a plot owned" +
						" by " + plot.getOwnerName() + ".");
				cache.setLastPlotLocation(plot.getOwnerName());
			}
		}
		if(plot == null) {
			if(cache.getLastPlotLocation() != null) {
				player.sendMessage(ChatColor.DARK_GREEN + "You have entered city grounds.");
				cache.setLastPlotLocation(null);
			}
		}
		else {
			if(!plot.getOwnerName().equals(cache.getLastPlotLocation())) {
				player.sendMessage(ChatColor.DARK_GREEN + "You have entered a plot owned" +
						" by " + plot.getOwnerName() + ".");
				cache.setLastPlotLocation(plot.getOwnerName());
			}
		}
	}
	
	@Override
	public void onPlayerQuit(PlayerQuitEvent event) {
		plugin.removeFromPlayerCache(event.getPlayer().getName());
		plugin.removeSelection(event.getPlayer().getName());
	}
}
