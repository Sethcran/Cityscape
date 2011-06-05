package com.sethcran.cityscape.commands.citycommands;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sethcran.cityscape.City;
import com.sethcran.cityscape.Cityscape;
import com.sethcran.cityscape.Constants;
import com.sethcran.cityscape.PlayerCache;
import com.sethcran.cityscape.RankPermissions;
import com.sethcran.cityscape.commands.CSCommand;

public class Setwarp extends CSCommand {

	public Setwarp(Cityscape plugin) {
		super(plugin);
		name = "setwarp";
		description = "Sets the town warp to the current location.";
		usage = "/c setwarp";
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		Player player = null;
		if(sender instanceof Player)
			player = (Player)sender;
		else {
			sender.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"You must be in game to do that.");
			return;
		}
		
		PlayerCache cache = plugin.getCache(player.getName());
		City playerCity = plugin.getCity(cache.getCity());
		if(playerCity == null) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"You must be in a city to do that.");
			return;
		}
		
		RankPermissions rp = playerCity.getRank(cache.getRank());
		
		if(rp == null) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"You do not have permission to do that.");
			return;
		}
		
		if(!rp.isSetWarp()) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
			"You do not have permission to do that.");
			return;
		}
		
		Location location = player.getLocation();
		City localCity = plugin.getCityAt(location.getBlockX(), location.getBlockZ(), 
				location.getWorld().getName());
		
		if(localCity == null) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"You must be standing in your city to do that.");
			return;
		}
		
		if(!localCity.getName().equals(playerCity.getName())) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"You must be standing in your city to do that.");
			return;
		}
		
		localCity.setSpawnX(location.getBlockX());
		localCity.setSpawnY(location.getBlockY());
		localCity.setSpawnZ(location.getBlockZ());
		localCity.setWorld(location.getWorld().getName());
		plugin.getDB().setWarp(localCity);
		
		player.sendMessage(Constants.CITYSCAPE + Constants.SUCCESS_COLOR +
				"You have set your city warp.");
		return;
	}

}
