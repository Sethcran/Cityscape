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

public class Promote extends CSCommand {

	public Promote(Cityscape plugin) {
		super(plugin);
		name = "promote";
		description = "Used to promote a player in your city to the selected rank.";
		usage = "/c promote [name] [rank]";
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
		
		if(args.length != 2) {
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
		if(rp != null && !rp.isPromote()) {
			ErrorManager.sendError(sender, CSError.NO_RANK_PERMISSION, null);
			return;
		}
		
		if(args[1].equals("Mayor")) {
			if(!plugin.getCity(playerCity).getMayor().equals(player.getName())) {
				ErrorManager.sendError(sender, CSError.IMPOSSIBLE, null);
				return;
			}
			plugin.getDB().setRank(args[0], args[1]);
			plugin.getDB().setRank(player.getName(), null);
			plugin.getDB().updateMayor(playerCity, args[0]);
			plugin.getCity(playerCity).setMayor(args[0]);
			PlayerCache cache = plugin.getCache(player.getName());
			cache.setRank(null);
			cache = plugin.getCache(args[0]);
			if(cache != null)
				cache.setRank("Mayor");
			plugin.sendMessageToCity(args[0] + " has been promoted to " + args[1] + ".", 
					playerCity);
			return;
		}
		
		City city = plugin.getCity(playerCity);
		if(!city.doesRankExist(args[1])) {
			ErrorManager.sendError(sender, CSError.RANK_DOES_NOT_EXIST, args[1]);
			return;
		}
		
		plugin.getDB().setRank(args[0], args[1]);
		PlayerCache cache = plugin.getCache(args[0]);
		if(cache != null)
			cache.setRank(args[1]);
		plugin.sendMessageToCity(args[0] + " has been promoted to " + args[1] + ".", 
				playerCity);
		plugin.addLogEntry("CITY", player.getName() + " promoted " + args[0] + " to " +
				args[1] + " in " + playerCity);
	}

}
