package com.sethcran.cityscape.logging;

public class CityChatLogEntry {
	private String city = null;
	private String message = null;
	private String player = null;
	
	public CityChatLogEntry(String city, String player, String message) {
		this.city = city;
		this.player = player;
		this.message = message;
	}
	
	public String getCity() {
		return city;
	}
	
	public String getMessage() {
		return message;
	}
	
	public String getPlayer() {
		return player;
	}
}
