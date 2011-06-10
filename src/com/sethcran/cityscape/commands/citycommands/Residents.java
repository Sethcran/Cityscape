package com.sethcran.cityscape.commands.citycommands;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sethcran.cityscape.City;
import com.sethcran.cityscape.Cityscape;
import com.sethcran.cityscape.Constants;
import com.sethcran.cityscape.commands.CSCommand;
import com.sethcran.cityscape.error.ErrorManager;
import com.sethcran.cityscape.error.ErrorManager.CSError;

public class Residents extends CSCommand {

	public Residents(Cityscape plugin) {
		super(plugin);
		name = "residents";
		description = "Lists all of the residents within your city.";
		usage = "/c residents";
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
		
		String city = null;
		
		if(args != null) {
			if(args.length > 1) {
				ErrorManager.sendError(sender, CSError.TOO_MANY_ARGUMENTS, null);
				sender.sendMessage(Constants.ERROR_COLOR + usage);
				return;
			}
			City c = plugin.getCity(args[0]);
			if(c == null) {
				ErrorManager.sendError(sender, CSError.CITY_DOES_NOT_EXIST, args[0]);
				return;
			}
			city = c.getName();
		}
		else
			city = plugin.getCache(player.getName()).getCity();
		
		if(city == null) {
			ErrorManager.sendError(sender, CSError.NOT_IN_CITY, null);
			return;
		}
		
		ArrayList<String> residents = plugin.getDB().getResidents(city);
		
		sender.sendMessage(Constants.CITYSCAPE + Constants.SUCCESS_COLOR +
				"Residents of " + city + ":");
		
		String message = "" + ChatColor.WHITE;
		for(String resident : residents) {
			if(message.length() + resident.length() > Constants.CHAT_LINE_LENGTH) {
				sender.sendMessage(message);
				message = "" + ChatColor.WHITE;
			}
			else if(!message.equals("" + ChatColor.WHITE))
				message += ", ";
			message += resident;
		}
		
		if(!message.equals("" + ChatColor.WHITE))
			sender.sendMessage(message);
	}
}
