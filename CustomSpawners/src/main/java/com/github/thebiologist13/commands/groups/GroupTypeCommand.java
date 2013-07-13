package com.github.thebiologist13.commands.groups;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.Group;
import com.github.thebiologist13.api.IObject;

public class GroupTypeCommand extends GroupCommand {

	public GroupTypeCommand(CustomSpawners plugin) {
		super(plugin);
	}

	public GroupTypeCommand(CustomSpawners plugin, String mainPerm) {
		super(plugin, mainPerm);
	}

	@Override
	public void run(Group group, CommandSender sender, String subCommand, String[] args) {
		
		String typeIn = getValue(args, 0, "spawner");
		
		Group.Type type = Group.Type.fromName(typeIn);
		
		if(type == null) {
			PLUGIN.sendMessage(sender, TYPE_NOT_DEFINED);
			return;
		}
		
		group.setGroup(new HashMap<IObject, Integer>());
		group.setType(type);
		
		PLUGIN.sendMessage(sender, ChatColor.GREEN + "Set group " + ChatColor.GOLD
				+ PLUGIN.getFriendlyName(group) + ChatColor.GREEN + " to be type " 
				+ ChatColor.GOLD + typeIn + ChatColor.GREEN + "!");
	}

}
