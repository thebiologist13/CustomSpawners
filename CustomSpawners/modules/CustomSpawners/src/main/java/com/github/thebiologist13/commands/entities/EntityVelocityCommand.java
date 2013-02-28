package com.github.thebiologist13.commands.entities;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.SpawnableEntity;
import com.github.thebiologist13.serialization.SVector;;

public class EntityVelocityCommand extends EntityCommand {

	public EntityVelocityCommand(CustomSpawners plugin) {
		super(plugin);
	}
	
	public EntityVelocityCommand(CustomSpawners plugin, String mainPerm) {
		super(plugin, mainPerm);
	}

	@Override
	public void run(SpawnableEntity entity, CommandSender sender, String subCommand, String[] args) {
		
		double x,y,z;
		
		String in = getValue(args, 0, "0,0,0");
		
		int firstCommaIndex = in.indexOf(",");
		int secondCommaIndex = in.indexOf(",", firstCommaIndex + 1);
		
		String xVal = in.substring(0, firstCommaIndex);
		String yVal = in.substring(firstCommaIndex + 1, secondCommaIndex);
		String zVal = in.substring(secondCommaIndex + 1, in.length());
		
		if(!CustomSpawners.isDouble(xVal) || !CustomSpawners.isDouble(yVal) || !CustomSpawners.isDouble(zVal)) {
			PLUGIN.sendMessage(sender, ChatColor.RED + "Invalid values for velocity. Please use the following format: " +
					ChatColor.GOLD + "/entities setvelocity <x value>,<y value>,<z value>.");
			return;
		}
		
		x = Double.parseDouble(xVal);
		y = Double.parseDouble(yVal);
		z = Double.parseDouble(zVal);
		
		entity.setVelocity(new SVector(x, y, z));
		
		PLUGIN.sendMessage(sender, getSuccessMessage(entity, "velocity", "(" + in + ")"));
		
	}

}
