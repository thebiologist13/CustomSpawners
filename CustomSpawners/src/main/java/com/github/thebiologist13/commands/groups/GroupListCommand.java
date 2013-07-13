package com.github.thebiologist13.commands.groups;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.Group;
import com.github.thebiologist13.api.IObject;

public class GroupListCommand extends GroupCommand {

	private List<Integer> children = new ArrayList<Integer>();
	
	public GroupListCommand(CustomSpawners plugin) {
		super(plugin);
	}

	public GroupListCommand(CustomSpawners plugin, String mainPerm) {
		super(plugin, mainPerm);
	}

	@Override
	public void run(Group group, CommandSender sender, String subCommand, String[] args) {
		
		String in = getValue(args, 0, ""); //TODO WIKI: "all" switch for list
		
		Collection<Group> groups = CustomSpawners.groups.values();
		children.clear();
		
		if(groups.size() == 0) {
			PLUGIN.sendMessage(sender, ChatColor.RED + "No groups have been created yet.");
			return;
		}
		
		Iterator<Group> itr0 = groups.iterator();
		while(itr0.hasNext()) {
			Group g = itr0.next();
			
			for(IObject i : g.getGroup().keySet()) {
				
				if(i instanceof Group) {
					Group g0 = (Group) i;
					children.add(g0.getId());
				}
			}
		}
		
		boolean showAll = in.equalsIgnoreCase("all");
		String msg = showAll ? "Created Groups:" : "Parent Groups:";
		PLUGIN.sendMessage(sender, ChatColor.GOLD + msg);
		
		Iterator<Group> itr = groups.iterator(); 
		while(itr.hasNext()) {
			
			Group g = itr.next();
			
			if(!showAll)
				if(children.contains(g.getId()))
					continue;
			
			String message = ChatColor.GOLD + "" + g.getId() + ChatColor.GREEN
					+ " of type " + ChatColor.GOLD + g.getType().toString(); 
			
			if(!g.getName().isEmpty()) {
				message += ChatColor.GREEN + " with name " + ChatColor.GOLD + g.getName();
			}
			
			message += ChatColor.GREEN + " and " + ChatColor.GOLD + g.getGroup().size()
					+ ChatColor.GREEN + " objects contained within.";
			
			PLUGIN.sendMessage(sender, message);
			
		}
	}

}
