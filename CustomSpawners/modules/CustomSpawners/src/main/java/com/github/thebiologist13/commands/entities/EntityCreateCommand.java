package com.github.thebiologist13.commands.entities;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.SpawnableEntity;

public class EntityCreateCommand extends EntityCommand {

	public EntityCreateCommand(CustomSpawners plugin) {
		super(plugin);
	}

	public EntityCreateCommand(CustomSpawners plugin, String mainPerm) {
		super(plugin, mainPerm);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void run(SpawnableEntity entity, CommandSender sender, String subCommand, String[] args) {
		
		final String NOT_ALLOWED_ENTITY = ChatColor.RED + "That entity type is disabled for those without permission.";
		final String NONEXISTANT_ENTITY = ChatColor.RED + "That is not a entity type";
		
		String in = getValue(args, 0, "pig");
		
		SpawnableEntity newEntity = new SpawnableEntity(EntityType.PIG, PLUGIN.getNextEntityId());
		
		List<?> notAllowed = CONFIG.getList("mobs.blacklist");
		
		boolean hasOverride = true;
		
		if(sender instanceof Player) {
			hasOverride = ((Player) sender).hasPermission("customspawners.limitoverride");
		}
		
		if(in.equals("spiderjockey") || in.equals("spider_jockey") ||
				in.equals("skeletonjockey") || in.equals("skeleton_jockey")) {
			newEntity.setType(EntityType.SPIDER);
			newEntity.setJockey(true);
			if((notAllowed.contains("spider_jockey") || notAllowed.contains("skeleton_jockey")) && !hasOverride) {
				PLUGIN.sendMessage(sender, NOT_ALLOWED_ENTITY);
				return;
			}
		} else if(in.equals("witherskeleton") || in.equals("wither_skeleton")) {
			newEntity.setType(EntityType.SKELETON);
			newEntity.setProp("wither", true);
			if(notAllowed.contains("wither_skeleton") && !hasOverride) {
				PLUGIN.sendMessage(sender, NOT_ALLOWED_ENTITY);
				return;
			}
		} else if(in.equals("crepp")) {
			newEntity.setType(EntityType.CREEPER);
			newEntity.setCharged(true);
			newEntity.setYield(0);
			if(notAllowed.contains("creeper") && !hasOverride) {
				PLUGIN.sendMessage(sender, NOT_ALLOWED_ENTITY);
				return;
			}
		} else if(in.equals("chargedcreeper") || in.equals("charged_creeper") ||
				in.equals("powercreeper") || in.equals("power_creeper")) {
			newEntity.setType(EntityType.CREEPER);
			newEntity.setCharged(true);
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
			
			newEntity.setType(type);
		}
		
		CustomSpawners.entities.put(newEntity.getId(), newEntity);
		
		if(CONFIG.getBoolean("data.autosave") && CONFIG.getBoolean("data.saveOnCreate")) {
			PLUGIN.getFileManager().autosave(newEntity);
		}
		
		PLUGIN.sendMessage(sender, ChatColor.GREEN + "Successfully created a new " + ChatColor.GOLD + 
				in + ChatColor.GREEN + " entity with ID number " + ChatColor.GOLD + newEntity.getId() + ChatColor.GREEN + "!");
		
	}
	
}
