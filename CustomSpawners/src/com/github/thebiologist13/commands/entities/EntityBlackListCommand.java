package com.github.thebiologist13.commands.entities;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.SpawnableEntity;

public class EntityBlackListCommand extends EntityCommand {

	public EntityBlackListCommand(CustomSpawners plugin) {
		super(plugin);
	}
	
	public EntityBlackListCommand(CustomSpawners plugin, String mainPerm) {
		super(plugin, mainPerm);
	}

	@Override
	public void run(SpawnableEntity entity, CommandSender sender, String subCommand, String[] args) {
		
		if(subCommand.equals("setuseblacklist")) {
			String in = getValue(args, 0, "false");
			entity.setUseBlacklist(Boolean.parseBoolean(in));
			
			PLUGIN.sendMessage(sender, getSuccessMessage(entity, "use blacklist", in));
			return;
		}
		
		String type = "";
		
		String in = getValue(args, 0, "void");
		
		type = PLUGIN.getDamageCause(in);
		
		if(type.isEmpty()) {
			PLUGIN.sendMessage(sender, ChatColor.RED + "\"" + in + "\" is not a valid damage cause.");
		}
		
		if(subCommand.equals("addblacklist")) {
			entity.addDamageBlacklist(type);
			PLUGIN.sendMessage(sender, ChatColor.GREEN + "Successfully blacklisted damage cause " + ChatColor.GOLD + 
					type + ChatColor.GREEN + " on entity " + ChatColor.GOLD + PLUGIN.getFriendlyName(entity) + 
					ChatColor.GREEN + "!");
		} else if(subCommand.equals("setblacklist")) {
			List<String> list = new ArrayList<String>();
			list.add(type);
			entity.setDamageBlacklist(list);
			PLUGIN.sendMessage(sender, ChatColor.GREEN + "Successfully blacklisted damage cause " + ChatColor.GOLD + 
					type + ChatColor.GREEN + " on entity " + ChatColor.GOLD + PLUGIN.getFriendlyName(entity) + 
					ChatColor.GREEN + "!");
		} else if(subCommand.equals("clearblacklist")) {
			entity.setDamageBlacklist(new ArrayList<String>());
			PLUGIN.sendMessage(sender, ChatColor.GREEN + "Successfully cleared blacklist on entity " + 
					ChatColor.GOLD + PLUGIN.getFriendlyName(entity) + ChatColor.GREEN + "!");
		}
		
	}
	
}
