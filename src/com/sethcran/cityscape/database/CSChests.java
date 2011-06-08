package com.sethcran.cityscape.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;

import com.sethcran.cityscape.Settings;

public class CSChests extends Table {

	public CSChests(Connection con, Settings settings) {
		super(con, settings);
	}

	public void addChest(Chest chest, String player) {
		String sql = 	"INSERT INTO cschestlostandfound " +
						"VALUES(null, ?);";
		try {
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, player);
			stmt.executeUpdate();
			sql = 		"SELECT LAST_INSERT_ID();";
			stmt = con.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()) {
				int id = rs.getInt(1);
				sql = 	"INSERT INTO cschestdata " +
						"VALUES(";
				boolean first = true;
				for(ItemStack i : chest.getInventory().getContents()) {
					if(i != null) {
						if(first) {
							sql += id + ", " + i.getTypeId() + ", " + i.getAmount() + ")";
							first = false;
						}
						sql += ", (" + id + ", " + i.getTypeId() + ", " + i.getAmount() + ")";
					}
				}
				sql += ";";
				Statement s = con.createStatement();
				s.executeUpdate(sql);
			}
		} catch (SQLException e) {
			if(settings.debug)
				e.printStackTrace();
		}
	}
	
	public ArrayList<ItemStack> getChestFromLostAndFound(String player) {
		String sql = 	"SELECT id " +
						"FROM cschestlostandfound " +
						"WHERE player = ?;";
		ArrayList<ItemStack> itemList = new ArrayList<ItemStack>();
		try {
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, player);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()) {
				int id = rs.getInt("id");
				sql = 	"SELECT type, amount " +
						"FROM cschestdata " +
						"WHERE id = ?;";
				stmt = con.prepareStatement(sql);
				stmt.setInt(1, id);
				ResultSet r = stmt.executeQuery();
				while(r.next()) {
					itemList.add(new ItemStack(r.getInt("type"), r.getInt("amount")));
				}
			}
		} catch (SQLException e) {
			if(settings.debug)
				e.printStackTrace();
		}
		
		return itemList;
	}
}
