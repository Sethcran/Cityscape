package com.sethcran.cityscape.commands.plotcommands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sethcran.cityscape.Cityscape;
import com.sethcran.cityscape.Constants;
import com.sethcran.cityscape.RankPermissions;
import com.sethcran.cityscape.Selection;
import com.sethcran.cityscape.commands.CSCommand;

public class Select extends CSCommand {

	public Select(Cityscape plugin) {
		super(plugin);
		name = "select";
		description = "Used to select areas for creating plots.";
		usage = "/c select";
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		Player player = null;
		if(sender instanceof Player) {
			player = (Player)sender;
		}
		else {
			sender.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"You must be in game to use this command.");
			return;
		}
		
		RankPermissions rp = plugin.getDB().getPermissions(player.getName());
		if(rp == null) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"You do not have permission to do that.");
			return;
		}
		
		if(!rp.isCreatePlots()) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"You do not have permission to do that.");
			return;
		}
		
		plugin.insertSelection(player.getName(), new Selection());
		player.sendMessage(Constants.CITYSCAPE + Constants.SUCCESS_COLOR +
				"You are now selecting plot areas.");
		player.sendMessage(Constants.SUCCESS_COLOR + "Type /plot unselect to stop");		
	}
}
