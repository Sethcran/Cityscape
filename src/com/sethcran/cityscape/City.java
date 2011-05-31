package com.sethcran.cityscape;

import java.util.ArrayList;
import java.util.HashMap;

import org.khelekore.prtree.PRTree;

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
	
	private boolean residentBuild = false;
	private boolean residentDestroy = false;
	private boolean residentSwitch = false;
	private boolean outsiderBuild = false;
	private boolean outsiderDestroy = false;
	private boolean outsiderSwitch = false;
	
	private PRTree<Plot> plotTree = null;
	private HashMap<String, Plot> plotMap = null;
	
	public City() {
		plotMap = new HashMap<String, Plot>();
		plotTree = new PRTree<Plot>(new CSMBRConverter(), Constants.PRTREE_BRANCH_FACTOR);
	}
	
	public void addPlot(Plot plot) {
		plotMap.put(plot.toString(), plot);
		plotTree = new PRTree<Plot>(new CSMBRConverter(), Constants.PRTREE_BRANCH_FACTOR);
		plotTree.load(plotMap.values());
	}

	public int getBaseClaims() {
		return baseClaims;
	}

	public int getBonusClaims() {
		return bonusClaims;
	}

	public String getFounded() {
		return founded;
	}

	public String getMayor() {
		return mayor;
	}

	public String getName() {
		return name;
	}
	
	public Plot getPlotAt(int x, int z) {
		ArrayList<Plot> plot = new ArrayList<Plot>();
		plotTree.find(x, z, x, z, plot);
		if(plot.isEmpty())
			return null;
		else
			return plot.get(0);
	}

	public int getRank() {
		return rank;
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

	public int getUsedClaims() {
		return usedClaims;
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
	
	public void loadMap(HashMap<String, Plot> map) {
		plotMap = map;
		plotTree = new PRTree<Plot>(new CSMBRConverter(), Constants.PRTREE_BRANCH_FACTOR);
		plotTree.load(map.values());
	}
	
	public void removePlot(Plot plot) {
		plotMap.remove(plot.toString());
		plotTree = new PRTree<Plot>(new CSMBRConverter(), Constants.PRTREE_BRANCH_FACTOR);
		plotTree.load(plotMap.values());
	}
	
	public void setBaseClaims(int baseClaims) {
		this.baseClaims = baseClaims;
	}
	
	public void setBonusClaims(int bonusClaims) {
		this.bonusClaims = bonusClaims;
	}
	
	public void setFounded(String founded) {
		this.founded = founded;
	}
	
	public void setMayor(String mayor) {
		this.mayor = mayor;
	}
	
	public void setName(String name) {
		this.name = name;
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
	
	public void setRank(int rank) {
		this.rank = rank;
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
	
	public void setSpawnX(int spawnX) {
		this.spawnX = spawnX;
	}
	
	public void setSpawnY(int spawnY) {
		this.spawnY = spawnY;
	}
	
	public void setSpawnZ(int spawnZ) {
		this.spawnZ = spawnZ;
	}
	
	public void setUsedClaims(int usedClaims) {
		this.usedClaims = usedClaims;
	}
}
