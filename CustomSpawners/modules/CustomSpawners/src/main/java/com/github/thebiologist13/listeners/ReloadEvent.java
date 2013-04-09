package com.github.thebiologist13.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerCommandEvent;

import com.github.thebiologist13.CustomSpawners;

public class ReloadEvent implements Listener {

	private final CustomSpawners PLUGIN;
	
	public ReloadEvent(CustomSpawners plugin) {
		this.PLUGIN = plugin;
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onReload(ServerCommandEvent ev) {
		String cmd = ev.getCommand().toLowerCase();
		if(cmd.equals("reload")) {
			try {
				PLUGIN.getServer().getScheduler().cancelTask(PLUGIN.autosaveId);
			} catch(Exception e) {}
			
		}
		
	}

}
