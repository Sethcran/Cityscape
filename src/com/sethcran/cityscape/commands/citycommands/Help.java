package com.sethcran.cityscape.commands.citycommands;

import org.bukkit.command.CommandSender;

import com.sethcran.cityscape.Cityscape;
import com.sethcran.cityscape.commands.CSCommand;

public class Help extends CSCommand {

	public Help(Cityscape plugin) {
		super(plugin);
		name = "help";
		description = "Used to get information on available commands.";
		usage = "/c help";
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		

	}

}
