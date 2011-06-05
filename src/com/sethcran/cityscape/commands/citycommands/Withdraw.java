package com.sethcran.cityscape.commands.citycommands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.iConomy.iConomy;
import com.iConomy.system.Holdings;
import com.sethcran.cityscape.City;
import com.sethcran.cityscape.Cityscape;
import com.sethcran.cityscape.Constants;
import com.sethcran.cityscape.PlayerCache;
import com.sethcran.cityscape.RankPermissions;
import com.sethcran.cityscape.commands.CSCommand;

public class Withdraw extends CSCommand {

	public Withdraw(Cityscape plugin) {
		super(plugin);
		name = "withdraw";
		description = "Withdraw money from the city bank.";
		usage = "/c withdraw [amount]";
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
		
		if(!rp.isWithdraw()) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"You do not have permission to do that.");
			return;
		}
		
		if(args == null) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"You must specify an amount to withdraw.");
			return;
		}
		
		if(args.length != 1) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"That command only requires an amount to withdraw.");
			return;
		}
		
		double amount;
		try {
			amount = Double.parseDouble(args[0]);
		} catch(NumberFormatException e) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"The argument must be a number.");
			return;
		}
		
		Holdings cityBalance = city.getAccount().getHoldings();
		Holdings playerBalance = iConomy.getAccount(player.getName()).getHoldings();
		
		cityBalance.subtract(amount);
		playerBalance.add(amount);
		
		player.sendMessage(Constants.CITYSCAPE + Constants.SUCCESS_COLOR +
				"You have withdrawn " + iConomy.format(amount) + ".");
	}

}
