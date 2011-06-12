package com.sethcran.cityscape.commands.citycommands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sethcran.cityscape.City;
import com.sethcran.cityscape.Cityscape;
import com.sethcran.cityscape.Constants;
import com.sethcran.cityscape.PlayerCache;
import com.sethcran.cityscape.commands.CSCommand;
import com.sethcran.cityscape.error.ErrorManager;
import com.sethcran.cityscape.error.ErrorManager.CSError;

public class Delete extends CSCommand {

	public Delete(Cityscape plugin) {
		super(plugin);
		name = "delete";
		description = "Used to delete your city.";
		usage = "/c delete cityname";
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
		
		if(!city.getMayor().equals(player.getName())) {
			ErrorManager.sendError(sender, CSError.MAYOR_ONLY, null);
			return;
		}
		
		if(args == null) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR + 
					"You must use /c delete " + city.getName() + " to confirm.");
			return;
		}
		
		if(args.length != 1) {
			ErrorManager.sendError(sender, CSError.TOO_MANY_ARGUMENTS, null);
			player.sendMessage(Constants.ERROR_COLOR + usage);
			return;
		}
		
		if(!args[0].equals(city.getName())) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR + 
					"You must use /c delete " + city.getName() + " to confirm.");
			return;
		}
		
		String cityName = city.getName();
		
		plugin.deleteCity(cityName);
		plugin.getDB().deleteCity(cityName);
		plugin.getServer().broadcastMessage(Constants.CITYSCAPE + Constants.SUCCESS_COLOR +
				"The city of " + cityName + " has fallen into ruin.");
		plugin.addLogEntry("CITY", player.getName() + " deleted the city of " + cityName);
	}

}
