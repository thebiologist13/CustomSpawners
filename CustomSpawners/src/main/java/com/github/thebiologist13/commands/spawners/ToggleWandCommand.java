package com.github.thebiologist13.commands.spawners;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.Spawner;

public class ToggleWandCommand extends SpawnerCommand {

	public ToggleWandCommand(CustomSpawners plugin) {
		super(plugin);
	}

	public ToggleWandCommand(CustomSpawners plugin, String mainPerm) {
		super(plugin, mainPerm);
	}

	@Override
	public void run(Spawner spawner, CommandSender sender, String subCommand, String[] args) {
		if(!(sender instanceof Player)) {
			PLUGIN.sendMessage(sender, NO_CONSOLE);
			return;
		}
		
		Player player = (Player) sender;
		
		if(CustomSpawners.selectMode.containsKey(player)) {
			CustomSpawners.selectMode.replace(player, !CustomSpawners.selectMode.get(player));
		} else {
			CustomSpawners.selectMode.put(player, true);
		}
		
		PLUGIN.sendMessage(player, ChatColor.GREEN + "Toggled CustomSpawners spawn area selection to " + ChatColor.GOLD + CustomSpawners.selectMode.get(player));
		
	}

}
