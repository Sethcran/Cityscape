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
		addToMap(new Ban(plugin));
		addToMap(new BuyClaims(plugin));
		addToMap(new Change(plugin));
		addToMap(new Claim(plugin));
		addToMap(new CreateCity(plugin));
		addToMap(new Decline(plugin));
		addToMap(new Default(plugin));
		addToMap(new Delete(plugin));
		addToMap(new Demote(plugin));
		addToMap(new Deposit(plugin));
		addToMap(new Help(plugin, this));
		addToMap(new Here(plugin));
		addToMap(new Invite(plugin));
		addToMap(new Invites(plugin));
		addToMap(new Leave(plugin));
		addToMap(new List(plugin));
		addToMap(new Map(plugin));
		addToMap(new Promote(plugin));
		addToMap(new Rank(plugin));
		addToMap(new Ranks(plugin));
		addToMap(new Remove(plugin));
		addToMap(new Rename(plugin));
		addToMap(new Residents(plugin));
		addToMap(new Settings(plugin));
		addToMap(new Setwarp(plugin));
		addToMap(new Unban(plugin));
		addToMap(new Unclaim(plugin));
		addToMap(new Warp(plugin));
		addToMap(new Welcome(plugin));
		addToMap(new Withdraw(plugin));
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		CSCommand cscommand = null;
		boolean wasNull = false;
		
		if(args.length > 0)
			cscommand = cityMap.get(args[0].toLowerCase());
		
		if(cscommand == null) {
			wasNull = true;
			cscommand = cityMap.get("default");
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
		cityMap.put(cmd.getName(), cmd);
		
		if(cmd.getAliases() != null) {
			for(String alias : cmd.getAliases()) {
				cityMap.put(alias, cmd);
			}
		}
	}
	
	public CSCommand getCommand(String cmd) {
		return cityMap.get(cmd);
	}
}