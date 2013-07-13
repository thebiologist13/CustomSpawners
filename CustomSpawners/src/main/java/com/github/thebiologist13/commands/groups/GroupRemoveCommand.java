package com.github.thebiologist13.commands.groups;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.Group;

public class GroupRemoveCommand extends GroupCommand {

	public GroupRemoveCommand(CustomSpawners plugin) {
		super(plugin);
	}

	public GroupRemoveCommand(CustomSpawners plugin, String mainPerm) {
		super(plugin, mainPerm);
	}

	@Override
	public void run(Group group, CommandSender sender, String subCommand, String[] args) {
		
		int id = group.getId();
		
		PLUGIN.removeGroup(group);
		PLUGIN.getFileManager().removeGroupDataFile(id);
		
		PLUGIN.sendMessage(sender, ChatColor.GREEN + "Group " + ChatColor.GOLD + 
				id + ChatColor.GREEN + " has been removed from the server.");
		
	}

}
