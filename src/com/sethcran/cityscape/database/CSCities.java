package com.sethcran.cityscape.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.sethcran.cityscape.City;
import com.sethcran.cityscape.Constants;
import com.sethcran.cityscape.Settings;

public class CSCities extends Table {
	
	public CSCities(Connection con, Settings settings) {
		super(con, settings);
	}
	
	public void addUsedClaims(String cityName, int numClaims) {
		String sql = 	"UPDATE cscities " +
						"SET usedClaims = usedClaims + ? " +
						"WHERE name = ?;";
		try {
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setInt(1, numClaims);
			stmt.setString(2, cityName);
			stmt.executeUpdate();
		} catch (SQLException e) {
			if(settings.debug)
				e.printStackTrace();
		}
	}
	
	public void createCity(String playerName, String cityName) {
		String sql = 	"INSERT INTO cscities " +
						"VALUES( ?, ?, ?, NOW(), ?, ?, ?, null, null, null, " +
						"?, ?, ?, ?, ?, ?);";
		try {
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, cityName);
			stmt.setString(2, playerName);
			stmt.setInt(3, Constants.DEFAULT_TOWN_RANK);
			stmt.setInt(4, 1);
			stmt.setInt(5, settings.defaultBaseClaims);
			stmt.setInt(6, 0);
			stmt.setBoolean(7, settings.defaultResidentBuild);
			stmt.setBoolean(8, settings.defaultResidentDestroy);
			stmt.setBoolean(9, settings.defaultResidentSwitch);
			stmt.setBoolean(10, settings.defaultOutsiderBuild);
			stmt.setBoolean(11, settings.defaultOutsiderDestroy);
			stmt.setBoolean(12, settings.defaultOutsiderSwitch);
			stmt.executeUpdate();
		} catch (SQLException e) {
			if(settings.debug)
				e.printStackTrace();
		}
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
	
	public ResultSet getCities() {
		String sql = 	"Select * " +
						"FROM cscities; ";
		try {
			ResultSet rs = con.createStatement().executeQuery(sql);
			return rs;
		} catch(SQLException e) {
			if(settings.debug)
				e.printStackTrace();
		}
		return null;
	}
	
	public City getCity(String cityName) {
		String sql = 	"SELECT * " +
						"FROM cscities " +
						"WHERE name = ?;";
		try {
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, cityName);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()) {
				City city = new City();
				city.setBaseClaims(rs.getInt("baseClaims"));
				city.setBonusClaims(rs.getInt("bonusClaims"));
				city.setFounded(rs.getString("founded"));
				city.setMayor(rs.getString("mayor"));
				city.setName(rs.getString("name"));
				city.setOutsiderBuild(rs.getBoolean("outsiderBuild"));
				city.setOutsiderDestroy(rs.getBoolean("outsiderDestroy"));
				city.setOutsiderSwitch(rs.getBoolean("outsiderSwitch"));
				city.setRank(rs.getInt("rank"));
				city.setResidentBuild(rs.getBoolean("residentBuild"));
				city.setResidentDestroy(rs.getBoolean("residentDestroy"));
				city.setResidentSwitch(rs.getBoolean("residentSwitch"));
				city.setSpawnX(rs.getInt("spawnX"));
				city.setSpawnY(rs.getInt("spawnY"));
				city.setSpawnZ(rs.getInt("spawnZ"));
				city.setUsedClaims(rs.getInt("usedClaims"));
				return city;
			}
		} catch(SQLException e) {
			if(settings.debug)
				e.printStackTrace();
		}
		return null;
	}
	
	public boolean hasClaims(String cityName, int numClaims) {
		String sql = 	"SELECT usedClaims, baseClaims, bonusClaims " +
						"FROM cscities " +
						"WHERE name = ?;";
		try {
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, cityName); 
			ResultSet rs = stmt.executeQuery();
			if(rs.next()) {
				int usedClaims = rs.getInt("usedClaims");
				int baseClaims = rs.getInt("baseClaims");
				int bonusClaims = rs.getInt("bonusClaims");
				if(numClaims + usedClaims <= baseClaims + bonusClaims)
					return true;
			}
			else
				return false; 	// ERROR!
		} catch (SQLException e) {
			if(settings.debug)
				e.printStackTrace();
		}
		
		return false;
	}
}
