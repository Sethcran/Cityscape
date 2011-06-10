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
import com.sethcran.cityscape.error.ErrorManager;
import com.sethcran.cityscape.error.ErrorManager.CSError;

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
			ErrorManager.sendError(sender, CSError.IN_GAME_ONLY, null);
			return;
		}
		
		if(args != null) {
			ErrorManager.sendError(sender, CSError.TOO_MANY_ARGUMENTS, null);
			sender.sendMessage(Constants.ERROR_COLOR + usage);
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
		
		if(!plot.isForSale()) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"That plot is not for sale.");
			return;
		}
		
		Holdings balance = iConomy.getAccount(player.getName()).getHoldings();
		
		if(!balance.hasEnough(plot.getPrice())) {
			ErrorManager.sendError(sender, CSError.NOT_ENOUGH_MONEY, 
					iConomy.format(plot.getPrice()));
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
