package com.sethcran.cityscape.commands.csadmincommands;

import org.bukkit.command.CommandSender;

import com.sethcran.cityscape.Cityscape;
import com.sethcran.cityscape.commands.CSCommand;

public class Default extends CSCommand {

	public Default(Cityscape plugin) {
		super(plugin);
		name = "default";
		description = "Default administration help.";
		usage = "/csa";
	}

	@Override
	public void execute(CommandSender sender, String[] args) {

	}

}
