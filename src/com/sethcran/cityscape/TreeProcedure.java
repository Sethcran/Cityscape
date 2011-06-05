package com.sethcran.cityscape;

import gnu.trove.TIntProcedure;

import java.util.ArrayList;

public class TreeProcedure implements TIntProcedure {
	
	ArrayList<Integer> intArray = new ArrayList<Integer>();
	
	String name = null;
	
	public TreeProcedure() {
		
	}
	
	public TreeProcedure(String name) {
		this.name = name;
	}

	@Override
	public boolean execute(int id) {
		intArray.add(id);
		return true;
	}
	
	public ArrayList<Integer> getId() {
		return intArray;
	}

}
