package com.sethcran.cityscape;

public class RankPermissions {
	private boolean addResident = false;
	private boolean removeResident = false;
	private boolean claim = false;
	private boolean unclaim = false;
	private boolean promote = false;
	private boolean demote = false;
	private boolean withdraw = false;
	private boolean changeRankName = false;
	private boolean setWelcome = false;
	private boolean setMayor = false;
	private boolean setWarp = false;
	private boolean setName = false;
	private boolean setPlotSale = false;
	private boolean setTaxes = false;
	private boolean setPrices = false;
	private boolean createPlots = false;
	private boolean sendChestsToLostAndFound = false;
	
	public RankPermissions() {
		
	}
	
	public RankPermissions(boolean b) {
		addResident = b;
		removeResident = b;
		claim = b;
		unclaim = b;
		promote = b;
		demote = b;
		withdraw = b;
		changeRankName = b;
		setWelcome = b;
		setMayor = b;
		setWarp = b;
		setName = b;
		setPlotSale = b;
		setTaxes = b;
		setPrices = b;
		createPlots = b;
		sendChestsToLostAndFound = b;
	}
	
	public boolean getAddResident() {
		return addResident;
	}
	
	public boolean getRemoveResident() {
		return removeResident;
	}
	
	public boolean getClaim() {
		return claim;
	}
	
	public boolean getUnclaim() {
		return unclaim;
	}
	
	public boolean getPromote() {
		return promote;
	}
	
	public boolean getDemote() {
		return demote;
	}
	
	public boolean getWithdraw() {
		return withdraw;
	}
	
	public boolean getChangeRankName() {
		return changeRankName;
	}
	
	public boolean getSetWelcome() {
		return setWelcome;
	}
	
	public boolean getSetMayor() {
		return setMayor;
	}
	
	public boolean getSetWarp() {
		return setWarp;
	}
	
	public boolean getSetName() {
		return setName;
	}
	
	public boolean getSetPlotSale() {
		return setPlotSale;
	}
	
	public boolean getSetTaxes() {
		return setTaxes;
	}
	
	public boolean getSetPrices() {
		return setPrices;
	}
	
	public boolean getCreatePlots() {
		return createPlots;
	}
	
	public boolean getSendChestsToLostAndFound() {
		return sendChestsToLostAndFound;
	}
	
	public void setAddResident(boolean b) {
		addResident = b;
	}
	
	public void setRemoveResident(boolean b) {
		removeResident = b;
	}
	
	public void setClaim(boolean b) {
		claim = b;
	}
	
	public void setUnclaim(boolean b) {
		unclaim = b;
	}
	
	public void setPromote(boolean b) {
		promote = b;
	}
	
	public void setDemote(boolean b) {
		demote = b;
	}
	
	public void setWithdraw(boolean b) {
		withdraw = b;
	}
	
	public void setChangeRankName(boolean b) {
		changeRankName = b;
	}
	
	public void setSetWelcome(boolean b) {
		setWelcome = b;
	}
	
	public void setSetMayor(boolean b) {
		setMayor = b;
	}
	
	public void setSetWarp(boolean b) {
		setWarp = b;
	}
	
	public void setSetName(boolean b) {
		setName = b;
	}
	
	public void setSetPlotSale(boolean b) {
		setPlotSale = b;
	}
	
	public void setSetTaxes(boolean b) {
		setTaxes = b;
	}
	
	public void setSetPrices(boolean b) {
		setPrices = b;
	}
	
	public void setCreatePlots(boolean b) {
		createPlots = b;
	}
	
	public void setSendChestsToLostAndFound(boolean b) {
		sendChestsToLostAndFound = b;
	}
}
