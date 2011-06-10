package com.sethcran.cityscape.commands.csadmincommands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sethcran.cityscape.Cityscape;
import com.sethcran.cityscape.Constants;
import com.sethcran.cityscape.commands.CSAdmin;
import com.sethcran.cityscape.commands.CSCommand;

public class Help extends CSCommand {
	
	private CSAdmin csacommands = null;
	
	String[] commands = { "ban", "claim", "default", "deletecity", "help", "setmayor",
			"unban", "unclaim" };

	public Help(Cityscape plugin, CSAdmin csacommands) {
		super(plugin);
		this.csacommands = csacommands;
		name = "help";
		description = "Displays help information for a specific admin command.";
		usage = "/csa help";
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		
		if(sender instanceof Player) {
			Player player = (Player)sender;
			if(!plugin.permissionHandler.has(player, "cs.admin")) {
				return;
			}
		}
		
		if(args == null) {
			sender.sendMessage(Constants.CITYSCAPE + Constants.SUCCESS_COLOR +
					"CSAdmin Commands List:");
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
		
		CSCommand cmd = csacommands.getCommand(args[0]);
		
		if(cmd == null) {
			sender.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"That command does not exist.");
			return;
		}
		
		sender.sendMessage(Constants.CITYSCAPE + Constants.SUCCESS_COLOR +
				"CSAdmin Command: " + args[0]);
		sender.sendMessage(Constants.GROUP_COLOR + "Description: " + ChatColor.WHITE + 
				cmd.getDescription());
		sender.sendMessage(Constants.GROUP_COLOR + "Usage: " + ChatColor.WHITE +
				cmd.getUsage());

	}
}
