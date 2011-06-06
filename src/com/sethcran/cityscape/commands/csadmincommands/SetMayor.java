package com.sethcran.cityscape.commands.csadmincommands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sethcran.cityscape.City;
import com.sethcran.cityscape.Cityscape;
import com.sethcran.cityscape.Constants;
import com.sethcran.cityscape.PlayerCache;
import com.sethcran.cityscape.commands.CSCommand;

public class SetMayor extends CSCommand {

	public SetMayor(Cityscape plugin) {
		super(plugin);
		name = "setmayor";
		description = "Forces the mayor of the selected city.";
		usage = "/csa setmayor city player";
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		Player player = null;
		if(sender instanceof Player)
			player = (Player)sender;
		else {
			sender.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"You must be in game to do that.");
			return;
		}
		
		if(!plugin.permissionHandler.has(player, "cs.admin")) {
			return;
		}
		
		if(args == null) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"That command requires more arguments.");
			player.sendMessage(Constants.ERROR_COLOR + usage);
			return;
		}
		
		if(args.length != 2) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"That command requires 2 arguments.");
			player.sendMessage(Constants.ERROR_COLOR + usage);
			return;
		}
		
		City city = plugin.getCity(args[0]);
		
		if(city == null) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"The city " + args[0] + " does not exist.");
			return;
		}
		
		if(!plugin.getDB().doesPlayerExist(args[1])) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"The player " + args[1] + " does not exist.");
			return;
		}
		
		String oldMayor = city.getMayor();
		PlayerCache oldCache = plugin.getCache(oldMayor);
		if(oldCache != null)
			oldCache.setRank(null);
		PlayerCache newCache = plugin.getCache(args[1]);
		if(newCache != null) {
			newCache.setRank("Mayor");
			newCache.setCity(args[0]);
		}
		city.setMayor(args[1]);
		
		plugin.getDB().setRank(oldMayor, null);
		plugin.getDB().setRank(args[1], "Mayor");
		plugin.getDB().updateMayor(args[0], args[1]);
	}

}
