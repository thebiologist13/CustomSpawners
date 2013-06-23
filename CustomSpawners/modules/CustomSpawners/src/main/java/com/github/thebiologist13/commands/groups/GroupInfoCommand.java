package com.github.thebiologist13.commands.groups;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.Group;
import com.github.thebiologist13.SpawnableEntity;
import com.github.thebiologist13.Spawner;
import com.github.thebiologist13.api.IObject;

public class GroupInfoCommand extends GroupCommand {

	public GroupInfoCommand(CustomSpawners plugin) {
		super(plugin);
	}

	public GroupInfoCommand(CustomSpawners plugin, String mainPerm) {
		super(plugin, mainPerm);
	}

	@Override
	public void run(Group group, CommandSender sender, String subCommand, String[] args) {
		PLUGIN.sendMessage(sender, getInfo(group));
	}

	private String[] getInfo(Group g) {
		String objects = "";

		int count = 0;
		for(IObject obj : g.getGroup().keySet()) {
			
			String info = "";
			
			if(obj instanceof Spawner) {
				Spawner s = (Spawner) obj;
				info = ChatColor.GOLD + "(Spawner) " + PLUGIN.getFriendlyName(s);
			} else if (obj instanceof SpawnableEntity) {
				SpawnableEntity s = (SpawnableEntity) obj;
				info = ChatColor.GOLD + "(SpawnableEntity) " + PLUGIN.getFriendlyName(s);
			} else if (obj instanceof Group) {
				Group g0 = (Group) obj;
				info = ChatColor.GOLD + "(Group) " + PLUGIN.getFriendlyName(g0);
			}
			
			if(count == 0) {
				objects += info;
			} else {
				objects += ", " + info;
			}
			
			count++;
		}

		String header = ChatColor.GREEN + "Information on group with ID " + ChatColor.GOLD + String.valueOf(g.getId());

		if(!g.getName().isEmpty()) {
			header += " (" + g.getName() + ")";
		}

		String[] message = {
				"",
				header + ChatColor.GREEN + ": ",
				"",
				ChatColor.GOLD + "Objects (" + g.getGroup().size() +"): " + objects,
				ChatColor.GOLD + "Type: " + g.getType().toString()
		};

		return message;

	}

}
