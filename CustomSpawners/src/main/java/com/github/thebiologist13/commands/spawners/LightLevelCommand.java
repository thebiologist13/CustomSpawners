package com.github.thebiologist13.commands.spawners;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.Spawner;

public class LightLevelCommand extends SpawnerCommand {

	public LightLevelCommand(CustomSpawners plugin) {
		super(plugin);
	}

	public LightLevelCommand(CustomSpawners plugin, String mainPerm) {
		super(plugin, mainPerm);
	}

	@Override
	public void run(Spawner spawner, CommandSender sender, String subCommand, String[] args) {
		
		String in = getValue(args, 0, "0");
		
		try {
			
			if(subCommand.equals("setmaxlight")) {
				
				byte level = (byte) handleDynamic(in, spawner.getMaxLightLevel());
				
				if(!(level <= 15 && level >= 0)) {
					PLUGIN.sendMessage(sender, ChatColor.RED + "The light level must be between 0 and 15.");
					return;
				}
				
				spawner.setMaxLightLevel(level);
				
				PLUGIN.sendMessage(sender, getSuccessMessage(spawner, "maximum light level", in));
				
			} else if(subCommand.equals("setminlight")) {
				
				byte level = (byte) handleDynamic(in, spawner.getMinLightLevel());
				
				if(!(level <= 15 && level >= 0)) {
					PLUGIN.sendMessage(sender, ChatColor.RED + "The light level must be between 0 and 15.");
					return;
				}
				
				spawner.setMinLightLevel(level);
				
				PLUGIN.sendMessage(sender, getSuccessMessage(spawner, "minimum light level", in));
				
			}
			
		} catch(IllegalArgumentException e) {
			PLUGIN.sendMessage(sender, ChatColor.RED + "The light level must be an integer.");
		}
		
	}
}
