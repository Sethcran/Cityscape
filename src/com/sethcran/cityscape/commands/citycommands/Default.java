package com.sethcran.cityscape.commands.citycommands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.sethcran.cityscape.Cityscape;
import com.sethcran.cityscape.commands.CSCommand;

public class Default extends CSCommand {

	public Default(Cityscape plugin) {
		super(plugin);
		name = "default";
		description = "Information on your current city status.";
		usage = "/city";
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		sender.sendMessage(ChatColor.GREEN + "This is where I will give information " +
				"on your current city.");
	}

}
