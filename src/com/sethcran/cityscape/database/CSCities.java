package com.sethcran.cityscape.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.sethcran.cityscape.Constants;
import com.sethcran.cityscape.Settings;

public class CSCities {
	Connection con = null;
	Settings settings = null;
	
	public CSCities(Connection con, Settings settings) {
		this.con = con;
		this.settings = settings;
	}
	
	public boolean doesCityExist(String cityName) {
		String sql = 	"SELECT * " +
						"FROM cscities " +
						"WHERE name = ?;";
		
		try {
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, cityName);
			ResultSet rs = stmt.executeQuery();
			if(rs.next())
				return true;
		} catch (SQLException e) {
			if(settings.debug)
			e.printStackTrace();
		}
		
		return false;
	}
	
	public boolean createCity(String playerName, String cityName) {
		String sql = 	"INSERT INTO cscities " +
						"VALUES( ?, ?, ?, NOW(), ?, ?, null);";
		try {
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, cityName);
			stmt.setString(2, playerName);
			stmt.setInt(3, Constants.DEFAULT_TOWN_RANK);
			stmt.setInt(4, settings.defaultBaseClaims);
			stmt.setInt(5, 0);
			stmt.executeUpdate();
		} catch (SQLException e) {
			if(settings.debug)
				e.printStackTrace();
			return false;
		}
		return true;
	}
}
