package com.sethcran.cityscape;

public class Claim {
	private String cityName = null;
	private String world = null;
	
	private int x = 0;
	private int z = 0;
	
	private boolean visited = false;
	
	public Claim() {
		
	}

	public Claim(String world, int x, int z) {
		this.world = world;
		this.x = x;
		this.z = z;
	}

	public Claim(String cityName, String world, int x, int z) {
		this.cityName = cityName;
		this.world = world;
		this.x = x;
		this.z = z;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Claim other = (Claim) obj;
		if (world == null) {
			if (other.world != null)
				return false;
		} else if (!world.equals(other.world))
			return false;
		if (x != other.x)
			return false;
		if (z != other.z)
			return false;
		return true;
	}

	public String getCityName() {
		return cityName;
	}

	public String getWorld() {
		return world;
	}

	public int getX() {
		return x;
	}
	
	public int getZ() {
		return z;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((world == null) ? 0 : world.hashCode());
		result = prime * result + x;
		result = prime * result + z;
		return result;
	}

	public boolean isVisited() {
		return visited;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public void setVisited(boolean visited) {
		this.visited = visited;
	}

	public void setWorld(String world) {
		this.world = world;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setZ(int z) {
		this.z = z;
	}
	
	@Override
	public String toString() {
		return world + x + z;
	}
}
