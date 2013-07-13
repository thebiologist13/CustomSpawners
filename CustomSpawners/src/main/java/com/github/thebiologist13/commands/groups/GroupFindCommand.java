package com.github.thebiologist13.commands.groups;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.Group;
import com.github.thebiologist13.api.IObject;
import com.github.thebiologist13.commands.SelectionParser;

public class GroupFindCommand extends GroupCommand {

	public GroupFindCommand(CustomSpawners plugin) {
		super(plugin);
	}

	public GroupFindCommand(CustomSpawners plugin, String mainPerm) {
		super(plugin, mainPerm);
	}

	@Override
	public void run(Group group, CommandSender sender, String subCommand, String[] args) {

		String typeIn = getValue(args, 0, "spawner");
		String idIn = getValue(args, 1, "0");

		if(!hasType(typeIn)) {
			PLUGIN.sendMessage(sender, TYPE_NOT_DEFINED);
			return;
		}

		Group.Type type = Group.Type.fromName(typeIn);
		IObject id = SelectionParser.getCorrect(idIn, type);
		
		if(id == null) { 
			PLUGIN.sendMessage(sender, NO_ID);
			return;
		}

		List<Group> inside = PLUGIN.findObjectInGroups(id);

		String msg = "";
		for(int i = 0; i < inside.size(); i++) {
			if(i == 0) {
				msg += PLUGIN.getFriendlyName(inside.get(i));
			} else {
				msg += ", " + PLUGIN.getFriendlyName(inside.get(i));
			}
		}

		if(inside.size() == 0) {
			PLUGIN.sendMessage(sender, ChatColor.GREEN + "Object " +  ChatColor.GOLD + 
					idIn + ChatColor.GREEN + " is not in any groups.");
			return;
		}
		
		PLUGIN.sendMessage(sender, ChatColor.GREEN + "Object " +  ChatColor.GOLD + 
				idIn + ChatColor.GREEN + " is in the following group(s): " + ChatColor.GOLD + msg);

	}

}
