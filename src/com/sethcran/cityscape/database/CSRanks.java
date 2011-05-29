package com.sethcran.cityscape.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.sethcran.cityscape.RankPermissions;
import com.sethcran.cityscape.Settings;

public class CSRanks extends Table {

	public CSRanks(Connection con, Settings settings) {
		super(con, settings);
	}
	
	public void createRank(String townName, String rank, RankPermissions rp) {
		String sql = 	"INSERT INTO csranks " +
						"VALUES(?, ?, ?, ?, ?, ?, " +
						"?, ?, ?, ?, ?, ?, " +
						"?, ?, ?, ?, ?, ?, " +
						"?);";
		try {
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, townName);
			stmt.setString(2, rank);
			stmt.setBoolean(3, rp.getAddResident());
			stmt.setBoolean(4, rp.getRemoveResident());
			stmt.setBoolean(5, rp.getClaim());
			stmt.setBoolean(6, rp.getUnclaim());
			stmt.setBoolean(7, rp.getPromote());
			stmt.setBoolean(8, rp.getDemote());
			stmt.setBoolean(9, rp.getWithdraw());
			stmt.setBoolean(10, rp.getChangeRankName());
			stmt.setBoolean(11, rp.getSetWelcome());
			stmt.setBoolean(12, rp.getSetMayor());
			stmt.setBoolean(13, rp.getSetWarp());
			stmt.setBoolean(14, rp.getSetName());
			stmt.setBoolean(15, rp.getSetPlotSale());
			stmt.setBoolean(16, rp.getSetTaxes());
			stmt.setBoolean(17, rp.getSetPrices());
			stmt.setBoolean(18, rp.getCreatePlots());
			stmt.setBoolean(19, rp.getSendChestsToLostAndFound());
			stmt.executeUpdate();
		} catch (SQLException e) {
			if(settings.debug)
				e.printStackTrace();
		}
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
				return rp;
			}
		} catch (SQLException e) {
			if(settings.debug)
				e.printStackTrace();
		}
		return null;
	}

}
