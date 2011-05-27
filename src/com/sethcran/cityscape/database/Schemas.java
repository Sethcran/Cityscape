package com.sethcran.cityscape.database;

import java.sql.Connection;

public class Schemas {
	private Connection con = null;
	
	public Schemas(Connection con) {
		this.con = con;
	}
	
	public void createCSDatabase() {
		createCitiesTable();
		createPlayersTable();
		createChestsTable();
		createClaimsTable();
		createPlotsTable();
		createPlayerTownDataTable();
	}
	
	public void createCitiesTable() {
		
	}
	
	public void createPlayersTable() {
		
	}
	
	public void createChestsTable() {
		
	}
	
	public void createClaimsTable() {
		
	}
	
	public void createPlotsTable() {
		
	}
	
	public void createPlayerTownDataTable() {
		
	}
}
