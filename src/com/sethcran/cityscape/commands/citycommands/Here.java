package com.sethcran.cityscape.commands.citycommands;

import org.bukkit.Chunk;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sethcran.cityscape.City;
import com.sethcran.cityscape.Cityscape;
import com.sethcran.cityscape.Constants;
import com.sethcran.cityscape.commands.CSCommand;

public class Here extends CSCommand {

	public Here(Cityscape plugin) {
		super(plugin);
		name = "here";
		description = "Gets the city description of city at current location.";
		usage = "/c here";
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		Player player = null;
		if(sender instanceof Player)
			player = (Player)sender;
		else {
			sender.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR + 
					"You can't do that command because you are not in game.");
			return;
		}
		
		Chunk chunk = player.getLocation().getBlock().getChunk();
		City city = plugin.getCityAt(chunk.getX(), chunk.getZ(), chunk.getWorld().getName());
		
		if(city == null) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR + 
					"You are in the wilderness.");
			return;
		}
		
		Default def = new Default(plugin);
		def.displayCityToSender(sender, city);
	}

}
