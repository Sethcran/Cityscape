package com.sethcran.cityscape.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.sethcran.cityscape.City;
import com.sethcran.cityscape.Cityscape;
import com.sethcran.cityscape.Plot;
import com.sethcran.cityscape.RankPermissions;
import com.sethcran.cityscape.Settings;

public class Database {
	private Connection con = null;
	
	private Settings settings = null;
	
	private CSCities cscities = null;
	private CSClaims csclaims = null;
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
		csclaims = new CSClaims(con, plugin.getSettings());
		csplayers = new CSPlayers(con, plugin.getSettings());
		csplayercitydata = new CSPlayerCityData(con, plugin.getSettings());
		csplots = new CSPlots(con, plugin.getSettings());
		csranks = new CSRanks(con, plugin.getSettings());
		csresidents = new CSResidents(con, plugin.getSettings());
	}
	
	public void claimChunk(String cityName, int x, int z, String worldName) {
		try {
			con.setAutoCommit(false);
			csclaims.claimChunk(cityName, x, z, worldName);
			cscities.addUsedClaims(cityName, 1);
			con.commit();
			con.setAutoCommit(true);
		} catch(SQLException e) {
			if(settings.debug)
				e.printStackTrace();
		}
	}

	public void createCity(String playerName, String cityName, 
			int x, int z, String worldName) {
		try {
			con.setAutoCommit(false);
			cscities.createCity(playerName, cityName);
			csclaims.claimChunk(cityName, x, z, worldName);
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
				ArrayList<Plot> plotMap = getPlots(city.getName());
				for(Plot plot : plotMap) {
					city.addPlot(plot);
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
	
	public String getCityNameAt(int x, int z) {
		return csclaims.getCityAt(x, z);
	}
	
	public City getCityAt(int x, int z) {
		String sql = 	"SELECT name, mayor, rank, founded, " +
						"usedClaims, baseClaims, bonusClaims, " +
						"spawnX, spawnY, spawnZ " +
						"FROM cscities, csclaims " +
						"WHERE cscities.name = csclaims.city " +
						"AND loc = POINT(?, ?);";
		try {
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setInt(1, x);
			stmt.setInt(2, z);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()) {
				City city = new City();
				city.setBaseClaims(rs.getInt("baseClaims"));
				city.setBonusClaims(rs.getInt("bonusClaims"));
				city.setFounded(rs.getString("founded"));
				city.setMayor(rs.getString("mayor"));
				city.setName(rs.getString("name"));
				city.setRank(rs.getInt("rank"));
				city.setSpawnX(rs.getInt("spawnX"));
				city.setSpawnY(rs.getInt("spawnY"));
				city.setSpawnZ(rs.getInt("spawnZ"));
				city.setUsedClaims(rs.getInt("usedClaims"));
				return city;
			}
		} catch (SQLException e) {
			if(settings.debug)
				e.printStackTrace();
		}
		return null;		
	}
	
	public Connection getConnection() {
		return con;
	}
	
	public String getCurrentCity(String playerName) {
		return csresidents.getCurrentCity(playerName);
	}
	
	public RankPermissions getPermissions(String townName, String rank) {
		return csranks.getPermissions(townName, rank);
	}
	
	public RankPermissions getPermissions(String playerName) {
		String sql = 	"SELECT * " +
						"FROM csresidents, csranks " +
						"WHERE csresidents.player = ? " +
						"AND csresidents.city = csranks.city " +
						"AND csresidents.rank = csranks.name;";
		try {
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, playerName);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()) {
				RankPermissions rp = new RankPermissions();
				rp.setAddResident(rs.getBoolean("addResident"));
				rp.setChangeRankName(rs.getBoolean("changeRankName"));
				rp.setClaim(rs.getBoolean("claim"));
				rp.setCreatePlots(rs.getBoolean("createPlots"));
				rp.setDemote(rs.getBoolean("demote"));
				rp.setPromote(rs.getBoolean("promote"));
				rp.setRemoveResident(rs.getBoolean("removeResident"));
				rp.setSetMayor(rs.getBoolean("setMayor"));
				rp.setSetName(rs.getBoolean("setName"));
				rp.setSetPlotSale(rs.getBoolean("setPlotSale"));
				rp.setSetPrices(rs.getBoolean("setPrices"));
				rp.setSetTaxes(rs.getBoolean("setTaxes"));
				rp.setSetWarp(rs.getBoolean("setWarp"));
				rp.setSetWelcome(rs.getBoolean("setWelcome"));
				rp.setUnclaim(rs.getBoolean("unclaim"));
				rp.setWithdraw(rs.getBoolean("withdraw"));
				rp.setSendChestsToLostAndFound(rs.getBoolean("sendChestsToLostAndFound"));
				rp.setCityBuild(rs.getBoolean("cityBuild"));
				rp.setCityDestroy(rs.getBoolean("cityDestroy"));
				rp.setCitySwitch(rs.getBoolean("citySwitch"));
				return rp;
			}
		} catch (SQLException e) {
			if(settings.debug)
				e.printStackTrace();
		}
		return null;
	}
	
	public ArrayList<Plot> getPlots(String cityName) {
		return csplots.getPlots(cityName);
	}
	
	public String getRank(String playerName) {
		return csresidents.getRank(playerName);
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
	
	public boolean isChunkClaimed(int x, int z) {
		return csclaims.isChunkClaimed(x, z);
	}
	
	public void updatePlayerTimeStamp(String playerName) {
		csplayers.updatePlayerTimeStamp(playerName);
	}
}
