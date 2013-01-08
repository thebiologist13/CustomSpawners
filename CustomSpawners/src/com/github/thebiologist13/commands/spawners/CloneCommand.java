package com.github.thebiologist13.commands.spawners;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.Spawner;

public class CloneCommand extends SpawnerCommand {

	public CloneCommand(CustomSpawners plugin) {
		super(plugin);
	}

	public CloneCommand(CustomSpawners plugin, String mainPerm) {
		super(plugin, mainPerm);
	}

	@Override
	public void run(Spawner spawner, CommandSender sender, String subCommand, String[] args) {
		if(!(sender instanceof Player)) {
			PLUGIN.sendMessage(sender, NO_CONSOLE);
			return;
		}
		
		Player player = (Player) sender;
		
		Location target = player.getTargetBlock(null, CONFIG.getInt("players.maxDistance", 5)).getLocation();
		
		if(target == null) {
			PLUGIN.sendMessage(player, ChatColor.RED + "You must look at a block to make a spawner there.");
			return;
		}
		
		int id = PLUGIN.getNextSpawnerId();
		Spawner spawner1 = PLUGIN.cloneWithNewId(spawner);
		spawner1.setName("");
		spawner1.setLoc(target);
		CustomSpawners.spawners.put(id, spawner1);
		
		PLUGIN.sendMessage(player, ChatColor.GREEN + "Cloned spawner " + ChatColor.GOLD + 
				PLUGIN.getFriendlyName(spawner) + ChatColor.GREEN + " to " + ChatColor.GOLD + 
				"(" + target.getBlockX() + "," + target.getBlockY() + "," + target.getBlockZ() + ")" +
				ChatColor.GREEN + ". The new spawner is " + ChatColor.GOLD + PLUGIN.getFriendlyName(spawner1));
		
	}

}
