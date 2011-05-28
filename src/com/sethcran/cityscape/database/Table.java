package com.sethcran.cityscape.database;

import java.sql.Connection;

import com.sethcran.cityscape.Settings;

public abstract class Table {
	protected Connection con;
	protected Settings settings;
	
	public Table(Connection con, Settings settings) {
		this.con = con;
		this.settings = settings;
	}
}
