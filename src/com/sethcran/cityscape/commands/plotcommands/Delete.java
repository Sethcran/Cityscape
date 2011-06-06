package com.sethcran.cityscape.commands.plotcommands;

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

public class Delete extends CSCommand {

	public Delete(Cityscape plugin) {
		super(plugin);
		name = "delete";
		description = "Deletes the plot where the player is standing.";
		usage = "/plot delete";
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		Player player = null;
		if(sender instanceof Player)
			player = (Player)sender;
		else {
			sender.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"Only a player in game can do that.");
			return;
		}
		
		if(args != null) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"That command does not take arguments.");
			return;
		}
		
		Location location = player.getLocation();
		City city = plugin.getCityAt(location.getBlockX(), location.getBlockZ(), 
				location.getWorld().getName());
		if(city == null) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"You  must be standing inside a plot to do that.");
			return;
		}
		
		PlayerCache cache = plugin.getCache(player.getName());
		if(!city.getName().equals(cache.getCity())) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"You must be in your city to do that.");
			return;
		}
		
		RankPermissions rp = city.getRank(cache.getRank());
		if(!rp.isCreatePlots()) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"You do not have permission to do that.");
			return;
		}
		
		Plot plot = city.getPlotAt(location.getBlockX(), location.getBlockZ());
		if(plot == null) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"You must be standing in a plot to do that.");
			return;
		}
		
		city.removePlot(plot);
		plugin.getDB().removePlot(plot.getId());
		
		player.sendMessage(Constants.CITYSCAPE + Constants.SUCCESS_COLOR + 
				"You have deleted the plot.");
	}

}
