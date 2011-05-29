package com.sethcran.cityscape.database;

import java.sql.Connection;

import com.sethcran.cityscape.Settings;

public class CSPlots extends Table {

	public CSPlots(Connection con, Settings settings) {
		super(con, settings);
	}

}
