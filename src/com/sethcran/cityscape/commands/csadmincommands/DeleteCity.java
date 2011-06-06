package com.sethcran.cityscape.commands.csadmincommands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sethcran.cityscape.City;
import com.sethcran.cityscape.Cityscape;
import com.sethcran.cityscape.Constants;
import com.sethcran.cityscape.commands.CSCommand;

public class DeleteCity extends CSCommand {

	public DeleteCity(Cityscape plugin) {
		super(plugin);
		name = "deletecity";
		description = "Deletes the selected player city.";
		usage = "/csa deletecity cityname";
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
		
		if(!plugin.permissionHandler.has(player, "cs.admin")) {
			return;
		}
		
		if(args == null) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"Deleting a city requires a city name.");
			return;
		}
		
		if(args.length != 1) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"Deleting a city requires only a city name.");
			return;
		}
		
		City city = plugin.getCity(args[0]);
		
		if(city == null) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"The city " + args[0] + " does not exist.");
			return;
		}
		
		plugin.deleteCity(args[0]);
		plugin.getDB().deleteCity(args[0]);
		
		plugin.getServer().broadcastMessage(Constants.CITYSCAPE + Constants.SUCCESS_COLOR +
				"The city of " + args[0] + " has fallen into ruin.");
		
		player.sendMessage(Constants.CITYSCAPE + Constants.SUCCESS_COLOR + 
				"You have deleted the city of " + args[0] + ".");
	}

}
