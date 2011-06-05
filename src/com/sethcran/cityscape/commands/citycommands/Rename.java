package com.sethcran.cityscape.commands.citycommands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sethcran.cityscape.City;
import com.sethcran.cityscape.Cityscape;
import com.sethcran.cityscape.Constants;
import com.sethcran.cityscape.PlayerCache;
import com.sethcran.cityscape.RankPermissions;
import com.sethcran.cityscape.commands.CSCommand;

public class Rename extends CSCommand {

	public Rename(Cityscape plugin) {
		super(plugin);
		name = "rename";
		description = "Used to rename a town or a rank.";
		usage = "/c rename [newname]";
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
		
		if(args == null) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"That command requires arguments.");
			player.sendMessage(Constants.ERROR_COLOR + usage);
			return;
		}
		
		if(args.length != 1) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"The new name can't contain spaces.");
			player.sendMessage(Constants.ERROR_COLOR + usage);
			return;
		}
		
		PlayerCache cache = plugin.getCache(player.getName());
		String cityName = cache.getCity();
		if(cityName == null) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"You must be in a city to do that.");
			return;
		}
		
		City city = plugin.getCity(cityName);
		RankPermissions rp = city.getRank(cache.getRank());
		if(!rp.isSetName()) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"You do not have permission to do that.");
			return;
		}
		
		if(!args[0].matches("[a-zA-Z]+")) {
			player.sendMessage(Constants.CITYSCAPE + ChatColor.RED +
					"The city name must consist only of alphabetic characters.");
			return;
		}
		
		plugin.renameCity(city.getName(), args[0]);
		plugin.getDB().renameCity(city.getName(), args[0]);
		
	}

}
