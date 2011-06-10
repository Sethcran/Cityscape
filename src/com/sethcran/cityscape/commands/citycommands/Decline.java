package com.sethcran.cityscape.commands.citycommands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sethcran.cityscape.Cityscape;
import com.sethcran.cityscape.Constants;
import com.sethcran.cityscape.commands.CSCommand;
import com.sethcran.cityscape.error.ErrorManager;
import com.sethcran.cityscape.error.ErrorManager.CSError;

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
			ErrorManager.sendError(sender, CSError.IN_GAME_ONLY, null);
			return;
		}
		
		if(args == null) {
			ErrorManager.sendError(sender, CSError.NOT_ENOUGH_ARGUMENTS, null);
			player.sendMessage(Constants.ERROR_COLOR + usage);
			return;
		}
		
		if(args.length > 1) {
			ErrorManager.sendError(sender, CSError.TOO_MANY_ARGUMENTS, null);
			player.sendMessage(Constants.ERROR_COLOR + usage);
			return;
		}
		
		if(!plugin.getDB().doesCityExist(args[0])) {
			ErrorManager.sendError(sender, CSError.CITY_DOES_NOT_EXIST, args[0]);
			return;
		}
		
		if(!plugin.getDB().isInvited(player.getName(), args[0])) {
			ErrorManager.sendError(sender, CSError.NOT_INVITED, args[0]);
			return;
		}
		
		plugin.getDB().removeInvite(player.getName(), args[0]);
		player.sendMessage(Constants.CITYSCAPE + Constants.SUCCESS_COLOR +
				"You have declined the invitation to join the city of " + args[0] + ".");
	}
}
