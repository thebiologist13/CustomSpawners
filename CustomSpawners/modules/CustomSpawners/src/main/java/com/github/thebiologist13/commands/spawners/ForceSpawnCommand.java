package com.github.thebiologist13.commands.spawners;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.Spawner;

public class ForceSpawnCommand extends SpawnerCommand {

	public ForceSpawnCommand(CustomSpawners plugin) {
		super(plugin);
	}
	
	public ForceSpawnCommand(CustomSpawners plugin, String mainPerm) {
		super(plugin, mainPerm);
	}

	@Override
	public void run(Spawner spawner, CommandSender sender, String subCommand, String[] args) {
		
		if(spawner.isHidden() && !permissible(sender, "customspawners.forcespawn.hidden")) {
			PLUGIN.sendMessage(sender, ChatColor.RED + "You are not allowed to force spawns for that spawner!");
			return;
		}
		
		spawner.forceSpawn();
		
	}
	
}
