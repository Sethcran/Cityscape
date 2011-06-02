package com.sethcran.cityscape.commands.citycommands;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sethcran.cityscape.City;
import com.sethcran.cityscape.Cityscape;
import com.sethcran.cityscape.Constants;
import com.sethcran.cityscape.commands.CSCommand;

public class Residents extends CSCommand {

	public Residents(Cityscape plugin) {
		super(plugin);
		name = "residents";
		description = "Lists all of the residents within your city.";
		usage = "/c residents";
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		Player player = null;
		if(sender instanceof Player)
			player = (Player)sender;
		else {
			sender.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"You are not in a city because you are not in game =p");
			return;
		}
		
		String city = null;
		
		if(args != null) {
			if(args.length > 1) {
				player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
						"That command does not take arguments.");
				return;
			}
			City c = plugin.getCity(args[0]);
			if(c == null) {
				player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
						"That city does not exist.");
				return;
			}
			city = c.getName();
		}
		else
			city = plugin.getCache(player.getName()).getCity();
		
		if(city == null) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"You are not in a city.");
			return;
		}
		
		ArrayList<String> residents = plugin.getDB().getResidents(city);
		
		if(residents.size() == 0) {
			sender.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"That city does not exist.");
			return;
		}
		
		sender.sendMessage(Constants.CITYSCAPE + Constants.SUCCESS_COLOR +
				"Residents of " + city + ":");
		
		String message = "" + ChatColor.WHITE;
		for(String resident : residents) {
			if(message.length() > Constants.CHAT_LINE_LENGTH) {
				sender.sendMessage(message);
				message = "" + ChatColor.WHITE;
			}
			else if(!message.equals("" + ChatColor.WHITE))
				message = message.concat(", ");
			message += resident;
		}
		
		if(!message.equals("" + ChatColor.WHITE))
			sender.sendMessage(message);
	}
}
