package com.sethcran.cityscape.commands.csadmincommands;

import org.bukkit.Chunk;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sethcran.cityscape.Cityscape;
import com.sethcran.cityscape.Claim;
import com.sethcran.cityscape.Constants;
import com.sethcran.cityscape.commands.CSCommand;
import com.sethcran.cityscape.error.ErrorManager;
import com.sethcran.cityscape.error.ErrorManager.CSError;

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
			ErrorManager.sendError(sender, CSError.IN_GAME_ONLY, null);
			return;
		}
		
		if(!plugin.permissionHandler.has(player, "cs.admin")) {
			return;
		}
		
		if(args != null) {
			ErrorManager.sendError(sender, CSError.TOO_MANY_ARGUMENTS, null);
			sender.sendMessage(Constants.ERROR_COLOR + usage);
			return;
		}
		
		Chunk chunk = player.getLocation().getBlock().getChunk();
		int x = chunk.getX();
		int z = chunk.getZ();
		Claim claim = plugin.getClaimAt(x, z, chunk.getWorld().getName());
		
		if(claim == null) {
			ErrorManager.sendError(sender, CSError.WILDERNESS, null);
			return;
		}
		
		String owner = claim.getCityName();
		plugin.removeClaim(claim);
		plugin.getDB().unclaimChunk(claim);
		
		player.sendMessage(Constants.CITYSCAPE + Constants.SUCCESS_COLOR +
				"You have unclaimed the claim at " + x + ", " + z + ".");
		plugin.addLogEntry("ADMIN", player.getName() + " unclaimed the claim at " + x + 
				", " + z + " owned by " + owner);
	}

}
