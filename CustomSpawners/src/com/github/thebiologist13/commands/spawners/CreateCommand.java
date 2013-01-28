package com.github.thebiologist13.commands.spawners;

import org.bukkit.ChatColor;
import org.bukkit.Material;
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
		
		Block target = player.getTargetBlock(CustomSpawners.transparent, CONFIG.getInt("players.maxDistance", 5));
		
		if(target == null) {
			PLUGIN.sendMessage(player, ChatColor.RED + "You must look at a block to make a spawner there.");
			return;
		}
		
		if(target.getType().equals(Material.AIR)) {
			PLUGIN.sendMessage(player, ChatColor.RED + "You must look at a block to make a spawner there.");
			return;
		}
		
		Spawner newSpawner = PLUGIN.createSpawner(entity, target.getLocation());
		
		PLUGIN.sendMessage(player, ChatColor.GREEN + "Successfully created a " + ChatColor.GOLD + PLUGIN.getFriendlyName(entity) + ChatColor.GREEN + 
				" spawner with ID " + ChatColor.GOLD + PLUGIN.getFriendlyName(newSpawner) + ChatColor.GREEN + "!");
		
	}
}
