package com.sethcran.cityscape.commands.citycommands;

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
			sender.sendMessage("Only a player in game can do that!");
			return;
		}
		
		if(args.length < 2) {
			player.sendMessage("You must provide a townname.");
			player.sendMessage(usage);
			return;
		}
		if(args.length > 2) {
			player.sendMessage("Spaces are not allowed in town names.");
			return;
		}
		if(args[1].length() > Constants.TOWN_MAX_NAME_LENGTH) {
			player.sendMessage("The town name must be under " + 
					Constants.TOWN_MAX_NAME_LENGTH + " characters.");
			return;
		}
		
		Holdings balance = iConomy.getAccount(player.getName()).getHoldings();
		if(balance == null) {
			player.sendMessage("There was an error executing that command.");
			return;
		}
		if(balance.hasEnough(plugin.getSettings().cityCost)) {
			CSPlayerCityData cs = new CSPlayerCityData(plugin.getDB().getConnection());
			String currentCity = cs.getCurrentCity(player.getName());
			if(currentCity == null) {
				CSCities csc = new CSCities(plugin.getDB().getConnection(), 
						plugin.getSettings());
				
				if(csc.doesCityExist(args[1])) {
					player.sendMessage("That city already exists!");
					return;
				}
				
				if(csc.createCity(player.getName(), args[1])) {
					String msg = "The town of " + args[1] + " was founded!";
					plugin.getServer().broadcastMessage(msg);
				}
				else
					player.sendMessage("There was an error founding your town.");
			}
			else
				player.sendMessage("You must first leave your current city.");
		}
		else
			player.sendMessage("You do not have enough money for that.");
	}
}
