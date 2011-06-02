package com.sethcran.cityscape.commands.citycommands;

import java.util.ArrayList;

import org.bukkit.command.CommandSender;

import com.sethcran.cityscape.City;
import com.sethcran.cityscape.Cityscape;
import com.sethcran.cityscape.Constants;
import com.sethcran.cityscape.commands.CSCommand;

public class List extends CSCommand {

	public List(Cityscape plugin) {
		super(plugin);
		name = "list";
		description = "Lists all of the cities on the server.";
		usage = "/c list";
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		ArrayList<City> cityList = plugin.getDB().getCities();
		
		sender.sendMessage(Constants.CITYSCAPE + Constants.SUCCESS_COLOR +
				"City List: ");
		String message = Constants.SUCCESS_COLOR;
		for(City city : cityList) {
			if(message.length() > Constants.CHAT_LINE_LENGTH) {
				sender.sendMessage(message);
				message = Constants.SUCCESS_COLOR;
			}
			else if(!message.equals(Constants.SUCCESS_COLOR))
				message = message.concat(", ");
			message = message.concat(city.getName());
		}
		
		if(!message.equals(Constants.SUCCESS_COLOR))
			sender.sendMessage(message);
	}

}
