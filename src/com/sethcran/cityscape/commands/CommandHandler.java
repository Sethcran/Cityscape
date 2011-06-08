package com.sethcran.cityscape.commands;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.sethcran.cityscape.Cityscape;
import com.sethcran.cityscape.Constants;

public class CommandHandler {

	private Cityscape plugin;
	private HashMap<String, CSCommand> commandMap = new HashMap<String, CSCommand>();
	
	public CommandHandler(Cityscape plugin) {
		this.plugin = plugin;
		addChestCommands();
		addCityCommands();
		addPlayerCommands();
		addPlotCommands();
		addCSAdminCommands();
	}
	
	public void addChestCommands() {
		Chest chest = new Chest(plugin);
		commandMap.put("chest", chest);
	}

	public void addCityCommands() {
		City city = new City(plugin);
		commandMap.put("city", city);
		commandMap.put("c", city);
	}
	
	public void addCSAdminCommands() {
		CSAdmin csadmin = new CSAdmin(plugin);
		commandMap.put("csadmin", csadmin);
		commandMap.put("csa", csadmin);
	}
	
	public void addPlayerCommands() {
		Player player = new Player(plugin);
		commandMap.put("player", player);
		commandMap.put("p", player);
	}
	
	public void addPlotCommands() {
		Plot plot = new Plot(plugin);
		commandMap.put("plot", plot);
	}
	
	public boolean handleCommand(CommandSender sender, Command cmd, 
			String label, String[] args) {
		String command = cmd.getName().toLowerCase();
		
		CSCommand cscommand = null;
		cscommand = commandMap.get(command);
		
		if(cscommand == null) {
			sender.sendMessage(Constants.CITYSCAPE + ChatColor.RED + 
					"That command does not exist.");
			return true;
		}
		
		cscommand.execute(sender, args);
		
		return true;
	}
}
