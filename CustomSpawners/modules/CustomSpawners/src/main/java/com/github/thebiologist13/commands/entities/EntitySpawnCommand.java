package com.github.thebiologist13.commands.entities;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.SpawnableEntity;

public class EntitySpawnCommand extends EntityCommand {

	public EntitySpawnCommand(CustomSpawners plugin) {
		super(plugin);
	}

	public EntitySpawnCommand(CustomSpawners plugin, String mainPerm) {
		super(plugin, mainPerm);
	}

	@Override
	public void run(SpawnableEntity entity, CommandSender sender, String subCommand, String[] args) {
		
		if(!(sender instanceof Player)) {
			PLUGIN.sendMessage(sender, NO_CONSOLE);
			return;
		}
		
		Player p = (Player) sender;
		
		String in = getValue(args, 0, "1");
		
		if(!CustomSpawners.isInteger(in)) {
			PLUGIN.sendMessage(p, NOT_INT_AMOUNT);
			return;
		}
		
		Location target = p.getTargetBlock(CustomSpawners.transparent, 25).getLocation();
		if(target == null) {
			PLUGIN.sendMessage(p, ChatColor.RED + "You must look at a block to spawn mobs there.");
			return;
		}
		
		target.setY(target.getY() + 1);
		
		int count = Integer.parseInt(in);
		
		for(int i = 0; i < count; i++) {
			CustomSpawners.serverSpawner.forceSpawnOnLoc(entity, target);
		}
		
		PLUGIN.sendMessage(p, ChatColor.GREEN + "Spawned " + ChatColor.GOLD + count 
				+ ChatColor.GREEN + " mobs of type " + ChatColor.GOLD + PLUGIN.getFriendlyName(entity) 
				+ ChatColor.GREEN + " at " + ChatColor.GOLD + "(" + target.getBlockX() + ", "
				+ target.getBlockY() + ", " + target.getBlockZ() + ")" + ChatColor.GREEN + ".");
		
	}

}
