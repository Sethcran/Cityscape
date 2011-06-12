package com.sethcran.cityscape.commands.citycommands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sethcran.cityscape.City;
import com.sethcran.cityscape.Cityscape;
import com.sethcran.cityscape.Constants;
import com.sethcran.cityscape.PlayerCache;
import com.sethcran.cityscape.RankPermissions;
import com.sethcran.cityscape.commands.CSCommand;
import com.sethcran.cityscape.error.ErrorManager;
import com.sethcran.cityscape.error.ErrorManager.CSError;

public class Demote extends CSCommand {

	public Demote(Cityscape plugin) {
		super(plugin);
		name = "demote";
		description = "Removes the rank from the specified player.";
		usage = "/c demote [player]";
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		Player player = null;
		if(sender instanceof Player) {
			player = (Player)sender;
		}
		else {
			ErrorManager.sendError(sender, CSError.IN_GAME_ONLY, null);
			return;
		}
		
		if(args == null) {
			ErrorManager.sendError(sender, CSError.NOT_ENOUGH_ARGUMENTS, null);
			player.sendMessage(Constants.ERROR_COLOR + usage);
			return;
		}
		
		if(args.length != 1) {
			ErrorManager.sendError(sender, CSError.TOO_MANY_ARGUMENTS, null);
			player.sendMessage(Constants.ERROR_COLOR + usage);
			return;
		}
		
		String playerCity = plugin.getCache(player.getName()).getCity();
		if(playerCity == null) {
			ErrorManager.sendError(sender, CSError.NOT_IN_CITY, null);
			return;
		}
		
		if(!plugin.getDB().doesPlayerExist(args[0])) {
			ErrorManager.sendError(sender, CSError.PLAYER_DOES_NOT_EXIST, args[0]);
			return;
		}
		
		String otherCity = plugin.getDB().getCurrentCity(args[0]);
		if(!playerCity.equals(otherCity)) {
			ErrorManager.sendError(sender, CSError.PLAYER_NOT_IN_YOUR_CITY, args[0]);
			return;
		}
		
		RankPermissions rp = plugin.getPermissions(player.getName());
		if(!rp.isDemote()) {
			ErrorManager.sendError(sender, CSError.NO_RANK_PERMISSION, null);
			return;
		}
		
		City city = plugin.getCity(playerCity);
		
		if(args[0].equals(city.getMayor())) {
			ErrorManager.sendError(sender, CSError.IMPOSSIBLE, null);
			return;
		}
		
		plugin.getDB().setRank(args[0], null);
		PlayerCache cache = plugin.getCache(args[0]);
		if(cache != null) {
			cache.setRank(null);
			Player other = plugin.getServer().getPlayer(args[0]);
			other.sendMessage(Constants.CITYSCAPE + Constants.SUCCESS_COLOR +
					"You have been demoted.");
		}
		player.sendMessage(Constants.CITYSCAPE + Constants.SUCCESS_COLOR +
				"You have demoted " + args[0] + ".");
		plugin.addLogEntry("CITY", player.getName() + " demoted " + args[0]);
	}

}
