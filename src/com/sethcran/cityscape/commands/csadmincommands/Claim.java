package com.sethcran.cityscape.commands.csadmincommands;

import org.bukkit.Chunk;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sethcran.cityscape.City;
import com.sethcran.cityscape.Cityscape;
import com.sethcran.cityscape.Constants;
import com.sethcran.cityscape.commands.CSCommand;

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
			sender.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"You must be in game to do that.");
			return;
		}
		
		if(!plugin.permissionHandler.has(player, "cs.admin")) {
			return;
		}
		
		if(args == null) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"That command requires a city to give the claim to.");
			return;
		}
		
		if(args.length != 1) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"That command requires only a city name.");
			return;
		}
		
		City city = plugin.getCity(args[0]);
		if(city == null) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"That city does not exist.");
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
				city.getName(), chunk.getWorld().getName(), chunk.getX(), chunk.getZ(), 
				plugin.getDB().getLastClaimID());
		plugin.addClaim(claim);
		plugin.addUsedClaim(claim.getCityName());
		
		player.sendMessage(Constants.CITYSCAPE + Constants.SUCCESS_COLOR +
				"You have claimed the chunk for " + args[0] + ".");		
	}

}
