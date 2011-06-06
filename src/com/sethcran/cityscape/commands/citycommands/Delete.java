package com.sethcran.cityscape.commands.citycommands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sethcran.cityscape.City;
import com.sethcran.cityscape.Cityscape;
import com.sethcran.cityscape.Constants;
import com.sethcran.cityscape.PlayerCache;
import com.sethcran.cityscape.commands.CSCommand;

public class Delete extends CSCommand {

	public Delete(Cityscape plugin) {
		super(plugin);
		name = "delete";
		description = "Used to delete your city.";
		usage = "/c delete cityname";
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		Player player = null;
		if(sender instanceof Player)
			player = (Player)sender;
		else {
			sender.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"Only a player in game can do that.");
			return;
		}
		
		PlayerCache cache = plugin.getCache(player.getName());
		City city = plugin.getCity(cache.getCity());
		
		if(city == null) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"You must be in a city to do that.");
			return;
		}
		
		if(!city.getMayor().equals(player.getName())) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR + 
					"Only the city mayor can destroy the city.");
			return;
		}
		
		if(args == null) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR + 
					"You must use /c delete " + city.getName() + " to confirm.");
			return;
		}
		
		if(args.length != 1) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR + 
					"That command only takes 1 argument.");
			return;
		}
		
		if(!args[0].equals(city.getName())) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR + 
					"You must use /c delete " + city.getName() + " to confirm.");
			return;
		}
		
		String cityName = city.getName();
		
		plugin.deleteCity(cityName);
		plugin.getDB().deleteCity(cityName);
		plugin.getServer().broadcastMessage(Constants.CITYSCAPE + Constants.SUCCESS_COLOR +
				"The city of " + cityName + " has fallen into ruin.");
	}

}
