package com.sethcran.cityscape;

import gnu.trove.TIntProcedure;

public class TreeProcedure implements TIntProcedure {
	
	private int id = 0;

	@Override
	public boolean execute(int id) {
		this.id = id;
		return false;
	}
	
	public int getId() {
		return id;
	}

}
