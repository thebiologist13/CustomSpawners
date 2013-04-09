package com.github.thebiologist13.commands.entities;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.SpawnableEntity;

public class EntityModifierCommand extends EntityCommand {

	public EntityModifierCommand(CustomSpawners plugin) {
		super(plugin);
	}

	public EntityModifierCommand(CustomSpawners plugin, String mainPerm) {
		super(plugin, mainPerm);
	}

	@Override
	public void run(SpawnableEntity entity, CommandSender sender, String subCommand, String[] args) {
		
		if(subCommand.equals("clearmodifier")) {
			
			entity.setModifiers(new HashMap<String, String>());
			PLUGIN.sendMessage(sender, ChatColor.GREEN + "Cleared modifiers of entity " + 
					ChatColor.GOLD + PLUGIN.getFriendlyName(entity));
			return;
		}
		
		String in = getValue(args, 0, "");
		
		if(in.isEmpty()) {
			PLUGIN.sendMessage(sender, ChatColor.RED + "You must enter an equation.");
			return;
		}
		
		if(in.indexOf("=") == -1) {
			PLUGIN.sendMessage(sender, ChatColor.RED + "You must have an equals sign " +
					"for the equation. Use the format: <property>=<expression>");
			return;
		}
		
		String moddedProp;
		String expression;
		
		try {
			moddedProp = in.split("=")[0].toLowerCase();
			expression = in.split("=")[1].toLowerCase();
			
			entity.evaluate(expression);
		} catch(Exception e) {
			PLUGIN.sendMessage(sender, ChatColor.RED + "You entered an invalid expression, make sure " +
					"you have valid opertors and the right format. The format is: <property>=<expression>");
			return;
		}
		
		if(subCommand.equals("addmodifier")) {
			
			entity.addModifier(moddedProp, expression);
			PLUGIN.sendMessage(sender, ChatColor.GREEN + "Added modifier " + ChatColor.GOLD + in + ChatColor.GREEN + 
					" to entity " + ChatColor.GOLD + PLUGIN.getFriendlyName(entity));
			
		} else if(subCommand.equals("setmodifier")) {
			
			entity.setModifiers(new HashMap<String, String>());
			entity.addModifier(moddedProp, expression);
			PLUGIN.sendMessage(sender, ChatColor.GREEN + "Set modifier " + ChatColor.GOLD + in + ChatColor.GREEN + 
					" of entity " + ChatColor.GOLD + PLUGIN.getFriendlyName(entity));
			
		}
		
	}

}
