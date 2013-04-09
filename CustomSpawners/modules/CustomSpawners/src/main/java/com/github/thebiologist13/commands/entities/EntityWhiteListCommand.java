package com.github.thebiologist13.commands.entities;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.SpawnableEntity;

public class EntityWhiteListCommand extends EntityCommand {
	
	public EntityWhiteListCommand(CustomSpawners plugin) {
		super(plugin);
	}
	
	public EntityWhiteListCommand(CustomSpawners plugin, String mainPerm) {
		super(plugin, mainPerm);
	}

	@Override
	public void run(SpawnableEntity entity, CommandSender sender, String subCommand, String[] args) {
		
		if(subCommand.equals("setusewhitelist")) {
			String in = getValue(args, 0, "false");
			entity.setUseWhitelist(Boolean.parseBoolean(in));
			
			PLUGIN.sendMessage(sender, getSuccessMessage(entity, "use whitelist", in));
			return;
		}
		
		String type = "";
		
		String in = getValue(args, 0, "void");
		
		type = PLUGIN.getDamageCause(in);
		
		if(type.isEmpty()) {
			PLUGIN.sendMessage(sender, ChatColor.RED + "\"" + in + "\" is not a valid damage cause.");
			return;
		}
		
		if(subCommand.equals("addwhitelist")) {
			entity.addDamageWhitelist(type);
			PLUGIN.sendMessage(sender, ChatColor.GREEN + "Successfully whitelisted damage cause " + ChatColor.GOLD + 
					type + ChatColor.GREEN + " on entity " + ChatColor.GOLD + PLUGIN.getFriendlyName(entity) + 
					ChatColor.GREEN + "!");
		} else if(subCommand.equals("setwhitelist")) {
			List<String> list = new ArrayList<String>();
			list.add(type);
			entity.setDamageWhitelist(list);
			PLUGIN.sendMessage(sender, ChatColor.GREEN + "Successfully whitelisted damage cause " + ChatColor.GOLD + 
					type + ChatColor.GREEN + " on entity " + ChatColor.GOLD + PLUGIN.getFriendlyName(entity) + 
					ChatColor.GREEN + "!");
		} else if(subCommand.equals("clearwhitelist")) {
			entity.setDamageWhitelist(new ArrayList<String>());
			PLUGIN.sendMessage(sender, ChatColor.GREEN + "Successfully cleared whitelist on entity " + 
					ChatColor.GOLD + PLUGIN.getFriendlyName(entity) + ChatColor.GREEN + "!");
		}
		
	}

}
