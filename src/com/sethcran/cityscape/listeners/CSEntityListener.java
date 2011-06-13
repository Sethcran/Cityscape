package com.sethcran.cityscape.listeners;

import java.util.List;

import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Painting;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityListener;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.painting.PaintingBreakEvent;
import org.bukkit.event.painting.PaintingPlaceEvent;

import com.sethcran.cityscape.City;
import com.sethcran.cityscape.Cityscape;
import com.sethcran.cityscape.Constants;
import com.sethcran.cityscape.Permissions;
import com.sethcran.cityscape.Plot;
import com.sethcran.cityscape.RankPermissions;
import com.sethcran.cityscape.Settings;

public class CSEntityListener extends EntityListener {
	
	private Cityscape plugin = null;
	
	public CSEntityListener(Cityscape plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public void onCreatureSpawn(CreatureSpawnEvent event) {
		CreatureType creature = event.getCreatureType();
		
		if(creature == CreatureType.MONSTER || creature == CreatureType.CREEPER ||
				creature == CreatureType.SKELETON || creature == CreatureType.SPIDER ||
				creature == CreatureType.ZOMBIE || creature == CreatureType.PIG_ZOMBIE ||
				creature == CreatureType.GHAST || creature == CreatureType.SLIME ||
				creature == CreatureType.GIANT) {
			
			Chunk chunk = event.getLocation().getBlock().getChunk();
			City city = plugin.getCityAt(chunk.getX(), chunk.getZ(), 
					chunk.getWorld().getName());
			
			if(city != null) {
				event.getEntity().remove();
			}
		}
	}
	
	@Override
	public void onEntityDamage(EntityDamageEvent event) {
		Entity entity = event.getEntity();
		if(!(entity instanceof Player)) 
			return;
		
		Player player = (Player)entity;
		Chunk chunk = player.getLocation().getBlock().getChunk();
		City city = plugin.getCityAt(chunk.getX(), chunk.getZ(), chunk.getWorld().getName());
		
		if(city == null)
			return;
		
		Settings settings = plugin.getSettings();
		if(settings.cityGod) {
			event.setCancelled(true);
			return;
		}
		
		if(!settings.cityPvp) {
			if(event instanceof EntityDamageByEntityEvent) {
				if(((EntityDamageByEntityEvent)event).getDamager() instanceof Player) {
					event.setCancelled(true);
					return;
				}
			}
		}
	}
	
	@Override
	public void onEntityTarget(EntityTargetEvent event) {
		Entity entity = event.getEntity();
		
		if(entity instanceof Player)
			return;
		
		Chunk chunk = entity.getLocation().getBlock().getChunk();
		
		City city = plugin.getCityAt(chunk.getX(), chunk.getZ(), 
				chunk.getWorld().getName());
		
		if(city != null) {
			entity.remove();
		}
	}
	

	@Override
	public void onPaintingBreak(PaintingBreakEvent event) {
		if(event.isCancelled())
			return;
		
		Painting painting = event.getPainting();
		List<Entity> eList = painting.getNearbyEntities(3, 3, 3);
		Entity e = eList.get(0);
		if(!(e instanceof Player))
			return;
		
		Player player = (Player)e;
		
		Block block = painting.getLocation().getBlock();
		Chunk chunk = block.getChunk();
		
		City city = plugin.getCityAt(chunk.getX(), chunk.getZ(), chunk.getWorld().getName());
		
		if(city == null)
			return;
		
		if(plugin.permissionHandler.has(player, "cityscape.bypass"))
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
				if(plot.getOwnerName().equals(player.getName())) {
					return;
				}
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
	public void onPaintingPlace(PaintingPlaceEvent event) {
		if(event.isCancelled())
			return;
		
		Player player= event.getPlayer();
		Block block = event.getPainting().getLocation().getBlock();
		Chunk chunk = block.getChunk();
		
		City city = plugin.getCityAt(chunk.getX(), chunk.getZ(), chunk.getWorld().getName());
		
		if(city == null)
			return;
		
		if(plugin.permissionHandler.has(player, "cityscape.bypass"))
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
				if(plot.getOwnerName().equals(player.getName())) {
					return;
				}
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
}
