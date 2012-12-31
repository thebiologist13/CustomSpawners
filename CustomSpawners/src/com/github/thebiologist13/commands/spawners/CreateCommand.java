package com.github.thebiologist13.commands.spawners;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.SpawnableEntity;
import com.github.thebiologist13.Spawner;

public class CreateCommand extends SpawnerCommand {

	public CreateCommand(CustomSpawners plugin) {
		super(plugin);
	}

	public CreateCommand(CustomSpawners plugin, String mainPerm) {
		super(plugin, mainPerm);
	}

	@Override
	public void run(Spawner spawner, CommandSender sender, String subCommand, String[] args) {
		
		if(!(sender instanceof Player)) {
			PLUGIN.sendMessage(sender, NO_CONSOLE);
			return;
		}
		
		Player player = (Player) sender;
		
		SpawnableEntity entity = null;
		
		String in = getValue(args, 0, "");
		
		if(in.isEmpty()) {
			entity = CustomSpawners.getEntity(CustomSpawners.entitySelection.get(player));
		} else {
			entity = CustomSpawners.getEntity(in);
		}
		
		if(entity == null) {
			PLUGIN.sendMessage(sender, NO_ID);
			return;
		}
		
		Block target = player.getTargetBlock(null, CONFIG.getInt("players.maxDistance", 5));
		
		if(target == null) {
			PLUGIN.sendMessage(player, ChatColor.RED + "You must look at a block to make a spawner there.");
			return;
		}
		
		int id = PLUGIN.getNextSpawnerId();
		
		Spawner newSpawner = new Spawner(entity, target.getLocation(), id);
		
		spawner.setRadius(CONFIG.getDouble("spawners.radius", 8));
		spawner.setRedstoneTriggered(CONFIG.getBoolean("spawners.redstoneTriggered", false));
		spawner.setMaxPlayerDistance(CONFIG.getInt("spawners.maxPlayerDistance", 16));
		spawner.setMinPlayerDistance(CONFIG.getInt("spawners.minPlayerDistance", 0));
		spawner.setActive(CONFIG.getBoolean("spawners.active", false));
		spawner.setMaxLightLevel((byte) CONFIG.getInt("spawners.maxLightLevel", 7));
		spawner.setMinLightLevel((byte) CONFIG.getInt("spawners.minLightLevel", 0));
		spawner.setHidden(CONFIG.getBoolean("spawners.hidden", false));
		spawner.setRate(CONFIG.getInt("spawners.rate", 120));
		spawner.setMobsPerSpawn(CONFIG.getInt("spawners.mobsPerSpawn", 2));
		spawner.setMaxMobs(CONFIG.getInt("spawners.maxMobs", 12));
		
		CustomSpawners.spawners.put(newSpawner.getId(), newSpawner);
		
		if(CONFIG.getBoolean("data.autosave") && CONFIG.getBoolean("data.saveOnCreate")) {
			PLUGIN.getFileManager().autosave(spawner);
		}
		
		PLUGIN.sendMessage(player, ChatColor.GREEN + "Successfully created a " + ChatColor.GOLD + PLUGIN.getFriendlyName(entity) + ChatColor.GREEN + 
				" spawner with ID " + ChatColor.GOLD + id + ChatColor.GREEN + "!");
		
	}
}
