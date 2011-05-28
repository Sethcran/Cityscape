package com.sethcran.cityscape.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.sethcran.cityscape.Settings;

public class CSPlayerCityData {
	Connection con = null;
	Settings settings = null;
	
	public CSPlayerCityData(Connection con, Settings settings) {
		this.con = con;
		this.settings = settings;
	}
	
	public String getCurrentCity(String playerName) {
		String sql = 	"SELECT city " +
						"FROM csplayercitydata " +
						"WHERE player = ? " +
						"GROUP BY player " +
						"HAVING MAX(startingfrom);";
	
		String currentCity = null;
		
		try {
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, playerName);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()) {
				currentCity = rs.getString("city");
				if(currentCity.equalsIgnoreCase("NULL"))
					currentCity = null;
			}
		} catch (SQLException e) {
			if(settings.debug)
				e.printStackTrace();
		}
		
		return currentCity;
	}
	
	public boolean addPlayerToCity(String playerName, String cityName) {
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
			return false;
		}
		
		return true;
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
