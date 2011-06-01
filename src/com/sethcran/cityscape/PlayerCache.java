package com.sethcran.cityscape;

import org.bukkit.Location;

public class PlayerCache {
	
	private String lastCityLocation;
	private String lastPlotLocation;
	private Location lastLocation;
	
	private String city;
	
	public PlayerCache(Location lastLocation, String lastPlotLocation, 
			String lastCityLocation, String city) {
		this.lastLocation = lastLocation;
		this.lastPlotLocation = lastPlotLocation;
		this.lastCityLocation = lastCityLocation;
		this.city = city;
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
}
