package com.sethcran.cityscape.commands;

import java.util.Arrays;
import java.util.HashMap;

import org.bukkit.command.CommandSender;

import com.sethcran.cityscape.Cityscape;
import com.sethcran.cityscape.commands.plotcommands.*;

public class Plot extends CSCommand {
	HashMap<String, CSCommand> plotMap = new HashMap<String, CSCommand>();
	
	public Plot(Cityscape plugin) {
		super(plugin);
		addToMap(new Build(plugin));
		addToMap(new Buy(plugin));
		addToMap(new Create(plugin));
		addToMap(new Default(plugin));
		addToMap(new Delete(plugin));
		addToMap(new Destroy(plugin));
		addToMap(new Help(plugin, this));
		addToMap(new Perms(plugin));
		addToMap(new Reclaim(plugin));
		addToMap(new Select(plugin));
		addToMap(new Sell(plugin));
		addToMap(new Settings(plugin));
		addToMap(new Switch(plugin));
		addToMap(new Unselect(plugin));
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		CSCommand cscommand = null;
		boolean wasNull = false;
		
		if(args.length > 0)
			cscommand = plotMap.get(args[0]);
		
		if(cscommand == null) {
			wasNull = true;
			cscommand = plotMap.get("default");
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
		plotMap.put(cmd.getName(), cmd);
		
		if(cmd.getAliases() != null) {
			for(String alias : cmd.getAliases()) {
				plotMap.put(alias, cmd);
			}
		}
	}
	
	public CSCommand getCommand(String cmd) {
		return plotMap.get(cmd);
	}

}
