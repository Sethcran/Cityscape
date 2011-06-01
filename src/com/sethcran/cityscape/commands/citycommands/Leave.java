package com.sethcran.cityscape.commands.citycommands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sethcran.cityscape.Cityscape;
import com.sethcran.cityscape.Constants;
import com.sethcran.cityscape.commands.CSCommand;

public class Leave extends CSCommand {

	public Leave(Cityscape plugin) {
		super(plugin);
		name = "leave";
		description = "Leaves your current city if you are in one.";
		usage = "/c leave";
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		Player player = null;
		if(sender instanceof Player)
			player = (Player)sender;
		else {
			sender.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR + 
					"Only players in the game can execute that command.");
			return;
		}
		
		if(args != null) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"That command does not take arguments.");
			return;
		}
		
		String playerCity = plugin.getCache(player.getName()).getCity();
		
		if(playerCity == null) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"You are not in a city.");
			return;
		}
		
		if(plugin.getCity(playerCity).getMayor().equals(player.getName())) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"You must first set someone else as mayor.");
			return;
		}
		
		plugin.getDB().leaveCity(player.getName());
		plugin.getCache(player.getName()).setCity(null);
		player.sendMessage(Constants.CITYSCAPE + Constants.SUCCESS_COLOR + 
				"You have left your town.");
	}

}
