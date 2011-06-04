package com.sethcran.cityscape;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import gnu.trove.TIntObjectHashMap;

import com.infomatiq.jsi.Rectangle;
import com.infomatiq.jsi.rtree.RTree;

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
	private boolean snow = false;
	
	private RTree plotTree = null;
	private TIntObjectHashMap<Plot> plotMap = null;
	private HashMap<String, RankPermissions> rankMap = null;
	
	public City() {
		rankMap = new HashMap<String, RankPermissions>();
		plotMap = new TIntObjectHashMap<Plot>();
		plotTree = new RTree();
		plotTree.init(null);
	}
	public void addPlot(Plot plot) {
		plotMap.put(plot.getId(), plot);
		plotTree.add(new Rectangle(plot.getXmin(), plot.getZmin(), 
				plot.getXmax(), plot.getZmax()), plot.getId());
	}
	
	public void addRank(RankPermissions rp) {
		rankMap.put(rp.getRankName(), rp);
	}
	
	public void changeRank(RankPermissions rp) {
		rankMap.remove(rp.getRankName());
		rankMap.put(rp.getRankName(), rp);
	}
	
	public boolean doesRankExist(String rank) {
		RankPermissions rp = rankMap.get(rank);
		if(rp == null)
			return false;
		return true;
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

	public int getNumRanks() {
		return rankMap.size();
	}

	public Plot getPlotAt(int x, int z) {
		TreeProcedure tproc = new TreeProcedure();
		plotTree.intersects(new Rectangle(x, z, x, z), tproc);
		for(int i : tproc.getId()) {
			return plotMap.get(i);
		}
		return null;
	}
	
	public int getRank() {
		return rank;
	}
	
	public RankPermissions getRank(String rankName) {
		return rankMap.get(rankName);
	}

	public ArrayList<String> getRanks() {
		Set<String> set = rankMap.keySet();
		ArrayList<String> list = new ArrayList<String>();
		for(String s : set) {
			list.add(s);
		}
		return list;
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
	public Plot isPlotIntersect(int xmin, int zmin, int xmax, int zmax) {
		TreeProcedure tproc = new TreeProcedure();
		plotTree.intersects(new Rectangle(xmin, zmin, xmax, zmax), tproc);
		for(int i : tproc.getId()) {
			Plot plot = plotMap.get(i);
			return plot;
		}
		return null;
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
	
	public void removePlot(Plot plot) {
		plotMap.remove(plot.getId());
		plotTree.delete(new Rectangle(plot.getXmin(), plot.getZmin(), 
				plot.getXmax(), plot.getZmax()), plot.getId());
	}
	
	public void removeRank(String rank) {
		rankMap.remove(rank);
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
	
	public void setSnow(boolean snow) {
		this.snow = snow;
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
