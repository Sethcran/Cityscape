package com.sethcran.cityscape.commands.citycommands;

import org.bukkit.command.CommandSender;

import com.sethcran.cityscape.Cityscape;
import com.sethcran.cityscape.commands.CSCommand;

public class Invites extends CSCommand {

	public Invites(Cityscape plugin) {
		super(plugin);
		name = "invites";
		description = "Displays a list of towns that have invited you to join.";
		usage = "/c invites";
	}

	@Override
	public void execute(CommandSender sender, String[] args) {

	}

}
