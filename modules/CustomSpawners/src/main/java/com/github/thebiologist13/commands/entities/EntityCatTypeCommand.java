package com.github.thebiologist13.commands.entities;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.SpawnableEntity;

public class EntityCatTypeCommand extends EntityCommand {

	public EntityCatTypeCommand(CustomSpawners plugin) {
		super(plugin);
	}

	public EntityCatTypeCommand(CustomSpawners plugin, String mainPerm) {
		super(plugin, mainPerm);
	}

	@Override
	public void run(SpawnableEntity entity, CommandSender sender, String subCommand, String[] args) {
		
		String type = "";
		
		String in = getValue(args, 0, "WILD_OCELOT");
		
		if(in.equals("none") || in.equals("wild") || in.equals("wild_ocelot") || in.equals("wildocelot")) {
			type = "WILD_OCELOT";
		} else if(in.equals("red_cat") || in.equals("red")|| in.equals("redcat")) {
			type = "RED_CAT";
		} else if(in.equals("black_cat") || in.equals("black")|| in.equals("blackcat")) {
			type = "BLACK_CAT";
		} else if(in.equals("siamese_cat") || in.equals("siamese")|| in.equals("siamesecat")) {
			type = "SIAMESE_CAT";
		} else {
			PLUGIN.sendMessage(sender, ChatColor.RED + "\"" + in + "\" is not a valid cat type.");
			return;
		}
		
		entity.setCatType(type);
		
		PLUGIN.sendMessage(sender, getSuccessMessage(entity, "cat type", type));
		
	}

}
