package com.sethcran.cityscape.commands.csadmincommands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sethcran.cityscape.City;
import com.sethcran.cityscape.Cityscape;
import com.sethcran.cityscape.Constants;
import com.sethcran.cityscape.commands.CSCommand;
import com.sethcran.cityscape.error.ErrorManager;
import com.sethcran.cityscape.error.ErrorManager.CSError;

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
			ErrorManager.sendError(sender, CSError.IN_GAME_ONLY, null);
			return;
		}
		
		if(!plugin.permissionHandler.has(player, "cs.admin")) {
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
		
		if(!plugin.getDB().doesPlayerExist(args[0])) {
			ErrorManager.sendError(sender, CSError.PLAYER_DOES_NOT_EXIST, args[0]);
			return;
		}
		
		City city = plugin.getCity(args[1]);
		
		if(city == null) {
			ErrorManager.sendError(sender, CSError.CITY_DOES_NOT_EXIST, args[1]);
			return;
		}
		
		city.ban(args[0]);
		plugin.getDB().ban(args[1], args[0]);
		player.sendMessage(Constants.CITYSCAPE + Constants.SUCCESS_COLOR +
				"You have banned " + args[0] + " from " + args[1] + ".");
	}

}
