package com.github.thebiologist13.commands;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.Spawner;

public class CreateCommand extends SpawnerSubCommand {
	
	private CustomSpawners plugin;
	
	private FileConfiguration config = null;
	
	public CreateCommand(CustomSpawners plugin) {
		this.plugin = plugin;
		this.config = plugin.getCustomConfig();
	}
	
	public void run(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		//Player
		Player p = null;
		//Type of entity for spawner
		EntityType type = null;
		//Target block to make spawner
		Block target = null;
		//ID of spawner to make
		int id = 0;
		
		//Makes sure the command was from in-game
		if(!(arg0 instanceof Player)) {
			plugin.log.info(NO_CONSOLE);
			return;
		}
		
		p = (Player) arg0;
		
		//Permission check
		if(p.hasPermission("customspawners.create")) {
			//Try to get the target block after scanning the distance from config
			target = p.getTargetBlock(null, plugin.getConfig().getInt("players.maxDistance", 50));
			
			//If no target block was found
			if(target == null) {
				p.sendMessage(INVALID_BLOCK);
				return;
			}
			
			//Try to parse an entity type from input. Would throw null pointer if it can't parse.
			type = getEntityType(arg3[1], plugin, p);
			
			//Gets the next available ID for a spawner
			for(Spawner s : plugin.spawners) {
				if(s.id >= id) {
					id = s.id + 1;
				}
			}
			
			//Creates a new instance of spawner using variables parsed from command
			Spawner spawner = new Spawner(type, target.getLocation(), id, plugin.getServer());
			
			//Setting default properties from config
			spawner.radius = config.getDouble("spawners.radius", 16);
			spawner.redstoneTriggered = config.getBoolean("spawners.redstoneTriggered", false);
			spawner.maxPlayerDistance = config.getInt("spawners.maxPlayerDistance", 128);
			spawner.minPlayerDistance = config.getInt("spawners.minPlayerDistance", 0);
			spawner.active = config.getBoolean("spawners.active", false);
			spawner.maxLightLevel = (byte) config.getInt("spawners.maxLightLevel", 7);
			spawner.minLightLevel = (byte) config.getInt("spawners.minLightLevel", 0);
			spawner.hidden = config.getBoolean("spawners.hidden", false);
			spawner.rate = config.getInt("spawners.rate", 120);
			spawner.ticksLeft = spawner.rate;
			spawner.mobsPerSpawn = config.getInt("spawners.mobsPerSpawn", 2);
			spawner.maxMobs = config.getInt("spawners.maxMobs", 64);
			
			plugin.spawners.add(spawner);
			
			//Success message
			p.sendMessage(ChatColor.GREEN + "Sucessfully created a " + ChatColor.GOLD + type.getName() + ChatColor.GREEN + 
					" spawner with ID " + ChatColor.GOLD + String.valueOf(spawner.id) + ChatColor.GREEN + "!");
		}
	}
}
