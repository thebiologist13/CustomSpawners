package com.github.thebiologist13.commands.entities;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.potion.PotionEffectType;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.SpawnableEntity;
import com.github.thebiologist13.serialization.SPotionEffect;

/**
 * Command for changing potion effects on an entity.
 * 
 * @author thebiologist13
 */
public class EntityActiveEffectsCommand extends EntityCommand {

	public EntityActiveEffectsCommand(CustomSpawners plugin) {
		super(plugin);
	}
	
	public EntityActiveEffectsCommand(CustomSpawners plugin, String perm) {
		super(plugin, perm);
	}

	@Override
	public void run(SpawnableEntity entity, CommandSender sender, String subCommand, String[] args) {
		
		SPotionEffect effect;
		PotionEffectType effectType;
		int amplifier;
		int duration;
		
		String eIn = getValue(args, 0, PotionEffectType.REGENERATION);
		String aIn = getValue(args, 1, 1);
		String dIn = getValue(args, 2, 1200);
		
		effectType = PLUGIN.getInputEffect(eIn);
		
		if(effectType == null) {
			PLUGIN.sendMessage(sender, ChatColor.RED + eIn + " is not a valid potion effect.");
			return;
		}
		
		if(!CustomSpawners.isInteger(aIn)) {
			PLUGIN.sendMessage(sender, ChatColor.RED + "The potion effect level must be an integer.");
			return;
		}
		
		amplifier = Integer.parseInt(aIn);
		
		if(amplifier < 1) {
			PLUGIN.sendMessage(sender, ChatColor.RED + "The potion effect level must be greater than or equal to one.");
			return;
		}
		
		if(!CustomSpawners.isInteger(dIn)) {
			PLUGIN.sendMessage(sender, ChatColor.RED + "The potion effect duration must be an integer.");
			return;
		}
		
		duration = Integer.parseInt(dIn);
		
		if(duration < 0) {
			PLUGIN.sendMessage(sender, ChatColor.RED + "The potion effect duration must be greater than 0.");
			return;
		}
		
		String strEffect = effectType.getName() + " " + amplifier + " - " + PLUGIN.convertTicksToTime(duration);
		effect = new SPotionEffect(effectType, duration, amplifier);
		
		if(subCommand.equals("addEffect")) {
			entity.addPotionEffect(effect);
			PLUGIN.sendMessage(sender, ChatColor.GREEN + "Successfully added potion effect " + ChatColor.GOLD + 
					strEffect + ChatColor.GREEN + " to entity " + ChatColor.GOLD + PLUGIN.getFriendlyName(entity) + 
					ChatColor.GREEN + "!");
		} else if(subCommand.equals("seteffect")) {
			List<SPotionEffect> toSet = new ArrayList<SPotionEffect>();
			toSet.add(effect);
			entity.setEffects(toSet);
			PLUGIN.sendMessage(sender, getSuccessMessage(entity, "effect", strEffect));
		} else if(subCommand.equals("cleareffects")) {
			entity.setEffects(new ArrayList<SPotionEffect>());
			PLUGIN.sendMessage(sender, ChatColor.GREEN + "Successfully cleared potion effects " + ChatColor.GOLD + 
					strEffect + ChatColor.GREEN + " on entity " + ChatColor.GOLD + PLUGIN.getFriendlyName(entity) + 
					ChatColor.GREEN + "!");
		}
		
	}

}
