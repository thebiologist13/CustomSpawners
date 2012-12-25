package com.github.thebiologist13.commands.entities;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.SpawnableEntity;
import com.github.thebiologist13.commands.SpawnerCommand;

public class EntityVillagerCommand extends SpawnerCommand {

	public EntityVillagerCommand(CustomSpawners plugin) {
		super(plugin);
	}

	@Override
	public void run(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		
		Player p = null;
		SpawnableEntity s = getSelectedEntity(arg0);
		final String PERMISSION = "customspawners.entities.setvillager";
		boolean value = false;
		
		if(arg3.length == 1) {
			plugin.sendMessage(arg0, LESS_ARGS);
			return;
		}
		
		if(!(arg0 instanceof Player)) {
			plugin.sendMessage(arg0, NO_CONSOLE);
			return;
		} else {
			p = (Player) arg0;
		}
		
		if(!p.hasPermission(PERMISSION)) {
			plugin.sendMessage(arg0, NO_PERMISSION);
			return;
		}
		
		if(s == null) { //No selection, must be length 3
			
			if(arg3.length >= 3) {
				s = CustomSpawners.getEntity(arg3[1]);
			} else {
				plugin.sendMessage(arg0, NEEDS_SELECTION);	
				return;
			}
			
			if(s == null) {
				plugin.sendMessage(arg0, NO_ID);
				return;
			}
			
			if(arg3[2].equalsIgnoreCase("true") || arg3[2].equalsIgnoreCase("false")) {
				if(arg3[2].equals("true")) {
					value = true;
				}
			} else {
				p.sendMessage(MUST_BE_BOOLEAN);
				return;
			}
			
		} else {
			
			if(arg3[1].equalsIgnoreCase("true") || arg3[1].equalsIgnoreCase("false")) {
				if(arg3[1].equals("true")) {
					value = true;
				}
			} else {
				p.sendMessage(MUST_BE_BOOLEAN);
				return;
			}
			
		}
		
		s.setProp("zombie", value);
		plugin.sendMessage(arg0, ChatColor.GREEN + "Successfully set entity with ID " + ChatColor.GOLD + plugin.getFriendlyName(s) + ChatColor.GREEN + "'s villager value.");
		
	}

}
