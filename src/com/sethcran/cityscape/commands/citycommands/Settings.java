package com.sethcran.cityscape.commands.citycommands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sethcran.cityscape.City;
import com.sethcran.cityscape.Cityscape;
import com.sethcran.cityscape.Constants;
import com.sethcran.cityscape.RankPermissions;
import com.sethcran.cityscape.commands.CSCommand;
import com.sethcran.cityscape.error.ErrorManager;
import com.sethcran.cityscape.error.ErrorManager.CSError;

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
			ErrorManager.sendError(sender, CSError.IN_GAME_ONLY, null);
			return;
		}
		
		if(args == null) {
			ErrorManager.sendError(sender, CSError.NOT_ENOUGH_ARGUMENTS, null);
			player.sendMessage(Constants.ERROR_COLOR + usage);
			return;
		}
		
		City city = plugin.getCity(plugin.getCache(player.getName()).getCity());
		
		if(city == null) {
			ErrorManager.sendError(sender, CSError.NOT_IN_CITY , null);
			return;
		}
		
		RankPermissions rp = city.getRank(plugin.getCache(player.getName()).getRank());
		
		if(rp == null) {
			ErrorManager.sendError(sender, CSError.NO_RANK_PERMISSION, null);
			return;
		}
		
		if(!rp.isSettings()) {
			ErrorManager.sendError(sender, CSError.NO_RANK_PERMISSION, null);
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
		plugin.addLogEntry("CITY", player.getName() + " updated the settings of " + 
				city.getName());
	}
	
	public void formatError(Player player) {
		ErrorManager.sendError(player, CSError.INCORRECT_FORMAT, null);
		player.sendMessage(Constants.ERROR_COLOR + usage);
	}

}
