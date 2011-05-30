package com.sethcran.cityscape;

public class City {
	private String name = null;
	private String mayor = null;
	
	private String founded = null;
	
	private int rank = 0;
	
	private int usedClaims = 0;
	private int baseClaims = 0;
	private int bonusClaims = 0;
	
	private int spawnX = 0;
	private int spawnY = 0;
	private int spawnZ = 0;
	
	public String getName() {
		return name;
	}
	
	public String getMayor() {
		return mayor;
	}
	
	public String getFounded() {
		return founded;
	}
	
	public int getRank() {
		return rank;
	}
	
	public int getUsedClaims() {
		return usedClaims;
	}
	
	public int getBaseClaims() {
		return baseClaims;
	}
	
	public int getBonusClaims() {
		return bonusClaims;
	}
	
	public int getSpawnX() {
		return spawnX;
	}
	
	public int getSpawnY() {
		return spawnY;
	}
	
	public int getSpawnZ() {
		return spawnZ;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setMayor(String mayor) {
		this.mayor = mayor;
	}
	public void setFounded(String founded) {
		this.founded = founded;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
	
	public void setUsedClaims(int usedClaims) {
		this.usedClaims = usedClaims;
	}
	
	public void setBaseClaims(int baseClaims) {
		this.baseClaims = baseClaims;
	}
	
	public void setBonusClaims(int bonusClaims) {
		this.bonusClaims = bonusClaims;
	}
	
	public void setSpawnX(int spawnX) {
		this.spawnX = spawnX;
	}
	
	public void setSpawnY(int spawnY) {
		this.spawnY = spawnY;
	}
	
	public void setSpawnZ(int spawnZ) {
		this.spawnZ = spawnZ;
	}
}
