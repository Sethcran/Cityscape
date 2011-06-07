package com.sethcran.cityscape.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.sethcran.cityscape.Claim;
import com.sethcran.cityscape.Settings;

public class CSClaims extends Table {

	public CSClaims(Connection con, Settings settings) {
		super(con, settings);
	}
	
	public void claimChunk(String cityName, String worldName, int x, int z) {
		String sql = 	"INSERT INTO csclaims " +
						"VALUES(?, ?, ?, ?, null);";
		try {
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, cityName);
			stmt.setString(2, worldName);
			stmt.setInt(3, x);
			stmt.setInt(4, z);
			stmt.executeUpdate();
		} catch (SQLException e) {
			if(settings.debug)
				e.printStackTrace();
		}
	}
	
	public Claim getClaimAt(String world, int x, int z) {
		String sql = 	"SELECT * " +
						"FROM csclaims " +
						"WHERE x = ? AND z = ?;";
		try {
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setInt(1, x);
			stmt.setInt(2, z);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()) {
				Claim claim = new Claim();
				claim.setCityName(rs.getString("city"));
				claim.setWorld(rs.getString("world"));
				claim.setX(rs.getInt("xmin"));
				claim.setZ(rs.getInt("zmin"));
				claim.setId(rs.getInt("id"));
				return claim;
			}
		} catch (SQLException e) {
			if(settings.debug)
				e.printStackTrace();
		}
		return null;
	}
	
	public ArrayList<Claim> getClaims() {
		String sql = 	"SELECT * " +
						"FROM csclaims;";
		ArrayList<Claim> claimList = new ArrayList<Claim>();
		try {
			ResultSet rs = con.createStatement().executeQuery(sql);
			while(rs.next()) {
				Claim claim = new Claim(rs.getString("city"), 
						rs.getString("world"),
						rs.getInt("x"), 
						rs.getInt("z"),
						rs.getInt("id"));
				claimList.add(claim);
			}
		} catch (SQLException e) {
			if(settings.debug)
				e.printStackTrace();
		}
		return claimList;
	}
	
	public int getLastID() {
		String sql = 	"SELECT LAST_INSERT_ID();";
		try {
			ResultSet rs = con.createStatement().executeQuery(sql);
			if(rs.next())
				return rs.getInt(1);
		} catch (SQLException e) {
			if(settings.debug)
				e.printStackTrace();
		}
		return 0;
	}
	
	public void unclaimChunk(Claim claim) {
		String sql = 	"DELETE FROM csclaims " +
						"WHERE id = ?;";
		try {
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setInt(1, claim.getId());
			stmt.executeUpdate();
		} catch (SQLException e) {
			if(settings.debug)
				e.printStackTrace();
		}
	}
}
