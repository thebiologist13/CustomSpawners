package com.github.thebiologist13.commands.spawners;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.Spawner;

public class KillTimeCommand extends SpawnerCommand {

	public KillTimeCommand(CustomSpawners plugin) {
		super(plugin);
	}

	public KillTimeCommand(CustomSpawners plugin, String mainPerm) {
		super(plugin, mainPerm);
	}

	@Override
	public void run(Spawner spawner, CommandSender sender, String subCommand, String[] args) {
		
		if(subCommand.equals("setkilltime")) {
			
			String in = getValue(args, 0, "3600");
			
			if(!CustomSpawners.isLong(in)) {
				PLUGIN.sendMessage(sender, ChatColor.RED + "The kill time must be a non-decimal number.");
				return;
			}
			
			long time = Long.parseLong(in);
			
			if(time < 0) {
				PLUGIN.sendMessage(sender, ChatColor.RED + "The kill time must be greater than 0.");
				return;
			}
			
			spawner.setKillTime(time);
			
			PLUGIN.sendMessage(sender, ChatColor.GREEN + "Successfully set the kill time to " + ChatColor.GOLD +
					time + ChatColor.GREEN + " on spawner " + ChatColor.GOLD + PLUGIN.getFriendlyName(spawner) +
					ChatColor.GREEN + ".");
			
		} else if(subCommand.equals("usekilltime")) {
			String in = getValue(args, 0, "false");
			spawner.setUseKillTime(Boolean.parseBoolean(in));
			
			PLUGIN.sendMessage(sender, getSuccessMessage(spawner, "use kill time", in));
		}
		
	}

}
