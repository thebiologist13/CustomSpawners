package com.github.thebiologist13.listeners;

import java.util.logging.Logger;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import com.github.thebiologist13.CustomSpawners;

public class PlayerClickEntityEvent implements Listener {

	private Logger log;
	
	public PlayerClickEntityEvent(CustomSpawners plugin) {
		this.log = plugin.log;
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEntityEvent event) {
		Player p = event.getPlayer();
		Entity click = event.getRightClicked();
		
		log.info("Entity ID: " + click.getEntityId());
		log.info("Entity UUID: " + click.getUniqueId().toString());
		
		p.sendMessage("Entity ID: " + click.getEntityId());
		p.sendMessage("Entity UUID: " + click.getUniqueId().toString());
	}
	
}
