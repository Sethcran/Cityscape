package com.sethcran.cityscape.commands.citycommands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sethcran.cityscape.Cityscape;
import com.sethcran.cityscape.Constants;
import com.sethcran.cityscape.RankPermissions;
import com.sethcran.cityscape.commands.CSCommand;

public class Invite extends CSCommand {

	public Invite(Cityscape plugin) {
		super(plugin);
		name = "invite";
		description = "Invites the specified player to join your town.";
		usage = "/c invite playername";
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
					"You must specify the player's name to invite.");
			return;
		}
		
		RankPermissions rp = plugin.getDB().getPermissions(player.getName());
		if(rp == null) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"You can not do that.");
			return;
		}
		
		if(!rp.isAddResident()) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"You do not have permission to do that.");
			return;
		}
		
		for(String playerName : args) {
			if(plugin.getDB().doesPlayerExist(playerName)) {
				String city = plugin.getDB().getCurrentCity(playerName);
				if(city != null) {
					player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
							"The user " + playerName + " is already in a city!");
				} 
				else {
					String cityName = plugin.getCache(player.getName()).getCity();
					plugin.getDB().addInvite(playerName, cityName);
					
					player.sendMessage(Constants.CITYSCAPE + Constants.SUCCESS_COLOR +
							"You have invited " + playerName + ".");
					Player invited = plugin.getServer().getPlayer(playerName);
					invited.sendMessage(Constants.CITYSCAPE + Constants.SUCCESS_COLOR +
							"You have been invited to join the city of " + cityName + ".");
				}
			}
			else {
				player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
						"That user does not exist.");
			}
		}
	}

}
