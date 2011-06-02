package com.sethcran.cityscape.commands.citycommands;

import org.bukkit.command.CommandSender;

import com.sethcran.cityscape.Cityscape;
import com.sethcran.cityscape.commands.CSCommand;

public class Decline extends CSCommand {

	public Decline(Cityscape plugin) {
		super(plugin);
		name = "decline";
		description = "Declines an invitation to join a city.";
		usage = "/c decline cityname";
	}

	@Override
	public void execute(CommandSender sender, String[] args) {

	}

}
