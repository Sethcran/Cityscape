package com.sethcran.cityscape.commands.playercommands;

import java.util.ArrayList;

import org.bukkit.command.CommandSender;

import com.sethcran.cityscape.Cityscape;
import com.sethcran.cityscape.Constants;
import com.sethcran.cityscape.commands.CSCommand;

public class History extends CSCommand {

	public History(Cityscape plugin) {
		super(plugin);
		name = "history";
		description = "Displays the history of the selected player.";
		usage = "/p history playername";
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(args == null) {
			sender.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"That command requires a playername.");
			return;
		}
		
		if(args.length != 1) {
			sender.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR + 
					"That command requires only a single player name.");
			return;
		}
		
		if(!plugin.getDB().doesPlayerExist(args[0])) {
			sender.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"That player does not exist.");
			return;
		}
		
		ArrayList<String> history = plugin.getDB().getHistory(args[0]);
		
		sender.sendMessage(Constants.CITYSCAPE + Constants.GROUP_COLOR +
				"Player History: " + Constants.MESSAGE_COLOR + args[0]);
		
		for(String s : history)
			sender.sendMessage(s);
	}

}
