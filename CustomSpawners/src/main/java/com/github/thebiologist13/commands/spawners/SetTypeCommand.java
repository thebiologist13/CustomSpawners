package com.github.thebiologist13.commands.spawners;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.SpawnableEntity;
import com.github.thebiologist13.Spawner;

public class SetTypeCommand extends SpawnerCommand {

	public SetTypeCommand(CustomSpawners plugin) {
		super(plugin);
	}

	public SetTypeCommand(CustomSpawners plugin, String mainPerm) {
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
		
		checkRecursiveSpawns(type, sender);
		
		List<Integer> typeList = new ArrayList<Integer>();
		typeList.add(type.getId());
		spawner.setTypeData(typeList);
		
		PLUGIN.sendMessage(sender, ChatColor.GREEN + "Made spawnable entity " + ChatColor.GOLD + PLUGIN.getFriendlyName(type)
				+ ChatColor.GREEN + " the spawned type of spawner " + ChatColor.GOLD + PLUGIN.getFriendlyName(spawner) + ChatColor.GREEN + "!");
		
	}
	
}
