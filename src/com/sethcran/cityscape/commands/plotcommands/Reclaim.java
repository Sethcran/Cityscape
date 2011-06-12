package com.sethcran.cityscape.commands.plotcommands;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sethcran.cityscape.City;
import com.sethcran.cityscape.Cityscape;
import com.sethcran.cityscape.Constants;
import com.sethcran.cityscape.PlayerCache;
import com.sethcran.cityscape.Plot;
import com.sethcran.cityscape.RankPermissions;
import com.sethcran.cityscape.commands.CSCommand;
import com.sethcran.cityscape.error.ErrorManager;
import com.sethcran.cityscape.error.ErrorManager.CSError;

public class Reclaim extends CSCommand {

	public Reclaim(Cityscape plugin) {
		super(plugin);
		name = "reclaim";
		description = "Changes the owner of a plot to the city.";
		usage = "/plot reclaim";
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
		
		Location location = player.getLocation();
		Chunk chunk = location.getBlock().getChunk();
		City city = plugin.getCityAt(chunk.getX(), chunk.getZ(), 
				chunk.getWorld().getName());
		
		if(city == null) {
			ErrorManager.sendError(sender, CSError.NOT_IN_CITY, null);
			return;
		}
		
		PlayerCache cache = plugin.getCache(player.getName());
		
		if(!city.getName().equals(cache.getCity())) {
			ErrorManager.sendError(sender, CSError.MUST_BE_STANDING_IN_CITY, null);
			return;
		}
		
		RankPermissions rp = city.getRank(cache.getRank());
		
		if(rp == null) {
			ErrorManager.sendError(sender, CSError.NO_RANK_PERMISSION, null);
			return;
		}
		
		if(!rp.isSetPlotSale()) {
			ErrorManager.sendError(sender, CSError.NO_RANK_PERMISSION, null);
			return;
		}
		
		Plot plot = city.getPlotAt(location.getBlockX(), location.getBlockZ());
		
		if(plot == null) {
			ErrorManager.sendError(sender, CSError.MUST_BE_STANDING_IN_PLOT, null);
			return;
		}
		
		plot.setOwnerName(city.getName());
		plot.setCityPlot(true);
		plot.setPrice(0);
		plugin.getDB().setPlotForSale(plot);
		
		player.sendMessage(Constants.CITYSCAPE + Constants.SUCCESS_COLOR + 
				"You have successfully reclaimed the plot.");
		plugin.addLogEntry("PLOT", player.getName() + " reclaimed a plot in " + 
				city.getName());
	}
}
