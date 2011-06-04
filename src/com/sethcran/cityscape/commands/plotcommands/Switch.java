package com.sethcran.cityscape.commands.plotcommands;

import java.util.Set;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sethcran.cityscape.City;
import com.sethcran.cityscape.Cityscape;
import com.sethcran.cityscape.Constants;
import com.sethcran.cityscape.Permissions;
import com.sethcran.cityscape.Plot;
import com.sethcran.cityscape.commands.CSCommand;

public class Switch extends CSCommand {

	public Switch(Cityscape plugin) {
		super(plugin);
		name = "switch";
		description = "Displays the list of cities and players who can switch here.";
		usage = "/plot switch";
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
		
		Location location = player.getLocation();
		City city = plugin.getCityAt(location.getBlockX(), location.getBlockZ(),
				location.getWorld().getName());
		if(city == null) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"You must be inside a city to do that.");
			return;
		}
		
		Plot plot = city.getPlotAt(location.getBlockX(), location.getBlockZ());
		if(plot == null) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"You must be inside a plot to do that.");
			return;
		}
		
		player.sendMessage(Constants.CITYSCAPE + Constants.SUCCESS_COLOR +
				"Plot Permissions:");
		
		if(plot.isOutsiderSwitch()) {
			player.sendMessage(Constants.SUCCESS_COLOR + "Everyone can switch here.");
			return;
		}
		
		if(plot.isResidentSwitch()) {
			player.sendMessage(Constants.SUCCESS_COLOR + "Residents can switch here.");
		}
		
		Set<Entry<String, Permissions>> citySet = plot.getCitiesWithPermissions();
		Set<Entry<String, Permissions>> playerSet = plot.getPlayersWithPermissions();
		
		player.sendMessage(Constants.CITYSCAPE + Constants.GROUP_COLOR + 
				"Cities with permissions to switch here:");
		
		String message = "" + ChatColor.WHITE;
		for(Entry<String, Permissions> e : citySet) {
			if(e.getValue().isCanSwitch()) {
				if(message.length() + e.getKey().length() > Constants.CHAT_LINE_LENGTH) {
					sender.sendMessage(message);
					message = "" + ChatColor.WHITE;
				}
				else if(!message.equals("" + ChatColor.WHITE))
					message += ", ";
				message += e.getKey();
			}
		}
		
		if(!message.equals("" + ChatColor.WHITE))
			sender.sendMessage(message);
		
		player.sendMessage(Constants.CITYSCAPE + Constants.GROUP_COLOR + 
				"Players with permissions to switch here:");
		
		message = "" + ChatColor.WHITE;
		for(Entry<String, Permissions> e : playerSet) {
			if(e.getValue().isCanSwitch()) {
				if(message.length() + e.getKey().length() > Constants.CHAT_LINE_LENGTH) {
					sender.sendMessage(message);
					message = "" + ChatColor.WHITE;
				}
				else if(!message.equals("" + ChatColor.WHITE))
					message += ", ";
				message += e.getKey();
			}
		}
		
		if(!message.equals("" + ChatColor.WHITE))
			sender.sendMessage(message);
	}

}
