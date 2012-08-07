package com.github.thebiologist13.commands.spawners;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.SpawnableEntity;
import com.github.thebiologist13.Spawner;
import com.github.thebiologist13.commands.SpawnerCommand;

public class CreateCommand extends SpawnerCommand {
	
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
		SpawnableEntity type = null;
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
		if(p.hasPermission("customspawners.spawners.create")) {
			//Try to get the target block after scanning the distance from config
			target = p.getTargetBlock(null, config.getInt("players.maxDistance", 50));
			
			//If no target block was found
			if(target == null) {
				p.sendMessage(INVALID_BLOCK);
				return;
			}
			
			//Try to parse an entity type from input. Null if invalid.
			type = plugin.getEntity(arg3[1]);
			
			if(type == null) {
				p.sendMessage(NO_ID);
			}
			
			if(type == null) {
				p.sendMessage(ENTITY_NONEXISTANT);
				return;
			}
			
			//Gets the next available ID for a spawner
			id = plugin.getNextSpawnerId();
			
			//Creates a new instance of spawner using variables parsed from command
			Spawner spawner = new Spawner(type, target.getLocation(), id, plugin.getServer());
			
			//Setting default properties from config
			spawner.setRadius(config.getDouble("spawners.radius", 16));
			spawner.setRedstoneTriggered(config.getBoolean("spawners.redstoneTriggered", false));
			spawner.setMaxPlayerDistance(config.getInt("spawners.maxPlayerDistance", 128));
			spawner.setMinPlayerDistance(config.getInt("spawners.minPlayerDistance", 0));
			spawner.setActive(config.getBoolean("spawners.active", false));
			spawner.setMaxLightLevel((byte) config.getInt("spawners.maxLightLevel", 7));
			spawner.setMinLightLevel((byte) config.getInt("spawners.minLightLevel", 0));
			spawner.setHidden(config.getBoolean("spawners.hidden", false));
			spawner.setRate(config.getInt("spawners.rate", 120));
			spawner.setMobsPerSpawn(config.getInt("spawners.mobsPerSpawn", 2));
			spawner.setMaxMobs(config.getInt("spawners.maxMobs", 64));
			
			CustomSpawners.spawners.put(id, spawner);
			
			if(config.getBoolean("data.autosave") && config.getBoolean("data.saveOnCreate")) {
				plugin.autosave(spawner);
			}
			
			//Success message
			if(type.getName().isEmpty()) {
				p.sendMessage(ChatColor.GREEN + "Successfully created a " + ChatColor.GOLD + type.getType().getName() + ChatColor.GREEN + 
						" spawner with ID " + ChatColor.GOLD + id + ChatColor.GREEN + "!");
			} else {
				p.sendMessage(ChatColor.GREEN + "Successfully created a " + ChatColor.GOLD + type.getName() + ChatColor.GREEN + 
						" spawner with ID " + ChatColor.GOLD + id + ChatColor.GREEN + "!");
			}
			
		} else {
			p.sendMessage(NO_PERMISSION);
			return;
		}
	}
}
