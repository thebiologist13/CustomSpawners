package com.github.thebiologist13.commands.entities;

import java.util.Arrays;
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
		
		int[] ids = new int[CustomSpawners.entities.size()];
		int current = 0;
		
		if(ids.length == 0) {
			PLUGIN.sendMessage(sender, ChatColor.RED + "No entities have been created yet.");
			return;
		}
		
		PLUGIN.sendMessage(sender, ChatColor.GOLD + "Created Entities:");
		
		Iterator<SpawnableEntity> itr = CustomSpawners.entities.values().iterator();
		while(itr.hasNext()) {
			int id = itr.next().getId();
			ids[current] = id;
			current++;
		}
		
		Arrays.sort(ids);
		
		for(int i : ids) {
			SpawnableEntity ent = CustomSpawners.getEntity(i);
			
			String baseMessage =  ChatColor.GOLD + String.valueOf(ent.getId()) + " (" + ent.getType().toString() + ")"; 
			
			if(!ent.getName().isEmpty())
				baseMessage += ChatColor.GREEN + " with name " + ChatColor.GOLD + ent.getName();
			
			PLUGIN.sendMessage(sender, baseMessage);
		}
		
	}

}
