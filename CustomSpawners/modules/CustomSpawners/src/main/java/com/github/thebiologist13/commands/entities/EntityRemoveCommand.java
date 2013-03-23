package com.github.thebiologist13.commands.entities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.SpawnableEntity;
import com.github.thebiologist13.Spawner;

public class EntityRemoveCommand extends EntityCommand {

	public EntityRemoveCommand(CustomSpawners plugin) {
		super(plugin);
	}

	public EntityRemoveCommand(CustomSpawners plugin, String mainPerm) {
		super(plugin, mainPerm);
	}

	@Override
	public void run(SpawnableEntity entity, CommandSender sender, String subCommand, String[] args) {
		
		int id = entity.getId();
		
		Iterator<Spawner> spawnerItr = CustomSpawners.spawners.values().iterator();
		while(spawnerItr.hasNext()) {
			Spawner s = spawnerItr.next();
			
			if(s.getTypeData().contains(id)) {
				List<Integer> defaultEntity = new ArrayList<Integer>();
				defaultEntity.add(CustomSpawners.defaultEntity.getId());
				s.setTypeData(defaultEntity);
				
				PLUGIN.sendMessage(sender, ChatColor.GOLD + "The spawner " + PLUGIN.getFriendlyName(s) + " has been set to the default entity due to removal.");
			}
		}
		
		PLUGIN.removeEntity(entity);
		PLUGIN.getFileManager().removeDataFile(id, false);
		
		PLUGIN.sendMessage(sender, ChatColor.GREEN + "Entity " + ChatColor.GOLD + id + ChatColor.GREEN + " has been removed from the server.");
		
	}
	
}
