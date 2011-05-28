package com.sethcran.cityscape.commands;

import java.util.Arrays;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.sethcran.cityscape.Cityscape;
import com.sethcran.cityscape.Constants;

public class CSAdmin extends CSCommand {
	
	HashMap<String, CSCommand> csAdminMap = new HashMap<String, CSCommand>();
	
	public CSAdmin(Cityscape plugin) {
		super(plugin);
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		CSCommand cscommand = null;
		
		if(args == null)
			cscommand = csAdminMap.get("default");
		else if(args.length == 0)
			cscommand = csAdminMap.get("default");
		else
			cscommand = csAdminMap.get(args[0]);
		
		if(cscommand == null) {
			sender.sendMessage(Constants.CITYSCAPE + ChatColor.RED + 
					"That command does not exist.");
			return;
		}
		
		if(args.length > 1)
			args = Arrays.copyOfRange(args, 1, args.length);
		else
			args = null;
		
		cscommand.execute(sender, args);
	}
}
