package com.github.thebiologist13.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.thebiologist13.CustomSpawners;

public class HelpCommand extends SpawnerCommand {

	public HelpCommand(CustomSpawners plugin) {
		super(plugin);
	}
	
	public void run(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		
		final String[] HELP_MESSAGE_0 = {
				ChatColor.GREEN + "* * * CustomSpawners Help * * *",
				ChatColor.GOLD + "Use /customspawners help [page #] to see a help page.",
				ChatColor.GOLD + "Arguments in " + ChatColor.AQUA + "[] are optional" + ChatColor.GOLD + ", they are often to identify a",
				ChatColor.GOLD + "object by ID number. Arguments in " + ChatColor.DARK_RED + "<> are required" + ChatColor.GOLD + ". They are",
				ChatColor.GOLD + "usually the value to set.",
				ChatColor.GREEN + "* * * * * * * * * * * * * * * *"
		};
		
		final String[] HELP_MESSAGE_1 = {
				ChatColor.GREEN + "* * * CustomSpawners Help Page 1 of 11 * * *",
				ChatColor.GOLD + "/customspawners help [page]" + ChatColor.GREEN + " -> Displays this message.", 
				ChatColor.GOLD + "/customspawners" + ChatColor.GREEN + " -> Displays the plugin info.", 
				ChatColor.GOLD + "/customspawners reloadspawners" + ChatColor.GREEN + " -> Reloads spawners from file.",
				ChatColor.GOLD + "/spawners create <type>" + ChatColor.GREEN + " -> Creates a new spawner of the specified entity.",
				ChatColor.GOLD + "/spawners setname [id] <name>" + ChatColor.GREEN + " -> Names or renames the spawner for easier selection.",
				ChatColor.GOLD + "/spawners remove [id]" + ChatColor.GREEN + " -> Removes a spawner.",
				ChatColor.GOLD + "/spawners select <id>" + ChatColor.GREEN + " -> Selects a spawner so the ID does not need to be entered every time.",
				ChatColor.GOLD + "/spawners listnear" + ChatColor.GREEN + " -> Displays nearby spawners with IDs and locations.",
				ChatColor.GREEN + "* * * * * * * * * * * * * * * *"
		};
		
		final String[] HELP_MESSAGE_2 = {
				ChatColor.GREEN + "* * * CustomSpawners Help Page 2 of 11 * * *",
				ChatColor.GOLD + "/spawners listall" + ChatColor.GREEN + " -> Displays all spawners with IDs and locations.",
				ChatColor.GOLD + "/spawners setactive [id]" + ChatColor.GREEN + " -> Makes a spawner active so it spawns.",
				ChatColor.GOLD + "/spawners setinactive [id]" + ChatColor.GREEN + " -> Makes a spawner inactive so it doesn't spawn.",
				ChatColor.GOLD + "/spawners sethidden [id]" + ChatColor.GREEN + " -> Makes a spawner hidden.", 
				ChatColor.GOLD + "/spawners setunhidden [id]" + ChatColor.GREEN + " -> Unhides a spawner.",
				ChatColor.GOLD + "/spawners info [id]" + ChatColor.GREEN + " -> Displays information on a spawner.",
				ChatColor.GOLD + "/spawners setmaxlight [id] <light level>" + ChatColor.GREEN + " -> Sets the maximum light before spawning.",
				ChatColor.GOLD + "/spawners setminlight [id] <light level>" + ChatColor.GREEN + " -> Sets the minimum light before spawning.",
				ChatColor.GREEN + "* * * * * * * * * * * * * * * *"
		};
		
		final String[] HELP_MESSAGE_3 = {
				ChatColor.GREEN + "* * * CustomSpawners Help Page 3 of 11 * * *",
				ChatColor.GOLD + "/spawners setmaxmobs [id] <max mobs>" + ChatColor.GREEN + " -> Sets the maximum mobs a spawner will spawn.",
				ChatColor.GOLD + "/spawners setmobsperspawn [id] <mobs per spawn>" + ChatColor.GREEN + " -> Sets mobs spawned per rate.",
				ChatColor.GOLD + "/spawners settype [id] <new type>" + ChatColor.GREEN + " -> Sets the type of mob to spawn.",
				ChatColor.GOLD + "/spawners addtype [id] <type>" + ChatColor.GREEN + " -> Adds a type of mob to spawn.",
				ChatColor.GOLD + "/spawners setredstone [id] <true or false>" + ChatColor.GREEN + " -> Sets whether a spawner must be powered.", 
				ChatColor.GOLD + "/spawners setrate [id] <rate>" + ChatColor.GREEN + " -> Sets how fast in ticks to spawn.",
				ChatColor.GOLD + "/spawners setradius [id] <radius>" + ChatColor.GREEN + " -> Sets the spawn area radius.",
				ChatColor.GOLD + "/spawners setlocation [id]" + ChatColor.GREEN + " -> Sets the location of a spawner.",
				ChatColor.GREEN + "* * * * * * * * * * * * * * * *"
		};
		
		final String[] HELP_MESSAGE_4 = {
				ChatColor.GREEN + "* * * CustomSpawners Help Page 4 of 11 * * *",
				ChatColor.GOLD + "/spawners setmaxdistance [id] <distance>" + ChatColor.GREEN + " -> Sets maximum distance a player can be.",
				ChatColor.GOLD + "/spawners setmindistance [id] <distance>" + ChatColor.GREEN + " -> Sets minimum distance a player can be.",
				ChatColor.GOLD + "/spawners forcespawn [id]" + ChatColor.GREEN + " -> Forces a spawner to spawn mobs regardless of conditions.",
				ChatColor.GOLD + "/spawners removeallmobs" + ChatColor.GREEN + " -> Kills all mobs created by spawners in the server.", 
				ChatColor.GOLD + "/spawners removemobs <id>" + ChatColor.GREEN + " -> Kills all mobs created by the spawner with specified ID.",
				ChatColor.GOLD + "/spawners activateall" + ChatColor.GREEN + " -> Sets all spawners on the server active.",
				ChatColor.GOLD + "/spawners deactivateall" + ChatColor.GREEN + " -> Sets all spawners on the server inactive.",
				ChatColor.GOLD + "/spawners setname [id] <name>" + ChatColor.GREEN + " -> Gives a name to the spawner.",
				ChatColor.GOLD + "/spawners convert [id]" + ChatColor.GREEN + " -> Converts a CustomSpawners spawner to a mob spawner block and back.",
				ChatColor.GREEN + "* * * * * * * * * * * * * * * *"
		};
		
		final String[] HELP_MESSAGE_5 = {
				ChatColor.GREEN + "* * * CustomSpawners Help Page 5 of 11 * * *",
				ChatColor.GOLD + "/entities create <type>" + ChatColor.GREEN + " -> Creates a new spawnable entity that can be customized.",
				ChatColor.GOLD + "/entities remove [id]" + ChatColor.GREEN + " -> Removes a spawnable entity so it can't be used by spawners.",
				ChatColor.GOLD + "/entities select <id>" + ChatColor.GREEN + " -> Selects an entity so the entity ID doesn't need to be entered every time.",
				ChatColor.GOLD + "/entities seteffect [id] <effect>" + ChatColor.GREEN + " -> Sets the potion effect on an entity.",
				ChatColor.GOLD + "/entities cleareffects [id]" + ChatColor.GREEN + " -> Clear all the potion effects on a spawner.",
				ChatColor.GOLD + "/entities addeffect [id] <effect>" + ChatColor.GREEN + " -> Adds a potion effect to a spawnable entity.",
				ChatColor.GOLD + "/entities setvelocity [id] <x velocity,y velocity,z velocity>" + ChatColor.GREEN + " -> Sets the velocity of spawned entities.",
				ChatColor.GOLD + "/entities setenderblock [id] <blockID>" + ChatColor.GREEN + " -> Sets the block ID held by spawned endermen.",
				ChatColor.GREEN + "* * * * * * * * * * * * * * * *"
		};
		
		final String[] HELP_MESSAGE_6 = {
				ChatColor.GREEN + "* * * CustomSpawners Help Page 6 of 11 * * *",
				ChatColor.GOLD + "/entities settype [id] <type>" + ChatColor.GREEN + " -> Sets the base entity type of a spawnable entity.",
				ChatColor.GOLD + "/entities setname [id] <name>" + ChatColor.GREEN + " -> Names or renames the entity for easier selection.",
				ChatColor.GOLD + "/entities setage [id] <age>" + ChatColor.GREEN + " -> Sets the age of a spawned entity.",
				ChatColor.GOLD + "/entities setadult [id]" + ChatColor.GREEN + " -> Makes this entity's age an adult.",
				ChatColor.GOLD + "/entities setbaby [id]" + ChatColor.GREEN + " -> Makes this entity's age a baby.",
				ChatColor.GOLD + "/entities sethealth [id] <health>" + ChatColor.GREEN + " -> Sets the health of spawned entities.", 
				ChatColor.GOLD + "/entities setair [id] <air>" + ChatColor.GREEN + " -> Sets the remaining air for an entity.",
				ChatColor.GOLD + "/entities setprofession [id] <profession>" + ChatColor.GREEN + " -> Sets the profession of a spawned villager.",
				ChatColor.GREEN + "* * * * * * * * * * * * * * * *"
		};
		
		final String[] HELP_MESSAGE_7 = {
				ChatColor.GREEN + "* * * CustomSpawners Help Page 7 of 11 * * *",
				ChatColor.GOLD + "/entities setenderblock [id] <blockID>" + ChatColor.GREEN + " -> Sets the block ID held by spawned endermen.",
				ChatColor.GOLD + "/entities setsaddled [id] <true or false>" + ChatColor.GREEN + " -> Sets whether spawned pigs have saddles.",
				ChatColor.GOLD + "/entities setcharged [id] <true or false>" + ChatColor.GREEN + " -> Sets whether spawned creepers are charged.",
				ChatColor.GOLD + "/entities setjockey [id] <true or false>" + ChatColor.GREEN + " -> Sets whether spawned spiders will have a skeleton rider.",
				ChatColor.GOLD + "/entities settamed [id] <true or false>" + ChatColor.GREEN + " -> Sets whether spawned wolves or ocelots will be tamed or not.",
				ChatColor.GOLD + "/entities setangry [id] <true or false>" + ChatColor.GREEN + " -> Sets whether iron golems, pigmen, or wolves will be angry.",
				ChatColor.GOLD + "/entities setsitting [id] <true or false>" + ChatColor.GREEN + " -> Sets whether a cat or wolf will be sitting.",
				ChatColor.GOLD + "/entities setcattype [id] <type>" + ChatColor.GREEN + " -> Sets the type of cat an ocelot is.",
				ChatColor.GREEN + "* * * * * * * * * * * * * * * *"
		};
		
		final String[] HELP_MESSAGE_8 = {
				ChatColor.GREEN + "* * * CustomSpawners Help Page 8 of 11 * * *",
				ChatColor.GOLD + "/entities setslimesize [id] <size>" + ChatColor.GREEN + " -> Sets the size of spawned slimes.",
				ChatColor.GOLD + "/entities setcolor [id] <color>" + ChatColor.GREEN + " -> Sets the color of spawned sheep.",
				ChatColor.GOLD + "/entities setpassive [id] <true or false>" + ChatColor.GREEN + " -> Sets if the entity will be passive.",
				ChatColor.GOLD + "/entities setblacklist [id] <true or false>" + ChatColor.GREEN + " -> Should it use a damage blacklist?",
				ChatColor.GOLD + "/entities setwhitelist [id] <true or false>" + ChatColor.GREEN + " -> Should it use a damage whitelist?",
				ChatColor.GOLD + "/entities addblacklistitem [id] <damage type>" + ChatColor.GREEN + " -> Add a damage typ to blacklist.",
				ChatColor.GOLD + "/entities addwhitelistitem [id] <damage type>" + ChatColor.GREEN + " -> Add a damage typ to whitelist.",
				ChatColor.GOLD + "/entities clearblacklist [id] " + ChatColor.GREEN + " -> Clears the damage blacklist.",
				ChatColor.GOLD + "/entities clearwhitelist [id] " + ChatColor.GREEN + " -> Clears the damage whitelist.",
				ChatColor.GREEN + "* * * * * * * * * * * * * * * *"
		};
		
		final String[] HELP_MESSAGE_9 = {
				ChatColor.GREEN + "* * * CustomSpawners Help Page 9 of 11 * * *",
				ChatColor.GOLD + "/entities additem [id] <item ID>" + ChatColor.GREEN + " -> Add an item to the itemlist.",
				ChatColor.GOLD + "/entities clearitems [id] " + ChatColor.GREEN + " -> Clears the itemlist.",
				ChatColor.GOLD + "/entities adddrop [id] <item:damage> <amount>" + ChatColor.GREEN + " -> Add an item to the drops.",
				ChatColor.GOLD + "/entities setdrops [id] <item:damage> <amount>" + ChatColor.GREEN + " -> Sets the mob's dropped item.",
				ChatColor.GOLD + "/entities cleardrops [id]" + ChatColor.GREEN + " -> Clears the drops.",
				ChatColor.GOLD + "/entities setusingdrops [id] <true or false>" + ChatColor.GREEN + " -> Set a entity to use custom drops.",
				ChatColor.GOLD + "/entities setcustomdamage [id] <true or false>" + ChatColor.GREEN + " -> Sets whether customized damage will be used.",
				ChatColor.GOLD + "/entities setdamageamount [id] <amount>" + ChatColor.GREEN + " -> Sets the damage amount.",
				ChatColor.GREEN + "* * * * * * * * * * * * * * * *"
		};
		
		final String[] HELP_MESSAGE_10 = {
				ChatColor.GREEN + "* * * CustomSpawners Help Page 10 of 11 * * *",
				ChatColor.GOLD + "/entities setfuselength [id] <length>" + ChatColor.GREEN + " -> Sets how long the fuse on TNT is in ticks.",
				ChatColor.GOLD + "/entities setincendiary [id] <true or false> " + ChatColor.GREEN + " -> Sets whether the explosive kills with fire.",
				ChatColor.GOLD + "/entities setyield [id] <yeild>" + ChatColor.GREEN + " -> Sets the power of an explosive. Five is normal.",
				ChatColor.GOLD + "/entities setitemtype [id] <item:damage>" + ChatColor.GREEN + " -> Sets the type of item or falling block to be.",
				ChatColor.GOLD + "/entities setpotiontype [id] <effect>" + ChatColor.GREEN + " -> Sets the type of potion a potion entity is.",
				ChatColor.GOLD + "/entities setexp [id] <dropped exp>" + ChatColor.GREEN + " -> Sets the dropped experience.",
				ChatColor.GOLD + "/entities clearinventroy [id]" + ChatColor.GREEN + " -> Sets the dropped experience.",
				ChatColor.GOLD + "/entities sethand [id] <dropped exp>" + ChatColor.GREEN + " -> Sets the dropped experience.",
				ChatColor.GOLD + "/entities sethelmet [id] <dropped exp>" + ChatColor.GREEN + " -> Sets the dropped experience.",
				ChatColor.GREEN + "* * * * * * * * * * * * * * * *"
		};
		
		final String[] HELP_MESSAGE_11 = {
				ChatColor.GREEN + "* * * CustomSpawners Help Page 11 of 11 * * *",
				ChatColor.GOLD + "/entities setchest [id] <dropped exp>" + ChatColor.GREEN + " -> Sets the dropped experience.",
				ChatColor.GOLD + "/entities setpants [id] <dropped exp>" + ChatColor.GREEN + " -> Sets the dropped experience.",
				ChatColor.GOLD + "/entities setboots [id] <dropped exp>" + ChatColor.GREEN + " -> Sets the dropped experience.",
				ChatColor.GOLD + "/entities addinv [id] <dropped exp>" + ChatColor.GREEN + " -> Sets the dropped experience.",
				ChatColor.GOLD + "/entities setwither [id] <dropped exp>" + ChatColor.GREEN + " -> Sets the dropped experience.",
				ChatColor.GREEN + "* * * * * * * * * * * * * * * *"
		};
		
		final String[] HELP_MESSAGE_NO_COLOR = {
				"* * * CustomSpawners Help * * *",
				"/customspawners help [page] -> Displays this message.", 
				"/customspawners reload -> Reloads everything.", 
				"/spawners removeallmobs -> Kills all mobs created by spawners in the server.",
				"/spawners removemobs [id] -> Kills all mobs created by the spawner with specified ID.",
				"/spawners activateall -> Sets all spawners on the server active.",
				"/spawners select <id> -> Sets all spawners on the server inactive.",
				"/spawners setactive [id] -> Sets all spawners on the server inactive.",
				"/spawners setinactive [id] -> Sets all spawners on the server inactive.",
				"/spawners forcespawn [id] -> Sets all spawners on the server inactive.",
				"/spawners setmaxmobs [id] <max mobs> -> Sets all spawners on the server inactive.",
				"/spawners setmobsperspawn [id] <mobs per spawn> -> Sets all spawners on the server inactive.",
				"/spawners listall -> Lists all the spawners that have been created.",
				"/spawners info [id] -> Displays info on a spawner.",
				"/entities info [id] -> Displays info on a entity.",
				"* * * * * * * * * * * * * * * *"
		};
		
		final String[] PLUGIN_INFO = {
				ChatColor.GREEN + "* * * " +  ChatColor.GOLD + " CustomSpawners " + plugin.getDescription().getVersion() + ChatColor.GREEN + " by thebiologist13* * *",
				"",
				ChatColor.GREEN + "Plugin on BukkitDev: ",
				ChatColor.AQUA + "http://dev.bukkit.org/server-mods/customspawners/",
				"",
				ChatColor.GREEN + "thebiologist13 on BukkitDev: ",
				ChatColor.AQUA + "http://dev.bukkit.org/profiles/thebiologist13/",
				"",
				ChatColor.GREEN + "Use \"/customspawners help [page]\" for help on using CustomSpawners",
				ChatColor.GREEN + "* * * * * * * * * * * * * * * *"
		};
		
		final String[] PLUGIN_INFO_NO_COLOR = {
				"* * * CustomSpawners " + plugin.getDescription().getVersion() + " by thebiologist13* * *",
				"",
				"Plugin on BukkitDev: ",
				"http://dev.bukkit.org/server-mods/customspawners/",
				"",
				"thebiologist13 on BukkitDev: ",
				"http://dev.bukkit.org/profiles/thebiologist13/",
				"",
				"Use \"/customspawners help [page]\" for help on using CustomSpawners",
				"* * * * * * * * * * * * * * * *"
		};
		
		final String INFO_PERM = "customspawners.customspawners";
		final String HELP_PERM = "customspawners.help";
		
		if(arg0 instanceof Player) {
			Player p = (Player) arg0;
			
			if(arg3.length == 0) {
				if(p.hasPermission(INFO_PERM)) {
					p.sendMessage(PLUGIN_INFO);
				} else {
					p.sendMessage(SpawnerCommand.NO_PERMISSION);
				}
			} else if(arg3.length == 1) {
				if(p.hasPermission(HELP_PERM)) {
					p.sendMessage(HELP_MESSAGE_0);
				} else {
					p.sendMessage(SpawnerCommand.NO_PERMISSION);
				}
			} else if(arg3.length == 2) {
				if(p.hasPermission(HELP_PERM)) {
					if(arg3[1].equalsIgnoreCase("1")) {
						p.sendMessage(HELP_MESSAGE_1);
					} else if(arg3[1].equalsIgnoreCase("2")) {
						p.sendMessage(HELP_MESSAGE_2);
					} else if(arg3[1].equalsIgnoreCase("3")) {
						p.sendMessage(HELP_MESSAGE_3);
					} else if(arg3[1].equalsIgnoreCase("4")) {
						p.sendMessage(HELP_MESSAGE_4);
					} else if(arg3[1].equalsIgnoreCase("5")) {
						p.sendMessage(HELP_MESSAGE_5);
					} else if(arg3[1].equalsIgnoreCase("6")) {
						p.sendMessage(HELP_MESSAGE_6);
					} else if(arg3[1].equalsIgnoreCase("7")) {
						p.sendMessage(HELP_MESSAGE_7);
					} else if(arg3[1].equalsIgnoreCase("8")) {
						p.sendMessage(HELP_MESSAGE_8);
					} else if(arg3[1].equalsIgnoreCase("9")) {
						p.sendMessage(HELP_MESSAGE_9);
					} else if(arg3[1].equalsIgnoreCase("10")) {
						p.sendMessage(HELP_MESSAGE_10);
					} else if(arg3[1].equalsIgnoreCase("11")) {
						p.sendMessage(HELP_MESSAGE_11);
					} else {
						p.sendMessage(ChatColor.RED + "That is not a page of the help dialogue.");
					}	
				} else {
					p.sendMessage(SpawnerCommand.NO_PERMISSION);
				}	
			} 
		} else {
			if(arg3.length == 0) {
				for(String s : PLUGIN_INFO_NO_COLOR) {
					log.info(s);
				}
			} else if(arg3.length == 1) {
				for(String s : HELP_MESSAGE_NO_COLOR) {
					log.info(s);
				}
			} else if(arg3.length == 2) {
				for(String s : HELP_MESSAGE_NO_COLOR) {
					log.info(s);
				}
			}
		}
	}
}
