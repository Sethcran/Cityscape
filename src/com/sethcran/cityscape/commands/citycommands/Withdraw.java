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
import com.sethcran.cityscape.error.ErrorManager;
import com.sethcran.cityscape.error.ErrorManager.CSError;

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
			ErrorManager.sendError(sender, CSError.IN_GAME_ONLY, null);
			return;
		}
		
		PlayerCache cache = plugin.getCache(player.getName());
		City city = plugin.getCity(cache.getCity());
		
		if(city == null) {
			ErrorManager.sendError(sender, CSError.NOT_IN_CITY, null);
			return;
		}
		
		RankPermissions rp = city.getRank(cache.getRank());
		
		if(rp == null) {
			ErrorManager.sendError(sender, CSError.NO_RANK_PERMISSION, null);
			return;
		}
		
		if(!rp.isWithdraw()) {
			ErrorManager.sendError(sender, CSError.NO_RANK_PERMISSION, null);
			return;
		}
		
		if(args == null) {
			ErrorManager.sendError(sender, CSError.NOT_ENOUGH_ARGUMENTS, null);
			sender.sendMessage(Constants.ERROR_COLOR + usage);
			return;
		}
		
		if(args.length != 1) {
			ErrorManager.sendError(sender, CSError.TOO_MANY_ARGUMENTS, null);
			sender.sendMessage(Constants.ERROR_COLOR + usage);
			return;
		}
		
		double amount;
		try {
			amount = Double.parseDouble(args[0]);
		} catch(NumberFormatException e) {
			ErrorManager.sendError(sender, CSError.INCORRECT_NUMBER_FORMAT, null);
			return;
		}
		
		Holdings cityBalance = city.getAccount().getHoldings();
		Holdings playerBalance = iConomy.getAccount(player.getName()).getHoldings();
		
		if(!cityBalance.hasEnough(amount)) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"The city bank does not have enough money.");
			return;
		}
		
		cityBalance.subtract(amount);
		playerBalance.add(amount);
		
		player.sendMessage(Constants.CITYSCAPE + Constants.SUCCESS_COLOR +
				"You have withdrawn " + iConomy.format(amount) + ".");
	}

}
