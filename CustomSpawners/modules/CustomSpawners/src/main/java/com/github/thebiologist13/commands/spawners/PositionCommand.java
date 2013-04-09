package com.github.thebiologist13.commands.spawners;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.Spawner;

public class PositionCommand extends SpawnerCommand {

	public PositionCommand(CustomSpawners plugin) {
		super(plugin);
	}

	public PositionCommand(CustomSpawners plugin, String mainPerm) {
		super(plugin, mainPerm);
	}

	@Override
	public void run(Spawner spawner, CommandSender sender, String subCommand, String[] args) {
		
		if(!(sender instanceof Player)) {
			PLUGIN.sendMessage(sender, NO_CONSOLE);
			return;
		}
		
		Player player = (Player) sender;
		Location location = player.getLocation();
		
		if(subCommand.equals("pos1")) {
			
			CustomSpawners.selectionPointOne.put(player, location);
			
			PLUGIN.sendMessage(player, ChatColor.GREEN + "Set spawn area selection point one to: " + ChatColor.GOLD + "(" +
					location.getBlockX() + "," + location.getBlockY() + "," + location.getBlockZ() + ")" + ChatColor.GREEN + ".");
			
		} else if(subCommand.equals("pos2")) {
			
			CustomSpawners.selectionPointTwo.put(player, location);
			
			PLUGIN.sendMessage(player, ChatColor.GREEN + "Set spawn area selection point two to: " + ChatColor.GOLD + "(" +
					location.getBlockX() + "," + location.getBlockY() + "," + location.getBlockZ() + ")" + ChatColor.GREEN + ".");
			
		}
		
	}

}