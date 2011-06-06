package com.sethcran.cityscape.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.sethcran.cityscape.Settings;

public class CSCityBanList extends Table {

	public CSCityBanList(Connection con, Settings settings) {
		super(con, settings);
	}
	
	public void ban(String city, String player) {
		String sql = 	"INSERT INTO cscitybanlist VALUES(" +
						"?, ?);";
		try {
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, city);
			stmt.setString(2, player);
			stmt.executeUpdate();
		} catch (SQLException e) {
			if(settings.debug)
				e.printStackTrace();
		}
	}
	
	public ArrayList<String> getBans(String city) {
		String sql = 	"SELECT player " +
						"FROM cscitybanlist " +
						"WHERE city = ?;";
		ArrayList<String> banList = new ArrayList<String>();
		try {
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, city);
			ResultSet rs = stmt.executeQuery();
			while(rs.next())
				banList.add(rs.getString("player"));
		} catch (SQLException e) {
			if(settings.debug)
				e.printStackTrace();
		}
		return banList;
	}
	
	public void unban(String city, String player) {
		String sql = 	"DELETE FROM cscitybanlist " +
						"WHERE city = ? " +
						"AND player = ?;";
		try {
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, city);
			stmt.setString(2, player);
			stmt.executeUpdate();
		} catch (SQLException e) {
			if(settings.debug)
				e.printStackTrace();
		}
	}

}
