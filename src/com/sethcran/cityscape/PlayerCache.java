package com.sethcran.cityscape;

import org.bukkit.Location;

public class PlayerCache {
	
	private String lastCityLocation = null;
	private String lastPlotLocation = null;
	private Location lastLocation = null;
	
	private String city = null;
	private String rank = null;
	
	private String firstLogin = null;
	private String lastLogin = null;

	public PlayerCache(Location lastLocation, String lastPlotLocation, 
			String lastCityLocation, String city, String rank) {
		this.lastLocation = lastLocation;
		this.lastPlotLocation = lastPlotLocation;
		this.lastCityLocation = lastCityLocation;
		this.city = city;
		this.rank = rank;
	}

	public String getCity() {
		return city;
	}

	public String getFirstLogin() {
		return firstLogin;
	}
	public Location getLastLocation() {
		return lastLocation;
	}
	
	public String getLastLogin() {
		return lastLogin;
	}
	
	public String getLastPlotLocation() {
		return lastPlotLocation;
	}

	public String getLastTownLocation() {
		return lastCityLocation;
	}

	public String getRank() {
		return rank;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setFirstLogin(String firstLogin) {
		this.firstLogin = firstLogin;
	}
	
	public void setLastLocation(Location lastLocation) {
		this.lastLocation = lastLocation;
	}
	
	public void setLastLogin(String lastLogin) {
		this.lastLogin = lastLogin;
	}
	
	public void setLastPlotLocation(String lastPlotLocation) {
		this.lastPlotLocation = lastPlotLocation;
	}
	
	public void setLastTownLocation(String lastCityLocation) {
		this.lastCityLocation = lastCityLocation;
	}
	
	public void setRank(String rank) {
		this.rank = rank;
	}
}
