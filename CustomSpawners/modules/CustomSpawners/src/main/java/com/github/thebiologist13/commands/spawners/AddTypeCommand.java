package com.github.thebiologist13.commands.spawners;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.SpawnableEntity;
import com.github.thebiologist13.Spawner;

public class AddTypeCommand extends SpawnerCommand {

	public AddTypeCommand(CustomSpawners plugin) {
		super(plugin);
	}

	public AddTypeCommand(CustomSpawners plugin, String mainPerm) {
		super(plugin, mainPerm);
	}

	@Override
	public void run(Spawner spawner, CommandSender sender, String subCommand, String[] args) {
		
		String in = getValue(args, 0, "");
		
		SpawnableEntity type = CustomSpawners.getEntity(in);
		
		if(type == null) {
			PLUGIN.sendMessage(sender, NO_ID);
			return;
		}
		
		spawner.addTypeData(type);
		
		PLUGIN.sendMessage(sender, ChatColor.GREEN + "Added spawnable entity " + ChatColor.GOLD + PLUGIN.getFriendlyName(type)
				+ ChatColor.GREEN + " to spawner " + ChatColor.GOLD + PLUGIN.getFriendlyName(spawner) + ChatColor.GREEN + "!");
		
	}

}
