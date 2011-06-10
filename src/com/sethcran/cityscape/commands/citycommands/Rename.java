package com.sethcran.cityscape.commands.citycommands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sethcran.cityscape.City;
import com.sethcran.cityscape.Cityscape;
import com.sethcran.cityscape.Constants;
import com.sethcran.cityscape.PlayerCache;
import com.sethcran.cityscape.RankPermissions;
import com.sethcran.cityscape.commands.CSCommand;
import com.sethcran.cityscape.error.ErrorManager;
import com.sethcran.cityscape.error.ErrorManager.CSError;

public class Rename extends CSCommand {

	public Rename(Cityscape plugin) {
		super(plugin);
		name = "rename";
		description = "Used to rename a town or a rank.";
		usage = "/c rename [newname]";
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
		
		if(args == null) {
			ErrorManager.sendError(sender, CSError.NOT_ENOUGH_ARGUMENTS, null);
			player.sendMessage(Constants.ERROR_COLOR + usage);
			return;
		}
		
		if(args.length != 1) {
			ErrorManager.sendError(sender, CSError.TOO_MANY_ARGUMENTS, null);
			player.sendMessage(Constants.ERROR_COLOR + usage);
			return;
		}
		
		PlayerCache cache = plugin.getCache(player.getName());
		String cityName = cache.getCity();
		if(cityName == null) {
			ErrorManager.sendError(sender, CSError.NOT_IN_CITY, null);
			return;
		}
		
		City city = plugin.getCity(cityName);
		RankPermissions rp = city.getRank(cache.getRank());
		if(!rp.isSetName()) {
			ErrorManager.sendError(sender, CSError.NO_RANK_PERMISSION, null);
			return;
		}
		
		if(!args[0].matches("[a-zA-Z]+")) {
			ErrorManager.sendError(sender, CSError.INCORRECT_NAME_FORMAT, null);
			return;
		}
		
		plugin.getDB().renameCity(city.getName(), args[0]);
		plugin.renameCity(city.getName(), args[0]);
		
	}

}
