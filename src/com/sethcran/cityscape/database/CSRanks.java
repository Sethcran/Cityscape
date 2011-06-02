package com.sethcran.cityscape.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.sethcran.cityscape.RankPermissions;
import com.sethcran.cityscape.Settings;

public class CSRanks extends Table {

	public CSRanks(Connection con, Settings settings) {
		super(con, settings);
	}
	
	public void createRank(String townName, RankPermissions rp) {
		String sql = 	"INSERT INTO csranks " +
						"VALUES(?, ?, ?, ?, ?, ?, " +
						"?, ?, ?, ?, ?, ?, " +
						"?, ?, ?, ?, ?, ?, " +
						"?, ?, ?, ?, ?);";
		try {
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, townName);
			stmt.setString(2, rp.getRankName());
			stmt.setBoolean(3, rp.isAddResident());
			stmt.setBoolean(4, rp.isRemoveResident());
			stmt.setBoolean(5, rp.isClaim());
			stmt.setBoolean(6, rp.isUnclaim());
			stmt.setBoolean(7, rp.isPromote());
			stmt.setBoolean(8, rp.isDemote());
			stmt.setBoolean(9, rp.isWithdraw());
			stmt.setBoolean(10, rp.isChangeRankName());
			stmt.setBoolean(11, rp.isSetWelcome());
			stmt.setBoolean(12, rp.isSetMayor());
			stmt.setBoolean(13, rp.isSetWarp());
			stmt.setBoolean(14, rp.isSetName());
			stmt.setBoolean(15, rp.isSetPlotSale());
			stmt.setBoolean(16, rp.isSetTaxes());
			stmt.setBoolean(17, rp.isSetPrices());
			stmt.setBoolean(18, rp.isCreatePlots());
			stmt.setBoolean(19, rp.isSendChestsToLostAndFound());
			stmt.setBoolean(20, rp.isCityBuild());
			stmt.setBoolean(21, rp.isCityDestroy());
			stmt.setBoolean(22, rp.isCitySwitch());
			stmt.setBoolean(23, rp.isChangeCityPlotPerms());
			stmt.executeUpdate();
		} catch (SQLException e) {
			if(settings.debug)
				e.printStackTrace();
		}
	}
	
	public int getNumRanks(String city) {
		String sql = 	"SELECT COUNT(*) " +
						"FROM csranks " +
						"WHERE city = ?;";
		try {
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, city);
			ResultSet rs = stmt.executeQuery();
			if(rs.next())
				return rs.getInt(1);
		} catch (SQLException e) {
			if(settings.debug)
				e.printStackTrace();
		}
		return 0;
	}
	
	public RankPermissions getPermissions(String townName, String rank) {
		String sql = 	"SELECT * " +
						"FROM csranks " +
						"WHERE city = ? AND name = ?;";
		try {
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, townName);
			stmt.setString(2, rank);
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
				rp.setChangeCityPlotPerms(rs.getBoolean("changeCityPlotPerms"));
				return rp;
			}
		} catch (SQLException e) {
			if(settings.debug)
				e.printStackTrace();
		}
		return null;
	}
	
	public ArrayList<String> getRanks(String city) {
		String sql = 	"SELECT name " +
						"FROM csranks " +
						"WHERE city = ?;";
		ArrayList<String> rankList = new ArrayList<String>();
		try {
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, city);
			ResultSet rs = stmt.executeQuery();
			while(rs.next())
				rankList.add(rs.getString("name"));
		} catch (SQLException e) {
			if(settings.debug)
				e.printStackTrace();
		}
		return rankList;
	}

}
