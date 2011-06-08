package com.sethcran.cityscape.commands.chestcommands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sethcran.cityscape.Cityscape;
import com.sethcran.cityscape.Constants;
import com.sethcran.cityscape.commands.CSCommand;

public class Send extends CSCommand {

	public Send(Cityscape plugin) {
		super(plugin);
		name = "send";
		description = "Sends a chest to the lost and found.";
		usage = "/chest send";
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		Player player = null;
		if(sender instanceof Player)
			player = (Player)sender;
		else {
			sender.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"Only players in game can do that.");
			return;
		}
		
		plugin.addSendingChests(player.getName());
		
		player.sendMessage(Constants.CITYSCAPE + Constants.SUCCESS_COLOR +
				"You are now sending chests.");
	}

}
