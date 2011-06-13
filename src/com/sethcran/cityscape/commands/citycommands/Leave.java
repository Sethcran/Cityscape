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

public class Leave extends CSCommand {

	public Leave(Cityscape plugin) {
		super(plugin);
		name = "leave";
		description = "Leaves your current city if you are in one.";
		usage = "/c leave";
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
		
		if(args != null) {
			ErrorManager.sendError(sender, CSError.TOO_MANY_ARGUMENTS, null);
			player.sendMessage(Constants.ERROR_COLOR + usage);
			return;
		}
		
		String playerCity = plugin.getCache(player.getName()).getCity();
		
		if(playerCity == null) {
			ErrorManager.sendError(sender, CSError.NOT_IN_CITY, null);
			return;
		}
		
		if(plugin.getCity(playerCity).getMayor().equals(player.getName())) {
			ErrorManager.sendError(sender, CSError.SET_MAYOR_FIRST, null);
			return;
		}
		
		plugin.getDB().leaveCity(player.getName());
		PlayerCache cache = plugin.getCache(player.getName());
		plugin.checkCityRank(playerCity);
		cache.setCity(null);
		cache.setRank(null);
		
		City city = plugin.getCity(playerCity);
		city.removePlayer(player.getName());
		
		plugin.sendMessageToCity(player.getName() + " has left the city.", playerCity);
		player.sendMessage(Constants.CITYSCAPE + Constants.SUCCESS_COLOR + 
				"You have left your town.");
		plugin.addLogEntry("CITY", player.getName() + " left the city of " + playerCity);
	}

}
