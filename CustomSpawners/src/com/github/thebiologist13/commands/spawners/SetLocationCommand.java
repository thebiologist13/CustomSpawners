package com.github.thebiologist13.commands.spawners;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.Spawner;

public class SetLocationCommand extends SpawnerCommand {

	public SetLocationCommand(CustomSpawners plugin) {
		super(plugin);
	}

	public SetLocationCommand(CustomSpawners plugin, String mainPerm) {
		super(plugin, mainPerm);
	}

	@Override
	public void run(Spawner spawner, CommandSender sender, String subCommand,String[] args) {
		
		if(!(sender instanceof Player)) {
			PLUGIN.sendMessage(sender, NO_CONSOLE);
			return;
		}
		
		Player player = (Player) sender;
		
		Block target = player.getTargetBlock(CustomSpawners.transparent, CONFIG.getInt("players.maxDistance", 5));
		
		if(target == null) {
			PLUGIN.sendMessage(player, ChatColor.RED + "You must look at a block to set the location there.");
			return;
		}
		
		spawner.setLoc(target.getLocation());
		
		PLUGIN.sendMessage(player, ChatColor.GREEN + "Set the location of spawner " + 
				ChatColor.GOLD + PLUGIN.getFriendlyName(spawner) + ChatColor.GREEN + 
				" to " + ChatColor.GOLD + "(" + spawner.getLoc().getBlockX() + ", " + 
				spawner.getLoc().getBlockY() + ", " + spawner.getLoc().getBlockZ() + ")" + 
				ChatColor.GREEN + "!");
		
	}
}
