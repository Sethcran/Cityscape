package com.sethcran.cityscape.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CSPlayerCityData {
	Connection con = null;
	
	public CSPlayerCityData(Connection con) {
		this.con = con;
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
			e.printStackTrace();
		}
		
		return currentCity;
	}
}
