package com.github.thebiologist13.commands.groups;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.Group;

public class GroupCreateCommand extends GroupCommand {

	public GroupCreateCommand(CustomSpawners plugin) {
		super(plugin);
	}

	public GroupCreateCommand(CustomSpawners plugin, String mainPerm) {
		super(plugin, mainPerm);
	}

	@Override
	public void run(Group group, CommandSender sender, String subCommand, String[] args) {

		String in = getValue(args, 0, "spawner");
		
		Group.Type type = Group.Type.fromName(in);
		
		if(type == null) {
			PLUGIN.sendMessage(sender, TYPE_NOT_DEFINED);
			return;
		}
		
		Group newGroup = new Group(PLUGIN.getNextGroupId(), type);
		
		CustomSpawners.groups.put(newGroup.getId(), newGroup);
		
		if(CONFIG.getBoolean("data.autosave") && CONFIG.getBoolean("data.saveOnCreate")) {
			PLUGIN.getFileManager().autosave(newGroup);
		}
		
		PLUGIN.sendMessage(sender, ChatColor.GREEN + "Successfully created a new " + 
				ChatColor.GOLD + in + ChatColor.GREEN + " group with ID number " + 
				ChatColor.GOLD + newGroup.getId() + ChatColor.GREEN + "!");	
	}

}
