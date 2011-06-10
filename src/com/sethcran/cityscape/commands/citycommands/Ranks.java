package com.sethcran.cityscape.commands.citycommands;

import java.util.ArrayList;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sethcran.cityscape.City;
import com.sethcran.cityscape.Cityscape;
import com.sethcran.cityscape.Constants;
import com.sethcran.cityscape.commands.CSCommand;
import com.sethcran.cityscape.error.ErrorManager;
import com.sethcran.cityscape.error.ErrorManager.CSError;

public class Ranks extends CSCommand {

	public Ranks(Cityscape plugin) {
		super(plugin);
		name = "ranks";
		description = "View a list of all of the ranks in a given city.";
		usage = "/c ranks (cityName)";
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(args == null) {
			Player player = null;
			if(sender instanceof Player)
				player = (Player)sender;
			else {
				ErrorManager.sendError(sender, CSError.IN_GAME_ONLY, null);
				return;
			}
			String city = plugin.getCache(player.getName()).getCity();
			if(city == null) {
				ErrorManager.sendError(sender, CSError.NOT_IN_CITY, null);
				return;
			}
			displayRanks(sender, city);
			return;
		}
		
		if(args.length > 1) {
			ErrorManager.sendError(sender, CSError.TOO_MANY_ARGUMENTS, null);
			sender.sendMessage(Constants.ERROR_COLOR + usage);
			return;
		}
		
		if(plugin.getCity(args[0]) == null) {
			ErrorManager.sendError(sender, CSError.CITY_DOES_NOT_EXIST, args[0]);
			return;
		}
		displayRanks(sender, args[0]);
	}
	
	public void displayRanks(CommandSender sender, String cityName) {
		City city = plugin.getCity(cityName);
		ArrayList<String> ranks = city.getRanks();
		sender.sendMessage(Constants.CITYSCAPE + Constants.SUCCESS_COLOR +
				"Ranks in the city of " + cityName + ":");
		for(String rank : ranks) {
			sender.sendMessage(Constants.SUCCESS_COLOR + rank);
		}
	}

}
