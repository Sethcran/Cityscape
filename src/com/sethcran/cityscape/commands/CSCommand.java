package com.sethcran.cityscape.commands;

import org.bukkit.command.CommandSender;

import com.sethcran.cityscape.Cityscape;

public abstract class CSCommand {
	protected String name;
	protected String description;
	protected String usage;
	protected String[] aliases;
	
	protected Cityscape plugin;
	
	public CSCommand(Cityscape plugin) {
		this.plugin = plugin;
	}
	
	public abstract void execute(CommandSender sender, String[] args);
	
	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getUsage() {
		return usage;
	}
	
	public String[] getAliases() {
		return aliases;
	}
}
