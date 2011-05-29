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
				rp.setAddResidents(rs.getBoolean("addResident"));
				rp.setChangeRankName(rs.getBoolean("changeRankName"));
				rp.setClaim(rs.getBoolean("claim"));
				rp.setCreatePlots(rs.getBoolean("createPlots"));
				rp.setDemote(rs.getBoolean("demote"));
				rp.setPromote(rs.getBoolean("promote"));
				rp.setRemoveResidents(rs.getBoolean("removeResident"));
				rp.setSetMayor(rs.getBoolean("setMayor"));
				rp.setSetName(rs.getBoolean("setName"));
				rp.setSetPlotSale(rs.getBoolean("setPlotSale"));
				rp.setSetPrices(rs.getBoolean("setPrices"));
				rp.setSetTaxes(rs.getBoolean("setTaxes"));
				rp.setSetWarp(rs.getBoolean("setWarp"));
				rp.setSetWelcome(rs.getBoolean("setWelcome"));
				rp.setUnclaim(rs.getBoolean("unclaim"));
				rp.setWithdraw(rs.getBoolean("withdraw"));
				return rp;
			}
		} catch (SQLException e) {
			if(settings.debug)
				e.printStackTrace();
		}
		return null;
	}

}
