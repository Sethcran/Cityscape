package com.sethcran.cityscape.commands.plotcommands;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sethcran.cityscape.City;
import com.sethcran.cityscape.Cityscape;
import com.sethcran.cityscape.Constants;
import com.sethcran.cityscape.Permissions;
import com.sethcran.cityscape.Plot;
import com.sethcran.cityscape.RankPermissions;
import com.sethcran.cityscape.commands.CSCommand;
import com.sethcran.cityscape.error.ErrorManager;
import com.sethcran.cityscape.error.ErrorManager.CSError;

public class Perms extends CSCommand {

	public Perms(Cityscape plugin) {
		super(plugin);
		name = "perms";
		description = "Used to change the permissions of the plot.";
		usage = "/plot perms [add(c)/remove(c)]:[name]:[all/build/destroy/switch]";
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
			ErrorManager.sendError(sender, CSError.WILDERNESS, null);
			return;
		}
		
		if(plugin.getCache(player.getName()).getCity() == null) {
			ErrorManager.sendError(sender, CSError.NOT_IN_CITY, null);
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
		
		if(plot.isCityPlot()) {
			RankPermissions rp = plugin.getPermissions(player.getName());
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
			if(!plot.getOwnerName().equals(player.getName())) {
				ErrorManager.sendError(sender, CSError.NO_PERMISSION, null);
				return;
			}
		}
		
		for(String s : args) {
			String[] each = s.split(":");
			if(each == null) {
				ErrorManager.sendError(sender, CSError.INCORRECT_FORMAT, null);
				player.sendMessage(Constants.ERROR_COLOR + usage);
				return;
			}
			
			if(each.length != 3) {
				ErrorManager.sendError(sender, CSError.INCORRECT_FORMAT, null);
				player.sendMessage(Constants.ERROR_COLOR + usage);
				return;
			}
			
			if(!("all".equalsIgnoreCase(each[2]) || 
					"build".equalsIgnoreCase(each[2]) ||
					"destroy".equalsIgnoreCase(each[2]) || 
					"switch".equalsIgnoreCase(each[2]))) {
				ErrorManager.sendError(sender, CSError.INCORRECT_FORMAT, null);
				player.sendMessage(Constants.ERROR_COLOR + usage);
				return;
			}
			
			if("remove".equalsIgnoreCase(each[0])) {
				Permissions perms = plot.getPlayerPermissions(each[1]);
				if(perms == null) {
					player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
							each[1] + " did not have permissions here.");
					return;
				}
				if(each[2].equalsIgnoreCase("all")) {
					perms.setCanBuild(false);
					perms.setCanDestroy(false);
					perms.setCanSwitch(false);
				}
				else if(each[2].equalsIgnoreCase("build"))
					perms.setCanBuild(false);
				else if(each[2].equalsIgnoreCase("destroy"))
					perms.setCanDestroy(false);
				else if(each[2].equalsIgnoreCase("switch"))
					perms.setCanSwitch(false);
				plot.removeFromPlayerPermissions(each[1]);
				if(!perms.isCanBuild() && !perms.isCanDestroy() && !perms.isCanSwitch()) {
					plugin.getDB().removePlotPermissions(plot.getId(), each[1], true);
				}
				else {
					plot.insertIntoPlayerPermissions(each[1], perms);
					plugin.getDB().setPlotPermissions(plot.getId(), each[1], perms, true);
				}
					
				player.sendMessage(Constants.CITYSCAPE + Constants.SUCCESS_COLOR +
						"The player " + each[1] + "'s permissions have changed.");
			}
			else if("removec".equalsIgnoreCase(each[0])) {
				Permissions perms = plot.getCityPermissions(each[1]);
				if(perms == null) {
					player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
							each[1] + " did not have permissions here.");
					return;
				}
				if(each[2].equalsIgnoreCase("all")) {
					perms.setCanBuild(false);
					perms.setCanDestroy(false);
					perms.setCanSwitch(false);
				}
				else if(each[2].equalsIgnoreCase("build"))
					perms.setCanBuild(false);
				else if(each[2].equalsIgnoreCase("destroy"))
					perms.setCanDestroy(false);
				else if(each[2].equalsIgnoreCase("switch"))
					perms.setCanSwitch(false);
				plot.removeFromCityPermissions(each[1]);
				if(!perms.isCanBuild() && !perms.isCanDestroy() && !perms.isCanSwitch()) {
					plugin.getDB().removePlotPermissions(plot.getId(), each[1], false);
				}
				else {
					plot.insertIntoCityPermissions(each[1], perms);
					plugin.getDB().setPlotPermissions(plot.getId(), each[1], perms, false);
				}
				
				player.sendMessage(Constants.CITYSCAPE + Constants.SUCCESS_COLOR +
						"The city of " + each[1] + "'s permissions have changed.");
			}
			else if("add".equalsIgnoreCase(each[0])) {
				if(!plugin.getDB().doesPlayerExist(each[1])) {
					ErrorManager.sendError(sender, CSError.PLAYER_DOES_NOT_EXIST, each[1]);
					return;
				}
				
				if(!plot.isCityPlot()) {
					if(each[1].equals(plot.getOwnerName())) {
						player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
								"You can't add the plot owner.");
						return;
					}
				}
				
				Permissions perms = plot.getPlayerPermissions(each[1]);
				if(perms == null)
					perms = new Permissions(false, false, false);
				
				if(each[2].equalsIgnoreCase("all")) {
					perms.setCanBuild(true);
					perms.setCanDestroy(true);
					perms.setCanSwitch(true);
				}
				else if(each[2].equalsIgnoreCase("build"))
					perms.setCanBuild(true);
				else if(each[2].equalsIgnoreCase("destroy"))
					perms.setCanDestroy(true);
				else if(each[2].equalsIgnoreCase("switch"))
					perms.setCanSwitch(true);
				plot.removeFromPlayerPermissions(each[1]);
				plot.insertIntoPlayerPermissions(each[1], perms);
				plugin.getDB().setPlotPermissions(plot.getId(), each[1], perms, true);
				
				player.sendMessage(Constants.CITYSCAPE + Constants.SUCCESS_COLOR +
						"The player " + each[1] + "'s permissions have changed.");
			}
			else if("addc".equalsIgnoreCase(each[0])) {
				if(!plugin.getDB().doesCityExist(each[1])) {
					ErrorManager.sendError(sender, CSError.PLAYER_DOES_NOT_EXIST, each[1]);
					return;
				}
				
				if(each[1].equals(plot.getOwnerName())) {
					player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
							"You can't add the plot owner.");
					return;
				}
				
				Permissions perms = plot.getPlayerPermissions(each[1]);
				if(perms == null)
					perms = new Permissions(false, false, false);
				
				if(each[2].equalsIgnoreCase("all")) {
					perms.setCanBuild(true);
					perms.setCanDestroy(true);
					perms.setCanSwitch(true);
				}
				else if(each[2].equalsIgnoreCase("build"))
					perms.setCanBuild(true);
				else if(each[2].equalsIgnoreCase("destroy"))
					perms.setCanDestroy(true);
				else if(each[2].equalsIgnoreCase("switch"))
					perms.setCanSwitch(true);
				plot.removeFromCityPermissions(each[1]);
				plot.insertIntoCityPermissions(each[1], perms);
				plugin.getDB().setPlotPermissions(plot.getId(), each[1], perms, false);
				
				player.sendMessage(Constants.CITYSCAPE + Constants.SUCCESS_COLOR +
						"The city of " + each[1] + "'s permissions have changed.");
				plugin.addLogEntry("PLOT", player.getName() + " changed the perms of a " + 
						"plot in " + city.getName());
			}
			
			
		}
	}
}
