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
		int xp = 0;
		
		if(in.equals("random") || in.equals("normal") || in.equals("-1"))
			xp = -1;
		
		if(!CustomSpawners.isInteger(in)) {
			PLUGIN.sendMessage(sender, ChatColor.RED + "The dropped experience must be an integer.");
			return;
		}
		
		xp = Integer.parseInt(in);
		
		entity.setDroppedExp(xp);
		
		PLUGIN.sendMessage(sender, getSuccessMessage(entity, "dropped experience", in));
		
	}

}
