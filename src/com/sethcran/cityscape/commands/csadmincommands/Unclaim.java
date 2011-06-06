package com.sethcran.cityscape.commands.csadmincommands;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sethcran.cityscape.Cityscape;
import com.sethcran.cityscape.Claim;
import com.sethcran.cityscape.Constants;
import com.sethcran.cityscape.commands.CSCommand;

public class Unclaim extends CSCommand {

	public Unclaim(Cityscape plugin) {
		super(plugin);
		name = "unclaim";
		description = "Unclaims the area from whoever owns it.";
		usage = "/csa unclaim";
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
		
		if(args != null) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"That command does not take arguments.");
			return;
		}
		
		Location location = player.getLocation();
		Claim claim = plugin.getClaimAt(location.getBlockX(), location.getBlockZ(), 
				location.getWorld().getName());
		
		if(claim == null) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"You are in the wilderness.");
			return;
		}
		
		plugin.removeClaim(claim);
		plugin.getDB().unclaimChunk(claim);
		
		player.sendMessage(Constants.CITYSCAPE + Constants.SUCCESS_COLOR +
				"You have unclaimed the claim at your location.");
	}

}
