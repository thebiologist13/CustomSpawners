package com.github.thebiologist13.commands.spawners;

import java.util.Iterator;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.Spawner;

public class DeactivateAllCommand extends SpawnerCommand {

	public DeactivateAllCommand(CustomSpawners plugin) {
		super(plugin);
	}

	public DeactivateAllCommand(CustomSpawners plugin, String mainPerm) {
		super(plugin, mainPerm);
	}

	@Override
	public void run(Spawner spawner, CommandSender sender, String subCommand, String[] args) {
		Iterator<Spawner> itr = CustomSpawners.spawners.values().iterator();
		while(itr.hasNext()) {
			itr.next().setActive(false);
		}
		PLUGIN.sendMessage(sender, ChatColor.GREEN + "All spawners set inactive.");
	}
	
}
