package com.sethcran.cityscape.commands.citycommands;

import org.bukkit.Chunk;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sethcran.cityscape.Cityscape;
import com.sethcran.cityscape.Constants;
import com.sethcran.cityscape.RankPermissions;
import com.sethcran.cityscape.commands.CSCommand;
import com.sethcran.cityscape.database.CSClaims;
import com.sethcran.cityscape.database.CSRanks;
import com.sethcran.cityscape.database.CSResidents;

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
		
		CSResidents csresidents = plugin.getDB().getCSResidents();
		CSClaims csclaims = plugin.getDB().getCSClaims();
		CSRanks csranks = plugin.getDB().getCSRanks();
		
		String playerCity = csresidents.getCurrentCity(player.getName());
		if(playerCity == null) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"You must be in a town to perform this command.");
			return;
		}
		
		String rank = csresidents.getRank(player.getName());
		if(rank == null) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"You do not have permission to do that.");
			return;
		}
		RankPermissions rp = csranks.getPermissions(playerCity, rank);
		if(!rp.getClaim()) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"You do not have the permission to do that.");
			return;
		}
		
		Chunk chunk = player.getLocation().getBlock().getChunk();
		int x = chunk.getX();
		int z = chunk.getZ();
		String city = csclaims.getCityAt(x, z);
		
		if(city != null) {
			if(city.equals(playerCity)) {
				player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
						"Your city already owns that claim!");
			}
			else {
				player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
						"Another town already owns this claim!");
			}
			return;
		}
		
		csclaims.claimChunk(playerCity, x, z, chunk.getWorld().getName());
		player.sendMessage(Constants.CITYSCAPE + Constants.SUCCESS_COLOR +
				"Your town has annexed the claim at " + x + ", " + z + ".");
	}
}
