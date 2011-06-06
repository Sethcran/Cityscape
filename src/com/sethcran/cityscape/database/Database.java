package com.sethcran.cityscape.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.sethcran.cityscape.City;
import com.sethcran.cityscape.Cityscape;
import com.sethcran.cityscape.Claim;
import com.sethcran.cityscape.Permissions;
import com.sethcran.cityscape.PlayerCache;
import com.sethcran.cityscape.Plot;
import com.sethcran.cityscape.RankPermissions;
import com.sethcran.cityscape.Settings;

public class Database {
	private Connection con = null;
	
	private Settings settings = null;
	
	private CSCities cscities = null;
	private CSCityBanList cscitybanlist = null;
	private CSClaims csclaims = null;
	private CSInvites csinvites = null;
	private CSPlayers csplayers = null;
	private CSPlayerCityData csplayercitydata = null;
	private CSPlots csplots = null;
	private CSRanks csranks = null;
	private CSResidents csresidents = null;
	
	public Database(Cityscape plugin) {		
		settings = plugin.getSettings();
		
		try {
			Class.forName(settings.databaseDriver);
			con = DriverManager.getConnection(settings.databaseUrl, 
					settings.databaseUsername, settings.databasePassword);
		} catch(ClassNotFoundException e) {
			if(settings.debug)
				e.printStackTrace();
		} catch(SQLException e) {
			if(settings.debug)
				e.printStackTrace();
		}
		
		String sql = "show tables like 'CSCities';";
		try {
			ResultSet rs = con.createStatement().executeQuery(sql);
			if(!rs.next()) {
				Schemas schemas = new Schemas(con);
				schemas.createCSDatabase();
			}
		} catch (SQLException e) {
			if(settings.debug)
				e.printStackTrace();
		}
		
		cscities = new CSCities(con, plugin.getSettings());
		cscitybanlist = new CSCityBanList(con, plugin.getSettings());
		csclaims = new CSClaims(con, plugin.getSettings());
		csinvites = new CSInvites(con, plugin.getSettings());
		csplayers = new CSPlayers(con, plugin.getSettings());
		csplayercitydata = new CSPlayerCityData(con, plugin.getSettings());
		csplots = new CSPlots(con, plugin.getSettings());
		csranks = new CSRanks(con, plugin.getSettings());
		csresidents = new CSResidents(con, plugin.getSettings());
	}
	
	public void addInvite(String player, String city) {
		csinvites.addInvite(player, city);
	}
	
	public void addPlayerToCity(String player, String city) {
		try {
			con.setAutoCommit(false);
			csresidents.setCurrentCity(player, city, null);
			csplayercitydata.addPlayerCityHistory(player, city);
			con.commit();
			con.setAutoCommit(true);
		} catch (SQLException e) {
			if(settings.debug)
				e.printStackTrace();
		}
	}
	
	public void addPlot(Plot plot) {
		csplots.addPlot(plot);
	}
	
	public void ban(String city, String player) {
		cscitybanlist.ban(city, player);
	}
	
	public void claimChunk(String cityName, String worldName,
			int xmin, int zmin, int xmax, int zmax) {
		try {
			con.setAutoCommit(false);
			csclaims.claimChunk(cityName, worldName, xmin, zmin, xmax, zmax);
			cscities.addUsedClaims(cityName, 1);
			con.commit();
			con.setAutoCommit(true);
		} catch(SQLException e) {
			if(settings.debug)
				e.printStackTrace();
		}
	}

	public void createCity(String playerName, String cityName, String worldName,
			int xmin, int zmin, int xmax, int zmax ) {
		try {
			con.setAutoCommit(false);
			cscities.createCity(playerName, cityName);
			csclaims.claimChunk(cityName, worldName, xmin, zmin, xmax, zmax);
			csplayercitydata.addPlayerCityHistory(playerName, cityName);
			RankPermissions rp = new RankPermissions(true);
			rp.setRankName("Mayor");
			csranks.createRank(cityName, rp);
			csresidents.setCurrentCity(playerName, cityName, "Mayor");
			con.commit();
			con.setAutoCommit(true);
		} catch(SQLException e) {
			if(settings.debug)
				e.printStackTrace();
		}
	}
	
	public void createRank(String city, String rank) {
		RankPermissions rp = new RankPermissions(false);
		rp.setRankName(rank);
		csranks.createRank(city, rp);
	}
	
	public void deleteCity(String city) {
		cscities.deleteCity(city);
	}
	
	public void deleteOldInvites() {
		csinvites.deleteOldInvites();
	}
	
	public boolean doesCityExist(String cityName) {
		return cscities.doesCityExist(cityName);
	}
	
	public boolean doesPlayerExist(String playerName) {
		return csplayers.doesPlayerExist(playerName);
	}
	
	public ArrayList<City> getCities() {
		
		ArrayList<City> cityArray = new ArrayList<City>();
		try {
			ResultSet rs = cscities.getCities();
			if(rs == null)
				return cityArray;
			while(rs.next()) {
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
				city.setSpawnX(rs.getDouble("spawnX"));
				city.setSpawnY(rs.getDouble("spawnY"));
				city.setSpawnZ(rs.getDouble("spawnZ"));
				city.setSpawnPitch(rs.getFloat("spawnPitch"));
				city.setUsedClaims(rs.getInt("usedClaims"));
				city.setSnow(rs.getBoolean("snow"));
				
				ArrayList<String> banList = cscitybanlist.getBans(city.getName());
				for(String s : banList) {
					city.ban(s);
				}
				
				ArrayList<Plot> plotList = getPlots(city.getName());
				for(Plot plot : plotList) {
					city.addPlot(plot);
				}
				
				ArrayList<RankPermissions> rankList = csranks.getRanks(city.getName());
				for(RankPermissions rp : rankList) {
					city.addRank(rp);
				}
				
				cityArray.add(city);
			}
		} catch (SQLException e) {
			if(settings.debug)
				e.printStackTrace();
		}
		return cityArray;
	}
	
	public City getCity(String cityName) {
		City city = cscities.getCity(cityName);
		ArrayList<Plot> plotMap = getPlots(city.getName());
		for(Plot plot : plotMap) {
			city.addPlot(plot);
		}
		return city;
	}
	
	public ArrayList<String> getCityNames() {
		return cscities.getCityNames();
	}
	
	public Claim getClaimAt(String world, int xmin, int zmin, int xmax, int zmax) {
		return csclaims.getClaimAt(world, xmin, zmin, xmax, zmax);
	}
	
	public ArrayList<Claim> getClaims() {
		return csclaims.getClaims();
	}
	
	public Connection getConnection() {
		return con;
	}
	
	public String getCurrentCity(String playerName) {
		return csresidents.getCurrentCity(playerName);
	}
	
	public ArrayList<String> getHistory(String player) {
		return csplayercitydata.getHistory(player);
	}
	
	public PlayerCache getInfo(String player) {
		PlayerCache cache = csresidents.getInfo(player);
		if(cache != null) {
			csplayers.getTimes(player, cache);
		}
		return cache;
	}
	
	public ArrayList<String> getInvites(String player) {
		return csinvites.getInvites(player);
	}
	
	public int getLastClaimID() {
		return csclaims.getLastID();
	}
	
	public int getLastPlotID() {
		return csplots.getLastID();
	}
	
	public String getPlayerCity(String playerName) {
		return csresidents.getCurrentCity(playerName);
	}
	
	public ArrayList<Plot> getPlots(String cityName) {
		return csplots.getPlots(cityName);
	}
	
	public String getRank(String playerName) {
		return csresidents.getRank(playerName);
	}
	
	public ArrayList<String> getResidents(String city) {
		return csresidents.getResidents(city);
	}
	
	public boolean hasClaims(String cityName, int numClaims) {
		return cscities.hasClaims(cityName, numClaims);
	}
	
	public void insertNewPlayer(String playerName) {
		try {
			con.setAutoCommit(false);
			csplayers.insertNewPlayer(playerName);
			csresidents.insertNewPlayer(playerName);
			csplayercitydata.addPlayerCityHistory(playerName, null);
			con.commit();
			con.setAutoCommit(true);
		} catch (SQLException e) {
			if(settings.debug)
				e.printStackTrace();
		}
	}
	
	public boolean isInvited(String player, String city) {
		return csinvites.isInvited(player, city);
	}
	
	public void leaveCity(String playerName) {
		try {
			con.setAutoCommit(false);
			csresidents.setCurrentCity(playerName, null, null);
			csplayercitydata.addPlayerCityHistory(playerName, null);
			csplots.removePlayer(playerName);
			con.commit();
			con.setAutoCommit(true);
		} catch(SQLException e) {
			if(settings.debug)
				e.printStackTrace();
		}
	}
	
	public void removeAllInvites(String player) {
		csinvites.removeAllInvites(player);
	}
	
	public void removeInvite(String player, String city) {
		csinvites.removeInvite(player, city);
	}
	
	public void removePlot(int id) {
		csplots.removePlot(id);
	}
	
	public void removePlotPermissions(int id, String name, boolean isPlayer) {
		csplots.removePlotPermissions(id, name, isPlayer);
	}
	
	public void renameCity(String oldName, String newName) {
		cscities.renameCity(oldName, newName);
		Cityscape.log.info("Done renaming cities.");
	}
	
	public void setBonusClaims(String city, int bonusClaims) {
		cscities.setBonusClaims(city, bonusClaims);
	}
	
	public void setPlotForSale(Plot plot) {
		csplots.setPlotForSale(plot);
	}
	
	public void setPlotPermissions(int id, String name, Permissions perms, 
			boolean isPlayer) {
		csplots.setPlotPermissions(id, name, perms, isPlayer);
	}
	
	public void setRank(String player, String rank) {
		csresidents.setRank(player, rank);
	}
	
	public void setRankPermissions(String city, RankPermissions rp) {
		csranks.setPermissions(city, rp);
	}
	
	public void setWarp(City city) {
		cscities.setWarp(city);
	}
	
	public void setWelcome(String city, String welcome) {
		cscities.setWelcome(city, welcome);
	}
	
	public void unban(String city, String player) {
		cscitybanlist.unban(city, player);
	}
	
	public void unclaimChunk(Claim claim) {
		try {
			con.setAutoCommit(false);
			csclaims.unclaimChunk(claim);
			cscities.removeUsedClaims(claim.getCityName(), 1);
			con.commit();
			con.setAutoCommit(true);
		} catch(SQLException e) {
			if(settings.debug)
				e.printStackTrace();
		}
	}
	
	public void updateCitySettings(City city) {
		cscities.updateCitySettings(city);
	}
	
	public void updateMayor(String city, String player) {
		cscities.updateMayor(city, player);
	}
	
	public void updatePlayerTimeStamp(String playerName) {
		csplayers.updatePlayerTimeStamp(playerName);
	}
	
	public void updatePlotSettings(Plot plot) {
		csplots.updatePlotSettings(plot);
	}
}
