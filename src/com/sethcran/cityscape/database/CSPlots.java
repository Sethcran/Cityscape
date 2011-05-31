package com.sethcran.cityscape.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import com.sethcran.cityscape.Plot;
import com.sethcran.cityscape.Settings;

public class CSPlots extends Table {

	public CSPlots(Connection con, Settings settings) {
		super(con, settings);
	}
	
	HashMap<String, Plot> getPlots(String cityName) {
		String sql = 	"SELECT * " +
						"FROM csplots " +
						"WHERE city = ?;";
		HashMap<String, Plot> plotMap = new HashMap<String, Plot>();
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
				
				sql = 	"SELECT * " +
						"FROM csplotpermissions " +
						"WHERE xmin = ?, xmax = ?, zmin = ?, zmax = ?;";
				stmt = con.prepareStatement(sql);
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
				plotMap.put(plot.toString(), plot);
			}
		} catch (SQLException e) {
			if(settings.debug)
				e.printStackTrace();
		}
		return plotMap;
	}

}
