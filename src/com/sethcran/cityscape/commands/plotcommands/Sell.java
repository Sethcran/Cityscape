package com.sethcran.cityscape.commands.plotcommands;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.iConomy.iConomy;
import com.sethcran.cityscape.City;
import com.sethcran.cityscape.Cityscape;
import com.sethcran.cityscape.Constants;
import com.sethcran.cityscape.Plot;
import com.sethcran.cityscape.RankPermissions;
import com.sethcran.cityscape.commands.CSCommand;

public class Sell extends CSCommand {

	public Sell(Cityscape plugin) {
		super(plugin);
		name = "sell";
		description = "Sets the plot for sale.";
		usage = "/plot sell";
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
		
		Location location = player.getLocation();
		Chunk chunk = location.getBlock().getChunk();
		City city = plugin.getCityAt(chunk.getX(), chunk.getZ(),
				chunk.getWorld().getName());
		
		if(city == null) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"You must be in city limits to do that.");
			return;
		}
		
		if(!city.getName().equals(plugin.getCache(player.getName()).getCity())) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"You must be in your own city to do that.");
			return;
		}
		
		Plot plot = city.getPlotAt(location.getBlockX(), location.getBlockZ());
		if(plot == null) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"You must be standing on one of your plots to do that.");
			return;
		}
		
		if(!plot.isCityPlot()) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"You can only set city plots for sale.");
			return;
		}
		
		RankPermissions rp = city.getRank(plugin.getCache(player.getName()).getRank());
		if(rp == null) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"You do not have permission to do that.");
			return;
		}
		if(!rp.isSetPlotSale()) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"You do not have permission to do that.");
			return;
		}
		
		if(args == null) {
			plot.setForSale(true);
			plugin.getDB().setPlotForSale(plot);
		}
		else if(args.length == 1) {
			try {
				int price = Integer.parseInt(args[0]);
				plot.setForSale(true);
				plot.setPrice(price);
				plugin.getDB().setPlotForSale(plot);
			} catch(NumberFormatException e) {
				player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
						"The argument must be an integer.");
				return;
			}
		}
		else {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"That command takes a price as an argument.");
			return;
		}
		
		player.sendMessage(Constants.CITYSCAPE + Constants.SUCCESS_COLOR +
				"You have set that plot for sale at " + 
				iConomy.format(plot.getPrice()) + ".");
	}
}
