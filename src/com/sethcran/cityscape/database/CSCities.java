package com.sethcran.cityscape.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.sethcran.cityscape.City;
import com.sethcran.cityscape.Cityscape;
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
						"VALUES( ?, ?, null, ?, NOW(), ?, ?, ?, null, null, null, null, " +
						"null, null, ?, ?, ?, ?, ?, ?, ?);";
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
			stmt.setBoolean(13, settings.defaultSnow);
			stmt.executeUpdate();
		} catch (SQLException e) {
			if(settings.debug)
				e.printStackTrace();
		}
	}
	
	public void deleteCity(String city) {
		String sql = 	"DELETE FROM cscities " +
						"WHERE name = ?;";
		try {
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, city);
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
				city.setWelcome(rs.getString("welcome"));
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
				city.setSnow(rs.getBoolean("snow"));
				city.setWorld(rs.getString("world"));
				return city;
			}
		} catch(SQLException e) {
			if(settings.debug)
				e.printStackTrace();
		}
		return null;
	}
	
	public ArrayList<String> getCityNames() {
		String sql = 	"SELECT name " +
						"FROM cscities;";
		ArrayList<String> cityList = new ArrayList<String>();
		try {
			ResultSet rs = con.createStatement().executeQuery(sql);
			while(rs.next())
				cityList.add(rs.getString("name"));
		} catch (SQLException e) {
			if(settings.debug)
				e.printStackTrace();
		}
		return cityList;
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
	
	public void removeUsedClaims(String city, int numClaims) {
		String sql = 	"UPDATE cscities " +
						"SET usedClaims = usedClaims - ? " +
						"WHERE name = ?;";
		try {
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setInt(1, numClaims);
			stmt.setString(2, city);
			stmt.executeUpdate();
		} catch (SQLException e) {
			if(settings.debug)
				e.printStackTrace();
		}
	}
	
	public void renameCity(String oldName, String newName) {
		String sql = 	"UPDATE cscities SET " +
						"name = ? " +
						"WHERE name = ?;";
		try {
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, newName);
			stmt.setString(2, oldName);
			stmt.executeUpdate();
			Cityscape.log.info(oldName + " Done updating cities. " + newName);
		} catch (SQLException e) {
			if(settings.debug)
				e.printStackTrace();
		}
	}
	
	public void setBonusClaims(String city, int bonusClaims) {
		String sql = 	"UPDATE cscities SET " +
						"bonusClaims = ? " +
						"WHERE name = ?;";
		try {
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setInt(1, bonusClaims);
			stmt.setString(2, city);
			stmt.executeUpdate();
		} catch (SQLException e) {
			if(settings.debug)
				e.printStackTrace();
		}
	}
	
	public void setUsedClaims(String city, int usedClaims) {
		String sql = 	"UPDATE cscities SET " +
						"usedClaims = ? " +
						"WHERE name = ?;";
		try {
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setInt(1, usedClaims);
			stmt.setString(2, city);
			stmt.executeUpdate();
		} catch (SQLException e) {
			if(settings.debug)
				e.printStackTrace();
		}
	}
	
	public void setWarp(City city) {
		String sql = 	"UPDATE cscities SET " +
						"spawnX = ?, " +
						"spawnY = ?, " +
						"spawnZ = ?, " +
						"spawnPitch = ?, " +
						"spawnYaw = ?, " +
						"world = ? " +
						"WHERE name = ?;";
		try {
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setDouble(1, city.getSpawnX());
			stmt.setDouble(2, city.getSpawnY());
			stmt.setDouble(3, city.getSpawnZ());
			stmt.setFloat(4, city.getSpawnPitch());
			stmt.setFloat(5, city.getSpawnYaw());
			stmt.setString(6, city.getWorld());
			stmt.setString(7, city.getName());
			stmt.executeUpdate();
		} catch (SQLException e) {
			if(settings.debug)
				e.printStackTrace();
		}
	}
	
	public void setWelcome(String city, String welcome) {
		String sql = 	"UPDATE cscities SET " +
						"welcome = ? " +
						"WHERE name = ?;";
		try {
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, welcome);
			stmt.setString(2, city);
			stmt.executeUpdate();
		} catch (SQLException e) {
			if(settings.debug)
				e.printStackTrace();
		}
	}
	
	public void updateCitySettings(City city) {
		String sql = 	"UPDATE cscities SET " +
						"residentBuild = ?, " +
						"residentDestroy = ?, " +
						"residentSwitch = ?, " +
						"outsiderBuild = ?, " +
						"outsiderDestroy = ?, " +
						"outsiderSwitch = ?, " +
						"snow = ? " +
						"WHERE name = ?;";
		try {
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setBoolean(1, city.isResidentBuild());
			stmt.setBoolean(2, city.isResidentDestroy());
			stmt.setBoolean(3, city.isResidentSwitch());
			stmt.setBoolean(4, city.isOutsiderBuild());
			stmt.setBoolean(5, city.isOutsiderDestroy());
			stmt.setBoolean(6, city.isOutsiderSwitch());
			stmt.setBoolean(7, city.isSnow());
			stmt.setString(8, city.getName());
			stmt.executeUpdate();
		} catch (SQLException e) {
			if(settings.debug)
				e.printStackTrace();
		}
						
	}
	
	public void updateMayor(String city, String player) {
		String sql = 	"UPDATE cscities SET " + 
						"mayor = ? " +
						"WHERE name = ?;";
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
