package com.sethcran.cityscape.commands;

import java.util.Arrays;
import java.util.HashMap;

import org.bukkit.command.CommandSender;

import com.sethcran.cityscape.Cityscape;
import com.sethcran.cityscape.commands.citycommands.*;

public class City extends CSCommand {
	HashMap<String, CSCommand> cityMap = new HashMap<String, CSCommand>();
	
	public City(Cityscape plugin) {
		super(plugin);
		
		addToMap(new Accept(plugin));
		addToMap(new Change(plugin));
		addToMap(new Claim(plugin));
		addToMap(new CreateCity(plugin));
		addToMap(new Decline(plugin));
		addToMap(new Default(plugin));
		addToMap(new Here(plugin));
		addToMap(new Invite(plugin));
		addToMap(new Invites(plugin));
		addToMap(new Leave(plugin));
		addToMap(new List(plugin));
		addToMap(new Rank(plugin));
		addToMap(new Ranks(plugin));
		addToMap(new Remove(plugin));
		addToMap(new Residents(plugin));
		addToMap(new Unclaim(plugin));
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		CSCommand cscommand = null;
		
		if(args.length > 0)
			cscommand = cityMap.get(args[0]);
		
		if(cscommand == null) {
			cscommand = cityMap.get("default");
			cscommand.execute(sender, args);
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