package com.sethcran.cityscape.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.sethcran.cityscape.Cityscape;
import com.sethcran.cityscape.Constants;

public class CityChat extends CSCommand {

	public CityChat(Cityscape plugin) {
		super(plugin);
		name = "cc";
		description = "Sends a message to all members of your city.";
		usage = "/cc message";
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		org.bukkit.entity.Player player = null;
		if(sender instanceof org.bukkit.entity.Player)
			player = (org.bukkit.entity.Player)sender;
		else {
			sender.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"Only players in game can do that.");
			return;
		}
		
		String playerCity = plugin.getCache(player.getName()).getCity();
		if(playerCity == null) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"You are not in a city.");
			return;
		}
		
		String message = new String();
		message += ChatColor.AQUA + "[" + player.getName() + "] " + 
				ChatColor.WHITE;
		String m = new String();
		for(String a : args)
			m += a + " ";
		message += m;
		
		plugin.sendMessageToCity(message, playerCity);
		plugin.addCityChatLogEntry(playerCity, player.getName(), m);
	}

}
