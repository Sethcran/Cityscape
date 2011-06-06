package com.sethcran.cityscape.commands.citycommands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sethcran.cityscape.City;
import com.sethcran.cityscape.Cityscape;
import com.sethcran.cityscape.Constants;
import com.sethcran.cityscape.PlayerCache;
import com.sethcran.cityscape.RankPermissions;
import com.sethcran.cityscape.commands.CSCommand;

public class Unban extends CSCommand {

	public Unban(Cityscape plugin) {
		super(plugin);
		name = "unban";
		description = "Unbans the players from entering your city.";
		usage = "/c unban playername (playername) ...";
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
		if(!rp.isUnban()) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"You do not have permission to do that.");
			return;
		}
		
		if(args == null) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"You must type names to unban from your city.");
			return;
		}
		
		for(String s : args) {
			if(city.isBanned(s)) {
				city.unban(s);
				plugin.getDB().unban(city.getName(), s);
				player.sendMessage(Constants.CITYSCAPE + Constants.SUCCESS_COLOR +
						"You have unbanned " + s + ".");
			}
			else {
				player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
						s + " was not banned.");
			}
		}
	}

}
