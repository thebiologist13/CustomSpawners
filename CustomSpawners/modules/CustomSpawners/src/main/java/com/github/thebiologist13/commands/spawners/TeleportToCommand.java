package com.github.thebiologist13.commands.spawners;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.Spawner;

public class TeleportToCommand extends SpawnerCommand {

	public TeleportToCommand(CustomSpawners plugin) {
		super(plugin);
	}

	public TeleportToCommand(CustomSpawners plugin, String mainPerm) {
		super(plugin, mainPerm);
	}

	@Override
	public void run(Spawner spawner, CommandSender sender, String subCommand, String[] args) {
		
		if(!(sender instanceof Player)) {
			PLUGIN.sendMessage(sender, NO_CONSOLE);
			return;
		}
		
		Player player = (Player) sender;
		
		Location to = spawner.getLoc();
		to.setY(to.getBlockY() + 1);
		
		player.teleport(to);
		
		PLUGIN.sendMessage(player, ChatColor.GREEN + "Teleported to spawner " 
				+ ChatColor.GOLD + PLUGIN.getFriendlyName(spawner) + ChatColor.GREEN + ".");
	}

}
