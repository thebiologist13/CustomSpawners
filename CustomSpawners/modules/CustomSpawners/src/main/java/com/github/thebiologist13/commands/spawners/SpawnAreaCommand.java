package com.github.thebiologist13.commands.spawners;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.Spawner;

public class SpawnAreaCommand extends SpawnerCommand {

	public SpawnAreaCommand(CustomSpawners plugin) {
		super(plugin);
	}

	public SpawnAreaCommand(CustomSpawners plugin, String mainPerm) {
		super(plugin, mainPerm);
	}

	@Override
	public void run(Spawner spawner, CommandSender sender, String subCommand, String[] args) {
		
		if(!(sender instanceof Player)) {
			PLUGIN.sendMessage(sender, NO_CONSOLE);
			return;
		}
		
		Player player = (Player) sender;
		
		String in = getValue(args, 0, "false");
		
		boolean useSpawnArea = Boolean.parseBoolean(in);
		
		if(!useSpawnArea) {
			
			spawner.setUseSpawnArea(useSpawnArea);
			
		} else if(CustomSpawners.selectionPointOne.containsKey(player) && 
				CustomSpawners.selectionPointTwo.containsKey(player)) {
			
			World p1World = CustomSpawners.selectionPointOne.get(player).getWorld();
			World p2World = CustomSpawners.selectionPointTwo.get(player).getWorld();
			
			if(!p1World.equals(p2World)) {
				PLUGIN.sendMessage(player, ChatColor.RED + "Spawn area selection points must be in the same world.");
				return;
			}
			
			Location[] areaPoints = new Location[2];
			
			areaPoints[0] = CustomSpawners.selectionPointOne.get(player);
			areaPoints[1] = CustomSpawners.selectionPointTwo.get(player);
			
			//Set
			spawner.setAreaPoints(areaPoints);
			spawner.setUseSpawnArea(useSpawnArea);
			
		} else {
			PLUGIN.sendMessage(player, ChatColor.RED + "You must have an area selected for a spawner to use as the spawn area.");
			return;
		}
		
		//Success Message
		PLUGIN.sendMessage(player, ChatColor.GREEN + "Set the spawn area of spawner " + 
				ChatColor.GOLD + PLUGIN.getFriendlyName(spawner) + ChatColor.GREEN + " to " + ChatColor.GOLD +
				String.valueOf(useSpawnArea) + ChatColor.GREEN + "!");
		
	}

}
