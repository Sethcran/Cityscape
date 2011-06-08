package com.sethcran.cityscape.commands;

import java.util.Arrays;
import java.util.HashMap;

import org.bukkit.command.CommandSender;

import com.sethcran.cityscape.Cityscape;
import com.sethcran.cityscape.commands.chestcommands.*;

public class Chest extends CSCommand {

HashMap<String, CSCommand> chestMap = new HashMap<String, CSCommand>();
	
	public Chest(Cityscape plugin) {
		super(plugin);
		
		addToMap(new Place(plugin));
		addToMap(new Send(plugin));		
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		CSCommand cscommand = null;
		boolean wasNull = false;
		
		if(args.length > 0)
			cscommand = chestMap.get(args[0]);
		
		if(cscommand == null) {
			wasNull = true;
			cscommand = chestMap.get("default");
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
		chestMap.put(cmd.getName(), cmd);
		
		if(cmd.getAliases() != null) {
			for(String alias : cmd.getAliases()) {
				chestMap.put(alias, cmd);
			}
		}
	}

}
