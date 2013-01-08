package com.github.thebiologist13.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.Spawner;

public class BreakEvent implements Listener {

	private final CustomSpawners PLUGIN;
	
	public BreakEvent(CustomSpawners plugin) {
		this.PLUGIN = plugin;
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onBlockBreak(BlockBreakEvent ev) {
		
		Player p = ev.getPlayer();
		
		if(CustomSpawners.selectMode.containsKey(p)) {
			boolean doBreak = CustomSpawners.selectMode.get(p);
			int configId = PLUGIN.getCustomConfig().getInt("players.selectionId");
			
			if((p.getItemInHand().getTypeId() == configId) && doBreak && p.hasPermission("customspawners.spawners.pos")) {
				ev.setCancelled(true);
				return;
			}
		}
		
		Spawner s = PLUGIN.getSpawnerAt(ev.getBlock().getLocation());
		
		if(!PLUGIN.getCustomConfig().getBoolean("spawners.deactivateOnBreak", true)) {
			return;
		}
		
		if(s != null) {
			s.setActive(false);
		}
		
	}
	
}
