package com.sethcran.cityscape.commands.citycommands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sethcran.cityscape.City;
import com.sethcran.cityscape.Cityscape;
import com.sethcran.cityscape.Constants;
import com.sethcran.cityscape.commands.CSCommand;

public class Default extends CSCommand {

	public Default(Cityscape plugin) {
		super(plugin);
		name = "default";
		description = "Information on your current city status.";
		usage = "/city";
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(args == null) {
			playerCity(sender);
			return;
		}
		
		if(args.length > 1) {
			sender.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"That command only needs a city name.");
			return;
		}
		
		selectedCity(sender, args[0]);
	}
	
	public void displayCityToSender(CommandSender sender, City city) {
		if(city == null) {
			sender.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"That city does not exist.");
			return;
		}
		
		sender.sendMessage(Constants.CITYSCAPE + Constants.TOWN_COLOR +
			city.getName() + " Info: ");
		sender.sendMessage(Constants.GROUP_COLOR + "City Size: " + Constants.MESSAGE_COLOR +
				city.getUsedClaims() + " / " + ( city.getBaseClaims() + 
				city.getBonusClaims() ) + ChatColor.AQUA + " [Bonus: " +
				city.getBonusClaims() + "]");
		
		sender.sendMessage(Constants.GROUP_COLOR + "Mayor: " + Constants.MESSAGE_COLOR +
				city.getMayor());
		
		sender.sendMessage(Constants.GROUP_COLOR + "Permissions:");
		
		if(city.isOutsiderBuild())
			sender.sendMessage(Constants.MESSAGE_COLOR + "Everyone can build.");
		else if(city.isResidentBuild())
			sender.sendMessage(Constants.MESSAGE_COLOR + "Residents can build.");
		else
			sender.sendMessage(Constants.MESSAGE_COLOR + "Nobody can build.");
		
		if(city.isOutsiderDestroy())
			sender.sendMessage(Constants.MESSAGE_COLOR + "Everyone can destroy.");
		else if(city.isResidentDestroy())
			sender.sendMessage(Constants.MESSAGE_COLOR + "Residents can destroy.");
		else
			sender.sendMessage(Constants.MESSAGE_COLOR + "Nobody can destroy.");
		
		if(city.isOutsiderSwitch())
			sender.sendMessage(Constants.MESSAGE_COLOR + "Everyone can switch.");
		else if(city.isResidentSwitch())
			sender.sendMessage(Constants.MESSAGE_COLOR + "Residents can switch.");
		else
			sender.sendMessage(Constants.MESSAGE_COLOR + "Nobody can switch.");
				
	}
	
	public void playerCity(CommandSender sender) {
		Player player = null;
		if(sender instanceof Player)
			player = (Player)sender;
		else {
			sender.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"You are not in a city because you are not in game =p");
			return;
		}
		
		City city = plugin.getCity(plugin.getCache(player.getName()).getCity());
		if(city == null) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"You are not in a city.");
			return;
		}
		else
			displayCityToSender(sender, city);
	}
	
	public void selectedCity(CommandSender sender, String args) {
		if(args == null) {
			sender.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR + 
					"You are not in a city!");
			return;
		}
		City city = plugin.getCity(args);
		if(city == null) {
			sender.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"That city does not exist: " + args);
			return;
		}
		displayCityToSender(sender, city);
	}

}
