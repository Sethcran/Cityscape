package com.sethcran.cityscape.commands.plotcommands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sethcran.cityscape.Cityscape;
import com.sethcran.cityscape.Constants;
import com.sethcran.cityscape.RankPermissions;
import com.sethcran.cityscape.Selection;
import com.sethcran.cityscape.commands.CSCommand;
import com.sethcran.cityscape.error.ErrorManager;
import com.sethcran.cityscape.error.ErrorManager.CSError;

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
			ErrorManager.sendError(sender, CSError.IN_GAME_ONLY, null);
			return;
		}
		
		RankPermissions rp = plugin.getPermissions(player.getName());
		if(rp == null) {
			ErrorManager.sendError(sender, CSError.NO_RANK_PERMISSION, null);
			return;
		}
		
		if(!rp.isCreatePlots()) {
			ErrorManager.sendError(sender, CSError.NO_RANK_PERMISSION, null);
			return;
		}
		
		plugin.insertSelection(player.getName(), new Selection());
		player.sendMessage(Constants.CITYSCAPE + Constants.SUCCESS_COLOR +
				"You are now selecting plot areas.");
		player.sendMessage(Constants.SUCCESS_COLOR + "Type /plot unselect to stop");		
	}
}
