package com.sethcran.cityscape.commands.citycommands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sethcran.cityscape.City;
import com.sethcran.cityscape.Cityscape;
import com.sethcran.cityscape.Constants;
import com.sethcran.cityscape.PlayerCache;
import com.sethcran.cityscape.RankPermissions;
import com.sethcran.cityscape.commands.CSCommand;

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
			sender.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"You must be in game to do this.");
			return;
		}
		
		if(args == null) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"You are not using that command correctly.");
			player.sendMessage(Constants.ERROR_COLOR + usage);
			return;
		}
		
		if(args.length != 1) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"You are not using that command correctly.");
			player.sendMessage(Constants.ERROR_COLOR + usage);
			return;
		}
		
		String playerCity = plugin.getCache(player.getName()).getCity();
		if(playerCity == null) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"You are not in a city!");
			return;
		}
		
		if(!plugin.getDB().doesPlayerExist(args[0])) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"That player does not exist.");
			return;
		}
		
		String otherCity = plugin.getDB().getCurrentCity(args[0]);
		if(!playerCity.equals(otherCity)) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"That player is not in your city.");
			return;
		}
		
		RankPermissions rp = plugin.getPermissions(player.getName());
		if(!rp.isDemote()) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"You do not have permission to do that.");
			return;
		}
		
		City city = plugin.getCity(playerCity);
		
		if(args[0].equals(city.getMayor())) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR + 
					"You can't demote the mayor. You must promote a new mayor.");
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
	}

}
