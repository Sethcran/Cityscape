package com.sethcran.cityscape.commands.plotcommands;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sethcran.cityscape.City;
import com.sethcran.cityscape.Cityscape;
import com.sethcran.cityscape.Constants;
import com.sethcran.cityscape.Plot;
import com.sethcran.cityscape.RankPermissions;
import com.sethcran.cityscape.commands.CSCommand;
import com.sethcran.cityscape.error.ErrorManager;
import com.sethcran.cityscape.error.ErrorManager.CSError;

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
			ErrorManager.sendError(sender, CSError.IN_GAME_ONLY, null);
			return;
		}
		
		if(args == null) {
			ErrorManager.sendError(sender, CSError.NOT_ENOUGH_ARGUMENTS, null);
			player.sendMessage(Constants.ERROR_COLOR + usage);
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
		
		if(!plot.getOwnerName().equalsIgnoreCase(player.getName())) {
			if(plot.isCityPlot()) {
				RankPermissions rp = city.getRank(plugin.getCache(
						player.getName()).getRank());
				if(rp == null) {
					ErrorManager.sendError(sender, CSError.NO_RANK_PERMISSION, null);
					return;
				}
				if(!rp.isChangeCityPlotPerms()) {
					ErrorManager.sendError(sender, CSError.NO_RANK_PERMISSION, null);
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
		plugin.addLogEntry("PLOT", player.getName() + " changed a plot's settings " + 
				"in " + plot.getCityName());
	}
	
	public void formatError(Player player) {
		ErrorManager.sendError(player, CSError.INCORRECT_FORMAT, null);
		player.sendMessage(Constants.ERROR_COLOR + usage);
	}

}
