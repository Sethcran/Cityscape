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
import com.sethcran.cityscape.error.ErrorManager;
import com.sethcran.cityscape.error.ErrorManager.CSError;

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
			ErrorManager.sendError(sender, CSError.IN_GAME_ONLY, null);
			return;
		}
		
		Location location = player.getLocation();
		Chunk chunk = location.getBlock().getChunk();
		City city = plugin.getCityAt(chunk.getX(), chunk.getZ(),
				chunk.getWorld().getName());
		
		if(city == null) {
			ErrorManager.sendError(sender, CSError.MUST_BE_STANDING_IN_CITY, null);
			return;
		}
		
		if(!city.getName().equals(plugin.getCache(player.getName()).getCity())) {
			ErrorManager.sendError(sender, CSError.MUST_BE_STANDING_IN_CITY, null);
			return;
		}
		
		Plot plot = city.getPlotAt(location.getBlockX(), location.getBlockZ());
		if(plot == null) {
			ErrorManager.sendError(sender, CSError.MUST_BE_STANDING_IN_PLOT, null);
			return;
		}
		
		if(!plot.isCityPlot()) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"You can only set city plots for sale.");
			return;
		}
		
		RankPermissions rp = city.getRank(plugin.getCache(player.getName()).getRank());
		if(rp == null) {
			ErrorManager.sendError(sender, CSError.NO_RANK_PERMISSION, null);
			return;
		}
		if(!rp.isSetPlotSale()) {
			ErrorManager.sendError(sender, CSError.NO_RANK_PERMISSION, null);
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
				ErrorManager.sendError(sender, CSError.INCORRECT_NUMBER_FORMAT, null);
				return;
			}
		}
		else {
			ErrorManager.sendError(sender, CSError.TOO_MANY_ARGUMENTS, null);
			sender.sendMessage(Constants.ERROR_COLOR + usage);
			return;
		}
		
		player.sendMessage(Constants.CITYSCAPE + Constants.SUCCESS_COLOR +
				"You have set that plot for sale at " + 
				iConomy.format(plot.getPrice()) + ".");
		plugin.addLogEntry("PLOT", player.getName() + " set a plot in " + city.getName() +
				" for sale");
	}
}
