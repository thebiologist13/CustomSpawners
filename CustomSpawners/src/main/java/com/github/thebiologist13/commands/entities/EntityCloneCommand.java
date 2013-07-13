package com.github.thebiologist13.commands.entities;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.SpawnableEntity;

public class EntityCloneCommand extends EntityCommand {

	public EntityCloneCommand(CustomSpawners plugin) {
		super(plugin);
	}

	public EntityCloneCommand(CustomSpawners plugin, String mainPerm) {
		super(plugin, mainPerm);
	}

	@Override
	public void run(SpawnableEntity entity, CommandSender sender, String subCommand, String[] args) {
		SpawnableEntity entity1 = PLUGIN.cloneWithNewId(entity);
		
		PLUGIN.sendMessage(sender, ChatColor.GREEN + "Cloned entity " + ChatColor.GOLD + 
				PLUGIN.getFriendlyName(entity) + ". The new entity is " + ChatColor.GOLD +
				PLUGIN.getFriendlyName(entity1));
	}

}
