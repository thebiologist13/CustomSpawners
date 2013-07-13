package com.github.thebiologist13.commands.entities;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.SpawnableEntity;

public class EntityDamageCommand extends EntityCommand {
	
	public EntityDamageCommand(CustomSpawners plugin) {
		super(plugin);
	}
	
	public EntityDamageCommand(CustomSpawners plugin, String mainPerm) {
		super(plugin, mainPerm);
	}

	@Override
	public void run(SpawnableEntity entity, CommandSender sender, String subCommand, String[] args) {
		
		if(subCommand.equals("setcustomdamage")) {
			String in = getValue(args, 0, "false");
			entity.setUsingCustomDamage(Boolean.parseBoolean(in));
			
			PLUGIN.sendMessage(sender, getSuccessMessage(entity, "use custom damage", in));
		} else if(subCommand.equals("setdamageamount")) {
			
			String in = getValue(args, 0, "2");
			
			try {
				entity.setDamage(handleDynamic(in, entity.getDamage(null)));
				
				PLUGIN.sendMessage(sender, getSuccessMessage(entity, "damage dealt", in));
			} catch(IllegalArgumentException e) {
				PLUGIN.sendMessage(sender, ChatColor.RED + "The damage dealt must be an integer.");
			}
			
		}
		
	}

}
