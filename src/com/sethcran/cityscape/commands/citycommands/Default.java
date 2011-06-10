package com.sethcran.cityscape.commands.citycommands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.iConomy.iConomy;
import com.iConomy.system.Holdings;
import com.sethcran.cityscape.City;
import com.sethcran.cityscape.Cityscape;
import com.sethcran.cityscape.Constants;
import com.sethcran.cityscape.commands.CSCommand;
import com.sethcran.cityscape.error.ErrorManager;
import com.sethcran.cityscape.error.ErrorManager.CSError;

public class Default extends CSCommand {

	public Default(Cityscape plugin) {
		super(plugin);
		name = "default";
		description = "Information on your current city status.";
		usage = "/city (cityname)";
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(args == null) {
			playerCity(sender);
			return;
		}
		
		if(args.length > 1) {
			ErrorManager.sendError(sender, CSError.TOO_MANY_ARGUMENTS, null);
			sender.sendMessage(Constants.ERROR_COLOR + usage);
			return;
		}
		
		selectedCity(sender, args[0]);
	}
	
	public void displayCityToSender(CommandSender sender, City city) {
		if(city == null) {
			ErrorManager.sendError(sender, CSError.CITY_DOES_NOT_EXIST_NS, null);
			return;
		}
		
		sender.sendMessage(Constants.CITYSCAPE + Constants.TOWN_COLOR +
			city.getName() + " Info: ");
		sender.sendMessage(Constants.GROUP_COLOR + "City Size: " + Constants.MESSAGE_COLOR +
				city.getUsedClaims() + " / " + ( city.getBaseClaims() + 
				city.getBonusClaims() ) + ChatColor.AQUA + " [Bonus: " +
				city.getBonusClaims() + "]");
		
		Holdings balance = city.getAccount().getHoldings();
		sender.sendMessage(Constants.GROUP_COLOR + "Mayor: " + Constants.MESSAGE_COLOR +
				city.getMayor() + "  " + Constants.GROUP_COLOR + "Bank: " + 
				Constants.MESSAGE_COLOR + iConomy.format(balance.balance()));
		
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
			ErrorManager.sendError(sender, CSError.IN_GAME_ONLY, null);
			return;
		}
		
		City city = plugin.getCity(plugin.getCache(player.getName()).getCity());
		if(city == null) {
			ErrorManager.sendError(sender, CSError.NOT_IN_CITY, null);
			return;
		}
		else
			displayCityToSender(sender, city);
	}
	
	public void selectedCity(CommandSender sender, String args) {
		if(args == null) {
			ErrorManager.sendError(sender, CSError.NOT_IN_CITY, null);
			return;
		}
		City city = plugin.getCity(args);
		if(city == null) {
			ErrorManager.sendError(sender, CSError.CITY_DOES_NOT_EXIST, args);
			return;
		}
		displayCityToSender(sender, city);
	}

}
