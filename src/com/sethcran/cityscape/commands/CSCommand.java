package com.sethcran.cityscape.commands;

import org.bukkit.command.CommandSender;

public abstract class CSCommand {
	protected String name;
	protected String description;
	protected String usage;
	protected String[] aliases;
	
	public CSCommand() {
		
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
