package com.sethcran.cityscape.commands.citycommands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sethcran.cityscape.City;
import com.sethcran.cityscape.Cityscape;
import com.sethcran.cityscape.Constants;
import com.sethcran.cityscape.PlayerCache;
import com.sethcran.cityscape.RankPermissions;
import com.sethcran.cityscape.commands.CSCommand;

public class Welcome extends CSCommand {

	public Welcome(Cityscape plugin) {
		super(plugin);
		name = "welcome";
		description = "Sets the city welcome message.";
		usage = "/c welcome message";
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		Player player = null;
		if(sender instanceof Player)
			player = (Player)sender;
		else {
			sender.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"Only players in game can do that.");
			return;
		}
		
		PlayerCache cache = plugin.getCache(player.getName());
		City city = plugin.getCity(cache.getCity());
		
		if(city == null) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"You must be in a city to do that.");
			return;
		}
		
		RankPermissions rp = city.getRank(cache.getRank());
		
		if(rp == null) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"You do not have permission to do that.");
			return;
		}
		
		if(!rp.isSetWelcome()) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"You do not have permission to do that.");
			return;
		}
		
		if(args == null) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"That command requires a message.");
			return;
		}
		
		String total = new String();
		for(String s : args)
			total += s + " ";
		if(total.length() > Constants.WELCOME_MESSAGE_LENGTH) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"The message can't be longer than 100 characters.");
			return;
		}
		
		city.setWelcome(total);
		plugin.getDB().setWelcome(city.getName(), total);
		
		plugin.sendMessageToCity(total, city.getName());
	}

}
