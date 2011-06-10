package com.sethcran.cityscape.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.sethcran.cityscape.Permissions;
import com.sethcran.cityscape.Plot;
import com.sethcran.cityscape.Settings;

public class CSPlots extends Table {

	public CSPlots(Connection con, Settings settings) {
		super(con, settings);
	}
	
	public void addPlot(Plot plot) {
		String sql = 	"INSERT INTO csplots VALUES(" +
						"?, ?, ?, ?, ?, ?, " +
						"?, ?, ?, ?, ?, ?, ?, ?, ?, ?, null)";
		try {
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, plot.getCityName());
			stmt.setString(2, plot.getOwnerName());
			stmt.setInt(3, plot.getXmin());
			stmt.setInt(4, plot.getZmin());
			stmt.setInt(5, plot.getXmax());
			stmt.setInt(6, plot.getZmax());
			stmt.setBoolean(7, plot.isResidentBuild());
			stmt.setBoolean(8, plot.isResidentDestroy());
			stmt.setBoolean(9, plot.isResidentSwitch());
			stmt.setBoolean(10, plot.isOutsiderBuild());
			stmt.setBoolean(11, plot.isOutsiderDestroy());
			stmt.setBoolean(12, plot.isOutsiderSwitch());
			stmt.setBoolean(13, plot.isCityPlot());
			stmt.setBoolean(14, plot.isSnow());
			stmt.setBoolean(15, plot.isForSale());
			stmt.setInt(16, plot.getPrice());
			stmt.executeUpdate();			
		} catch (SQLException e) {
			if(settings.debug)
				e.printStackTrace();
		}
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
	
	public ArrayList<Plot> getPlots(String cityName) {
		String sql = 	"SELECT * " +
						"FROM csplots " +
						"WHERE city = ?;";
		ArrayList<Plot> plotList = new ArrayList<Plot>();
		try {
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, cityName);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				Plot plot = new Plot(rs.getInt("xmin"), rs.getInt("zmin"), 
						rs.getInt("xmax"), rs.getInt("zmax"));
				plot.setCityName(rs.getString("city"));
				plot.setOutsiderBuild(rs.getBoolean("outsiderBuild"));
				plot.setOutsiderDestroy(rs.getBoolean("outsiderDestroy"));
				plot.setOutsiderSwitch(rs.getBoolean("outsiderSwitch"));
				plot.setOwnerName(rs.getString("owner"));
				plot.setResidentBuild(rs.getBoolean("residentBuild"));
				plot.setResidentDestroy(rs.getBoolean("residentDestroy"));
				plot.setResidentSwitch(rs.getBoolean("residentSwitch"));
				plot.setCityPlot(rs.getBoolean("cityPlot"));
				plot.setId(rs.getInt("id"));
				plot.setSnow(rs.getBoolean("snow"));
				plot.setForSale(rs.getBoolean("forSale"));
				plot.setPrice(rs.getInt("price"));
				
				sql = 	"SELECT * " +
						"FROM csplotpermissions " +
						"WHERE id = ?;";
				stmt = con.prepareStatement(sql);
				stmt.setInt(1, plot.getId());
				ResultSet permResults = stmt.executeQuery();
				while(permResults.next()) {
					if(permResults.getBoolean("isPlayer")) {
						plot.insertIntoPlayerPermissions(permResults.getString("name"),
								new Permissions(permResults.getBoolean("build"), 
								permResults.getBoolean("destroy"), 
								permResults.getBoolean("switch")));
					}
					else {
						plot.insertIntoCityPermissions(permResults.getString("name"),
								new Permissions(permResults.getBoolean("build"), 
								permResults.getBoolean("destroy"), 
								permResults.getBoolean("switch")));
					}
				}
				plotList.add(plot);
			}
		} catch (SQLException e) {
			if(settings.debug)
				e.printStackTrace();
		}
		return plotList;
	}
	
	public void removeAllPlotPerms(int id) {
		String sql = 	"DELETE FROM csplotpermissions " +
						"WHERE id = ?;";
		try {
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setInt(1, id);
			stmt.executeUpdate();
		} catch (SQLException e) {
			if(settings.debug)
				e.printStackTrace();
		}
	}
	
	public void removePlayer(String player) {
		String sql = 	"UPDATE csplots SET " +
						"owner = city, " +
						"price = 0 " +
						"WHERE owner = ?;";
		try {
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, player);
			stmt.executeUpdate();
		} catch (SQLException e) {
			if(settings.debug)
				e.printStackTrace();
		}
	}
	
	public void removePlot(int id) {
		String sql = 	"DELETE FROM csplots " +
						"WHERE id = ?;";
		try {
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setInt(1, id);
			stmt.executeUpdate();
		} catch (SQLException e) {
			if(settings.debug)
				e.printStackTrace();
		}
	}
	
	public void removePlotPermissions(int id, String name, boolean isPlayer) {
		String sql = 	"DELETE FROM csplotpermissions " +
						"WHERE id = ? " +
						"AND name = ? " +
						"AND isPlayer = ?;";
		try {
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setInt(1, id);
			stmt.setString(2, name);
			stmt.setBoolean(3, isPlayer);
			stmt.executeUpdate();
		} catch (SQLException e) {
			if(settings.debug)
				e.printStackTrace();
		}
	}
	
	public void setPlotForSale(Plot plot) {
		String sql = 	"UPDATE csplots SET " +
						"forSale = ?, " +
						"price = ?, " +
						"owner = ? " +
						"WHERE id = ?;";
		try {
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setBoolean(1, plot.isForSale());
			stmt.setInt(2, plot.getPrice());
			stmt.setString(3, plot.getOwnerName());
			stmt.setInt(4, plot.getId());
			stmt.executeUpdate();
		} catch (SQLException e) {
			if(settings.debug)
				e.printStackTrace();
		}
	}
	
	public void setPlotPermissions(int id, String name, Permissions perms,
			boolean isPlayer) {
		String sql = 	"UPDATE csplotpermissions " +
						"SET build = ?, " +
						"destroy = ?, switch = ? " +
						"WHERE id = ? " +
						"AND name = ? " +
						"AND isPlayer = ?;";
		try {
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setBoolean(1, perms.isCanBuild());
			stmt.setBoolean(2, perms.isCanDestroy());
			stmt.setBoolean(3, perms.isCanSwitch());
			stmt.setInt(4, id);
			stmt.setString(5, name);
			stmt.setBoolean(6, isPlayer);
			int i = stmt.executeUpdate();
			if(i == 0) {
				sql = 	"INSERT INTO csplotpermissions VALUES(" +
						"?, ?, ?, ?, ?, ?);";
				stmt = con.prepareStatement(sql);
				stmt.setInt(1, id);
				stmt.setString(2, name);
				stmt.setBoolean(3, isPlayer);
				stmt.setBoolean(4, perms.isCanBuild());
				stmt.setBoolean(5, perms.isCanDestroy());
				stmt.setBoolean(6, perms.isCanSwitch());
				stmt.executeUpdate();
			}
		} catch (SQLException e) {
			if(settings.debug)
				e.printStackTrace();
		}
	}
	
	public void updatePlotSettings(Plot plot) {
		String sql = 	"UPDATE csplots SET " +
						"residentBuild = ?, " +
						"residentDestroy = ?, " +
						"residentSwitch = ?, " +
						"outsiderBuild = ?, " +
						"outsiderDestroy = ?, " +
						"outsiderSwitch = ?, " +
						"snow = ? " +
						"WHERE id = ?;";
		try {
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setBoolean(1, plot.isResidentBuild());
			stmt.setBoolean(2, plot.isResidentDestroy());
			stmt.setBoolean(3, plot.isResidentSwitch());
			stmt.setBoolean(4, plot.isOutsiderBuild());
			stmt.setBoolean(5, plot.isOutsiderDestroy());
			stmt.setBoolean(6, plot.isOutsiderSwitch());
			stmt.setBoolean(7, plot.isSnow());
			stmt.setInt(8, plot.getId());
			stmt.executeUpdate();
		} catch (SQLException e) {
			if(settings.debug)
				e.printStackTrace();
		}
	}

}
