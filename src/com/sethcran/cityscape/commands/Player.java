package com.sethcran.cityscape.commands;

import java.util.Arrays;
import java.util.HashMap;

import org.bukkit.command.CommandSender;

import com.sethcran.cityscape.Cityscape;
import com.sethcran.cityscape.commands.playercommands.*;

public class Player extends CSCommand {

	HashMap<String, CSCommand> playerMap = new HashMap<String, CSCommand>();
	
	public Player(Cityscape plugin) {
		super(plugin);
		
		addToMap(new Default(plugin));
		addToMap(new History(plugin));
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		CSCommand cscommand = null;
		boolean wasNull = false;
		
		if(args.length > 0)
			cscommand = playerMap.get(args[0]);
		
		if(cscommand == null) {
			wasNull = true;
			cscommand = playerMap.get("default");
		}
		
		if(args.length > 1) {
			if(wasNull)
				args = null;
			else
				args = Arrays.copyOfRange(args, 1, args.length);
		}
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
