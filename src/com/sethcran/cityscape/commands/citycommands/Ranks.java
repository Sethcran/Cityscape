package com.sethcran.cityscape.commands.citycommands;

import java.util.ArrayList;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sethcran.cityscape.Cityscape;
import com.sethcran.cityscape.Constants;
import com.sethcran.cityscape.commands.CSCommand;

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
				sender.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
						"Please provide a city name.");
				return;
			}
			String city = plugin.getCache(player.getName()).getCity();
			if(city == null) {
				player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
						"You are not currently in a city!");
				return;
			}
			displayRanks(sender, city);
			return;
		}
		
		if(args.length > 1) {
			sender.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"That command only needs a city name.");
			return;
		}
		
		if(plugin.getCity(args[0]) == null) {
			sender.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"That city does not exist.");
			return;
		}
		displayRanks(sender, args[0]);
	}
	
	public void displayRanks(CommandSender sender, String city) {
		ArrayList<String> ranks = plugin.getDB().getRanks(city);
		sender.sendMessage(Constants.CITYSCAPE + Constants.SUCCESS_COLOR +
				"Ranks in the city of " + city + ":");
		for(String rank : ranks) {
			sender.sendMessage(Constants.SUCCESS_COLOR + rank);
		}
	}

}
