package com.github.thebiologist13.commands.groups;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.Group;
import com.github.thebiologist13.SpawnableEntity;
import com.github.thebiologist13.Spawner;
import com.github.thebiologist13.api.IObject;
import com.github.thebiologist13.commands.SubCommand;

public abstract class GroupCommand extends SubCommand {

	public final String NO_GROUP = ChatColor.RED + "You must have a group selected, or define a group.";
	public final String TYPE_NOT_DEFINED = ChatColor.RED + "You must specify the type of group as spawner or entity!";
	public final String NOT_SAME_TYPE = ChatColor.RED + "Group type and object type must be the same.";

	public GroupCommand(CustomSpawners plugin) {
		super(plugin, "");
	}

	public GroupCommand(CustomSpawners plugin, String mainPerm) {
		super(plugin, mainPerm);
	}
	
	public boolean hasType(String in) {
		in = ChatColor.stripColor(in);
		if(in.equals("spawner") || in.equals("entity") 
				|| in.equals("spawnableentity") || in.equals("group"))
			return true;
		return false;
	}

	public abstract void run(Group group, CommandSender sender, String subCommand, String[] args);
	
	public boolean valid(IObject obj, Group g) {
		if(obj instanceof Spawner && g.getType().equals(Group.Type.SPAWNER))
			return true;
		else if(obj instanceof SpawnableEntity && g.getType().equals(Group.Type.ENTITY))
			return true;
		else if(obj instanceof Group) {
			Group test = (Group) obj;
			if(test.getType().equals(g.getType()))
				return true;
			else
				return false;
		} else
			return false;
	}
	
}
