package com.github.thebiologist13.commands.spawners;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.Spawner;

public class PerSpawnCommand extends SpawnerCommand {

	public PerSpawnCommand(CustomSpawners plugin) {
		super(plugin);
	}

	public PerSpawnCommand(CustomSpawners plugin, String mainPerm) {
		super(plugin, mainPerm);
	}

	@Override
	public void run(Spawner spawner, CommandSender sender, String subCommand, String[] args) {
		
		String in = getValue(args, 0, "2");
		
		if(!CustomSpawners.isInteger(in)) {
			PLUGIN.sendMessage(sender, NOT_INT_AMOUNT);
			return;
		}
		
		int mobs = Integer.parseInt(in);
		
		if(mobs < 0) {
			PLUGIN.sendMessage(sender, ChatColor.RED + "The mobs per spawn must be greater than zero.");
			return;
		}
		
		if(mobs > CONFIG.getInt("spawners.mobsPerSpawnLimit", 16) && !permissible(sender, "customspawners.limitoverride")) {
			PLUGIN.sendMessage(sender, NO_OVERRIDE);
			return;
		}
		
		spawner.setMobsPerSpawn(mobs);
		
		PLUGIN.sendMessage(sender, getSuccessMessage(spawner, "mobs per spawn", in));
		
	}
	
}
