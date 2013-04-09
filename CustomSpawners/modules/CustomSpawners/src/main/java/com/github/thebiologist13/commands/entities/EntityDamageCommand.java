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
			final String MUST_BE_INTEGER = ChatColor.RED + "The damage dealt must be an integer.";
			
			String in = getValue(args, 0, "2");
			
			if(!CustomSpawners.isInteger(in)) {
				PLUGIN.sendMessage(sender, MUST_BE_INTEGER);
				return;
			}
			
			entity.setDamage(Integer.parseInt(in));
			
			PLUGIN.sendMessage(sender, getSuccessMessage(entity, "damage dealt", in));
		}
		
	}

}
