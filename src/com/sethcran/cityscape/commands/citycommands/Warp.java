package com.sethcran.cityscape.commands.citycommands;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sethcran.cityscape.City;
import com.sethcran.cityscape.Cityscape;
import com.sethcran.cityscape.Constants;
import com.sethcran.cityscape.commands.CSCommand;

public class Warp extends CSCommand {

	public Warp(Cityscape plugin) {
		super(plugin);
		name = "warp";
		description = "Warps to the selected city's warp.";
		usage = "/c warp [cityname]";
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
		
		City city = null;
		
		if(args == null) {
			city = plugin.getCity(plugin.getCache(player.getName()).getCity());
			if(city == null) {
				player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
						"You are not in a city.");
				return;
			}
		}
		else if(args.length != 1){
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"That command only requires a city name.");
			return;
		}
		else {
			city = plugin.getCity(args[0]);
		}
		
		if(city == null) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"That city does not exist.");
			return;
		}
		
		if(city.getWorld() == null) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"That city has not set a warp yet.");
			return;
		}
		
		Location location = new Location(plugin.getServer().getWorld(city.getWorld()), 
				city.getSpawnX(), city.getSpawnY(), city.getSpawnZ());
		
		player.teleport(location);
		player.sendMessage(Constants.CITYSCAPE + Constants.SUCCESS_COLOR +
				"You have warped to " + city.getName() + ".");
		
	}

}
