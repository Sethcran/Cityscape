package com.sethcran.cityscape.commands.citycommands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.iConomy.iConomy;
import com.iConomy.system.Holdings;
import com.sethcran.cityscape.Cityscape;
import com.sethcran.cityscape.Constants;
import com.sethcran.cityscape.commands.CSCommand;
import com.sethcran.cityscape.database.CSCities;
import com.sethcran.cityscape.database.CSPlayerCityData;

public class CreateCity extends CSCommand {
	
	public CreateCity(Cityscape plugin) {
		super(plugin);
		name = "create";
		description = "Creates a new city at the chunk you are standing on " +
				"if you have enough money.";
		usage = "/city create [cityname]";
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		Player player = null;
		if(sender instanceof Player)
			player = (Player)sender;
		else {
			sender.sendMessage(ChatColor.GOLD + "[Cityscape] " + ChatColor.RED + 
					"Only a player in game can do that!");
			return;
		}
		
		if(args.length < 2) {
			player.sendMessage(ChatColor.GOLD + "[Cityscape] " + ChatColor.RED + 
					"You must provide a townname.");
			player.sendMessage(ChatColor.RED + usage);
			return;
		}
		if(args.length > 2) {
			player.sendMessage(ChatColor.GOLD + "[Cityscape] " + ChatColor.RED + 
					"Spaces are not allowed in town names.");
			return;
		}
		if(args[1].length() > Constants.TOWN_MAX_NAME_LENGTH) {
			player.sendMessage(ChatColor.GOLD + "[Cityscape] " + ChatColor.RED + 
					"The town name must be under " + Constants.TOWN_MAX_NAME_LENGTH + 
					" characters.");
			return;
		}
		
		Holdings balance = iConomy.getAccount(player.getName()).getHoldings();
		if(balance == null) {
			player.sendMessage(ChatColor.GOLD + "[Cityscape] " + ChatColor.RED +
					"There was an error executing that command.");
			return;
		}
		if(balance.hasEnough(plugin.getSettings().cityCost)) {
			CSPlayerCityData cspcd = new CSPlayerCityData(plugin.getDB().getConnection(),
					plugin.getSettings());
			String currentCity = cspcd.getCurrentCity(player.getName());
			if(currentCity == null) {
				CSCities csc = new CSCities(plugin.getDB().getConnection(), 
						plugin.getSettings());
				
				if(csc.doesCityExist(args[1])) {
					player.sendMessage(ChatColor.GOLD + "[Cityscape] " + ChatColor.RED +
							"That city already exists!");
					return;
				}
				
				if(csc.createCity(player.getName(), args[1])) {
					cspcd.addPlayerToCity(player.getName(), args[1]);
					plugin.getServer().broadcastMessage(ChatColor.GOLD + "[Cityscape] " + 
							ChatColor.GREEN + "The city of " + args[1] + " was founded!");
				}
				else
					player.sendMessage(ChatColor.GOLD + "[Cityscape] " + ChatColor.RED + 
							"There was an error founding your town.");
			}
			else
				player.sendMessage(ChatColor.GOLD + "[Cityscape] " + ChatColor.RED + 
						"You must first leave your current city.");
		}
		else
			player.sendMessage(ChatColor.GOLD + "[Cityscape] " + ChatColor.RED + 
					"You do not have enough money for that.");
	}
}
