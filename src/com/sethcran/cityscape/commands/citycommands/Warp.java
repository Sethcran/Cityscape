package com.sethcran.cityscape.commands.citycommands;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sethcran.cityscape.City;
import com.sethcran.cityscape.Cityscape;
import com.sethcran.cityscape.Constants;
import com.sethcran.cityscape.commands.CSCommand;
import com.sethcran.cityscape.error.ErrorManager;
import com.sethcran.cityscape.error.ErrorManager.CSError;

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
			ErrorManager.sendError(sender, CSError.IN_GAME_ONLY, null);
			return;
		}
		
		City city = null;
		
		if(args == null) {
			city = plugin.getCity(plugin.getCache(player.getName()).getCity());
			if(city == null) {
				ErrorManager.sendError(sender, CSError.NOT_IN_CITY, null);
				return;
			}
		}
		else if(args.length != 1){
			ErrorManager.sendError(sender, CSError.TOO_MANY_ARGUMENTS, null);
			sender.sendMessage(Constants.ERROR_COLOR + usage);
			return;
		}
		else {
			city = plugin.getCity(args[0]);
		}
		
		if(city == null) {
			ErrorManager.sendError(sender, CSError.CITY_DOES_NOT_EXIST_NS, null);
			return;
		}
		
		if(city.getWorld() == null) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"That city has not set a warp yet.");
			return;
		}
		
		if(city.isBanned(player.getName())) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"You are banned from entering " + city.getName() + ".");
			return;
		}
		
		Location location = new Location(plugin.getServer().getWorld(city.getWorld()), 
				city.getSpawnX(), city.getSpawnY(), city.getSpawnZ(), 
				city.getSpawnYaw(), city.getSpawnPitch());
		
		player.teleport(location);
		player.sendMessage(Constants.CITYSCAPE + Constants.SUCCESS_COLOR +
				"You have warped to " + city.getName() + ".");
		plugin.addLogEntry("CITY", player.getName() + " warped to " +  city.getName());
		
	}

}
