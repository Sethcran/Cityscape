package com.sethcran.cityscape.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.sethcran.cityscape.Settings;

public class CSPlayers extends Table {

	public CSPlayers(Connection con, Settings settings) {
		super(con, settings);
	}
	
	public boolean doesPlayerExist(String playerName) {
		String sql = 	"SELECT * " +
						"FROM csplayers " +
						"WHERE name = ?";
		try {
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, playerName);
			ResultSet rs = stmt.executeQuery();
			if(!rs.next())
				return false;
		} catch (SQLException e) {
			if(settings.debug)
				e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public void insertNewPlayer(String playerName) {
		String sql = 	"INSERT INTO csplayers " +
						"VALUES( ?, NOW(), NOW());";
		try {
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, playerName);
			stmt.executeUpdate();
		} catch (SQLException e) {
			if(settings.debug)
				e.printStackTrace();
		}
	}
	
	public void updatePlayerTimeStamp(String playerName) {
		String sql = 	"UPDATE csplayers " +
						"SET lastLogin = NOW() " +
						"WHERE name = ?;";
		try {
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, playerName);
			stmt.executeUpdate();
		} catch (SQLException e) {
			if(settings.debug)
				e.printStackTrace();
		}
	}
}
