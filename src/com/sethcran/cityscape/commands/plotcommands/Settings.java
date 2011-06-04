package com.sethcran.cityscape.commands.plotcommands;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sethcran.cityscape.City;
import com.sethcran.cityscape.Cityscape;
import com.sethcran.cityscape.Constants;
import com.sethcran.cityscape.Plot;
import com.sethcran.cityscape.RankPermissions;
import com.sethcran.cityscape.commands.CSCommand;

public class Settings extends CSCommand {

	public Settings(Cityscape plugin) {
		super(plugin);
		name = "settings";
		description = "Used for plot settings.";
		usage = "/plot settings [residentbuild/outsiderdestroy/.../snow]:[on/off]";
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		Player player = null;
		if(sender instanceof Player)
			player = (Player)sender;
		else {
			sender.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR + 
					"Only players in game can do that.");
			return;
		}
		
		if(args == null) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"That command takes arguments.");
			player.sendMessage(Constants.ERROR_COLOR + usage);
			return;
		}
		
		Location location = player.getLocation();
		City city = plugin.getCityAt(location.getBlockX(), location.getBlockZ(), 
				location.getWorld().getName());
		
		if(city == null) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"You must be standing on one of your plots to do that.");
			return;
		}
		
		Plot plot = city.getPlotAt(location.getBlockX(), location.getBlockZ());
		if(plot == null) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"You must be standing on one of your plots to do that.");
			return;
		}
		
		if(!plot.getOwnerName().equalsIgnoreCase(player.getName())) {
			if(plot.isCityPlot()) {
				RankPermissions rp = city.getRank(plugin.getCache(
						player.getName()).getRank());
				if(rp == null) {
					player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
							"You do not have permission to do that.");
					return;
				}
				if(!rp.isChangeCityPlotPerms()) {
					player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
							"You do not have permission to do that.");
					return;
				}
			}
			else {
				player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
						"That is not your plot.");
				return;
			}
		}
		
		for(String s : args) {
			String[] each = s.split(":");
			if(each == null) {
				formatError(player);
				return;
			}
			
			if(each.length != 2) {
				formatError(player);
				return;
			}
			
			if(each[0].equalsIgnoreCase("residentbuild")) {
				if(each[1].equalsIgnoreCase("on"))
					plot.setResidentBuild(true);
				else if(each[1].equalsIgnoreCase("off"))
					plot.setResidentBuild(false);
				else {
					formatError(player);
					return;
				}
			}
			else if(each[0].equalsIgnoreCase("residentdestroy")) {
				if(each[1].equalsIgnoreCase("on"))
					plot.setResidentDestroy(true);
				else if(each[1].equalsIgnoreCase("off"))
					plot.setResidentDestroy(false);
				else {
					formatError(player);
					return;
				}
			}
			else if(each[0].equalsIgnoreCase("residentswitch")) {
				if(each[1].equalsIgnoreCase("on"))
					plot.setResidentSwitch(true);
				else if(each[1].equalsIgnoreCase("off"))
					plot.setResidentSwitch(false);
				else {
					formatError(player);
					return;
				}
			}
			else if(each[0].equalsIgnoreCase("outsiderbuild")) {
				if(each[1].equalsIgnoreCase("on"))
					plot.setOutsiderBuild(true);
				else if(each[1].equalsIgnoreCase("off"))
					plot.setOutsiderBuild(false);
				else {
					formatError(player);
					return;
				}
			}
			else if(each[0].equalsIgnoreCase("outsiderdestroy")) {
				if(each[1].equalsIgnoreCase("on"))
					plot.setOutsiderDestroy(true);
				else if(each[1].equalsIgnoreCase("off"))
					plot.setOutsiderDestroy(false);
				else {
					formatError(player);
					return;
				}
			}
			else if(each[0].equalsIgnoreCase("outsiderswitch")) {
				if(each[1].equalsIgnoreCase("on"))
					plot.setOutsiderSwitch(true);
				else if(each[1].equalsIgnoreCase("off"))
					plot.setOutsiderSwitch(false);
				else {
					formatError(player);
					return;
				}
			}
			else if(each[0].equalsIgnoreCase("snow")) {
				if(each[1].equalsIgnoreCase("on"))
					plot.setSnow(true);
				else if(each[1].equalsIgnoreCase("off"))
					plot.setSnow(false);
				else {
					formatError(player);
					return;
				}
			}
			else {
				formatError(player);
				return;
			}
		}
		
		player.sendMessage(Constants.CITYSCAPE + Constants.SUCCESS_COLOR +
				"You have changed the settings of this plot.");
		plugin.getDB().updatePlotSettings(plot);
	}
	
	public void formatError(Player player) {
		player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
				"There was an error with your format.");
		player.sendMessage(Constants.ERROR_COLOR + usage);
	}

}
