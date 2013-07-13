package com.github.thebiologist13.commands.entities;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.HorseData;
import com.github.thebiologist13.SpawnableEntity;

public class EntityHorseCommand extends EntityCommand {

	public EntityHorseCommand(CustomSpawners plugin) {
		super(plugin);
	}

	public EntityHorseCommand(CustomSpawners plugin, String mainPerm) {
		super(plugin, mainPerm);
	}

	@Override
	public void run(SpawnableEntity entity, CommandSender sender, String subCommand, String[] args) {

		if(subCommand.equals("chests")) {
			String in = getValue(args, 0, "false");
			entity.setChests(Boolean.parseBoolean(in));
			
			PLUGIN.sendMessage(sender, getSuccessMessage(entity, "chests", in));
		}
		
		String in = getValue(args, 0, "");

		if(in.isEmpty()) {
			PLUGIN.sendMessage(sender, ChatColor.RED + "You must have something to set!");
			return;
		}

		if(subCommand.equals("horsecolor")) {

			HorseData.Color color = HorseData.Color.fromName(in);

			if(color == null) {
				PLUGIN.sendMessage(sender, ChatColor.RED + "That is not a color.");
				return;
			}

			entity.setHorseColor(color);
			
			PLUGIN.sendMessage(sender, ChatColor.GREEN + "Successfully set the horse color to " + 
					ChatColor.GOLD + in + ChatColor.GREEN + " on entity " + ChatColor.GOLD + 
					PLUGIN.getFriendlyName(entity) + ChatColor.GREEN + ".");

		} else if(subCommand.equals("horsetype")) {

			HorseData.Type type = HorseData.Type.fromName(in);

			if(type == null) {
				PLUGIN.sendMessage(sender, ChatColor.RED + "That is not a type.");
				return;
			}

			entity.setHorseType(type);

			PLUGIN.sendMessage(sender, ChatColor.GREEN + "Successfully set the horse type to " + 
					ChatColor.GOLD + in + ChatColor.GREEN + " on entity " + ChatColor.GOLD + 
					PLUGIN.getFriendlyName(entity) + ChatColor.GREEN + ".");
			
		} else if(subCommand.equals("horsevariant")) {

			HorseData.Variant variant = HorseData.Variant.fromName(in);

			if(variant == null) {
				PLUGIN.sendMessage(sender, ChatColor.RED + "That is not a variant.");
				return;
			}

			entity.setHorseVariant(variant);

			PLUGIN.sendMessage(sender, ChatColor.GREEN + "Successfully set the horse variant to " + 
					ChatColor.GOLD + in + ChatColor.GREEN + " on entity " + ChatColor.GOLD + 
					PLUGIN.getFriendlyName(entity) + ChatColor.GREEN + ".");
			
		}

	}

}
