package com.sethcran.cityscape.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.sethcran.cityscape.Settings;

public class CSPlayerCityData extends Table {
	
	public CSPlayerCityData(Connection con, Settings settings) {
		super(con, settings);
	}
	
	public void addPlayerToCity(String playerName, String cityName) {
		String sql = 	"INSERT INTO csplayercitydata " +
						"VALUES( ?, ?, NOW());";
		try {
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, playerName);
			stmt.setString(2, cityName);
			stmt.executeUpdate();
		} catch (SQLException e) {
			if(settings.debug)
				e.printStackTrace();
		}
	}
	
	public String getCurrentCity(String playerName) {
		String sql = 	"SELECT DISTINCT city " +
						"FROM csplayercitydata t1" +
						"WHERE startingfrom = ( " +
							"SELECT MAX(startingfrom) " +
							"FROM csplayercitydata t2 " +
							"WHERE player = ? " +
							"GROUP BY player));";
	
		String currentCity = null;
		
		try {
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, playerName);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()) {
				currentCity = rs.getString("city");
			}
		} catch (SQLException e) {
			if(settings.debug)
				e.printStackTrace();
		}
		
		return currentCity;
	}
	
	public boolean removePlayerFromCity(String playerName) {
		String sql = 	"INSERT INTO csplayercitydata " +
						"VALUES( ?, ?, NOW());";
		try {
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, playerName);
			stmt.setString(2, null);
			stmt.executeUpdate();
		} catch (SQLException e) {
			if(settings.debug)
				e.printStackTrace();
			return false;
		}
		
		return true;
	}
}
