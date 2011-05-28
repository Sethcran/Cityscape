package com.sethcran.cityscape.commands;

import java.util.HashMap;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.sethcran.cityscape.Cityscape;
import com.sethcran.cityscape.commands.citycommands.*;

public class CommandHandler {

	private Cityscape plugin;
	private HashMap<String, HashMap<String, CSCommand>> commandMap = 
		new HashMap<String, HashMap<String, CSCommand>>();
	
	public CommandHandler(Cityscape plugin) {
		this.plugin = plugin;
		addCityCommands();
		addPlayerCommands();
		addCSAdminCommands();
	}

	public boolean handleCommand(CommandSender sender, Command cmd, 
			String label, String[] args) {
		String command = cmd.getName().toLowerCase();
		
		HashMap<String, CSCommand> map = commandMap.get(command);
		if(map == null) {
			sender.sendMessage("That command does not exist.");
			return false;
		}
		
		if(args.length > 0) {
			CSCommand cscommand = map.get(args[0]);
			if(cscommand == null) {
				sender.sendMessage("That command does not exist.");
				return false;
			}
			cscommand.execute(sender, args);
			return true;
		}
		else {
			CSCommand cscommand = map.get("default");
			cscommand.execute(sender, args);
			return true;
		}
	}
	
	public void addCityCommands() {
		HashMap<String, CSCommand> map = new HashMap<String, CSCommand>();
		
		addToMap(new CreateCity(plugin), map);
		
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
	
	private void addToMap(CSCommand cmd, HashMap<String, CSCommand> map) {
		map.put(cmd.getName(), cmd);
		
		if(cmd.getAliases() != null) {
			for(String alias : cmd.getAliases()) {
				map.put(alias, cmd);
			}
		}
	}
}
