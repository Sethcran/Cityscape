package com.sethcran.cityscape.commands.citycommands;

import org.bukkit.Chunk;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sethcran.cityscape.City;
import com.sethcran.cityscape.Cityscape;
import com.sethcran.cityscape.Constants;
import com.sethcran.cityscape.PlayerCache;
import com.sethcran.cityscape.RankPermissions;
import com.sethcran.cityscape.commands.CSCommand;
import com.sethcran.cityscape.error.ErrorManager;
import com.sethcran.cityscape.error.ErrorManager.CSError;

public class Ban extends CSCommand {

	public Ban(Cityscape plugin) {
		super(plugin);
		name = "ban";
		description = "Bans the players from entering your city.";
		usage = "/c ban playername (playername) ...";
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
		
		PlayerCache cache = plugin.getCache(player.getName());		
		City city = plugin.getCity(cache.getCity());
		
		if(city == null) {
			ErrorManager.sendError(sender, CSError.NOT_IN_CITY, null);
			return;
		}
		
		RankPermissions rp = city.getRank(cache.getRank());
		if(!rp.isBan()) {
			ErrorManager.sendError(sender, CSError.NO_RANK_PERMISSION, null);
			return;
		}
		
		if(args == null) {
			ErrorManager.sendError(sender, CSError.NOT_ENOUGH_ARGUMENTS, null);
			player.sendMessage(Constants.ERROR_COLOR + usage);
			return;
		}
		
		for(String s : args) {
			if(s.equals(player.getName())) {
				ErrorManager.sendError(sender, CSError.NOT_APPLICABLE_TO_SELF, null);
			}
			else if(plugin.getDB().doesPlayerExist(s)) {
				String c = plugin.getDB().getPlayerCity(s);
				if(city.getName().equals(c)) {
					ErrorManager.sendError(sender, CSError.PLAYER_IN_YOUR_CITY, c);
					return;
				}
				city.ban(s);
				plugin.getDB().ban(city.getName(), s);
				player.sendMessage(Constants.CITYSCAPE + Constants.SUCCESS_COLOR + 
						"You have banned " + s + ".");
				plugin.addLogEntry("CITY", player.getName() + " banned " + s + " from " +
						city.getName());
				
				Player p = plugin.getServer().getPlayer(s);
				if(p != null) {
					Chunk chunk = p.getLocation().getBlock().getChunk();
					City lcity = plugin.getCityAt(chunk.getX(), chunk.getZ(), 
							chunk.getWorld().getName());
					if(lcity != null && lcity.getName().equals(city.getName())) {
						p.teleport(plugin.getServer().getWorld(
								chunk.getWorld().getName()).getSpawnLocation());
						p.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR + 
						"You have been banned from " + city.getName() + ".");
					}
				}
			}
			else {
				ErrorManager.sendError(sender, CSError.PLAYER_DOES_NOT_EXIST, s);
			}
		}			
	}
}
