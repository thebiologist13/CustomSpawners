package com.github.thebiologist13.commands.entities;

import java.util.Collection;
import java.util.Iterator;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.SpawnableEntity;

public class EntityListAllCommand extends EntityCommand {

	public EntityListAllCommand(CustomSpawners plugin) {
		super(plugin);
	}

	public EntityListAllCommand(CustomSpawners plugin, String mainPerm) {
		super(plugin, mainPerm);
	}

	@Override
	public void run(SpawnableEntity entity, CommandSender sender, String subCommand, String[] args) {
		
		Collection<SpawnableEntity> entities = CustomSpawners.entities.values();
		
		if(entities.size() == 0) {
			PLUGIN.sendMessage(sender, ChatColor.RED + "No entities have been created yet.");
			return;
		}
		
		PLUGIN.sendMessage(sender, ChatColor.GOLD + "Created Entities:");
		
		Iterator<SpawnableEntity> itr = entities.iterator();
		while(itr.hasNext()) {
			SpawnableEntity ent = itr.next();
			
			String baseMessage =  ChatColor.GOLD + String.valueOf(ent.getId()) + "(" + ent.getType().toString() + ")"; 
			
			if(ent.getName().isEmpty()) {
				PLUGIN.sendMessage(sender, baseMessage + ChatColor.GREEN + " with name " + ChatColor.GOLD + ent.getName());
			} else {
				PLUGIN.sendMessage(sender, baseMessage);
			}
			
		}
		
	}

}
