package com.github.thebiologist13.commands.spawners;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.Spawner;

public class NameCommand extends SpawnerCommand {

	public NameCommand(CustomSpawners plugin) {
		super(plugin);
	}

	public NameCommand(CustomSpawners plugin, String mainPerm) {
		super(plugin, mainPerm);
	}

	@Override
	public void run(Spawner spawner, CommandSender sender, String subCommand, String[] args) {
		
		String in = getValue(args, 0, "");
		
		if(in.isEmpty()) {
			PLUGIN.sendMessage(sender, ChatColor.RED + "You must provide a name.");
			return;
		}
		
		if(in.equalsIgnoreCase(spawner.getName())) {
			PLUGIN.sendMessage(sender, ChatColor.RED + "That spawner already has this name!");
			return;
		}
		
		if(CustomSpawners.getSpawner(in) != null) {
			PLUGIN.sendMessage(sender, ChatColor.RED + "That name is already taken for a spawner.");
			return;
		}
		
		spawner.setName(in);
		
		PLUGIN.sendMessage(sender, ChatColor.GREEN + "Renamed spawner to be " +
				ChatColor.GOLD + PLUGIN.getFriendlyName(spawner) + ChatColor.GREEN + "!");
		
	}

}
