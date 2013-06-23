package com.github.thebiologist13.commands.groups;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.Group;
import com.github.thebiologist13.api.IObject;

public class GroupClearCommand extends GroupCommand {

	public GroupClearCommand(CustomSpawners plugin) {
		super(plugin);
	}

	public GroupClearCommand(CustomSpawners plugin, String mainPerm) {
		super(plugin, mainPerm);
	}

	@Override
	public void run(Group group, CommandSender sender, String subCommand, String[] args) {
		group.setGroup(new HashMap<IObject, Integer>());
		
		PLUGIN.sendMessage(sender, ChatColor.GREEN + "Cleared group " + ChatColor.GOLD
				+ PLUGIN.getFriendlyName(group) + ChatColor.GREEN + "!");
	}

}
