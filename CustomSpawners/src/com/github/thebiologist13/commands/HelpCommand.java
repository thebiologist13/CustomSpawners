package com.github.thebiologist13.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.thebiologist13.CustomSpawners;

public class HelpCommand extends SpawnerSubCommand {
	
	private CustomSpawners plugin;
	
	public HelpCommand(CustomSpawners plugin) {
		this.plugin = plugin;
	}
	
	public void run(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		final String[] HELP_MESSAGE_1 = {
				ChatColor.GREEN + "* * * Custom Spawners Help Page 1 of 3* * *",
				ChatColor.GOLD + "/customspawner help [page]" + ChatColor.GREEN + " -> Displays this message.", 
				ChatColor.GOLD + "/customspawner listnear" + ChatColor.GREEN + " -> Displays nearby spawners with IDs and locations.",
				ChatColor.GOLD + "/customspawner listall" + ChatColor.GREEN + " -> Displays all spawners with IDs and locations.",
				ChatColor.GOLD + "/customspawner create <type>" + ChatColor.GREEN + " -> Creates a new spawner of of a certain type.",
				ChatColor.GOLD + "/customspawner remove [id]" + ChatColor.GREEN + " -> Removes a spawner.",
				ChatColor.GOLD + "/customspawner select <id>" + ChatColor.GREEN + " -> Selects a spawners so ID does not need to be entered.",
				ChatColor.GOLD + "/customspawner setactive [id]" + ChatColor.GREEN + " -> Makes a spawner active so it spawns.",
				ChatColor.GOLD + "/customspawner setinactive [id]" + ChatColor.GREEN + " -> Makes a spawner inactive so it doesn't spawn.",
				ChatColor.GREEN + "* * * * * * * * * * * * * * * *"
		};
		
		final String[] HELP_MESSAGE_2 = {
				ChatColor.GREEN + "* * * Custom Spawners Help Page 2 of 3* * *",
				ChatColor.GOLD + "/customspawner sethidden [id]" + ChatColor.GREEN + " -> Makes a spawner hidden.", 
				ChatColor.GOLD + "/customspawner setunhidden [id]" + ChatColor.GREEN + " -> Unhides a spawner.",
				ChatColor.GOLD + "/customspawner info [id]" + ChatColor.GREEN + " -> Displays information on a spawner.",
				ChatColor.GOLD + "/customspawner setmaxlight [id] <light level>" + ChatColor.GREEN + " -> Sets the maximum light before spawning.",
				ChatColor.GOLD + "/customspawner setminlight [id] <light level>" + ChatColor.GREEN + " -> Sets the minimum light before spawning.",
				ChatColor.GOLD + "/customspawner setmaxmobs [id] <max mobs>" + ChatColor.GREEN + " -> Sets the maximum mobs a spawner will spawn.",
				ChatColor.GOLD + "/customspawner setmobsperspawn [id] <mobs per spawn>" + ChatColor.GREEN + " -> Sets mobs spawned per rate.",
				ChatColor.GOLD + "/customspawner settype [id] <new type>" + ChatColor.GREEN + " -> Sets the type of mob to spawn.",
				ChatColor.GREEN + "* * * * * * * * * * * * * * * *"
		};
		
		final String[] HELP_MESSAGE_3 = {
				ChatColor.GREEN + "* * * Custom Spawners Help Page 3 of 3* * *",
				ChatColor.GOLD + "/customspawner setredstone [id] <true or false>" + ChatColor.GREEN + " -> Sets whether a spawner must be powered.", 
				ChatColor.GOLD + "/customspawner setrate [id] <rate>" + ChatColor.GREEN + " -> Sets how fast in ticks to spawn.",
				ChatColor.GOLD + "/customspawner setradius [id] <radius>" + ChatColor.GREEN + " -> Sets the spawn area radius.",
				ChatColor.GOLD + "/customspawner setlocation [id]" + ChatColor.GREEN + " -> Sets the location of a spawner.",
				ChatColor.GOLD + "/customspawner setmaxdistance [id] <distance>" + ChatColor.GREEN + " -> Sets maximum distance a player can be.",
				ChatColor.GOLD + "/customspawner setmindistance [id] <distance>" + ChatColor.GREEN + " -> Sets minimum distance a player can be.",
				ChatColor.GOLD + "/customspawner reloadspawners" + ChatColor.GREEN + " -> Reloads spawners from file.",
				ChatColor.GREEN + "* * * * * * * * * * * * * * * *"
		};
		
		final String[] HELP_MESSAGE_NO_COLOR = {
				"* * * Custom Spawners Help * * *",
				"/customspawner help -> Displays this message.", 
				"/customspawner reloadspawners -> Reloads all spawners.", 
				"* * * * * * * * * * * * * * * *"
		};
		
		if(arg0 instanceof Player) {
			Player p = (Player) arg0;
			if(p.hasPermission("customspawners.help")) {
				if(arg3.length == 1) {
					p.sendMessage(HELP_MESSAGE_1);
				} else if(arg3.length == 2) {
					if(arg3[1].equalsIgnoreCase("1")) {
						p.sendMessage(HELP_MESSAGE_1);
					} else if(arg3[1].equalsIgnoreCase("2")) {
						p.sendMessage(HELP_MESSAGE_2);
					} else if(arg3[1].equalsIgnoreCase("3")) {
						p.sendMessage(HELP_MESSAGE_3);
					} else {
						p.sendMessage(ChatColor.RED + "That is not a page of the help dialogue.");
					}
				}
			}
		} else {
			for(int i = 0; i < HELP_MESSAGE_NO_COLOR.length; i++) {
				plugin.log.info(HELP_MESSAGE_NO_COLOR[i]);
			}
		}
	}
}
