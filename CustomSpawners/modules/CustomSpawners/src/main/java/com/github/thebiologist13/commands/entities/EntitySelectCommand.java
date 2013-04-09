package com.github.thebiologist13.commands.entities;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.SpawnableEntity;

public class EntitySelectCommand extends EntityCommand {

	public EntitySelectCommand(CustomSpawners plugin) {
		super(plugin);
	}
	
	public EntitySelectCommand(CustomSpawners plugin, String mainPerm) {
		super(plugin, mainPerm);
	}

	@Override
	public void run(SpawnableEntity entity, CommandSender sender, String subCommand, String[] args) {
		
		String in = getValue(args, 0, "none");
		
		Player player = null;
		
		if(sender instanceof Player) {
			player = (Player) sender;
		}
		
		if(in.equals("none") || in.equals("nothing")) {
			if(player != null) {
				CustomSpawners.entitySelection.remove(player);
			} else {
				CustomSpawners.consoleEntity = -1;
			}
			PLUGIN.sendMessage(sender, ChatColor.GREEN + "You have deselected your entity.");
			return;
		}
		
		SpawnableEntity selection = CustomSpawners.getEntity(in);
		
		if(selection == null) {
			PLUGIN.sendMessage(sender, NO_ID);
			return;
		}
		
		if(player != null) {
			CustomSpawners.entitySelection.put(player, selection.getId());
		} else {
			CustomSpawners.consoleEntity = selection.getId();
		}
		
		PLUGIN.sendMessage(sender, ChatColor.GREEN + "Selected entity " + 
				ChatColor.GOLD + PLUGIN.getFriendlyName(selection) + ChatColor.GREEN + ".");
		
	}

}
