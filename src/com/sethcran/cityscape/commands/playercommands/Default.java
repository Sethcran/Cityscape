package com.sethcran.cityscape.commands.playercommands;


import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.iConomy.iConomy;
import com.iConomy.system.Holdings;
import com.sethcran.cityscape.Cityscape;
import com.sethcran.cityscape.Constants;
import com.sethcran.cityscape.PlayerCache;
import com.sethcran.cityscape.commands.CSCommand;

public class Default extends CSCommand {

	public Default(Cityscape plugin) {
		super(plugin);
		name = "default";
		description = "Displays player information.";
		usage = "/p (playername)";
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(!(sender instanceof Player)) {
			if(args == null) {
				sender.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
						"That command requires a player name for out of game use.");
				return;
			}
		}
		
		PlayerCache cache = null;
		if(args == null) {
			cache = plugin.getCache(((Player)sender).getName());
		}
		else if(args.length != 1) {
			sender.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"That command only requires a player name.");
			return;
		}
		else {
			cache = plugin.getDB().getInfo(args[0]);
			if(cache == null) {
				sender.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
						"That player does not exist.");
				return;
			}
		}
		
		String message = Constants.CITYSCAPE + Constants.GROUP_COLOR + 
				"Player Information: " + Constants.MESSAGE_COLOR;
		if(args == null)
			message += ((Player)sender).getName();
		else
			message += args[0];
		sender.sendMessage(message);
		
		sender.sendMessage(Constants.GROUP_COLOR + "Registered: " + Constants.MESSAGE_COLOR +
				cache.getFirstLogin());
		sender.sendMessage(Constants.GROUP_COLOR + "Last Online: " + Constants.MESSAGE_COLOR +
				cache.getLastLogin());
		message = Constants.GROUP_COLOR + "City: " + Constants.MESSAGE_COLOR;
		if(cache.getCity() == null)
			message += "none  ";
		else
			message += cache.getCity() + "  ";
		message += Constants.GROUP_COLOR + "Rank: " + Constants.MESSAGE_COLOR;
		if(cache.getRank() == null)
			message += "none";
		else
			message += cache.getRank();
		sender.sendMessage(message);
		
		Holdings balance = null;
		if(args == null)
			balance = iConomy.getAccount(((Player)sender).getName()).getHoldings();
		else
			balance = iConomy.getAccount(args[0]).getHoldings();
		sender.sendMessage(Constants.GROUP_COLOR + "Bank: " + Constants.MESSAGE_COLOR +
				iConomy.format(balance.balance()));
	}
}
