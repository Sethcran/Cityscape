package com.sethcran.cityscape.commands.citycommands;

import java.sql.SQLException;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.iConomy.iConomy;
import com.iConomy.system.Holdings;
import com.sethcran.cityscape.Cityscape;
import com.sethcran.cityscape.Constants;
import com.sethcran.cityscape.RankPermissions;
import com.sethcran.cityscape.commands.CSCommand;
import com.sethcran.cityscape.database.CSCities;
import com.sethcran.cityscape.database.CSClaims;
import com.sethcran.cityscape.database.CSPlayerCityData;
import com.sethcran.cityscape.database.CSRanks;
import com.sethcran.cityscape.database.CSResidents;

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
		
		// Check if that sender is a player
		if(sender instanceof Player)
			player = (Player)sender;
		else {
			sender.sendMessage(Constants.CITYSCAPE + ChatColor.RED + 
					"Only a player in game can do that!");
			return;
		}
		
		// Check that arguments were provided correctly
		if(args == null) {
			player.sendMessage(Constants.CITYSCAPE + ChatColor.RED +
					"You must provide a townname.");
			return;
		}
		if(args.length > 1) {
			player.sendMessage(Constants.CITYSCAPE + ChatColor.RED + 
					"Spaces are not allowed in town names.");
			return;
		}
		
		// Check that town name is of appropriate length
		if(args[0].length() > Constants.TOWN_MAX_NAME_LENGTH) {
			player.sendMessage(Constants.CITYSCAPE + ChatColor.RED + 
					"The town name must be under " + Constants.TOWN_MAX_NAME_LENGTH + 
					" characters.");
			return;
		}
		
		// Check that town name is alphabetic
		if(!args[0].matches("[a-zA-Z]+")) {
			player.sendMessage(Constants.CITYSCAPE + ChatColor.RED +
					"The town name must consist only of alphabetic characters.");
			return;
		}
		
		// Check that user has permissions to create a city.
		if(!plugin.permissionHandler.has(player, "cityscape.createcity")) {
			player.sendMessage(Constants.CITYSCAPE + ChatColor.RED + 
					"You do not have permission to create a city.");
			return;
		}
		
		// Check that user has enough money.
		Holdings balance = iConomy.getAccount(player.getName()).getHoldings();
		if(balance == null) {
			player.sendMessage(Constants.CITYSCAPE + ChatColor.RED +
					"There was an error executing that command.");
			return;
		}	
		if(!balance.hasEnough(plugin.getSettings().cityCost)) {
			player.sendMessage(Constants.CITYSCAPE + ChatColor.RED + 
			"You do not have enough money for that.");
			return;
		}
		
		// Get needed tables;
		CSCities cscities = plugin.getDB().getCSCities();
		CSClaims csclaims = plugin.getDB().getCSClaims();
		CSPlayerCityData csplayercitydata = plugin.getDB().getCSPlayerCityData();
		CSResidents csresidents = plugin.getDB().getCSResidents();
		CSRanks csranks = plugin.getDB().getCSRanks();
		
		// Check that player is not in a city
		String currentCity = csresidents.getCurrentCity(player.getName());
		if(currentCity != null) {
			player.sendMessage(Constants.CITYSCAPE + ChatColor.RED + 
				"You must first leave your current city.");
			return;
		}
		
		// Check that selected cityname does not exist
		if(cscities.doesCityExist(args[0])) {
			player.sendMessage(Constants.CITYSCAPE + ChatColor.RED +
					"That city already exists!");
			return;
		}
		
		// Get the coordinates and world where player is standing
		Chunk chunk = player.getLocation().getBlock().getChunk();
		int x = chunk.getX();
		int z = chunk.getZ();
		String worldName = chunk.getWorld().getName();
		
		// Check that the current chunk is unclaimed
		if(csclaims.isChunkClaimed(x, z)) {
			player.sendMessage(Constants.CITYSCAPE + ChatColor.RED +
					"A city already owns that spot!");
		}
		
		RankPermissions rp = new RankPermissions(true);
		
		// Set as Transaction and execute;
		try {
			plugin.getDB().getConnection().setAutoCommit(false);
		
			cscities.createCity(player.getName(), args[0]);
			csclaims.claimChunk(args[0], x, z, worldName);
			csplayercitydata.addPlayerToCity(player.getName(), args[0]);
			csresidents.setCurrentCity(player.getName(), args[0]);
			csranks.createRank(args[0], "Mayor", rp);
			csresidents.setRank(player.getName(), "Mayor");
			plugin.getDB().getConnection().commit();
			plugin.getServer().broadcastMessage(Constants.CITYSCAPE + 
					ChatColor.GREEN + "The city of " + args[0] + " was founded!");
			
			plugin.getDB().getConnection().setAutoCommit(true);			
		} catch (SQLException e) {
			if(plugin.getSettings().debug)
				e.printStackTrace();
		}
		
		balance.subtract(plugin.getSettings().cityCost);
	}
}
