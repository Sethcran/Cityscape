package com.sethcran.cityscape.error;

import org.bukkit.command.CommandSender;
import com.sethcran.cityscape.Constants;

public class ErrorManager {
	public enum CSError {
		ALREADY_IN_A_CITY,
		CITY_ALREADY_EXISTS,
		CITY_ALREADY_OWNS,
		CITY_DOES_NOT_EXIST,
		CITY_DOES_NOT_EXIST_NS,
		CLAIM_NOT_CONNECTED,
		IMPOSSIBLE,
		INCORRECT_FORMAT,
		INCORRECT_NAME_FORMAT,
		INCORRECT_NUMBER_FORMAT,
		IN_GAME_ONLY,
		LENGTH_EXCEEDED,
		MAYOR_ONLY,
		MUST_BE_STANDING_IN_CITY,
		MUST_BE_STANDING_IN_PLOT,
		NOT_APPLICABLE_TO_SELF,
		NOT_ENOUGH_ARGUMENTS,
		NOT_ENOUGH_CLAIMS,
		NOT_ENOUGH_MONEY,
		NOT_INVITED,
		NOT_IN_CITY,
		NO_CLAIMS_AVAILABLE,
		NO_INVITES,
		NO_PERMISSION,
		NO_RANK_PERMISSION,
		OTHER_ALREADY_IN_CITY,
		OTHER_NOT_IN_YOUR_CITY,
		PLAYER_DOES_NOT_EXIST,
		PLAYER_NOT_IN_YOUR_CITY,
		RANK_ALREADY_EXISTS,
		RANK_DOES_NOT_EXIST,
		RANK_DOES_NOT_EXIST_NS,
		SET_MAYOR_FIRST,
		TOO_MANY_ARGUMENTS,
		WILDERNESS,
	}
	
	public static void sendError(CommandSender sender, CSError type, String args) {
		String message = Constants.CITYSCAPE + Constants.ERROR_COLOR;
		switch(type) {
		case ALREADY_IN_A_CITY: sender.sendMessage(message +
				"You are already in a city."); break;
		case CITY_ALREADY_EXISTS: sender.sendMessage(message +
				"The city of " + args + " already exists."); break;
		case CITY_ALREADY_OWNS: sender.sendMessage(message +
				"The city of " + args + " already owns this claim."); break;
		case CITY_DOES_NOT_EXIST: sender.sendMessage(message + 
				"The city " + args + " does not exist."); break;
		case CITY_DOES_NOT_EXIST_NS: sender.sendMessage(message +
				"That city does not exist."); break;
		case CLAIM_NOT_CONNECTED: sender.sendMessage(message +
				"That claim is not connected to your land."); break;
		case IMPOSSIBLE: sender.sendMessage(message +
				"That can not be done."); break;
		case INCORRECT_FORMAT: sender.sendMessage(message +
				"There was an error in your format."); break;
		case INCORRECT_NAME_FORMAT: sender.sendMessage(message +
				"The name can only contain alphabetic characters."); break;
		case INCORRECT_NUMBER_FORMAT: sender.sendMessage(message +
				"Your number was not formatted correctly."); break;
		case IN_GAME_ONLY: sender.sendMessage(message +	
				"Only players in game can do that."); break;
		case LENGTH_EXCEEDED: sender.sendMessage(message +
				"That name must be under " + args + " characters."); break;
		case MAYOR_ONLY: sender.sendMessage(message +
				"Only the mayor can do that."); break;
		case MUST_BE_STANDING_IN_CITY: sender.sendMessage(message +
				"You must be standing in your city to do that."); break;
		case MUST_BE_STANDING_IN_PLOT: sender.sendMessage(message +
				"You must be standing in a plot to do that."); break;
		case NOT_APPLICABLE_TO_SELF: sender.sendMessage(message + 
				"You can't do that to yourself."); break;
		case NOT_ENOUGH_ARGUMENTS: sender.sendMessage(message + 
				"That command requires additional arguments."); break;
		case NOT_ENOUGH_CLAIMS: sender.sendMessage(message +
				"Your city does not have the " + args + " claim needed."); break;
		case NOT_ENOUGH_MONEY: sender.sendMessage(message +
				"You do not have enough money. You need " + args + "."); break;
		case NOT_INVITED: sender.sendMessage(message + 
				"You have not been invited to join " + args + "."); break;
		case NOT_IN_CITY: sender.sendMessage(message +
				"You must be in a city to do that."); break;
		case NO_CLAIMS_AVAILABLE: sender.sendMessage(message +
				"There were no claims available in that area."); break;
		case NO_INVITES: sender.sendMessage(message +
				"You have not been invited to any cities."); break;
		case NO_PERMISSION: sender.sendMessage(message +
				"You do not have permission to do that."); break;
		case NO_RANK_PERMISSION: sender.sendMessage(message + 
				"Your rank does not have permission to do that."); break;
		case OTHER_ALREADY_IN_CITY: sender.sendMessage(message +
				"That person is already in a city."); break;
		case OTHER_NOT_IN_YOUR_CITY: sender.sendMessage(message +
				args + " is not in your city."); break;
		case PLAYER_DOES_NOT_EXIST: sender.sendMessage(message + 
				"The player " + args + " does not exist."); break;
		case PLAYER_NOT_IN_YOUR_CITY: sender.sendMessage(message +
				"The player " + args + " is not in your city."); break;
		case RANK_ALREADY_EXISTS: sender.sendMessage(message +
				"That rank already exists."); break;
		case RANK_DOES_NOT_EXIST: sender.sendMessage(message +
				"The rank " + args + " does not exist."); break;
		case RANK_DOES_NOT_EXIST_NS: sender.sendMessage(message +
				"That rank does not exist."); break;
		case SET_MAYOR_FIRST: sender.sendMessage(message +
				"You must set another mayor first."); break;
		case TOO_MANY_ARGUMENTS: sender.sendMessage(message + 
				"That command requires fewer arguments."); break;
		case WILDERNESS: sender.sendMessage(message + 
				"You are in the wilderness."); break;
		
		}
	}
}
