package com.sethcran.cityscape.commands.plotcommands;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sethcran.cityscape.City;
import com.sethcran.cityscape.Cityscape;
import com.sethcran.cityscape.Plot;
import com.sethcran.cityscape.RankPermissions;
import com.sethcran.cityscape.commands.CSCommand;
import com.sethcran.cityscape.error.ErrorManager;
import com.sethcran.cityscape.error.ErrorManager.CSError;

public class RemoveAll extends CSCommand {

	public RemoveAll(Cityscape plugin) {
		super(plugin);
		name = "removeall";
		description = "Removes all player and city permissions for this plot.";
		usage = "/plot removeall";
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
		
		Location location= player.getLocation();
		Chunk chunk = location.getBlock().getChunk();
		City city = plugin.getCityAt(chunk.getX(), chunk.getZ(), chunk.getWorld().getName());
		
		if(city == null) {
			ErrorManager.sendError(sender, CSError.MUST_BE_STANDING_IN_CITY, null);
			return;
		}
		
		String playerCity = plugin.getCache(player.getName()).getCity();
		if(!city.getName().equals(playerCity)) {
			ErrorManager.sendError(sender, CSError.MUST_BE_STANDING_IN_CITY, null);
			return;
		}
		
		Plot plot = city.getPlotAt(location.getBlockX(), location.getBlockZ());
		if(plot == null) {
			ErrorManager.sendError(sender, CSError.MUST_BE_STANDING_IN_PLOT, null);
			return;
		}
		
		if(plot.isCityPlot()) {
			RankPermissions rp = plugin.getPermissions(player.getName());
			if(!rp.isChangeCityPlotPerms()) {
				ErrorManager.sendError(sender, CSError.NO_RANK_PERMISSION, null);
				return;
			}
		}
		else {
			if(!player.getName().equals(plot.getOwnerName())) {
				ErrorManager.sendError(sender, CSError.NOT_PLOT_OWNER, null);
				return;
			}
		}
		
		plot.removeAllPerms();

	}

}
