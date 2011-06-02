package com.sethcran.cityscape.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.sethcran.cityscape.Settings;

public class CSInvites extends Table {

	public CSInvites(Connection con, Settings settings) {
		super(con, settings);
	}
	
	public void addInvite(String player, String city) {
		String sql = 	"INSERT INTO csinvites " +
						"VALUES(?, ?, NOW());";
		try {
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, player);
			stmt.setString(2, city);
			stmt.executeUpdate();
		} catch (SQLException e) {
			if(settings.debug)
				e.printStackTrace();
		}
	}
	
	public void deleteOldInvites() {
		String sql = 	"DELETE FROM csinvites " +
						"WHERE day < DATE_SUB(NOW(), INTERVAL 1 WEEK);";
		try {
			con.createStatement().executeUpdate(sql);
		} catch (SQLException e) {
			if(settings.debug)
				e.printStackTrace();
		}
	}
	
	public ArrayList<String> getInvites(String player) {
		String sql = 	"SELECT city " +
						"FROM csinvites " +
						"WHERE player = ?;";
		ArrayList<String> invites = new ArrayList<String>();
		try {
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, player);
			ResultSet rs = stmt.executeQuery();
			while(rs.next())
				invites.add(rs.getString("city"));
		} catch (SQLException e) {
			if(settings.debug)
				e.printStackTrace();
		}
		return invites;
	}
	
	public boolean isInvited(String player, String city) {
		String sql = 	"SELECT * " +
						"FROM csinvites " +
						"WHERE player = ? AND city = ?;";
		try {
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, player);
			stmt.setString(2, city);
			ResultSet rs = stmt.executeQuery();
			if(rs.next())
				return true;
		} catch (SQLException e) {
			if(settings.debug)
				e.printStackTrace();
		}
		return false;
	}
	
	public void removeInvite(String player, String city) {
		String sql = 	"DELETE FROM csinvites " +
						"WHERE player = ? AND city = ?;";
		try {
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, player);
			stmt.setString(2, city);
			stmt.executeUpdate();
		} catch (SQLException e) {
			if(settings.debug)
				e.printStackTrace();
		}
	}
}