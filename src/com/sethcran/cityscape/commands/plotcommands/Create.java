package com.sethcran.cityscape.commands.plotcommands;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sethcran.cityscape.City;
import com.sethcran.cityscape.Cityscape;
import com.sethcran.cityscape.Constants;
import com.sethcran.cityscape.Plot;
import com.sethcran.cityscape.RankPermissions;
import com.sethcran.cityscape.Selection;
import com.sethcran.cityscape.Settings;
import com.sethcran.cityscape.commands.CSCommand;
import com.sethcran.cityscape.error.ErrorManager;
import com.sethcran.cityscape.error.ErrorManager.CSError;

public class Create extends CSCommand {

	public Create(Cityscape plugin) {
		super(plugin);
		name = "create";
		description = "Used to create a plot where selected.";
		usage = "/plot create";
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
		
		RankPermissions rp = plugin.getPermissions(player.getName());
		if(rp == null) {
			ErrorManager.sendError(sender, CSError.NO_RANK_PERMISSION, null);
			return;
		}
		if(!rp.isCreatePlots()) {
			ErrorManager.sendError(sender, CSError.NO_RANK_PERMISSION, null);
			return;
		}
		
		Selection selection = plugin.getSelection(player.getName());
		if(selection == null) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"You must first create a selection for the plot.");
			player.sendMessage(Constants.ERROR_COLOR + "Use /plot select");
			return;
		}
		
		if(!(selection.isSetFirst() && selection.isSetSecond())) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"You must first select two points to define the area.");
			return;
		}
		
		if(!selection.getFirstWorld().equals(selection.getSecondWorld())) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"Your selection is in two different worlds!");
			return;
		}
		
		String playerCity = plugin.getCache(player.getName()).getCity();
		City city = null;
		
		World world = plugin.getServer().getWorld(selection.getFirstWorld());
		Chunk minChunk = world.getBlockAt(selection.getXmin(), 0, 
				selection.getZmin()).getChunk();
		Chunk maxChunk = world.getBlockAt(selection.getXmax(), 0, 
				selection.getZmax()).getChunk();
		int xmin = minChunk.getX();
		int zmin = minChunk.getZ();
		int xmax = maxChunk.getX();
		int zmax = maxChunk.getZ();
		
		for(int i = xmin; i <= xmax; i++) {
			for(int j = zmin; j <= zmax; j++) {
				city = plugin.getCityAt(i, j, selection.getFirstWorld());
				if(city == null) {
					player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
							"An area of your selection is outside city limits.");
					return;
				}
				if(!city.getName().equals(playerCity)) {
					player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
							"An area of your selection belongs to another city.");
					return;
				}
			}
		}
		
		Plot p = city.isPlotIntersect(selection.getXmin(), selection.getZmin(),
				selection.getXmax(), selection.getZmax());
		if(p != null){
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"That selection intersects with a plot owned by " + p.getOwnerName());
			return;
		}
		
		Settings settings = plugin.getSettings();
		
		Plot plot = new Plot(selection.getXmin(), selection.getZmin(), 
				selection.getXmax(), selection.getZmax());
		plot.setCityName(playerCity);
		plot.setOwnerName(playerCity);
		plot.setOutsiderBuild(settings.defaultOutsiderBuild);
		plot.setOutsiderDestroy(settings.defaultOutsiderDestroy);
		plot.setOutsiderSwitch(settings.defaultOutsiderSwitch);
		plot.setResidentBuild(settings.defaultResidentBuild);
		plot.setResidentDestroy(settings.defaultResidentDestroy);
		plot.setResidentSwitch(settings.defaultResidentSwitch);
		plot.setCityPlot(true);
		plot.setSnow(settings.defaultSnow);
		
		plugin.getDB().addPlot(plot);
		plot.setId(plugin.getDB().getLastPlotID());
		city.addPlot(plot);
		
		player.sendMessage(Constants.CITYSCAPE + Constants.SUCCESS_COLOR +
				"You have created a plot.");
		plugin.addLogEntry("PLOT", player.getName() + " created a plot in " + 
				city.getName());
	}
}
