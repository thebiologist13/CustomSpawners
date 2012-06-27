package com.github.thebiologist13.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import com.github.thebiologist13.CustomSpawners;

public class PlayerLogoutEvent implements Listener {
	
	private CustomSpawners plugin;
	
	public PlayerLogoutEvent(CustomSpawners plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerLogout(PlayerQuitEvent ev) {
		if(plugin.selection.containsKey(ev.getPlayer())) {
			plugin.selection.remove(ev.getPlayer());
		}
	}
}
