package com.sethcran.cityscape.commands.citycommands;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.sethcran.cityscape.Cityscape;
import com.sethcran.cityscape.Constants;
import com.sethcran.cityscape.commands.CSCommand;

public class List extends CSCommand {

	public List(Cityscape plugin) {
		super(plugin);
		name = "list";
		description = "Lists all of the cities on the server.";
		usage = "/c list";
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		ArrayList<String> cityList = plugin.getDB().getCityNames();
		
		sender.sendMessage(Constants.CITYSCAPE + Constants.SUCCESS_COLOR +
				"City List: ");
		String message = "" + ChatColor.WHITE;
		for(String city : cityList) {
			if(message.length() > Constants.CHAT_LINE_LENGTH) {
				sender.sendMessage(message);
				message = "" + ChatColor.WHITE;
			}
			else if(!message.equals("" + ChatColor.WHITE))
				message = message.concat(", ");
			message += city;
		}
		
		if(!message.equals("" + ChatColor.WHITE))
			sender.sendMessage(message);
	}

}
