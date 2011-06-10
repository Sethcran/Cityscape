package com.sethcran.cityscape.commands.citycommands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sethcran.cityscape.City;
import com.sethcran.cityscape.Cityscape;
import com.sethcran.cityscape.Constants;
import com.sethcran.cityscape.RankPermissions;
import com.sethcran.cityscape.commands.CSCommand;
import com.sethcran.cityscape.error.ErrorManager;
import com.sethcran.cityscape.error.ErrorManager.CSError;

public class Rank extends CSCommand {

	public Rank(Cityscape plugin) {
		super(plugin);
		name = "rank";
		description = "Views rank information, or used to create a new rank.";
		usage = "/c rank [cityname] [new/create] [rankname]";
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(args == null) {
			ErrorManager.sendError(sender, CSError.NOT_ENOUGH_ARGUMENTS, null);
			sender.sendMessage(Constants.ERROR_COLOR + usage);
			return;
		}
		
		if(args.length == 1) {
			viewRankInfoCurrentCity(sender, args[0]);
		}
		
		if(args.length == 2) {
			if(args[0].equalsIgnoreCase("new") || args[0].equalsIgnoreCase("create")) {
				createRank(sender, args[1]);
			}
			else {
				if(!plugin.getDB().doesCityExist(args[0]))
					viewRankInfo(sender, null, args[1]);
				else
					viewRankInfo(sender, args[0], args[1]);
			}
		}

	}
	
	public void createRank(CommandSender sender, String rank) {
		Player player = null;
		if(sender instanceof Player)
			player = (Player)sender;
		else {
			ErrorManager.sendError(sender, CSError.IN_GAME_ONLY, null);
			return;
		}
		
		String cityName = plugin.getCache(player.getName()).getCity();
		if(cityName == null) {
			ErrorManager.sendError(sender, CSError.NOT_IN_CITY, null);
			return;
		}
		
		City city = plugin.getCity(cityName);
		if(!city.getMayor().equals(player.getName())) {
			ErrorManager.sendError(sender, CSError.MAYOR_ONLY, null);
			return;
		}
		
		if(city.getNumRanks() >= plugin.getSettings().maxCityRanks) {
			player.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR + 
					"Your city already has the maximum number of ranks available!");
		}
		
		if(rank.length() > Constants.RANK_MAX_NAME_LENGTH ) {
			ErrorManager.sendError(sender, CSError.LENGTH_EXCEEDED, 
					"" + Constants.RANK_MAX_NAME_LENGTH);
			return;
		}
		
		if(!rank.matches("[a-zA-Z]+")) {
			ErrorManager.sendError(sender, CSError.INCORRECT_NAME_FORMAT, null);
			return;
		}
		
		if(city.doesRankExist(rank)) {
			ErrorManager.sendError(sender, CSError.RANK_ALREADY_EXISTS, null);
			return;
		}
		
		plugin.getDB().createRank(cityName, rank);
		RankPermissions perms = new RankPermissions(false);
		perms.setRankName(rank);
		city.addRank(perms);
		plugin.sendMessageToCity(player.getName() + " created the rank of " + rank + ".", 
				cityName);
	}
	
	public void viewRankInfo(CommandSender sender, String cityName, String rank) {
		if(cityName == null) {
			ErrorManager.sendError(sender, CSError.CITY_DOES_NOT_EXIST_NS, null);
			return;
		}
		
		City city = plugin.getCity(cityName);
		RankPermissions rp = city.getRank(rank);
		
		if(rp == null) {
			ErrorManager.sendError(sender, CSError.RANK_DOES_NOT_EXIST_NS, null);
			return;
		}
		
		sender.sendMessage(Constants.CITYSCAPE + Constants.SUCCESS_COLOR +
				"Rank Information: " + ChatColor.DARK_AQUA + rank + 
				Constants.SUCCESS_COLOR + " of " + ChatColor.DARK_AQUA +
				cityName);
		
		if(rp.areAll(true)) {
			sender.sendMessage(Constants.SUCCESS_COLOR + "Can do everything.");
			return;
		}
		
		if(rp.areAll(false)) {
			sender.sendMessage(Constants.SUCCESS_COLOR + "Can not do anything.");
		}
		
		if(rp.isAddResident())
			sender.sendMessage(Constants.SUCCESS_COLOR + "Can add residents to the city.");
		if(rp.isRemoveResident())
			sender.sendMessage(Constants.SUCCESS_COLOR + 
					"Can remove residents from the city.");
		if(rp.isCityBuild())
			sender.sendMessage(Constants.SUCCESS_COLOR + "Can build anywhere in the city.");
		if(rp.isCityDestroy())
			sender.sendMessage(Constants.ERROR_COLOR + "Can destroy anywhere in the city.");
		if(rp.isCitySwitch())
			sender.sendMessage(Constants.SUCCESS_COLOR + "Can switch anywhere in the city.");
		if(rp.isClaim())
			sender.sendMessage(Constants.SUCCESS_COLOR + "Can claim land for the city.");
		if(rp.isUnclaim())
			sender.sendMessage(Constants.SUCCESS_COLOR + "Can unclaim land of the city.");
		if(rp.isCreatePlots())
			sender.sendMessage(Constants.SUCCESS_COLOR + "Can create plots in the city.");
		if(rp.isChangeCityPlotPerms())
			sender.sendMessage(Constants.SUCCESS_COLOR + 
					"Can change city plot permissions.");
		if(rp.isPromote())
			sender.sendMessage(Constants.SUCCESS_COLOR + "Can promote people to new ranks.");
		if(rp.isDemote())
			sender.sendMessage(Constants.SUCCESS_COLOR + 
					"Can demote people from their rank.");
		if(rp.isSendChestsToLostAndFound())
			sender.sendMessage(Constants.SUCCESS_COLOR + 
					"Can send chests to the lost and found.");
		if(rp.isSettings())
			sender.sendMessage(Constants.SUCCESS_COLOR + "Can change city settings.");
		if(rp.isSetMayor())
			sender.sendMessage(Constants.ERROR_COLOR + "Can set a new mayor.");
		if(rp.isSetName())
			sender.sendMessage(Constants.SUCCESS_COLOR + "Can change the city name.");
		if(rp.isSetPlotSale())
			sender.sendMessage(Constants.SUCCESS_COLOR + "Can set plots for sale.");
		if(plugin.getSettings().taxes) {
			if(rp.isSetTaxes())
				sender.sendMessage(Constants.SUCCESS_COLOR + "Can change taxes.");
		}
		if(plugin.getSettings().cityWarps) {
			if(rp.isSetWarp())
				sender.sendMessage(Constants.SUCCESS_COLOR + "Can change the city warp.");
		}
		if(rp.isWithdraw())
			sender.sendMessage(Constants.ERROR_COLOR + 
					"Can withdraw money from the city bank.");
		if(rp.isBan()) {
			sender.sendMessage(Constants.SUCCESS_COLOR + "Can ban players from the city.");
		}
		if(rp.isUnban()) {
			sender.sendMessage(Constants.SUCCESS_COLOR + "Can unban players from the city.");
		}
	}
	
	public void viewRankInfoCurrentCity(CommandSender sender, String rank) {
		Player player = null;
		if(sender instanceof Player)
			player = (Player)sender;
		else {
			ErrorManager.sendError(sender, CSError.IN_GAME_ONLY, null);
			return;
		}
		
		String city = plugin.getCache(player.getName()).getCity();
		if(city == null) {
			ErrorManager.sendError(sender, CSError.NOT_IN_CITY, null);
			return;
		}
		viewRankInfo(sender, city, rank);
	}

}
