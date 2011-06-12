package com.sethcran.cityscape.commands.citycommands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sethcran.cityscape.Cityscape;
import com.sethcran.cityscape.Constants;
import com.sethcran.cityscape.RankPermissions;
import com.sethcran.cityscape.commands.CSCommand;
import com.sethcran.cityscape.error.ErrorManager;
import com.sethcran.cityscape.error.ErrorManager.CSError;

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
			ErrorManager.sendError(sender, CSError.IN_GAME_ONLY, null);
			return;
		}
		
		if(args == null) {
			ErrorManager.sendError(sender, CSError.NOT_ENOUGH_ARGUMENTS, null);
			player.sendMessage(Constants.ERROR_COLOR + usage);
			return;
		}
		
		RankPermissions rp = plugin.getPermissions(player.getName());
		if(rp == null) {
			ErrorManager.sendError(sender, CSError.NO_RANK_PERMISSION, null);
			return;
		}
		
		if(!rp.isAddResident()) {
			ErrorManager.sendError(sender, CSError.NO_RANK_PERMISSION, null);
			return;
		}
		
		for(String resident : args) {
			if(plugin.getDB().doesPlayerExist(resident)) {
				String city = plugin.getDB().getCurrentCity(resident);
				if(city != null) {
					ErrorManager.sendError(sender, CSError.OTHER_ALREADY_IN_CITY, null);
				} 
				else {
					String cityName = plugin.getCache(player.getName()).getCity();
					plugin.getDB().addInvite(resident, cityName);
					
					player.sendMessage(Constants.CITYSCAPE + Constants.SUCCESS_COLOR +
							"You have invited " + resident + ".");
					Player invited = plugin.getServer().getPlayer(resident);
					invited.sendMessage(Constants.CITYSCAPE + Constants.SUCCESS_COLOR +
							"You have been invited to join the city of " + cityName + ".");
					invited.sendMessage(Constants.SUCCESS_COLOR + "Type /c invites to" +
							" view a list of your invites.");
					plugin.addLogEntry("CITY", player.getName() + " invited " + resident +
							" to join the city of " + cityName);
					return;
				}
			}
			else {
				ErrorManager.sendError(sender, CSError.PLAYER_DOES_NOT_EXIST, resident);
			}
		}
	}

}
