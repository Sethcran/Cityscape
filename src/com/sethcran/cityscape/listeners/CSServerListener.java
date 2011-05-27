package com.sethcran.cityscape.listeners;

import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.server.ServerListener;
import org.bukkit.plugin.Plugin;

import com.iConomy.iConomy;
import com.sethcran.cityscape.Cityscape;

public class CSServerListener extends ServerListener{
	private Cityscape plugin;
	
	public CSServerListener(Cityscape plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public void onPluginDisable(PluginDisableEvent event) {
		if(plugin.iconomy != null) {
			if(event.getPlugin().getDescription().getName().equals("iConomy")) {
				plugin.iconomy = null;
				System.out.println("Cityscape unhooked from iConomy.");
			}
		}
	}
	
	@Override
	public void onPluginEnable(PluginEnableEvent event) {
		if(plugin.iconomy == null) {
			Plugin iconomy = plugin.getServer().getPluginManager().getPlugin("iConomy");
			
			if(iconomy != null) {
				if(iconomy.isEnabled() && iconomy.getClass().getName().equals("com.iConomy.iConomy")) {
					plugin.iconomy = (iConomy)iconomy;
					System.out.println("Cityscape hooked into iConomy.");
				}
			}
		}
	}
}
