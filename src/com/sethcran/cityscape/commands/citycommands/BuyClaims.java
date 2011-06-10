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
import com.sethcran.cityscape.error.ErrorManager;
import com.sethcran.cityscape.error.ErrorManager.CSError;

public class BuyClaims extends CSCommand {

	public BuyClaims(Cityscape plugin) {
		super(plugin);
		name = "buyclaims";
		description = "Buys the selected number of claims for your city.";
		usage = "/c buyclaims amount OR /c buyclaims 'price'";
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		Player player = null;
		if(sender instanceof Player)
			player = (Player)sender;
		else {
			ErrorManager.sendError(sender, CSError.IN_GAME_ONLY, null);
			return;
		}
		
		PlayerCache cache = plugin.getCache(player.getName());
		City city = plugin.getCity(cache.getCity());
		
		if(city == null) {
			ErrorManager.sendError(sender, CSError.NOT_IN_CITY, null);
			return;
		}
		
		if(args == null) {
			ErrorManager.sendError(sender, CSError.NOT_ENOUGH_ARGUMENTS, null);
			player.sendMessage(Constants.ERROR_COLOR + usage);
			return;
		}
		
		if(args.length != 1) {
			ErrorManager.sendError(sender, CSError.TOO_MANY_ARGUMENTS, null);
			player.sendMessage(Constants.ERROR_COLOR + usage);
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
			ErrorManager.sendError(sender, CSError.INCORRECT_NUMBER_FORMAT, null);
			player.sendMessage(Constants.ERROR_COLOR + usage);
			return;
		}
		total = claims * plugin.getSettings().claimCost;
		
		Holdings balance = iConomy.getAccount(player.getName()).getHoldings();
		
		if(!balance.hasEnough(total)) {
			ErrorManager.sendError(sender, CSError.NOT_ENOUGH_MONEY, iConomy.format(total));
			return;
		}
		
		balance.subtract(total);
		city.setBonusClaims(city.getBonusClaims() + claims);
		plugin.getDB().setBonusClaims(city.getName(), city.getBonusClaims());
		
		player.sendMessage(Constants.CITYSCAPE + Constants.SUCCESS_COLOR +
				"You have bought " + claims + " claims for " + iConomy.format(total) + ".");
	}

}
