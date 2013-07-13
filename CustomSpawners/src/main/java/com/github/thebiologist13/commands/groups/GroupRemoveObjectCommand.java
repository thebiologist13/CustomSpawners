package com.github.thebiologist13.commands.groups;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.Group;
import com.github.thebiologist13.api.IObject;
import com.github.thebiologist13.commands.SelectionParser;

public class GroupRemoveObjectCommand extends GroupCommand {

	public GroupRemoveObjectCommand(CustomSpawners plugin) {
		super(plugin);
	}

	public GroupRemoveObjectCommand(CustomSpawners plugin, String mainPerm) {
		super(plugin, mainPerm);
	}

	@Override
	public void run(Group group, CommandSender sender, String subCommand, String[] args) {

		String typeIn = getValue(args, 0, "");
		String idIn = getValue(args, 1, "");
		
		if(!hasType(typeIn)) {
			PLUGIN.sendMessage(sender, TYPE_NOT_DEFINED);
			return;
		}
		
		Group.Type type = Group.Type.fromName(typeIn);
		IObject id = null;
		
		try {
			id = SelectionParser.get(idIn, type, group.getType());
		} catch (ParentChildException e) {
			PLUGIN.sendMessage(sender, PARENT_CHILD);
			return;
		} catch (TypeException e) {
			PLUGIN.sendMessage(sender, NOT_SAME_TYPE);
			return;
		}
		
		if(id == null) { 
			PLUGIN.sendMessage(sender, NO_ID);
			return;
		}
		
		if(!valid(id, group)) {
			PLUGIN.sendMessage(sender, NOT_SAME_TYPE);
			return;
		}
		
		if(id instanceof Group) {
			Group g = (Group) id;
			
			if(g.getId() == -1) {
				for(IObject i : g.getGroup().keySet()) {
					group.removeItem(i);
				}
			} else {
				group.removeItem(id);
			}
		} else {
			group.removeItem(id);
		}
		
		PLUGIN.sendMessage(sender, ChatColor.GREEN + "Removed object " + ChatColor.GOLD
				+ idIn + ChatColor.GREEN + " from group " + ChatColor.GOLD + PLUGIN.getFriendlyName(group) 
				+ ChatColor.GREEN + "!");
		
	}

}
