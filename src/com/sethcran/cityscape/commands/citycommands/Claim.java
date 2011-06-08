package com.sethcran.cityscape.commands.citycommands;

import java.util.ArrayList;

import org.bukkit.Chunk;
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
		usage = "/city claim (rect) (radius)";
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
		
		City pcity = plugin.getCity(playerCity);
		
		if(!pcity.hasClaims(1)) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"Your city does not have enough claims available!");
			return;
		}
		
		Chunk chunk = player.getLocation().getBlock().getChunk();
		String world = chunk.getWorld().getName();
		
		int x = chunk.getX();
		int z = chunk.getZ();
		
		City city = plugin.getCityAt(x, z, world);
		
		if(city != null) {
			if(city.getName().equals(playerCity)) {
				player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
						"Your city already owns that claim!");
			}
			else {
				player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
						"Another city already owns this claim!");
			}
			return;
		}
		
		City test1 = plugin.getCityAt(x + 1, z, world);
		City test2 = plugin.getCityAt(x - 1, z, world);
		City test3 = plugin.getCityAt(x, z + 1, world);
		City test4 = plugin.getCityAt(x, z - 1, world);
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
		
		if(args != null && args.length == 2) {
			if(args[0].equalsIgnoreCase("rect")) {
				int radius = 0;
				try {
					radius = Integer.parseInt(args[1]);
				} catch(NumberFormatException e) {
					player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
							"You must provide an integer for the radius.");
					return;
				}
				
				ArrayList<com.sethcran.cityscape.Claim> claimList = 
					new ArrayList<com.sethcran.cityscape.Claim>();
				
				for(int i = x - radius; i <= x + radius; i++) {
					for(int j = z - radius; j <= z + radius; j++) {
						City localCity = plugin.getCityAt(i, j, world);
						if(localCity == null) {
							claimList.add(new com.sethcran.cityscape.Claim(
									pcity.getName(), world, i, j));
						}
					}
				}
				
				if(claimList.isEmpty()) {
					player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
							"There are no claims in that radius available.");
					return;
				}
				
				if(!pcity.hasClaims(claimList.size())) {
					player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
							"You do not have the " + claimList.size() + " claims needed.");
					return;
				}
				
				for(com.sethcran.cityscape.Claim c : claimList) {
					plugin.addClaim(c);
					plugin.addUsedClaim(c.getCityName());
				}
				
				plugin.getDB().claimMany(claimList);
				
				player.sendMessage(Constants.CITYSCAPE + Constants.SUCCESS_COLOR +
						"You have claimed " + claimList.size() + " claims.");
				return;
			}
		}
		
		plugin.getDB().claimChunk(playerCity, world, x, z);
		com.sethcran.cityscape.Claim claim = new com.sethcran.cityscape.Claim(
				playerCity, world, x, z);
		plugin.addClaim(claim);
		plugin.addUsedClaim(claim.getCityName());
		
		player.sendMessage(Constants.CITYSCAPE + Constants.SUCCESS_COLOR +
				"Your city has annexed the claim at " + x + 
				", " + z + ".");
	}
}
