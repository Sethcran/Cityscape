package com.sethcran.cityscape.commands.citycommands;

import java.util.Arrays;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sethcran.cityscape.City;
import com.sethcran.cityscape.Cityscape;
import com.sethcran.cityscape.Constants;
import com.sethcran.cityscape.RankPermissions;
import com.sethcran.cityscape.commands.CSCommand;

public class Change extends CSCommand {

	public Change(Cityscape plugin) {
		super(plugin);
		name = "change";
		description = "Changes the given ranks permissions.";
		usage = "/c change [rank] [level:on/off]";
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		Player player = null;
		if(sender instanceof Player)
			player = (Player)sender;
		else {
			sender.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"Only a player in game can do that.");
			return;
		}
		
		if(args == null) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"That command requires arguments.");
			player.sendMessage(Constants.ERROR_COLOR + usage);
			return;
		}
		
		if(args.length < 2) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"That command requires more arguments.");
			player.sendMessage(Constants.ERROR_COLOR + usage);
			return;
		}
		
		String cityName = plugin.getCache(player.getName()).getCity();
		
		if(cityName == null) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"You are not in a city.");
			return;
		}
		
		City city = plugin.getCity(cityName);
		
		if(!city.getMayor().equals(player.getName())) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"Only the city mayor can do that!");
			return;
		}
		
		RankPermissions rp = city.getRank(args[0]);
		
		if(rp == null) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"That rank does not exist!");
			return;
		}
		
		if(rp.getRankName().equals("Mayor")) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR + 
					"You can't change the Mayor permissions.");
			return;
		}
		
		if(args[1].equalsIgnoreCase("all:on")) {
			rp = new RankPermissions(true);
			rp.setRankName(args[0]);
			update(player, cityName, rp);
			return;
		}
		else if(args[1].equalsIgnoreCase("all:off")) {
			rp = new RankPermissions(false);
			rp.setRankName(args[0]);
			update(player, cityName, rp);
			return;
		}
		
		String[] perms = Arrays.copyOfRange(args, 1, args.length);
		
		for(String perm : perms) {
			String[] each = perm.split(":");
			if(each == null) {
				player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
						"There was an error in your format.");
				return;
			}
			if(each.length != 2) {
				player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
						"There was an error in your format.");
				return;
			}
			
			if(each[0].equalsIgnoreCase("addresidents")) {
				if(each[1].equalsIgnoreCase("on"))
					rp.setAddResident(true);
				else
					rp.setAddResident(false);
			}
			else if(each[0].equalsIgnoreCase("removeresidents")) {
				if(each[1].equalsIgnoreCase("on"))
					rp.setRemoveResident(true);
				else
					rp.setRemoveResident(false);
			}
			else if(each[0].equalsIgnoreCase("claim")) {
				if(each[1].equalsIgnoreCase("on"))
					rp.setClaim(true);
				else
					rp.setClaim(false);
			}
			else if(each[0].equalsIgnoreCase("unclaim")) {
				if(each[1].equalsIgnoreCase("on"))
					rp.setUnclaim(true);
				else
					rp.setUnclaim(false);
			}
			else if(each[0].equalsIgnoreCase("promote")) {
				if(each[1].equalsIgnoreCase("on"))
					rp.setPromote(true);
				else
					rp.setPromote(false);
			}
			else if(each[0].equalsIgnoreCase("demote")) {
				if(each[1].equalsIgnoreCase("on"))
					rp.setDemote(true);
				else
					rp.setDemote(false);
			}
			else if(each[0].equalsIgnoreCase("withdraw")) {
				if(each[1].equalsIgnoreCase("on"))
					rp.setWithdraw(true);
				else
					rp.setWithdraw(false);
			}
			else if(each[0].equalsIgnoreCase("changesettings")) {
				if(each[1].equalsIgnoreCase("on"))
					rp.setSettings(true);
				else
					rp.setSettings(false);
			}
			else if(each[0].equalsIgnoreCase("changecityplotperms")) {
				if(each[1].equalsIgnoreCase("on"))
					rp.setChangeCityPlotPerms(true);
				else
					rp.setChangeCityPlotPerms(false);
			}
			else if(each[0].equalsIgnoreCase("setwelcome")) {
				if(each[1].equalsIgnoreCase("on"))
					rp.setSetWelcome(true);
				else
					rp.setSetWelcome(false);
			}
			else if(each[0].equalsIgnoreCase("setmayor")) {
				if(each[1].equalsIgnoreCase("on"))
					rp.setSetMayor(true);
				else
					rp.setSetMayor(false);
			}
			else if(each[0].equalsIgnoreCase("setwarp")) {
				if(each[1].equalsIgnoreCase("on"))
					rp.setSetWarp(true);
				else
					rp.setSetWarp(false);
			}
			else if(each[0].equalsIgnoreCase("setname")) {
				if(each[1].equalsIgnoreCase("on"))
					rp.setSetName(true);
				else
					rp.setSetName(false);
			}
			else if(each[0].equalsIgnoreCase("setplotsale")) {
				if(each[1].equalsIgnoreCase("on"))
					rp.setSetPlotSale(true);
				else
					rp.setSetPlotSale(false);
			}
			else if(each[0].equalsIgnoreCase("settaxes")) {
				if(each[1].equalsIgnoreCase("on"))
					rp.setSetTaxes(true);
				else
					rp.setSetTaxes(false);
			}
			else if(each[0].equalsIgnoreCase("createplots")) {
				if(each[1].equalsIgnoreCase("on"))
					rp.setCreatePlots(true);
				else
					rp.setCreatePlots(false);
			}
			else if(each[0].equalsIgnoreCase("sendcheststolostandfound")) {
				if(each[1].equalsIgnoreCase("on"))
					rp.setSendChestsToLostAndFound(true);
				else
					rp.setSendChestsToLostAndFound(false);
			}
			else if(each[0].equalsIgnoreCase("citybuild")) {
				if(each[1].equalsIgnoreCase("on"))
					rp.setCityBuild(true);
				else
					rp.setCityBuild(false);
			}
			else if(each[0].equalsIgnoreCase("citydestroy")) {
				if(each[1].equalsIgnoreCase("on"))
					rp.setCityDestroy(true);
				else
					rp.setCityDestroy(false);
			}
			else if(each[0].equalsIgnoreCase("cityswitch")) {
				if(each[1].equalsIgnoreCase("on"))
					rp.setCitySwitch(true);
				else
					rp.setCitySwitch(false);
			}
			else {
				player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
						"A permission name was not detected. Aborting.");
				return;
			}
		}
		update(player, cityName, rp);
	}
	
	public void update(Player player, String cityName, RankPermissions rp) {
		plugin.getDB().setRankPermissions(cityName, rp);
		plugin.getCity(cityName).changeRank(rp);
		player.sendMessage(Constants.CITYSCAPE + Constants.SUCCESS_COLOR +
				"You have updated the permissions of " + rp.getRankName() + ".");
	}
}
