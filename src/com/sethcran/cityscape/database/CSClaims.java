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
						"VALUES(?, ?, ?, ?);";
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
	
	public void claimMany(ArrayList<Claim> claims) {
		String sql = 	"INSERT INTO csclaims " +
						"VALUES(";
		int count = 1;
		boolean first = true;
		for(int i = 0; i < claims.size(); i++) {
			if(first) {
				sql += "?, ?, ?, ?)";
				first = false;
			}
			else
				sql += ",(?, ?, ?, ?)";
		}
		sql += ";";
		try {
			PreparedStatement stmt = con.prepareStatement(sql);
			for(Claim c : claims) {
				stmt.setString(count, c.getCityName());
				stmt.setString(count + 1, c.getWorld());
				stmt.setInt(count + 2, c.getX());
				stmt.setInt(count + 3, c.getZ());
				count += 4;
			}
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
						rs.getInt("z"));
				claimList.add(claim);
			}
		} catch (SQLException e) {
			if(settings.debug)
				e.printStackTrace();
		}
		return claimList;
	}
	
	public void unclaimAll(String city, Claim claim) {
		String sql = 	"DELETE FROM csclaims " +
						"WHERE city = ?;";
		try {
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, city);
			stmt.executeUpdate();
			claimChunk(city, claim.getWorld(), claim.getX(), claim.getZ());
		} catch (SQLException e) {
			if(settings.debug)
				e.printStackTrace();
		}
	}
	
	public void unclaimChunk(Claim claim) {
		String sql = 	"DELETE FROM csclaims " +
						"WHERE x = ? " +
						"AND z = ?;";
		try {
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setInt(1, claim.getX());
			stmt.setInt(2, claim.getZ());
			stmt.executeUpdate();
		} catch (SQLException e) {
			if(settings.debug)
				e.printStackTrace();
		}
	}
}
