package com.sethcran.cityscape.commands.citycommands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sethcran.cityscape.Cityscape;
import com.sethcran.cityscape.Constants;
import com.sethcran.cityscape.PlayerCache;
import com.sethcran.cityscape.RankPermissions;
import com.sethcran.cityscape.commands.CSCommand;
import com.sethcran.cityscape.error.ErrorManager;
import com.sethcran.cityscape.error.ErrorManager.CSError;

public class Remove extends CSCommand {

	public Remove(Cityscape plugin) {
		super(plugin);
		name = "remove";
		description = "Removes a resident from the city.";
		usage = "/c remove residentname";
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
			sender.sendMessage(Constants.ERROR_COLOR + usage);
			return;
		}
		
		RankPermissions rp = plugin.getPermissions(player.getName());
		if(rp == null) {
			ErrorManager.sendError(sender, CSError.NO_RANK_PERMISSION, null);
			return;
		}
		
		if(!rp.isRemoveResident()) {
			ErrorManager.sendError(sender, CSError.NO_RANK_PERMISSION, null);
			return;
		}
		
		for(String resident : args) {
			if(plugin.getDB().doesPlayerExist(resident)) {
				String cityName = plugin.getCache(player.getName()).getCity();
				String city = plugin.getDB().getCurrentCity(resident);
				if(city == null) {
					ErrorManager.sendError(sender, CSError.OTHER_NOT_IN_YOUR_CITY, resident);
				} 
				else if(!city.equals(cityName)) {
					ErrorManager.sendError(sender, CSError.OTHER_NOT_IN_YOUR_CITY, resident);
				}
				else if(player.getName().equals(resident)) {
					ErrorManager.sendError(sender, CSError.IMPOSSIBLE, null);
				}
				else {
					plugin.getDB().leaveCity(resident);
					PlayerCache pc = plugin.getCache(resident);
					if(pc != null) {
						pc.setCity(null);
						pc.setRank(null);
					}
					plugin.checkCityRank(plugin.getCache(player.getName()).getCity());
					plugin.sendMessageToCity(player.getName() + " has removed " + 
							resident + " from the city.", cityName);
					Player removed = plugin.getServer().getPlayer(resident);
					if(removed != null) {
						removed.sendMessage(Constants.CITYSCAPE + Constants.SUCCESS_COLOR +
								"You have been removed from the city of " + cityName + ".");
					}
					plugin.addLogEntry("CITY", player.getName() + " has removed " + resident +
							" from " + cityName);
				}
			}
			else {
				ErrorManager.sendError(sender, CSError.PLAYER_DOES_NOT_EXIST, resident);
			}
		}
	}

}
