package com.github.thebiologist13.commands.groups;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.Group;

public class GroupSelectCommand extends GroupCommand {

	public GroupSelectCommand(CustomSpawners plugin) {
		super(plugin);
	}

	public GroupSelectCommand(CustomSpawners plugin, String mainPerm) {
		super(plugin, mainPerm);
	}

	@Override
	public void run(Group group, CommandSender sender, String subCommand, String[] args) {
		String in = getValue(args, 0, "none");
		
		Player player = null;
		
		if(sender instanceof Player) {
			player = (Player) sender;
		}
		
		if(in.equals("none") || in.equals("nothing")) {
			if(player != null) {
				CustomSpawners.groupSelection.remove(player);
			} else {
				CustomSpawners.consoleGroup = -1;
			}
			PLUGIN.sendMessage(sender, ChatColor.GREEN + "You have deselected your group.");
			return;
		}
		
		Group selection = CustomSpawners.getGroup(in);
		
		if(selection == null) {
			PLUGIN.sendMessage(sender, NO_ID);
			return;
		}
		
		if(player != null) {
			CustomSpawners.groupSelection.put(player, selection.getId());
		} else {
			CustomSpawners.consoleGroup = selection.getId();
		}
		
		PLUGIN.sendMessage(sender, ChatColor.GREEN + "Selected group " + 
				ChatColor.GOLD + PLUGIN.getFriendlyName(selection) + ChatColor.GREEN + ".");
	}

}
