package com.sethcran.cityscape.commands.citycommands;

import java.util.ArrayList;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sethcran.cityscape.Cityscape;
import com.sethcran.cityscape.Constants;
import com.sethcran.cityscape.commands.CSCommand;
import com.sethcran.cityscape.error.ErrorManager;
import com.sethcran.cityscape.error.ErrorManager.CSError;

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
			ErrorManager.sendError(sender, CSError.IN_GAME_ONLY, null);
			return;
		}
		
		if(args != null) {
			ErrorManager.sendError(sender, CSError.TOO_MANY_ARGUMENTS, null);
			player.sendMessage(Constants.ERROR_COLOR + usage);
			return;
		}
		
		ArrayList<String> cities = plugin.getDB().getInvites(player.getName());
		
		if(cities.size() == 0) {
			ErrorManager.sendError(sender, CSError.NO_INVITES, null);
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
