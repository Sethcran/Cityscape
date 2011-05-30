package com.sethcran.cityscape;

import java.util.HashMap;

public class Plot {
	private int xmin = 0;
	private int zmin = 0;
	private int xmax = 0;
	private int zmax = 0;
	
	private boolean residentBuild = false;
	private boolean residentDestroy = false;
	private boolean residentSwitch = false;
	private boolean outsiderBuild = false;
	private boolean outsiderDestroy = false;
	private boolean outsiderSwitch = false;

	String cityName = null;
	String ownerName = null;

	HashMap<String, Permissions> playerPermissions = null;
	HashMap<String, Permissions> cityPermissions = null;

	public Plot(int xmin, int zmin, int xmax, int zmax) {
		this.xmin = xmin;
		this.zmin = zmin;
		this.xmax = xmax;
		this.zmax = zmax;
		
		playerPermissions = new HashMap<String, Permissions>();
		cityPermissions = new HashMap<String, Permissions>();
	}

	public String getCityName() {
		return cityName;
	}

	public Permissions getCityPermissions(String cityName) {
		return cityPermissions.get(cityName);
	}

	public String getOwnerName() {
		return ownerName;
	}

	public Permissions getPlayerPermissions(String playerName) {
		return playerPermissions.get(playerName);
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

	public void insertIntoCityPermissions(String cityName, boolean canBuild,
			boolean canDestroy, boolean canSwitch) {
		Permissions p = new Permissions(canBuild, canDestroy, canSwitch);
		cityPermissions.put(cityName, p);
	}

	public void insertIntoPlayerPermissions(String playerName, boolean canBuild, 
			boolean canDestroy, boolean canSwitch) {
		Permissions p = new Permissions(canBuild, canDestroy, canSwitch);
		playerPermissions.put(playerName, p);
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

	public void setCityName(String cityName) {
		this.cityName = cityName;
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
}
