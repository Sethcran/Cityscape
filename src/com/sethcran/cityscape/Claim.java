package com.sethcran.cityscape;

public class Claim {
	private String cityName = null;
	private String world = null;
	
	private int xmin = 0;
	private int zmin = 0;
	private int xmax = 0;
	private int zmax = 0;
	
	private int id = 0;
	
	public Claim() {
		
	}

	public Claim(String cityName, String world, int xmin, int zmin, 
			int xmax, int zmax, int id) {
		this.cityName = cityName;
		this.xmin = xmin;
		this.zmin = zmin;
		this.xmax = xmax;
		this.zmax = zmax;
		this.id = id;
		this.world = world;
	}

	public String getCityName() {
		return cityName;
	}

	public int getId() {
		return id;
	}

	public String getWorld() {
		return world;
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
	
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public void setWorld(String world) {
		this.world = world;
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
