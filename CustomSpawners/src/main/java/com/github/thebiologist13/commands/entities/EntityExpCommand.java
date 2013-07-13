package com.github.thebiologist13.commands.entities;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.SpawnableEntity;

public class EntityExpCommand extends EntityCommand {

	public EntityExpCommand(CustomSpawners plugin) {
		super(plugin);
	}
	
	public EntityExpCommand(CustomSpawners plugin, String mainPerm) {
		super(plugin, mainPerm);
	}
	
	@Override
	public void run(SpawnableEntity entity, CommandSender sender, String subCommand, String[] args) {
		
		String in = getValue(args, 0, "-1");
		
		if(in.equals("vanilla") || in.equals("random") || in.equals("normal") || in.equals("-1")) {
			entity.setDroppedExp(-1);
			
			PLUGIN.sendMessage(sender, getSuccessMessage(entity, "dropped experience", "vanilla amount"));
		}
		
		try {
			entity.setDroppedExp(handleDynamic(in, entity.getDroppedExp(null)));
			PLUGIN.sendMessage(sender, getSuccessMessage(entity, "dropped experience", in));
		} catch(IllegalArgumentException e) {
			PLUGIN.sendMessage(sender, ChatColor.RED + "The dropped experience must be an integer.");
		}
		
	}

}
