package com.sethcran.cityscape.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.sethcran.cityscape.Settings;

public class CSResidents extends Table {

	public CSResidents(Connection con, Settings settings) {
		super(con, settings);
	}
	
	public String getCurrentCity(String playerName) {
		String sql = 	"SELECT city " +
						"FROM csresidents " +
						"WHERE player = ?;";
		try {
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, playerName);
			ResultSet rs = stmt.executeQuery();
			if(rs.next())
				return rs.getString("city");
			else
				return null;
		} catch (SQLException e) {
			if(settings.debug)
				e.printStackTrace();
		}
		return null;
	}
	
	public void insertNewPlayer(String playerName) {
		String sql = 	"INSERT INTO csresidents " +
						"VALUES(?, null);";
		try {
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, playerName);
			stmt.executeUpdate();
		} catch (SQLException e) {
			if(settings.debug)
				e.printStackTrace();
		}
	}
	
	public void setCurrentCity(String playerName, String cityName) {
		String sql = 	"UPDATE csresidents " +
						"SET city = ? " +
						"WHERE player = ?;";
		try {
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, cityName);
			stmt.setString(2, playerName);
			stmt.executeUpdate();
		} catch (SQLException e) {
			if(settings.debug)
				e.printStackTrace();
		}
	}

}
