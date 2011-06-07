package com.sethcran.cityscape.commands.plotcommands;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.iConomy.iConomy;
import com.iConomy.system.Holdings;
import com.sethcran.cityscape.City;
import com.sethcran.cityscape.Cityscape;
import com.sethcran.cityscape.Constants;
import com.sethcran.cityscape.Plot;
import com.sethcran.cityscape.commands.CSCommand;

public class Buy extends CSCommand {

	public Buy(Cityscape plugin) {
		super(plugin);
		name = "buy";
		description = "Used to buy a plot that is for sale.";
		usage = "/plot buy";
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
		
		if(args != null) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"That command does not take arguments.");
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
		
		if(!plot.isForSale()) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"That plot is not for sale.");
			return;
		}
		
		Holdings balance = iConomy.getAccount(player.getName()).getHoldings();
		
		if(!balance.hasEnough(plot.getPrice())) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"You do not have enough money. That costs " +
					iConomy.format(plot.getPrice()) + ".");
			return;
		}
		
		plot.setOwnerName(player.getName());
		plot.setForSale(false);
		plot.setCityPlot(false);
		plugin.getDB().setPlotForSale(plot);
		balance.subtract(plot.getPrice());
		
		Holdings cityBalance = city.getAccount().getHoldings();
		cityBalance.add(plot.getPrice());
		
		player.sendMessage(Constants.CITYSCAPE + Constants.SUCCESS_COLOR +
				"You have bought the plot for " +iConomy.format(plot.getPrice()) + ".");

	}

}
