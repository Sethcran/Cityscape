package com.sethcran.cityscape;

public class Permissions {
	private boolean canBuild = false;
	private boolean canDestroy = false;
	private boolean canSwitch = false;
	
	public Permissions(boolean canBuild, boolean canDestroy, boolean canSwitch) {
		this.canBuild = canBuild;
		this.canDestroy = canDestroy;
		this.canSwitch = canSwitch;
	}

	public boolean isCanBuild() {
		return canBuild;
	}

	public boolean isCanDestroy() {
		return canDestroy;
	}

	public boolean isCanSwitch() {
		return canSwitch;
	}

	public void setCanBuild(boolean canBuild) {
		this.canBuild = canBuild;
	}

	public void setCanDestroy(boolean canDestroy) {
		this.canDestroy = canDestroy;
	}

	public void setCanSwitch(boolean canSwitch) {
		this.canSwitch = canSwitch;
	}
}
