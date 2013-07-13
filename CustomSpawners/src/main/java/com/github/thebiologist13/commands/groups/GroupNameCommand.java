package com.github.thebiologist13.commands.groups;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.Group;

public class GroupNameCommand extends GroupCommand {

	public GroupNameCommand(CustomSpawners plugin) {
		super(plugin);
	}

	public GroupNameCommand(CustomSpawners plugin, String mainPerm) {
		super(plugin, mainPerm);
	}

	@Override
	public void run(Group group, CommandSender sender, String subCommand, String[] args) {
		
		String in = getValue(args, 0, "");

		if(in.isEmpty()) {
			PLUGIN.sendMessage(sender, ChatColor.RED + "You must provide a name.");
			return;
		}

		if(in.equalsIgnoreCase(group.getName())) {
			PLUGIN.sendMessage(sender, ChatColor.RED + "That group already has this name!");
			return;
		}

		group.setName(in);

		PLUGIN.sendMessage(sender, ChatColor.GREEN + "Renamed group to be " +
				ChatColor.GOLD + PLUGIN.getFriendlyName(group) + ChatColor.GREEN + "!");
		
	}

}
