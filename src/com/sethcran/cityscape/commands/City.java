package com.sethcran.cityscape.commands;

import java.util.Arrays;
import java.util.HashMap;

import org.bukkit.command.CommandSender;

import com.sethcran.cityscape.Cityscape;
import com.sethcran.cityscape.Constants;
import com.sethcran.cityscape.commands.citycommands.*;

public class City extends CSCommand {
	HashMap<String, CSCommand> cityMap = new HashMap<String, CSCommand>();
	
	public City(Cityscape plugin) {
		super(plugin);
		
		addToMap(new Accept(plugin));
		addToMap(new Claim(plugin));
		addToMap(new CreateCity(plugin));
		addToMap(new Decline(plugin));
		addToMap(new Default(plugin));
		addToMap(new Invite(plugin));
		addToMap(new Invites(plugin));
		addToMap(new Leave(plugin));
		addToMap(new Remove(plugin));
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		CSCommand cscommand = null;
		
		if(args == null)
			cscommand = cityMap.get("default");
		else if(args.length == 0)
			cscommand = cityMap.get("default");
		else
			cscommand = cityMap.get(args[0]);
		
		if(cscommand == null) {
			sender.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR + 
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
		cityMap.put(cmd.getName(), cmd);
		
		if(cmd.getAliases() != null) {
			for(String alias : cmd.getAliases()) {
				cityMap.put(alias, cmd);
			}
		}
	}
}