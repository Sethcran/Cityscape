package com.sethcran.cityscape.commands.citycommands;

import java.util.ArrayList;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sethcran.cityscape.Cityscape;
import com.sethcran.cityscape.Constants;
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
		Player player = null;
		if(sender instanceof Player)
			player = (Player)sender;
		else {
			sender.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"Only players in game can use this command.");
			return;
		}
		
		if(args != null) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"That command does not take arguments.");
			return;
		}
		
		ArrayList<String> cities = plugin.getDB().getInvites(player.getName());
		
		if(cities.size() == 0) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"You have not been invited to join any cities.");
			return;
		}
		
		player.sendMessage(Constants.CITYSCAPE + Constants.SUCCESS_COLOR +
				"You have been invited to join the cities of: ");
		for(String city : cities)
			player.sendMessage(Constants.SUCCESS_COLOR + city);
		player.sendMessage(Constants.CITYSCAPE + Constants.SUCCESS_COLOR +
				"Type /c accept cityname to accept an invitation.");
		player.sendMessage(Constants.CITYSCAPE + Constants.SUCCESS_COLOR +
				"Type /c decline cityname to decline an invitiation.");
	}

}
