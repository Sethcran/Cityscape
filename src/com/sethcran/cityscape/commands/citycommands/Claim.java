package com.sethcran.cityscape.commands.citycommands;

import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sethcran.cityscape.City;
import com.sethcran.cityscape.Cityscape;
import com.sethcran.cityscape.Constants;
import com.sethcran.cityscape.RankPermissions;
import com.sethcran.cityscape.commands.CSCommand;

public class Claim extends CSCommand {

	public Claim(Cityscape plugin) {
		super(plugin);
		name = "claim";
		description = "Claims the chunk you are standing on for your town.";
		usage = "/city claim";
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
		
		RankPermissions rp = plugin.getPermissions(player.getName());
		if(rp == null) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR + 
					"You do not have permission to do that.");
			return;
		}
		
		if(!rp.isClaim()) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"You do not have permission to do that.");
			return;
		}
		
		if(!plugin.getDB().hasClaims(playerCity, 1)) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"Your city does not have enough claims available!");
			return;
		}
		
		Chunk chunk = player.getLocation().getBlock().getChunk();
		String world = chunk.getWorld().getName();
		
		Block minBlock = chunk.getBlock(0, 0, 0);
		Block maxBlock = chunk.getBlock(15, 0, 15);
		int xmin = minBlock.getX();
		int zmin = minBlock.getZ();
		int xmax = maxBlock.getX();
		int zmax = maxBlock.getZ();
		
		int x = player.getLocation().getBlockX();
		int z = player.getLocation().getBlockZ();
		
		City city = plugin.getCityAt(x, z, world);
		
		if(city != null) {
			if(city.getName().equals(playerCity)) {
				player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
						"Your city already owns that claim!");
			}
			else {
				player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
						"Another town already owns this claim!");
			}
			return;
		}
		
		City test1 = plugin.getCityAt(x + 16, z, world);
		City test2 = plugin.getCityAt(x - 16, z, world);
		City test3 = plugin.getCityAt(x, z + 16, world);
		City test4 = plugin.getCityAt(x, z - 16, world);
		boolean good = false;
		
		if(test1 != null) {
			if(test1.getName().equals(playerCity))
				good = true;
		}
		else if(test2 != null) {
			if(test2.getName().equals(playerCity))
				good = true;
		}
		else if(test3 != null) {
			if(test3.getName().equals(playerCity))
				good = true;
		}
		else if(test4 != null) {
			if(test4.getName().equals(playerCity))
				good = true;
		}
		
		if(!good) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"That claim is not connected to your land.");
			return;
		}
		
		plugin.getDB().claimChunk(playerCity, world, xmin, zmin, xmax, zmax);
		com.sethcran.cityscape.Claim claim = new com.sethcran.cityscape.Claim(
				playerCity, world, xmin, zmin, xmax, zmax, plugin.getDB().getLastClaimID());
		plugin.addClaim(claim);
		plugin.addUsedClaim(claim.getCityName());
		
		player.sendMessage(Constants.CITYSCAPE + Constants.SUCCESS_COLOR +
				"Your city has annexed the claim at " + chunk.getX() + 
				", " + chunk.getZ() + ".");
	}
}
