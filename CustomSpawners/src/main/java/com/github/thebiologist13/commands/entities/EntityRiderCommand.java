package com.github.thebiologist13.commands.entities;

import org.bukkit.command.CommandSender;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.SpawnableEntity;

public class EntityRiderCommand extends EntityCommand {

	public EntityRiderCommand(CustomSpawners plugin, String mainPerm) {
		super(plugin, mainPerm);
	}

	public EntityRiderCommand(CustomSpawners plugin) {
		super(plugin);
	}

	@Override
	public void run(SpawnableEntity entity, CommandSender sender, String subCommand, String[] args) {
		
		String in = getValue(args, 0, "");
		
		if(in.equalsIgnoreCase("none") ||
				in.equals("")) {
			entity.setRider(null);
			PLUGIN.sendMessage(sender, getSuccessMessage(entity, "rider", "none"));
			return;
		}
		
		SpawnableEntity rider = CustomSpawners.getEntity(in);
		
		if(rider == null) {
			PLUGIN.sendMessage(sender, NO_ID);
			return;
		}
		
		entity.setRider(rider);
		
		PLUGIN.sendMessage(sender, getSuccessMessage(entity, "rider", PLUGIN.getFriendlyName(rider)));
		
	}

}
