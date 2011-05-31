package com.sethcran.cityscape;


import gnu.trove.TIntObjectHashMap;

import com.infomatiq.jsi.Rectangle;
import com.infomatiq.jsi.rtree.RTree;

import com.sethcran.cityscape.TreeProcedure;

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
	
	private RTree plotTree = null;
	private TIntObjectHashMap<Plot> plotMap = null;
	
	public City() {
		plotMap = new TIntObjectHashMap<Plot>();
		plotTree = new RTree();
		plotTree.init(null);
	}
	
	public void addPlot(Plot plot) {
		plot.setId(plotTree.size() + 1);
		plotMap.put(plot.getId(), plot);
		plotTree.add(new Rectangle(plot.getXmin(), plot.getZmin(), 
				plot.getXmax(), plot.getZmax()), plot.getId());
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
		TreeProcedure tproc = new TreeProcedure();
		plotTree.intersects(new Rectangle(x, z, x, z), tproc);
		return plotMap.get(tproc.getId());
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
	
	public void removePlot(Plot plot) {
		plotMap.remove(plot.getId());
		plotTree.delete(new Rectangle(plot.getXmin(), plot.getZmin(), 
				plot.getXmax(), plot.getZmax()), plot.getId());
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
