package com.sethcran.cityscape.commands.citycommands;

import org.bukkit.Chunk;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sethcran.cityscape.City;
import com.sethcran.cityscape.Cityscape;
import com.sethcran.cityscape.Constants;
import com.sethcran.cityscape.RankPermissions;
import com.sethcran.cityscape.commands.CSCommand;

public class Unclaim extends CSCommand {

	public Unclaim(Cityscape plugin) {
		super(plugin);
		name = "unclaim";
		description = "Used to unclaim the land the user is standing on.";
		usage = "/c unclaim";
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		Player player = null;
		if(sender instanceof Player)
			player = (Player)sender;
		else {
			sender.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"Only in game players can do this.");
			return;
		}
		
		String playerCity = plugin.getCache(player.getName()).getCity();
		if(playerCity == null) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"You must be in a city to perform this command.");
			return;
		}
		
		RankPermissions rp = plugin.getDB().getPermissions(player.getName());
		if(rp == null) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR + 
					"You do not have permission to do that.");
			return;
		}
		
		if(!rp.isUnclaim()) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"You do not have permission to do that.");
			return;
		}
		
		int x = player.getLocation().getBlockX();
		int z = player.getLocation().getBlockZ();
		String world = player.getWorld().getName();
		City city = plugin.getCityAt(x, z, world);
		
		if(city == null) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR + 
					"You are in the wilderness.");
			return;
		}
		
		if(!city.getName().equals(playerCity)) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR + 
					"Your city does not own this claim.");
			return;
		}
		
		if(city.getUsedClaims() == 1) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"You can't unclaim your last claim!");
			return;
		}
		
		Chunk chunk = player.getLocation().getBlock().getChunk();
		
		com.sethcran.cityscape.Claim claim = plugin.getClaimAt(x, z, world);
		plugin.getDB().unclaimChunk(claim);
		plugin.removeClaim(claim);
		
		player.sendMessage(Constants.CITYSCAPE + Constants.SUCCESS_COLOR +
				"Your town has unclaimed the area at " + chunk.getX() + ", " +
				chunk.getZ() + ".");
	}

}
