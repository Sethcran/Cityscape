package com.sethcran.cityscape.commands.citycommands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.iConomy.iConomy;
import com.iConomy.system.Holdings;
import com.sethcran.cityscape.City;
import com.sethcran.cityscape.Cityscape;
import com.sethcran.cityscape.Constants;
import com.sethcran.cityscape.PlayerCache;
import com.sethcran.cityscape.commands.CSCommand;

public class BuyClaims extends CSCommand {

	public BuyClaims(Cityscape plugin) {
		super(plugin);
		name = "buyclaims";
		description = "Buys the selected number of claims for your city.";
		usage = "/c buyclaims amount";
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
		
		if(args == null) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"You must provide a number of claims to buy.");
			return;
		}
		
		if(args.length != 1) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"That command only takes 1 argument.");
			return;
		}
		
		if(args[0].equalsIgnoreCase("price")) {
			player.sendMessage(Constants.CITYSCAPE + "Price per claim: " + 
					plugin.getSettings().claimCost);
			return;
		}
		
		double total;
		int claims;
		try {
			claims = Integer.parseInt(args[0]);
		} catch(NumberFormatException e) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"The number must be an integer.");
			return;
		}
		total = claims * plugin.getSettings().claimCost;
		
		Holdings balance = iConomy.getAccount(player.getName()).getHoldings();
		
		if(!balance.hasEnough(total)) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"You do not have enough. That takes " + iConomy.format(total) + ".");
			return;
		}
		
		balance.subtract(total);
		city.setBonusClaims(city.getBonusClaims() + claims);
		plugin.getDB().setBonusClaims(city.getName(), city.getBonusClaims());
		
		player.sendMessage(Constants.CITYSCAPE + Constants.SUCCESS_COLOR +
				"You have bought " + claims + " claims for " + iConomy.format(total) + ".");
	}

}
