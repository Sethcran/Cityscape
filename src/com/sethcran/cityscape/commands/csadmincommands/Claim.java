package com.sethcran.cityscape.commands.csadmincommands;

import org.bukkit.Chunk;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sethcran.cityscape.City;
import com.sethcran.cityscape.Cityscape;
import com.sethcran.cityscape.Constants;
import com.sethcran.cityscape.commands.CSCommand;
import com.sethcran.cityscape.error.ErrorManager;
import com.sethcran.cityscape.error.ErrorManager.CSError;

public class Claim extends CSCommand {

	public Claim(Cityscape plugin) {
		super(plugin);
		name = "claim";
		description = "Claims the area for the specified town.";
		return;
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		Player player = null;
		if(sender instanceof Player)
			player = (Player)sender;
		else {
			ErrorManager.sendError(sender, CSError.IN_GAME_ONLY, null);
			return;
		}
		
		if(!plugin.permissionHandler.has(player, "cs.admin")) {
			return;
		}
		
		if(args == null) {
			ErrorManager.sendError(sender, CSError.NOT_ENOUGH_ARGUMENTS, null);
			sender.sendMessage(Constants.ERROR_COLOR + usage);
			return;
		}
		
		if(args.length != 1) {
			ErrorManager.sendError(sender, CSError.TOO_MANY_ARGUMENTS, null);
			return;
		}
		
		City city = plugin.getCity(args[0]);
		if(city == null) {
			ErrorManager.sendError(sender, CSError.CITY_DOES_NOT_EXIST, args[0]);
			return;
		}
		
		Chunk chunk = player.getLocation().getBlock().getChunk();
		com.sethcran.cityscape.Claim claim = plugin.getClaimAt(chunk.getX(), chunk.getZ(), 
				chunk.getWorld().getName());
		if(claim != null) {
			plugin.getDB().unclaimChunk(claim);
			plugin.removeClaim(claim);
		}
		
		plugin.getDB().claimChunk(city.getName(), chunk.getWorld().getName(), 
				chunk.getX(), chunk.getZ());
		claim = new com.sethcran.cityscape.Claim(
				city.getName(), chunk.getWorld().getName(), chunk.getX(), chunk.getZ());
		plugin.addClaim(claim);
		plugin.addUsedClaim(claim.getCityName());
		
		player.sendMessage(Constants.CITYSCAPE + Constants.SUCCESS_COLOR +
				"You have claimed the chunk for " + args[0] + ".");	
		plugin.addLogEntry("ADMIN", player.getName() + " claimed " + chunk.getX() + ", " +
				chunk.getZ() + " for " + args[0]);
	}

}
