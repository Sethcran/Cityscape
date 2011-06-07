package com.sethcran.cityscape.listeners;

import org.bukkit.Chunk;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityListener;
import org.bukkit.event.entity.EntityTargetEvent;

import com.sethcran.cityscape.City;
import com.sethcran.cityscape.Cityscape;

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
}
