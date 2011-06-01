package com.sethcran.cityscape;

import java.util.ArrayList;

import gnu.trove.TIntProcedure;

public class TreeProcedure implements TIntProcedure {
	
	ArrayList<Integer> intArray = new ArrayList<Integer>();

	@Override
	public boolean execute(int id) {
		intArray.add(id);
		return false;
	}
	
	public ArrayList<Integer> getId() {
		return intArray;
	}

}
