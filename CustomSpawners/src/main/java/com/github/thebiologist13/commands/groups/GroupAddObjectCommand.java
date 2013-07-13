package com.github.thebiologist13.commands.groups;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.Group;
import com.github.thebiologist13.api.IObject;
import com.github.thebiologist13.commands.SelectionParser;

public class GroupAddObjectCommand extends GroupCommand {

	public GroupAddObjectCommand(CustomSpawners plugin) {
		super(plugin);
	}

	public GroupAddObjectCommand(CustomSpawners plugin, String mainPerm) {
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

		if(PLUGIN.findObjectInGroups(id).contains(group)) {
			PLUGIN.sendMessage(sender, ChatColor.RED + "This object has already been added to that group.");
			return;
		}

		if(!valid(id, group)) {
			PLUGIN.sendMessage(sender, NOT_SAME_TYPE);
			return;
		}

		if(id instanceof Group) {
			Group g = (Group) id;

			if(g.getId() == -1) { //Dashed group
				for(IObject i : g.getGroup().keySet()) {
					group.addItem(i);
				}
			} else {
				if(PLUGIN.findObjectInGroups(g).size() != 0) {
					PLUGIN.sendMessage(sender, ChatColor.RED + "A group can only be a child of one group.");
					return;
				}

				if(g.contains(group)) {
					PLUGIN.sendMessage(sender, ChatColor.RED + "Cannot add parent group to child group.");
					return;
				}
				group.addItem(id);
			}
		} else {
			group.addItem(id);
		}

		PLUGIN.sendMessage(sender, ChatColor.GREEN + "Added object " + ChatColor.GOLD
				+ idIn + ChatColor.GREEN + " to group " + ChatColor.GOLD + PLUGIN.getFriendlyName(group) 
				+ ChatColor.GREEN + "!");

	}

}
