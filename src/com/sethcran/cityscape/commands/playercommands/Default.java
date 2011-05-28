package com.sethcran.cityscape.commands.playercommands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sethcran.cityscape.Cityscape;
import com.sethcran.cityscape.Constants;
import com.sethcran.cityscape.commands.CSCommand;

public class Default extends CSCommand {

	public Default(Cityscape plugin) {
		super(plugin);
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		Player player = null;
		if(sender instanceof Player)
			player = (Player)sender;
		else {
			sender.sendMessage(Constants.CITYSCAPE + ChatColor.RED +
					"Only a player in game can execute this command.");
			return;
		}
		
		player.sendMessage(ChatColor.GREEN + "This will be player data.");
	}

}
