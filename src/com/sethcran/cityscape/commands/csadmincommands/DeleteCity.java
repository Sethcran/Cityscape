package com.sethcran.cityscape.commands.csadmincommands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sethcran.cityscape.City;
import com.sethcran.cityscape.Cityscape;
import com.sethcran.cityscape.Constants;
import com.sethcran.cityscape.commands.CSCommand;
import com.sethcran.cityscape.error.ErrorManager;
import com.sethcran.cityscape.error.ErrorManager.CSError;

public class DeleteCity extends CSCommand {

	public DeleteCity(Cityscape plugin) {
		super(plugin);
		name = "deletecity";
		description = "Deletes the selected player city.";
		usage = "/csa deletecity cityname";
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
		
		City city = plugin.getCity(args[0]);
		
		if(city == null) {
			ErrorManager.sendError(sender, CSError.CITY_DOES_NOT_EXIST, args[0]);
			return;
		}
		
		plugin.deleteCity(args[0]);
		plugin.getDB().deleteCity(args[0]);
		
		plugin.getServer().broadcastMessage(Constants.CITYSCAPE + Constants.SUCCESS_COLOR +
				"The city of " + args[0] + " has fallen into ruin.");
		
		player.sendMessage(Constants.CITYSCAPE + Constants.SUCCESS_COLOR + 
				"You have deleted the city of " + args[0] + ".");
	}

}
