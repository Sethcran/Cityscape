package com.sethcran.cityscape.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.sethcran.cityscape.Plot;
import com.sethcran.cityscape.Settings;

public class CSPlots extends Table {

	public CSPlots(Connection con, Settings settings) {
		super(con, settings);
	}
	
	public void addPlot(Plot plot) {
		String sql = 	"INSERT INTO csplots VALUES(" +
						"?, ?, ?, ?, ?, ?, " +
						"?, ?, ?, ?, ?, ?, null)";
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
				plot.setId(rs.getInt("id"));
				
				sql = 	"SELECT * " +
						"FROM csplotpermissions " +
						"WHERE id = ?;";
				stmt = con.prepareStatement(sql);
				stmt.setInt(1, plot.getId());
				ResultSet permResults = stmt.executeQuery();
				while(permResults.next()) {
					if(permResults.getBoolean("isPlayer")) {
						plot.insertIntoPlayerPermissions(permResults.getString("name"),
								permResults.getBoolean("build"), 
								permResults.getBoolean("destroy"), 
								permResults.getBoolean("switch"));
					}
					else {
						plot.insertIntoCityPermissions(permResults.getString("name"),
								permResults.getBoolean("build"), 
								permResults.getBoolean("destroy"), 
								permResults.getBoolean("switch"));
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

}
