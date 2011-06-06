package com.sethcran.cityscape.commands.csadmincommands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sethcran.cityscape.City;
import com.sethcran.cityscape.Cityscape;
import com.sethcran.cityscape.Constants;
import com.sethcran.cityscape.commands.CSCommand;

public class Ban extends CSCommand {

	public Ban(Cityscape plugin) {
		super(plugin);
		name = "ban";
		description = "Bans the selected player from entering the city.";
		usage = "/csa ban player city";
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
		
		if(!plugin.permissionHandler.has(player, "cs.admin")) {
			return;
		}
		
		if(args == null) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"That command requires a player and city.");
			return;
		}
		
		if(args.length != 1) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"That command requires a player and city.");
			return;
		}
		
		if(!plugin.getDB().doesPlayerExist(args[0])) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"The player " + args[0] + " does not exist.");
			return;
		}
		
		City city = plugin.getCity(args[1]);
		
		if(city == null) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"The city " + args[1] + " does not exist.");
			return;
		}
		
		city.ban(args[0]);
		plugin.getDB().ban(args[1], args[0]);
		player.sendMessage(Constants.CITYSCAPE + Constants.SUCCESS_COLOR +
				"You have banned " + args[0] + " from " + args[1] + ".");
	}

}
