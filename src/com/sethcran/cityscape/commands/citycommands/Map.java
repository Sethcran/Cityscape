package com.sethcran.cityscape.commands.citycommands;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sethcran.cityscape.City;
import com.sethcran.cityscape.Cityscape;
import com.sethcran.cityscape.Constants;
import com.sethcran.cityscape.PlayerCache;
import com.sethcran.cityscape.commands.CSCommand;

public class Map extends CSCommand {

	public Map(Cityscape plugin) {
		super(plugin);
		name = "map";
		description = "Displays a map of the nearby claims to the user.";
		usage = "/c map";
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		Player player = null;
		if(sender instanceof Player)
			player = (Player)sender;
		else {
			sender.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"Only a player in game can do that.");
			return;
		}
		
		PlayerCache cache = plugin.getCache(player.getName());
		Chunk chunk = player.getLocation().getBlock().getChunk();
		String world = chunk.getWorld().getName();
		int x = chunk.getX();
		int z = chunk.getZ();
		
		player.sendMessage(Constants.CITYSCAPE + Constants.SUCCESS_COLOR + "City Map:");
		String message = new String();
		
		for(int i = x - 4; i <= x + 4; i++) {
			if(i == x - 1)
				message = "      N            ";
			else if(i == x)
				message = " W     E        ";
			else if(i == x + 1)
				message = "      S            ";
			else
				message = "                    ";
			for(int j = z + 15; j >= z - 15; j--) {
				City city = plugin.getCityAt(i, j, world);
				if(city == null) {
					if(i == x && j == z)
						message += ChatColor.YELLOW;
					else
						message += ChatColor.GRAY;
					message += "-";
				}
				else if(city.getName().equals(cache.getCity())) {
					if(i == x && j == z)
						message += ChatColor.YELLOW;
					else
						message += ChatColor.GREEN;
					message += "+";
				}
				else {
					if(i == x && j == z)
						message += ChatColor.YELLOW;
					else
						message += ChatColor.WHITE;
					message += "+";
				}
			}
			player.sendMessage(message);
		}

	}

}
