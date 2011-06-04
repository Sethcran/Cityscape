package com.sethcran.cityscape;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

public class Plot {
	private int xmin = 0;
	private int zmin = 0;
	private int xmax = 0;
	private int zmax = 0;
	
	private int id;
	
	private boolean residentBuild = false;
	private boolean residentDestroy = false;
	private boolean residentSwitch = false;
	private boolean outsiderBuild = false;
	private boolean outsiderDestroy = false;
	private boolean outsiderSwitch = false;
	private boolean cityPlot = false;
	private boolean snow = false;
	
	private String cityName = null;
	private String ownerName = null;

	private HashMap<String, Permissions> playerPermissions = null;
	private HashMap<String, Permissions> cityPermissions = null;

	public Plot(int xmin, int zmin, int xmax, int zmax) {
		this.xmin = xmin;
		this.zmin = zmin;
		this.xmax = xmax;
		this.zmax = zmax;
		
		playerPermissions = new HashMap<String, Permissions>();
		cityPermissions = new HashMap<String, Permissions>();
	}
	public Set<Entry<String, Permissions>> getCitiesWithPermissions() {
		return cityPermissions.entrySet();
	}

	public String getCityName() {
		return cityName;
	}
	public Permissions getCityPermissions(String cityName) {
		return cityPermissions.get(cityName);
	}

	public int getId() {
		return id;
	}
	
	public String getOwnerName() {
		return ownerName;
	}

	public Permissions getPlayerPermissions(String playerName) {
		return playerPermissions.get(playerName);
	}

	public Set<Entry<String, Permissions>> getPlayersWithPermissions() {
		return playerPermissions.entrySet();
	}
	
	public int getXmax() {
		return xmax;
	}
	
	public int getXmin() {
		return xmin;
	}

	public int getZmax() {
		return zmax;
	}

	public int getZmin() {
		return zmin;
	}

	public void insertIntoCityPermissions(String cityName, Permissions perms) {
		cityPermissions.put(cityName, perms);
	}

	public void insertIntoPlayerPermissions(String playerName, Permissions perms) {
		playerPermissions.put(playerName, perms);
	}

	public boolean isCityPlot() {
		return cityPlot;
	}

	public boolean isOutsiderBuild() {
		return outsiderBuild;
	}

	public boolean isOutsiderDestroy() {
		return outsiderDestroy;
	}

	public boolean isOutsiderSwitch() {
		return outsiderSwitch;
	}

	public boolean isResidentBuild() {
		return residentBuild;
	}

	public boolean isResidentDestroy() {
		return residentDestroy;
	}

	public boolean isResidentSwitch() {
		return residentSwitch;
	}

	public boolean isSnow() {
		return snow;
	}

	public void removeFromCityPermissions(String city) {
		cityPermissions.remove(city);
	}
	
	public void removeFromPlayerPermissions(String player) {
		playerPermissions.remove(player);
	}
	
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public void setCityPlot(boolean cityPlot) {
		this.cityPlot = cityPlot;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setOutsiderBuild(boolean outsiderBuild) {
		this.outsiderBuild = outsiderBuild;
	}
	
	public void setOutsiderDestroy(boolean outsiderDestroy) {
		this.outsiderDestroy = outsiderDestroy;
	}
	
	public void setOutsiderSwitch(boolean outsiderSwitch) {
		this.outsiderSwitch = outsiderSwitch;
	}
	
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
	public void setResidentBuild(boolean residentBuild) {
		this.residentBuild = residentBuild;
	}
	
	public void setResidentDestroy(boolean residentDestroy) {
		this.residentDestroy = residentDestroy;
	}
	
	public void setResidentSwitch(boolean residentSwitch) {
		this.residentSwitch = residentSwitch;
	}
	
	public void setSnow(boolean snow) {
		this.snow = snow;
	}
	
	public void setXmax(int xmax) {
		this.xmax = xmax;
	}
	
	public void setXmin(int xmin) {
		this.xmin = xmin;
	}
	
	public void setZmax(int zmax) {
		this.zmax = zmax;
	}
	
	public void setZmin(int zmin) {
		this.zmin = zmin;
	}
	
	@Override
	public String toString() {
		return cityName + ownerName + xmax + xmin + zmax + zmin;
	}
}
