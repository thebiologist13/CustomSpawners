package com.github.thebiologist13.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import com.github.thebiologist13.CustomSpawners;

public class PlayerLogoutEvent implements Listener {

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerLogout(PlayerQuitEvent ev) {
		if(CustomSpawners.spawnerSelection.containsKey(ev.getPlayer())) {
			CustomSpawners.spawnerSelection.remove(ev.getPlayer());
		}
		
		if(CustomSpawners.entitySelection.containsKey(ev.getPlayer())) {
			CustomSpawners.entitySelection.remove(ev.getPlayer());
		}
	}
}
