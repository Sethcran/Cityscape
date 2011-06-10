package com.sethcran.cityscape.commands.citycommands;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.iConomy.iConomy;
import com.iConomy.system.Holdings;
import com.sethcran.cityscape.City;
import com.sethcran.cityscape.Cityscape;
import com.sethcran.cityscape.Constants;
import com.sethcran.cityscape.PlayerCache;
import com.sethcran.cityscape.RankPermissions;
import com.sethcran.cityscape.commands.CSCommand;
import com.sethcran.cityscape.database.Database;
import com.sethcran.cityscape.error.ErrorManager;
import com.sethcran.cityscape.error.ErrorManager.CSError;

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
			ErrorManager.sendError(sender, CSError.IN_GAME_ONLY, null);
			return;
		}
		
		// Check that arguments were provided correctly
		if(args == null) {
			ErrorManager.sendError(sender, CSError.NOT_ENOUGH_ARGUMENTS, null);
			player.sendMessage(Constants.ERROR_COLOR + usage);
			return;
		}
		if(args.length > 1) {
			ErrorManager.sendError(sender, CSError.TOO_MANY_ARGUMENTS, null);
			player.sendMessage(Constants.ERROR_COLOR + usage);
			return;
		}
		
		// Check that town name is of appropriate length
		if(args[0].length() > Constants.TOWN_MAX_NAME_LENGTH) {
			ErrorManager.sendError(sender, CSError.LENGTH_EXCEEDED, 
					"" + Constants.TOWN_MAX_NAME_LENGTH);
			return;
		}
		
		// Check that town name is alphabetic
		if(!args[0].matches("[a-zA-Z]+")) {
			ErrorManager.sendError(sender, CSError.INCORRECT_NAME_FORMAT, null);
			return;
		}
		
		// Check that user has permissions to create a city.
		if(!plugin.permissionHandler.has(player, "cityscape.createcity")) {
			ErrorManager.sendError(sender, CSError.NO_PERMISSION, null);
			return;
		}
		
		// Check that user has enough money.
		Holdings balance = iConomy.getAccount(player.getName()).getHoldings();
		if(!balance.hasEnough(plugin.getSettings().cityCost)) {
			ErrorManager.sendError(sender, CSError.NOT_ENOUGH_MONEY, 
					"" + plugin.getSettings().cityCost);
			return;
		}
		
		// Get needed tables;
		Database db = plugin.getDB();
		
		// Check that player is not in a city
		String currentCity = db.getCurrentCity(player.getName());
		if(currentCity != null) {
			ErrorManager.sendError(sender, CSError.ALREADY_IN_A_CITY, null);
			return;
		}
		
		// Check that selected cityname does not exist
		if(db.doesCityExist(args[0])) {
			ErrorManager.sendError(sender, CSError.CITY_ALREADY_EXISTS, args[0]);
			return;
		}
		
		// Get the coordinates and world where player is standing
		Chunk chunk = player.getLocation().getBlock().getChunk();
		int x = chunk.getX();
		int z = chunk.getZ();
		String worldName = chunk.getWorld().getName();
		
		// Check that the current chunk is unclaimed
		City localCity = plugin.getCityAt(x, z, worldName);
		if(localCity != null) {
			ErrorManager.sendError(sender, CSError.CITY_ALREADY_OWNS, localCity.getName());
			return;
		}
		
		db.createCity(player.getName(), args[0], worldName, x, z);
		
		plugin.getServer().broadcastMessage(Constants.CITYSCAPE + 
				ChatColor.GREEN + "The city of " + args[0] + " was founded!");
		
		City city = plugin.getDB().getCity(args[0]);
		plugin.insertIntoCityCache(args[0], city);
		com.sethcran.cityscape.Claim claim = new com.sethcran.cityscape.Claim(
				args[0], worldName, x, z);
		plugin.addClaim(claim);
		
		PlayerCache cache = plugin.getCache(player.getName());
		cache.setCity(args[0]);
		cache.setRank("Mayor");
		RankPermissions rp = new RankPermissions(true);
		rp.setRankName("Mayor");
		city.addRank(rp);
		
		city.getAccount();
		
		plugin.getDB().removeAllInvites(player.getName());
		
		balance.subtract(plugin.getSettings().cityCost);
	}
}
