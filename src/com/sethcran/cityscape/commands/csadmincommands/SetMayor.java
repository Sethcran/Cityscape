package com.sethcran.cityscape.commands.csadmincommands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sethcran.cityscape.City;
import com.sethcran.cityscape.Cityscape;
import com.sethcran.cityscape.Constants;
import com.sethcran.cityscape.PlayerCache;
import com.sethcran.cityscape.commands.CSCommand;
import com.sethcran.cityscape.error.ErrorManager;
import com.sethcran.cityscape.error.ErrorManager.CSError;

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
			ErrorManager.sendError(sender, CSError.IN_GAME_ONLY, null);
			return;
		}
		
		if(!plugin.permissionHandler.has(player, "cs.admin")) {
			return;
		}
		
		if(args == null) {
			ErrorManager.sendError(sender, CSError.NOT_ENOUGH_ARGUMENTS, null);
			player.sendMessage(Constants.ERROR_COLOR + usage);
			return;
		}
		
		if(args.length != 2) {
			ErrorManager.sendError(sender, CSError.TOO_MANY_ARGUMENTS, null);
			player.sendMessage(Constants.ERROR_COLOR + usage);
			return;
		}
		
		City city = plugin.getCity(args[0]);
		
		if(city == null) {
			ErrorManager.sendError(sender, CSError.CITY_DOES_NOT_EXIST, args[0]);
			return;
		}
		
		if(!plugin.getDB().doesPlayerExist(args[1])) {
			ErrorManager.sendError(sender, CSError.PLAYER_DOES_NOT_EXIST, args[1]);
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
		plugin.addLogEntry("ADMIN", player.getName() + " set the mayor of " + args[0] + 
				" to " + args[1]);
	}

}
