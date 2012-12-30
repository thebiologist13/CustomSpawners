package com.github.thebiologist13.commands.entities;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

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
		
		String type = "";
		
		String in = getValue(args, 0, "void");
		
		if(in.equals("blockexplosion")) {
			type = "BLOCK_EXPLOSION";
		} else if(in.equals("entityexplosion") || in.equals("creeper")) {
			type = "ENTITY_EXPLOSION";
		} else if(in.equals("firetick") || in.equals("burning")) {
			type = "FIRE_TICK";
		} else if(in.equals("attack") || in.equals("entityattack")) {
			type = "ENTITY_ATTACK";
		} else if(in.equals("item") || in.equals("itemdamage")) {
			type = "ITEM";
		} else if(in.equals("spawnerfire") || in.equals("spawnerfireticks")) {
			type = "SPAWNER_FIRE_TICKS";
		} else {
			for(DamageCause c : DamageCause.values()) {
				if(c.toString().equalsIgnoreCase(in)) {
					type = in;
					break;
				}
			}
		}
		
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
