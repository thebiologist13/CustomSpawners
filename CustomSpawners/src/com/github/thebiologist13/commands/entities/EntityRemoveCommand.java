package com.github.thebiologist13.commands.entities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.SpawnableEntity;
import com.github.thebiologist13.Spawner;
import com.github.thebiologist13.commands.SubCommand;

public class EntityRemoveCommand extends SubCommand {

	public EntityRemoveCommand(CustomSpawners plugin) {
		super(plugin);
	}

	@Override
	public void run(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		//Player
		Player p = null;
		//SpawnableEntity to remove
		SpawnableEntity e = null;
		
		//Check that sender is a player
		if(!(arg0 instanceof Player)) {
			plugin.log.info(NO_CONSOLE);
			return;
		}
		
		p = (Player) arg0;
		
		//Check permissions
		if(p.hasPermission("customspawners.entities.remove")) {
			
			//If the player wants to remove the selected entity
			if(CustomSpawners.entitySelection.containsKey(p) && arg3.length == 1) {
				
				e = CustomSpawners.getEntity(CustomSpawners.entitySelection.get(p).toString());
			
			//When the arg3 length is 1, but no entity is selected.
			} else if(arg3.length == 1){
					
					p.sendMessage(NEEDS_SELECTION);
					return;
					
			//If the player want to remove a entity with a specified ID
			} else if(arg3.length == 2) {
				
				e = CustomSpawners.getEntity(arg3[1]);
			
			//General error message
			} else {
				
				plugin.log.info(GENERAL_ERROR);
				return;
				
			}
			
			//Check if the ID entered is the ID of a entity
			if(e == null) {
				p.sendMessage(NO_ID);
				return;
			}
			
			//Remove the entity
			plugin.removeEntity(e);
			plugin.getFileManager().removeDataFile(e.getId(), false);
			
			Iterator<Spawner> spawnerItr = CustomSpawners.spawners.values().iterator();
			while(spawnerItr.hasNext()) {
				Spawner s = spawnerItr.next();
				
				if(s.getTypeData().contains(e.getId())) {
					List<Integer> defaultEntity = new ArrayList<Integer>();
					defaultEntity.add(CustomSpawners.defaultEntity.getId());
					s.setTypeData(defaultEntity);
					
					p.sendMessage(ChatColor.GOLD + "The spawner with ID " + s.getId() + " has been set to the default entity due to removal.");
				}
			}
			
			//Send success message
			p.sendMessage(ChatColor.GREEN + "Successfully removed entity with ID " + ChatColor.GOLD + e.getId() + ChatColor.GREEN + "!");
		} else {
			p.sendMessage(NO_PERMISSION);
			return;
		}
	}
}
