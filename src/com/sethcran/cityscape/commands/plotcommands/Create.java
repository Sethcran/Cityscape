package com.sethcran.cityscape.commands.plotcommands;

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
			sender.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"Only a player in game can do that.");
			return;
		}
		
		RankPermissions rp = plugin.getPermissions(player.getName());
		if(rp == null) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"You do not have permission to do that.");
			return;
		}
		if(!rp.isCreatePlots()) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"You do not have permission to do that.");
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
		
		for(int i = selection.getXmin(); i < selection.getXmax(); i += 16) {
			for(int j = selection.getZmin(); j < selection.getZmax(); j += 16) {
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
	}
}
