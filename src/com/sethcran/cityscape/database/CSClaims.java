package com.sethcran.cityscape.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.sethcran.cityscape.Settings;

public class CSClaims extends Table {

	public CSClaims(Connection con, Settings settings) {
		super(con, settings);
	}
	
	public void claimChunk(String cityName, int x, int z) {
		String sql = 	"INSERT INTO csclaims " +
						"VALUES( POINT(?, ?), ?);";
		try {
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setInt(1, x);
			stmt.setInt(2, z);
			stmt.setString(3, cityName);
			stmt.executeUpdate();
		} catch (SQLException e) {
			if(settings.debug)
				e.printStackTrace();
		}
	}
	
	public String getCityAt(int x, int z) {
		String sql = 	"SELECT city " +
						"FROM csclaims " +
						"WHERE loc = POINT(?, ?);";
		try {
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setInt(1, x);
			stmt.setInt(1, z);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()) {
				return rs.getString("city");
			}
			else
				return null;
		} catch (SQLException e) {
			if(settings.debug)
				e.printStackTrace();
		}
		return null;
	}
	
	public boolean isChunkClaimed(int x, int z) {
		String sql = 	"SELECT * " +
						"FROM csclaims " +
						"WHERE loc = POINT(?, ?);";
		try {
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setInt(1, x);
			stmt.setInt(2, z);
			ResultSet rs = stmt.executeQuery();
			if(rs.next())
				return true;
			else
				return false;
		} catch (SQLException e) {
			if(settings.debug)
				e.printStackTrace();
		}
		return true;
	}
}
