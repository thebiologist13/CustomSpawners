package com.github.thebiologist13.commands.entities;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.SpawnableEntity;

public class EntitySetTypeCommand extends EntityCommand {

	public EntitySetTypeCommand(CustomSpawners plugin) {
		super(plugin);
	}

	public EntitySetTypeCommand(CustomSpawners plugin, String mainPerm) {
		super(plugin, mainPerm);
	}

	@Override
	public void run(SpawnableEntity entity, CommandSender sender, String subCommand, String[] args) {
		
		final String NOT_ALLOWED_ENTITY = ChatColor.RED + "That entity type is disabled for those without permission.";
		final String NONEXISTANT_ENTITY = ChatColor.RED + "That is not a entity type";
		
		String in = getValue(args, 0, "pig");
		
		boolean hasOverride = true;
		
		if(sender instanceof Player) {
			hasOverride = ((Player) sender).hasPermission("customspawners.limitoverride");
		}
		
		try {
			PLUGIN.parseEntityType(in, entity, hasOverride);
		} catch(IllegalArgumentException e) {
			
			if(e.getMessage() == null) {
				e.printStackTrace();
				return;
			}
			
			if(e.getMessage().equals("Invalid entity type.")) {
				PLUGIN.sendMessage(sender, NONEXISTANT_ENTITY);
				return;
			} else if(e.getMessage().equals("Not allowed entity.")) {
				PLUGIN.sendMessage(sender, NOT_ALLOWED_ENTITY);
				return;
			}
		}
		
		PLUGIN.sendMessage(sender, getSuccessMessage(entity, "entity type", in));
		
	}

}
