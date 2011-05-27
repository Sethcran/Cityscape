package com.sethcran.cityscape.commands;

import java.util.HashMap;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandHandler {

	private HashMap<String, HashMap<String, CSCommand>> commandMap = 
		new HashMap<String, HashMap<String, CSCommand>>();
	
	public CommandHandler() {
		addCityCommands();
		addPlayerCommands();
		addCSAdminCommands();
	}

	public boolean handleCommand(CommandSender sender, Command cmd, 
			String label, String[] args) {
		String command = cmd.getName().toLowerCase();
		Player player = null;
		if(sender instanceof Player)
			player = (Player)sender;
		else
			return false;
		
		HashMap<String, CSCommand> map = commandMap.get(command);
		if(map == null) {
			player.sendMessage("That command does not exist.");
			return false;
		}
		
		if(args.length > 0) {
			CSCommand cscommand = map.get(args[0]);
			if(cscommand == null) {
				player.sendMessage("That command does not exist.");
				return false;
			}
			cscommand.execute(sender, args);
		}
		
		return false;
	}
	
	public void addCityCommands() {
		HashMap<String, CSCommand> map = new HashMap<String, CSCommand>();
		
		commandMap.put("city", map);
		commandMap.put("c", map);
	}
	
	public void addPlayerCommands() {
		HashMap<String, CSCommand> map = new HashMap<String, CSCommand>();
		commandMap.put("player", map);
		commandMap.put("p", map);
	}
	
	public void addCSAdminCommands() {
		HashMap<String, CSCommand> map = new HashMap<String, CSCommand>();
		commandMap.put("csadmin", map);
		commandMap.put("csa", map);
	}
}