package com.github.thebiologist13.commands.entities;

import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.SpawnableEntity;
import com.github.thebiologist13.commands.SpawnerCommand;

public class EntityListAllCommand extends SpawnerCommand {

	//private CustomSpawners plugin = null;
	
	private Logger log = null;
	
	public EntityListAllCommand(CustomSpawners plugin) {
		//this.plugin = plugin;
		this.log = plugin.log;
	}
	
	@Override
	public void run(CommandSender arg0, Command arg1, String arg2, String[] arg3) {

		final String NO_ENTITIES = "No entities have been created yet.";
		
		Player p = null;
		
		if(arg0 instanceof Player) {
			p = (Player) arg0;
		}
		
		if(p == null) {
			if(CustomSpawners.entities.size() == 0) {
				log.info(NO_ENTITIES);
			} else {
				log.info("Entities: ");
				for(SpawnableEntity s : CustomSpawners.entities.values()) {
					if(!s.getName().isEmpty()) {
						log.info(String.valueOf(s.getId()) + " with name " + s.getName());
					} else {
						log.info(String.valueOf(s.getId()));
					}
				}
			}
		} else {
			if(p.hasPermission("customspawners.entities.listall")) {
				if(CustomSpawners.entities.size() == 0) {
					p.sendMessage(ChatColor.RED + NO_ENTITIES);
				} else {
					p.sendMessage(ChatColor.GOLD + "Entities: ");
					for(SpawnableEntity s : CustomSpawners.entities.values()) {
						if(!s.getName().isEmpty()) {
							p.sendMessage(ChatColor.GOLD + String.valueOf(s.getId()) + ChatColor.GREEN + " with name " + ChatColor.GOLD + s.getName());
						} else {
							p.sendMessage(ChatColor.GOLD + String.valueOf(s.getId()));
						}
						
					}
					
				}
				
			}
			
		}

	}

}
