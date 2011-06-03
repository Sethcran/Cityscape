package com.sethcran.cityscape.commands.citycommands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sethcran.cityscape.Cityscape;
import com.sethcran.cityscape.Constants;
import com.sethcran.cityscape.RankPermissions;
import com.sethcran.cityscape.commands.CSCommand;

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
		
		if(args.length != 2) {
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
		
		RankPermissions rp = plugin.getDB().getPermissions(player.getName());
		if(!rp.isPromote()) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"You do not have permission to do that.");
			return;
		}
		
		if(args[1].equalsIgnoreCase("mayor")) {
			if(!plugin.getCity(playerCity).getMayor().equals(player.getName())) {
				player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
						"You do not have permission to do that.");
				return;
			}
		}
		
		if(!plugin.getDB().doesRankExist(playerCity, args[1])) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"That rank does not exist.");
			return;
		}
		
		plugin.getDB().setRank(args[0], args[1]);
		plugin.sendMessageToCity(args[0] + " has been promoted to " + args[1] + ".", 
				playerCity);
	}

}
