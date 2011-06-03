package com.sethcran.cityscape;

public class Selection {
	private int xmin = 0;
	private int zmin = 0;
	private int xmax = 0;
	private int zmax = 0;
	
	boolean setFirst = false;
	boolean setSecond = false;
	
	String firstWorld = null;
	String secondWorld = null;

	public String getFirstWorld() {
		return firstWorld;
	}

	public String getSecondWorld() {
		return secondWorld;
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
	
	public boolean isSetFirst() {
		return setFirst;
	}

	public boolean isSetSecond() {
		return setSecond;
	}

	public void setFirst(int x, int z, String world) {
		setFirst = true;
		if(setSecond) {
			if(x > xmax) {
				xmin = xmax;
				xmax = x;
			}
			else
				xmin = x;
			if(z > zmax) {
				zmin = zmax;
				zmax = z;
			}
			else
				zmin = z;
		}
		else {
			xmin = x;
			zmin = z;
		}
		firstWorld = world;
	}

	public void setFirstWorld(String firstWorld) {
		this.firstWorld = firstWorld;
	}

	public void setSecond(int x, int z, String world) {
		setSecond = true;
		if(setFirst) {
			if(x < xmin) {
				xmax = xmin;
				xmin = x;
			}
			else
				xmax = x;
			if(z < zmin) {
				zmax = zmin;
				zmin = z;
			}
			else
				zmax = z;
		}
		else {
			xmax = x;
			zmin = z;
		}
		secondWorld = world;
	}

	public void setSecondWorld(String secondWorld) {
		this.secondWorld = secondWorld;
	}

	public void setSetFirst(boolean setFirst) {
		this.setFirst = setFirst;
	}

	public void setSetSecond(boolean setSecond) {
		this.setSecond = setSecond;
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
