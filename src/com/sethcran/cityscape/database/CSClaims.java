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
	
	public void claimChunk(String cityName, String worldName, 
			int xmin, int zmin, int xmax, int zmax) {
		String sql = 	"INSERT INTO csclaims " +
						"VALUES(?, ?, ?, ?, ?, ?, null);";
		try {
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, cityName);
			stmt.setString(2, worldName);
			stmt.setInt(3, xmin);
			stmt.setInt(4, zmin);
			stmt.setInt(5, xmax);
			stmt.setInt(6, zmax);
			stmt.executeUpdate();
		} catch (SQLException e) {
			if(settings.debug)
				e.printStackTrace();
		}
	}
	
	public Claim getClaimAt(String world, int xmin, int zmin, int xmax, int zmax) {
		String sql = 	"SELECT * " +
						"FROM csclaims " +
						"WHERE xmin = ? AND zmin = ? " +
						"AND xmax = ? AND zmax = ?;";
		try {
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setInt(1, xmin);
			stmt.setInt(2, zmin);
			stmt.setInt(3, xmax);
			stmt.setInt(4, zmax);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()) {
				Claim claim = new Claim();
				claim.setCityName(rs.getString("city"));
				claim.setWorld(rs.getString("world"));
				claim.setXmin(rs.getInt("xmin"));
				claim.setZmin(rs.getInt("zmin"));
				claim.setXmax(rs.getInt("xmax"));
				claim.setZmax(rs.getInt("zmax"));
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
						rs.getInt("xmin"), 
						rs.getInt("zmin"), 
						rs.getInt("xmax"), 
						rs.getInt("zmax"), 
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
	
	public void renameCity(String oldName, String newName) {
		String sql = 	"UPDATE csclaims SET " +
						"city = ? " +
						"WHERE city = ?;";
		try {
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, newName);
			stmt.setString(2, oldName);
			stmt.executeUpdate();
		} catch (SQLException e) {
			if(settings.debug)
				e.printStackTrace();
		}
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
