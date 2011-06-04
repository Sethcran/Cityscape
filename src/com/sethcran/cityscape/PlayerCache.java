package com.sethcran.cityscape;

import org.bukkit.Location;

public class PlayerCache {
	
	private String lastCityLocation;
	private String lastPlotLocation;
	private Location lastLocation;
	
	private String city;
	private String rank;
	
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

	public Location getLastLocation() {
		return lastLocation;
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
	
	public void setLastLocation(Location lastLocation) {
		this.lastLocation = lastLocation;
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
