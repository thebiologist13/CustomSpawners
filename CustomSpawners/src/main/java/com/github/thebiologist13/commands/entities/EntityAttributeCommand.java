package com.github.thebiologist13.commands.entities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.SpawnableEntity;
import com.github.thebiologist13.attributelib.Attribute;
import com.github.thebiologist13.attributelib.Modifier;
import com.github.thebiologist13.attributelib.Operation;
import com.github.thebiologist13.attributelib.VanillaAttribute;

public class EntityAttributeCommand extends EntityCommand {

	public EntityAttributeCommand(CustomSpawners plugin) {
		super(plugin);
	}

	public EntityAttributeCommand(CustomSpawners plugin, String mainPerm) {
		super(plugin, mainPerm);
	}

	@Override
	public void run(SpawnableEntity entity, CommandSender sender, String subCommand, String[] args) {
		
		/*
		 * Syntax:
		 * 
		 * <attribute name> <base> [<modifier name>,<operator>,<amount>;...]
		 */
		
		if(subCommand.equals("clearattributes")) {
			entity.setAttributes(new ArrayList<Attribute>());
			PLUGIN.sendMessage(sender, ChatColor.GREEN + "Successfully cleared attributes on entity " +
					ChatColor.GOLD + PLUGIN.getFriendlyName(entity) + ChatColor.GREEN + ".");
			return;
		}
		
		String in = getValue(args, 0, "generic.maxHealth"); 
		
		VanillaAttribute att = PLUGIN.parseAttribute(in);
		
		if(att == null) {
			PLUGIN.sendMessage(sender, ChatColor.RED + "That is not an attribute type.");
			return;
		}
		
		String in0 = getValue(args, 1, "" + att.getDefaultBase());
		
		if(!CustomSpawners.isDouble(in0)) {
			PLUGIN.sendMessage(sender, ChatColor.RED + "The base value must be number.");
			return;
		}
		
		double base = Double.parseDouble(in0);
		
		if(base < att.getMinimum() || (base > att.getMaximum() && att.getMaximum() != -1)) {
			PLUGIN.sendMessage(sender, ChatColor.RED + "The base value must be between the " +
					"minimum and maximum values of the attribute.");
			return;
		}
		
		Attribute newAtt = new Attribute(att);
		newAtt.setBase(base);
		
		String in1 = getValue(args, 2, "");
		
		if(!in1.isEmpty()) {
			String[] splitMods = in1.split(";");
			
			for(String mod : splitMods) {
				String[] splitCommas = mod.split(",");
				String modName = splitCommas[0];
				String operator = splitCommas[1];
				String amount = splitCommas[2];
				
				Operation op = Operation.fromName(operator);
				
				if(op == null && CustomSpawners.isInteger(operator))
					op = Operation.fromId(Integer.parseInt(operator));
				
				if(op == null) {
					PLUGIN.sendMessage(sender, ChatColor.RED + "\"" + modName + 
							"\" is not an operation for modifiers.");
					return;
				}
				
				if(!CustomSpawners.isDouble(amount)) {
					PLUGIN.sendMessage(sender, ChatColor.RED + "The amount for a modifier must be a number.");
					return;
				}
				
				double amt = Double.parseDouble(amount);
				
				Modifier newMod = new Modifier(modName, op, amt);
				newAtt.addModifier(newMod);
			}
			
		}
		
		Iterator<Attribute> attItr = entity.getAttributes().iterator();
		while(attItr.hasNext()) {
			Attribute a = attItr.next();
			if(a.getAttribute().equals(newAtt.getAttribute())) {
				attItr.remove();
				PLUGIN.sendMessage(sender, ChatColor.GOLD + "Replaced attribute with same type.");
			}
		}
		
		if(subCommand.equals("addattribute")) {
			entity.addAttribute(newAtt);
			PLUGIN.sendMessage(sender, ChatColor.GREEN + "Successfully added attribute " + ChatColor.GOLD +
					in + ChatColor.GREEN + " to entity " + ChatColor.GOLD + PLUGIN.getFriendlyName(entity) + ChatColor.GREEN + ".");
		} else if(subCommand.equals("setattribute")) {
			List<Attribute> list = new ArrayList<Attribute>();
			list.add(newAtt);
			entity.setAttributes(list);
			PLUGIN.sendMessage(sender, ChatColor.GREEN + "Successfully set attribute to " + ChatColor.GOLD +
					in + ChatColor.GREEN + " on entity " + ChatColor.GOLD + PLUGIN.getFriendlyName(entity) + ChatColor.GREEN + ".");
		}
		
	}

}
