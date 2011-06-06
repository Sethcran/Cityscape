package com.sethcran.cityscape.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.sethcran.cityscape.Constants;
import com.sethcran.cityscape.Settings;

public class CSPlayerCityData extends Table {
	
	public CSPlayerCityData(Connection con, Settings settings) {
		super(con, settings);
	}
	
	public boolean addPlayerCityHistory(String playerName, String cityName) {
		String sql = 	"INSERT INTO csplayercitydata " +
						"VALUES( ?, ?, NOW());";
		try {
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, playerName);
			if(cityName == null)
				stmt.setString(2, null);
			else
				stmt.setString(2, cityName);
			stmt.executeUpdate();
		} catch (SQLException e) {
			if(settings.debug)
				e.printStackTrace();
			return false;
		}
		
		return true;
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
	
	public ArrayList<String> getHistory(String player) {
		String sql = 	"SELECT city, startingfrom " +
						"FROM csplayercitydata " +
						"WHERE player = ? " +
						"ORDER BY(startingfrom) ASC;";
		ArrayList<String> history = new ArrayList<String>();
		try {
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, player);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				history.add(Constants.GROUP_COLOR + "City: " + Constants.MESSAGE_COLOR +
						(rs.getString("city") == null ? "None" : rs.getString("city")) + 
						Constants.GROUP_COLOR + "  Since: " + Constants.MESSAGE_COLOR + 
						rs.getString("startingfrom"));
			}
		} catch (SQLException e) {
			if(settings.debug)
				e.printStackTrace();
		}
		return history;
	}
}
