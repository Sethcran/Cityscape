package com.sethcran.cityscape.commands.citycommands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sethcran.cityscape.City;
import com.sethcran.cityscape.Cityscape;
import com.sethcran.cityscape.Constants;
import com.sethcran.cityscape.RankPermissions;
import com.sethcran.cityscape.commands.CSCommand;

public class Settings extends CSCommand {

	public Settings(Cityscape plugin) {
		super(plugin);
		name = "settings";
		description = "Used for city settings.";
		usage = "/c settings [residentbuild/outsiderdestroy/.../snow]:[on/off]";
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
		
		City city = plugin.getCity(plugin.getCache(player.getName()).getCity());
		
		if(city == null) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"You are not in a city.");
			return;
		}
		
		RankPermissions rp = city.getRank(plugin.getCache(player.getName()).getRank());
		
		if(rp == null) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR + 
					"You do not have permission to do that.");
			return;
		}
		
		if(!rp.isSettings()) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR + 
					"You do not have permission to do that.");
			return;
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
					city.setResidentBuild(true);
				else if(each[1].equalsIgnoreCase("off"))
					city.setResidentBuild(false);
				else {
					formatError(player);
					return;
				}
			}
			else if(each[0].equalsIgnoreCase("residentdestroy")) {
				if(each[1].equalsIgnoreCase("on"))
					city.setResidentDestroy(true);
				else if(each[1].equalsIgnoreCase("off"))
					city.setResidentDestroy(false);
				else {
					formatError(player);
					return;
				}
			}
			else if(each[0].equalsIgnoreCase("residentswitch")) {
				if(each[1].equalsIgnoreCase("on"))
					city.setResidentSwitch(true);
				else if(each[1].equalsIgnoreCase("off"))
					city.setResidentSwitch(false);
				else {
					formatError(player);
					return;
				}
			}
			else if(each[0].equalsIgnoreCase("outsiderbuild")) {
				if(each[1].equalsIgnoreCase("on"))
					city.setOutsiderBuild(true);
				else if(each[1].equalsIgnoreCase("off"))
					city.setOutsiderBuild(false);
				else {
					formatError(player);
					return;
				}
			}
			else if(each[0].equalsIgnoreCase("outsiderdestroy")) {
				if(each[1].equalsIgnoreCase("on"))
					city.setOutsiderDestroy(true);
				else if(each[1].equalsIgnoreCase("off"))
					city.setOutsiderDestroy(false);
				else {
					formatError(player);
					return;
				}
			}
			else if(each[0].equalsIgnoreCase("outsiderswitch")) {
				if(each[1].equalsIgnoreCase("on"))
					city.setOutsiderSwitch(true);
				else if(each[1].equalsIgnoreCase("off"))
					city.setOutsiderSwitch(false);
				else {
					formatError(player);
					return;
				}
			}
			else if(each[0].equalsIgnoreCase("snow")) {
				if(each[1].equalsIgnoreCase("on"))
					city.setSnow(true);
				else if(each[1].equalsIgnoreCase("off"))
					city.setSnow(false);
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
				"You have changed the settings of " + city.getName() + ".");
		plugin.getDB().updateCitySettings(city);
	}
	
	public void formatError(Player player) {
		player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
				"There was an error with your format.");
		player.sendMessage(Constants.ERROR_COLOR + usage);
	}

}
