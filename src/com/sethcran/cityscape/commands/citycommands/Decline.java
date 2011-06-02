package com.sethcran.cityscape.commands.citycommands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sethcran.cityscape.Cityscape;
import com.sethcran.cityscape.Constants;
import com.sethcran.cityscape.commands.CSCommand;

public class Decline extends CSCommand {

	public Decline(Cityscape plugin) {
		super(plugin);
		name = "decline";
		description = "Declines an invitation to join a city.";
		usage = "/c decline cityname";
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		Player player = null;
		if(sender instanceof Player)
			player = (Player)sender;
		else {
			sender.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"Only players in game can use this command.");
			return;
		}
		
		if(args == null) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"You must provide a city name to decline.");
			return;
		}
		
		if(args.length > 1) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"Please provide only a single city name to decline.");
			return;
		}
		
		if(!plugin.getDB().doesCityExist(args[0])) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"That city does not exist.");
			return;
		}
		
		if(!plugin.getDB().isInvited(player.getName(), args[0])) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"You have not been invited to join " + args[0] + ".");
			return;
		}
		
		plugin.getDB().removeInvite(player.getName(), args[0]);
		player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
				"You have declined the invitation to join the city of " + args[0] + ".");
	}
}
