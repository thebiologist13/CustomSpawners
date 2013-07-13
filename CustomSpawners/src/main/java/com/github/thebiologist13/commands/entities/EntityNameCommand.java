package com.github.thebiologist13.commands.entities;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.SpawnableEntity;

public class EntityNameCommand extends EntityCommand {

	public EntityNameCommand(CustomSpawners plugin) {
		super(plugin);
	}

	public EntityNameCommand(CustomSpawners plugin, String mainPerm) {
		super(plugin, mainPerm);
	}

	//FIXME Not showing up?
	@Override
	public void run(SpawnableEntity entity, CommandSender sender, String subCommand, String[] args) {
		
		if(subCommand.equals("showname")) {
			String in = getValue(args, 0, "false");
			entity.setShowName(Boolean.parseBoolean(in));
			
			PLUGIN.sendMessage(sender, getSuccessMessage(entity, "show custom name", in));
			return;
		}
		
		String in = getValue(args, 0, "");
		
		if(in.isEmpty()) {
			PLUGIN.sendMessage(sender, ChatColor.RED + "You must provide a name.");
			return;
		}
		
		if(in.equalsIgnoreCase(entity.getName())) {
			PLUGIN.sendMessage(sender, ChatColor.RED + "That entity already has this name!");
			return;
		}
		
		if(CustomSpawners.getEntity(in) != null) {
			PLUGIN.sendMessage(sender, ChatColor.RED + "That name is already taken for an entity.");
			return;
		}
		
		entity.setName(in);
		
		PLUGIN.sendMessage(sender, ChatColor.GREEN + "Renamed entity to be " +
				ChatColor.GOLD + PLUGIN.getFriendlyName(entity) + ChatColor.GREEN + "!");
		
	}

}