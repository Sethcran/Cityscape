package com.sethcran.cityscape.commands.citycommands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sethcran.cityscape.Cityscape;
import com.sethcran.cityscape.Constants;
import com.sethcran.cityscape.PlayerCache;
import com.sethcran.cityscape.RankPermissions;
import com.sethcran.cityscape.commands.CSCommand;

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
			sender.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"Only players in game can use that command.");
			return;
		}
		
		if(args == null) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"You must specify the player's name to remove.");
			return;
		}
		
		RankPermissions rp = plugin.getPermissions(player.getName());
		if(rp == null) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"You can not do that.");
			return;
		}
		
		if(!rp.isRemoveResident()) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"You do not have permission to do that.");
			return;
		}
		
		for(String resident : args) {
			if(plugin.getDB().doesPlayerExist(resident)) {
				String cityName = plugin.getCache(player.getName()).getCity();
				String city = plugin.getDB().getCurrentCity(resident);
				if(city == null) {
					player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
							"The user " + resident + " is not in your city!");
				} 
				else if(!city.equals(cityName)) {
					player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
							"The user " + resident + " is not in your city!");
				}
				else if(player.getName().equals(resident)) {
					player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
							"You can't remove yourself from the city. Use /leave.");
				}
				else {
					plugin.getDB().leaveCity(resident);
					PlayerCache pc = plugin.getCache(resident);
					if(pc != null) {
						pc.setCity(null);
						pc.setRank(null);
					}
					
					plugin.sendMessageToCity(player.getName() + " has removed " + 
							resident + " from the city.", cityName);
					Player removed = plugin.getServer().getPlayer(resident);
					removed.sendMessage(Constants.CITYSCAPE + Constants.SUCCESS_COLOR +
							"You have been removed from the city of " + cityName + ".");
				}
			}
			else {
				player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
						"That user does not exist.");
			}
		}
	}

}
