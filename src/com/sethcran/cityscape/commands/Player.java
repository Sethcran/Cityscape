package com.sethcran.cityscape.commands;

import java.util.Arrays;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.sethcran.cityscape.Cityscape;
import com.sethcran.cityscape.Constants;
import com.sethcran.cityscape.commands.playercommands.Default;

public class Player extends CSCommand {

	HashMap<String, CSCommand> playerMap = new HashMap<String, CSCommand>();
	
	public Player(Cityscape plugin) {
		super(plugin);
		
		addToMap(new Default(plugin));
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		CSCommand cscommand = null;
		
		if(args == null)
			cscommand = playerMap.get("default");
		else if(args.length == 0)
			cscommand = playerMap.get("default");
		else
			cscommand = playerMap.get(args[0]);
		
		if(cscommand == null) {
			sender.sendMessage(Constants.CITYSCAPE + ChatColor.RED + 
					"That command does not exist.");
			return;
		}
		
		if(args.length > 1)
			args = Arrays.copyOfRange(args, 1, args.length);
		else
			args = null;
		
		cscommand.execute(sender, args);
	}
	
	private void addToMap(CSCommand cmd) {
		playerMap.put(cmd.getName(), cmd);
		
		if(cmd.getAliases() != null) {
			for(String alias : cmd.getAliases()) {
				playerMap.put(alias, cmd);
			}
		}
	}

}
