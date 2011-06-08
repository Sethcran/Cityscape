package com.sethcran.cityscape.commands.chestcommands;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.sethcran.cityscape.Cityscape;
import com.sethcran.cityscape.Constants;
import com.sethcran.cityscape.commands.CSCommand;

public class Place extends CSCommand {

	public Place(Cityscape plugin) {
		super(plugin);
		name = "place";
		description = "Places a chest from the lost and found where the player is looking.";
		usage = "/chest place";
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		Player player = null;
		if(sender instanceof Player)
			player = (Player)sender;
		else {
			sender.sendMessage(Constants.CITYSCAPE + Constants.ERROR_COLOR +
					"Only a player in game can do that.");
			return;
		}
		
		Block b = player.getTargetBlock(null, 10);
		Block block = b.getFace(BlockFace.UP);
		
		block.setType(Material.CHEST);
		Chest chest = (Chest)block.getState();
		Inventory i = chest.getInventory();
		
		ArrayList<ItemStack> itemList = 
			plugin.getDB().getChestFromLostAndFound(player.getName());

		for(ItemStack is : itemList) {
			i.addItem(is);
		}
	}

}
