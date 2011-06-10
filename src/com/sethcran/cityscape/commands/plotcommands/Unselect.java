package com.sethcran.cityscape.commands.plotcommands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sethcran.cityscape.Cityscape;
import com.sethcran.cityscape.Constants;
import com.sethcran.cityscape.commands.CSCommand;
import com.sethcran.cityscape.error.ErrorManager;
import com.sethcran.cityscape.error.ErrorManager.CSError;

public class Unselect extends CSCommand {

	public Unselect(Cityscape plugin) {
		super(plugin);
		name = "unselect";
		description = "Used to stop making plot selections.";
		usage = "/plot unselect";
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
		
		plugin.removeSelection(player.getName());
		player.sendMessage(Constants.CITYSCAPE + Constants.SUCCESS_COLOR +
				"You are not selecting anything.");
	}
}
