package com.sethcran.cityscape.commands.citycommands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.sethcran.cityscape.Cityscape;
import com.sethcran.cityscape.Constants;
import com.sethcran.cityscape.commands.CSCommand;
import com.sethcran.cityscape.commands.City;

public class Help extends CSCommand {
	
	City citycommands = null;
	
	String[] commands = { "accept", "ban", "buyclaims", "change", "claim", "createcity",
			"decline", "default", "delete", "demote", "deposit", "help", "here", "invite",
			"invites", "leave", "list", "map", "promote", "rank", "ranks", "remove",
			"rename", "residents", "settings", "setwarp", "unban", "unclaim", "warp",
			"welcome", "withdraw" };

	public Help(Cityscape plugin, City citycommands) {
		super(plugin);
		this.citycommands = citycommands;
		name = "help";
		description = "Used to get information on /c commands.";
		usage = "/c help";
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		 
		if(args == null) {
			sender.sendMessage(Constants.CITYSCAPE + Constants.SUCCESS_COLOR +
					"City Commands List:");
			String message = "" + ChatColor.WHITE;
			for(String c : commands) {
				if(message.length() + c.length() > Constants.CHAT_LINE_LENGTH) {
					sender.sendMessage(message);
					message = "" + ChatColor.WHITE;
				}
				else if(!message.equals("" + ChatColor.WHITE))
					message += ", ";
				message += c;
			}
			
			if(!message.equals("" + ChatColor.WHITE))
				sender.sendMessage(message);
			return;
		}
		
		if(args.length != 1) {
			sender.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"You must type a specific command that you want help on.");
			return;
		}
		
		CSCommand cmd = citycommands.getCommand(args[0]);
		
		if(cmd == null) {
			sender.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"That command does not exist.");
			return;
		}
		
		sender.sendMessage(Constants.CITYSCAPE + Constants.SUCCESS_COLOR +
				"City Command: " + args[0]);
		sender.sendMessage(Constants.GROUP_COLOR + "Description: " + ChatColor.WHITE + 
				cmd.getDescription());
		sender.sendMessage(Constants.GROUP_COLOR + "Usage: " + ChatColor.WHITE +
				cmd.getUsage());

	}

}
