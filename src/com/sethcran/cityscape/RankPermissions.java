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
	private boolean changeCityPlotPerms = false;
	private boolean setWelcome = false;
	private boolean setMayor = false;
	private boolean setWarp = false;
	private boolean setName = false;
	private boolean setPlotSale = false;
	private boolean setTaxes = false;
	private boolean setPrices = false;
	private boolean createPlots = false;
	private boolean sendChestsToLostAndFound = false;
	private boolean cityBuild = false;
	private boolean cityDestroy = false;
	private boolean citySwitch = false;
	
	private String rankName = null;
	
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
		changeCityPlotPerms = b;
		setWelcome = b;
		setMayor = b;
		setWarp = b;
		setName = b;
		setPlotSale = b;
		setTaxes = b;
		setPrices = b;
		createPlots = b;
		sendChestsToLostAndFound = b;
		cityBuild = b;
		cityDestroy = b;
		citySwitch = b;
	}
	
	public boolean areAll(boolean b) {
		if(b == true) {
			if(addResident && removeResident && claim && unclaim && promote && demote &&
					withdraw && changeRankName && changeCityPlotPerms && setWelcome &&
					setMayor && setWarp && setPlotSale && setTaxes && setPrices &&
					createPlots && sendChestsToLostAndFound && cityBuild && cityDestroy &&
					citySwitch)
				return true;
			else
				return false;
		}
		else {
			if(!addResident && !removeResident && !claim && !unclaim && !promote && !demote 
					&& !withdraw && !changeRankName && !changeCityPlotPerms && 
					!setWelcome && !setMayor && !setWarp && !setPlotSale && !setTaxes && 
					!setPrices && !createPlots && !sendChestsToLostAndFound && 
					!cityBuild && !cityDestroy && !citySwitch)
				return true;
			else
				return false;
		}
	}

	public String getRankName() {
		return rankName;
	}

	public boolean isAddResident() {
		return addResident;
	}

	public boolean isChangeCityPlotPerms() {
		return changeCityPlotPerms;
	}

	public boolean isChangeRankName() {
		return changeRankName;
	}

	public boolean isCityBuild() {
		return cityBuild;
	}

	public boolean isCityDestroy() {
		return cityDestroy;
	}

	public boolean isCitySwitch() {
		return citySwitch;
	}

	public boolean isClaim() {
		return claim;
	}

	public boolean isCreatePlots() {
		return createPlots;
	}

	public boolean isDemote() {
		return demote;
	}

	public boolean isPromote() {
		return promote;
	}

	public boolean isRemoveResident() {
		return removeResident;
	}

	public boolean isSendChestsToLostAndFound() {
		return sendChestsToLostAndFound;
	}

	public boolean isSetMayor() {
		return setMayor;
	}

	public boolean isSetName() {
		return setName;
	}

	public boolean isSetPlotSale() {
		return setPlotSale;
	}

	public boolean isSetPrices() {
		return setPrices;
	}

	public boolean isSetTaxes() {
		return setTaxes;
	}

	public boolean isSetWarp() {
		return setWarp;
	}

	public boolean isSetWelcome() {
		return setWelcome;
	}

	public boolean isUnclaim() {
		return unclaim;
	}

	public boolean isWithdraw() {
		return withdraw;
	}

	public void setAddResident(boolean addResident) {
		this.addResident = addResident;
	}

	public void setChangeCityPlotPerms(boolean changeCityPlotPerms) {
		this.changeCityPlotPerms = changeCityPlotPerms;
	}

	public void setChangeRankName(boolean changeRankName) {
		this.changeRankName = changeRankName;
	}

	public void setCityBuild(boolean cityBuild) {
		this.cityBuild = cityBuild;
	}

	public void setCityDestroy(boolean cityDestroy) {
		this.cityDestroy = cityDestroy;
	}

	public void setCitySwitch(boolean citySwitch) {
		this.citySwitch = citySwitch;
	}

	public void setClaim(boolean claim) {
		this.claim = claim;
	}

	public void setCreatePlots(boolean createPlots) {
		this.createPlots = createPlots;
	}

	public void setDemote(boolean demote) {
		this.demote = demote;
	}

	public void setPromote(boolean promote) {
		this.promote = promote;
	}

	public void setRankName(String rankName) {
		this.rankName = rankName;
	}

	public void setRemoveResident(boolean removeResident) {
		this.removeResident = removeResident;
	}

	public void setSendChestsToLostAndFound(boolean sendChestsToLostAndFound) {
		this.sendChestsToLostAndFound = sendChestsToLostAndFound;
	}

	public void setSetMayor(boolean setMayor) {
		this.setMayor = setMayor;
	}

	public void setSetName(boolean setName) {
		this.setName = setName;
	}

	public void setSetPlotSale(boolean setPlotSale) {
		this.setPlotSale = setPlotSale;
	}

	public void setSetPrices(boolean setPrices) {
		this.setPrices = setPrices;
	}

	public void setSetTaxes(boolean setTaxes) {
		this.setTaxes = setTaxes;
	}

	public void setSetWarp(boolean setWarp) {
		this.setWarp = setWarp;
	}

	public void setSetWelcome(boolean setWelcome) {
		this.setWelcome = setWelcome;
	}

	public void setUnclaim(boolean unclaim) {
		this.unclaim = unclaim;
	}

	public void setWithdraw(boolean withdraw) {
		this.withdraw = withdraw;
	}
}
