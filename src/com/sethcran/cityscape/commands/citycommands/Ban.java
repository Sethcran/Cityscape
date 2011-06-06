package com.sethcran.cityscape.commands.citycommands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sethcran.cityscape.City;
import com.sethcran.cityscape.Cityscape;
import com.sethcran.cityscape.Constants;
import com.sethcran.cityscape.PlayerCache;
import com.sethcran.cityscape.RankPermissions;
import com.sethcran.cityscape.commands.CSCommand;

public class Ban extends CSCommand {

	public Ban(Cityscape plugin) {
		super(plugin);
		name = "ban";
		description = "Bans the players from entering your city.";
		usage = "/c ban playername (playername) ...";
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		Player player = null;
		if(sender instanceof Player)
			player = (Player)sender;
		else {
			sender.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"You must be in game to do that.");
			return;
		}
		
		PlayerCache cache = plugin.getCache(player.getName());		
		City city = plugin.getCity(cache.getCity());
		
		if(city == null) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"You must be in a city to do that.");
			return;
		}
		
		RankPermissions rp = city.getRank(cache.getRank());
		if(!rp.isBan()) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"You do not have permission to do that.");
			return;
		}
		
		if(args == null) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"You must type names to ban from your city.");
			return;
		}
		
		for(String s : args) {
			if(s.equals(player.getName())) {
				player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
						"You can't ban yourself.");
			}
			else if(plugin.getDB().doesPlayerExist(s)) {
				city.ban(s);
				plugin.getDB().ban(city.getName(), s);
				player.sendMessage(Constants.CITYSCAPE + Constants.SUCCESS_COLOR + 
						"You have banned " + s + ".");
			}
			else {
				player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR + 
						s + " couldn't be banned. The player does not exist.");
			}
		}			
	}
}
