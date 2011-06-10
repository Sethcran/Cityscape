package com.sethcran.cityscape.commands.playercommands;

import java.util.ArrayList;

import org.bukkit.command.CommandSender;

import com.sethcran.cityscape.Cityscape;
import com.sethcran.cityscape.Constants;
import com.sethcran.cityscape.commands.CSCommand;
import com.sethcran.cityscape.error.ErrorManager;
import com.sethcran.cityscape.error.ErrorManager.CSError;

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
			ErrorManager.sendError(sender, CSError.NOT_ENOUGH_ARGUMENTS, null);
			sender.sendMessage(Constants.ERROR_COLOR + usage);
			return;
		}
		
		if(args.length != 1) {
			ErrorManager.sendError(sender, CSError.TOO_MANY_ARGUMENTS, null);
			sender.sendMessage(Constants.ERROR_COLOR + usage);
			return;
		}
		
		if(!plugin.getDB().doesPlayerExist(args[0])) {
			ErrorManager.sendError(sender, CSError.PLAYER_DOES_NOT_EXIST, args[0]);
			return;
		}
		
		ArrayList<String> history = plugin.getDB().getHistory(args[0]);
		
		sender.sendMessage(Constants.CITYSCAPE + Constants.GROUP_COLOR +
				"Player History: " + Constants.MESSAGE_COLOR + args[0]);
		
		for(String s : history)
			sender.sendMessage(s);
	}

}
