package com.github.thebiologist13.commands.entities;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
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
		
		List<?> notAllowed = CONFIG.getList("mobs.blacklist");
		
		boolean hasOverride = true;
		
		if(sender instanceof Player) {
			hasOverride = ((Player) sender).hasPermission("customspawners.limitoverride");
		}
		
		if(in.equals("spiderjockey") || in.equals("spider_jockey") ||
				in.equals("skeletonjockey") || in.equals("skeleton_jockey")) {
			entity.setType(EntityType.SPIDER);
			entity.setJockey(true);
			if((notAllowed.contains("spider_jockey") || notAllowed.contains("skeleton_jockey")) && !hasOverride) {
				PLUGIN.sendMessage(sender, NOT_ALLOWED_ENTITY);
				return;
			}
		} else if(in.equals("witherskeleton") || in.equals("wither_skeleton")) {
			entity.setType(EntityType.SKELETON);
			entity.setProp("wither", true);
			if(notAllowed.contains("wither_skeleton") && !hasOverride) {
				PLUGIN.sendMessage(sender, NOT_ALLOWED_ENTITY);
				return;
			}
		} else if(in.equals("crepp")) {
			entity.setType(EntityType.CREEPER);
			entity.setCharged(true);
			entity.setYield(0);
			if(notAllowed.contains("creeper") && !hasOverride) {
				PLUGIN.sendMessage(sender, NOT_ALLOWED_ENTITY);
				return;
			}
		} else {
			EntityType type = PLUGIN.parseEntityType(in, hasOverride);
			
			if(type == null) {
				PLUGIN.sendMessage(sender, NONEXISTANT_ENTITY);
				return;
			}
			
			if(!PLUGIN.allowedEntity(type)) {
				PLUGIN.sendMessage(sender, NOT_ALLOWED_ENTITY);
				return;
			}
			
			entity.setType(type);
		}
		
		PLUGIN.sendMessage(sender, getSuccessMessage(entity, "entity type", in));
		
	}

}