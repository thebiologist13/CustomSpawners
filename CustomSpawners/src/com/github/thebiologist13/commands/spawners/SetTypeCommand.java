package com.github.thebiologist13.commands.spawners;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.SpawnableEntity;
import com.github.thebiologist13.Spawner;
import com.github.thebiologist13.commands.SpawnerCommand;

@SuppressWarnings("unused")
public class SetTypeCommand extends SpawnerCommand {
	
	private CustomSpawners plugin;
	
	public SetTypeCommand(CustomSpawners plugin) {
		this.plugin = plugin;
	}
	
	public void run(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		//Player
		Player p = null;
		
		//New entity type
		SpawnableEntity type = null;
		
		//Spawner
		Spawner s = null;
		
		//Make sure it is a player
		if(!(arg0 instanceof Player)) {
			plugin.log.info(NO_CONSOLE);
			return;
		}
		
		p = (Player) arg0;
		
		if(p.hasPermission("customspawners.spawners.settype")) {
			//If they want to set the spawn type of a selected spawner
			if(CustomSpawners.spawnerSelection.containsKey(p) && arg3.length == 2) {
				
				if(plugin.isInteger(arg3[1])) {
					type = plugin.getEntityById(Integer.parseInt(arg3[1]));
				} else {
					type = plugin.getEntityByName(arg3[1]);
				}
				
				if(type == null) {
					p.sendMessage(ENTITY_NONEXISTANT);
					return;
				}
				
				s = plugin.getSpawnerById(CustomSpawners.spawnerSelection.get(p));
				
			//Argument length is for a selected spawner, but none is selected
			} else if(arg3.length == 2) {
				
				p.sendMessage(NEEDS_SELECTION);
				return;
				
			//If they want to set spawn type by ID
			} else if(arg3.length == 3) {
			
				if(plugin.isInteger(arg3[2])) {
					type = plugin.getEntityById(Integer.parseInt(arg3[2]));
				} else {
					type = plugin.getEntityByName(arg3[2]);
				}
				
				if(type == null) {
					p.sendMessage(ENTITY_NONEXISTANT);
					return;
				}
				
				if(!plugin.isInteger(arg3[1])) {
					p.sendMessage(ID_NOT_NUMBER);
					return;
				}
				
				if(!plugin.isValidSpawner(Integer.parseInt(arg3[1]))) {
					p.sendMessage(NO_ID);
					return;
				}
				
				s = plugin.getSpawnerById(Integer.parseInt(arg3[1]));
				
			//General error
			} else {
				
				p.sendMessage(GENERAL_ERROR);
				return;
				
			}
			
			HashMap<Integer, SpawnableEntity> typeAsMap = new HashMap<Integer, SpawnableEntity>();
			typeAsMap.put(type.getId(), type);
			
			//Set the new type
			s.setTypeData(typeAsMap);
			
			//Success message
			p.sendMessage(ChatColor.GREEN + "Successfully changed entity type of spawner with ID " + 
					ChatColor.GOLD + String.valueOf(s.getId()) + ChatColor.GREEN + " to " + ChatColor.GOLD +
					type.getType().getName() + ChatColor.GREEN + "!");
			
		} else {
			p.sendMessage(NO_PERMISSION);
			return;
		}
	}
}
