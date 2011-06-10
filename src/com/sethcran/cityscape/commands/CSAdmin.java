package com.sethcran.cityscape.commands;

import java.util.Arrays;
import java.util.HashMap;

import org.bukkit.command.CommandSender;

import com.sethcran.cityscape.Cityscape;
import com.sethcran.cityscape.commands.csadmincommands.*;

public class CSAdmin extends CSCommand {
	
	HashMap<String, CSCommand> csAdminMap = new HashMap<String, CSCommand>();
	
	public CSAdmin(Cityscape plugin) {
		super(plugin);
		
		addToMap(new Ban(plugin));
		addToMap(new Claim(plugin));
		addToMap(new Default(plugin));
		addToMap(new DeleteCity(plugin));
		addToMap(new Help(plugin, this));
		addToMap(new SetMayor(plugin));
		addToMap(new Unban(plugin));
		addToMap(new Unclaim(plugin));
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		CSCommand cscommand = null;
		boolean wasNull = false;
		
		if(args.length > 0)
			cscommand = csAdminMap.get(args[0].toLowerCase());
		
		if(cscommand == null) {
			wasNull = true;
			cscommand = csAdminMap.get("default");
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
		csAdminMap.put(cmd.getName(), cmd);
		
		if(cmd.getAliases() != null) {
			for(String alias : cmd.getAliases()) {
				csAdminMap.put(alias, cmd);
			}
		}
	}
	
	public CSCommand getCommand(String cmd) {
		return csAdminMap.get(cmd);
	}
}
