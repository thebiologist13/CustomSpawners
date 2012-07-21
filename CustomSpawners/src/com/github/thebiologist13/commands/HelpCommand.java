package com.github.thebiologist13.commands;

import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.thebiologist13.CustomSpawners;

public class HelpCommand extends SpawnerCommand implements CommandExecutor {

	private CustomSpawners plugin = null;
	
	private Logger log = null;
	
	final String[] HELP_MESSAGE_1 = {
			ChatColor.GREEN + "* * * CustomSpawners Help Page 1 of 7 * * *",
			ChatColor.GOLD + "/customspawners help [page]" + ChatColor.GREEN + " -> Displays this message.", 
			ChatColor.GOLD + "/customspawners" + ChatColor.GREEN + " -> Displays the plugin info.", 
			ChatColor.GOLD + "/customspawners reloadspawners" + ChatColor.GREEN + " -> Reloads spawners from file.",
			ChatColor.GOLD + "/spawners create <type>" + ChatColor.GREEN + " -> Creates a new spawner of the specified entity.",
			ChatColor.GOLD + "/spawners remove [id]" + ChatColor.GREEN + " -> Removes a spawner.",
			ChatColor.GOLD + "/spawners select <id>" + ChatColor.GREEN + " -> Selects a spawner so the ID does not need to be entered every time.",
			ChatColor.GOLD + "/spawners listnear" + ChatColor.GREEN + " -> Displays nearby spawners with IDs and locations.",
			ChatColor.GOLD + "/spawners listall" + ChatColor.GREEN + " -> Displays all spawners with IDs and locations.",
			ChatColor.GREEN + "* * * * * * * * * * * * * * * *"
	};
	
	final String[] HELP_MESSAGE_2 = {
			ChatColor.GREEN + "* * * CustomSpawners Help Page 2 of 7 * * *",
			ChatColor.GOLD + "/spawners setactive [id]" + ChatColor.GREEN + " -> Makes a spawner active so it spawns.",
			ChatColor.GOLD + "/spawners setinactive [id]" + ChatColor.GREEN + " -> Makes a spawner inactive so it doesn't spawn.",
			ChatColor.GOLD + "/spawners sethidden [id]" + ChatColor.GREEN + " -> Makes a spawner hidden.", 
			ChatColor.GOLD + "/spawners setunhidden [id]" + ChatColor.GREEN + " -> Unhides a spawner.",
			ChatColor.GOLD + "/spawners info [id]" + ChatColor.GREEN + " -> Displays information on a spawner.",
			ChatColor.GOLD + "/spawners setmaxlight [id] <light level>" + ChatColor.GREEN + " -> Sets the maximum light before spawning.",
			ChatColor.GOLD + "/spawners setminlight [id] <light level>" + ChatColor.GREEN + " -> Sets the minimum light before spawning.",
			ChatColor.GOLD + "/spawners setmaxmobs [id] <max mobs>" + ChatColor.GREEN + " -> Sets the maximum mobs a spawner will spawn.",
			ChatColor.GREEN + "* * * * * * * * * * * * * * * *"
	};
	
	final String[] HELP_MESSAGE_3 = {
			ChatColor.GREEN + "* * * CustomSpawners Help Page 3 of 7 * * *",
			ChatColor.GOLD + "/spawners setmobsperspawn [id] <mobs per spawn>" + ChatColor.GREEN + " -> Sets mobs spawned per rate.",
			ChatColor.GOLD + "/spawners settype [id] <new type>" + ChatColor.GREEN + " -> Sets the type of mob to spawn.",
			ChatColor.GOLD + "/spawners setredstone [id] <true or false>" + ChatColor.GREEN + " -> Sets whether a spawner must be powered.", 
			ChatColor.GOLD + "/spawners setrate [id] <rate>" + ChatColor.GREEN + " -> Sets how fast in ticks to spawn.",
			ChatColor.GOLD + "/spawners setradius [id] <radius>" + ChatColor.GREEN + " -> Sets the spawn area radius.",
			ChatColor.GOLD + "/spawners setlocation [id]" + ChatColor.GREEN + " -> Sets the location of a spawner.",
			ChatColor.GOLD + "/spawners setmaxdistance [id] <distance>" + ChatColor.GREEN + " -> Sets maximum distance a player can be.",
			ChatColor.GOLD + "/spawners setmindistance [id] <distance>" + ChatColor.GREEN + " -> Sets minimum distance a player can be.",
			ChatColor.GREEN + "* * * * * * * * * * * * * * * *"
	};
	
	final String[] HELP_MESSAGE_4 = {
			ChatColor.GREEN + "* * * CustomSpawners Help Page 4 of 7 * * *",
			ChatColor.GOLD + "/spawners forcespawn [id]" + ChatColor.GREEN + " -> Forces a spawner to spawn mobs regardless of conditions.",
			ChatColor.GOLD + "/spawners removeallmobs" + ChatColor.GREEN + " -> Kills all mobs created by spawners in the server.", 
			ChatColor.GOLD + "/spawners removemobs <id>" + ChatColor.GREEN + " -> Kills all mobs created by the spawner with specified ID.",
			ChatColor.GOLD + "/spawners activateall" + ChatColor.GREEN + " -> Sets all spawners on the server active.",
			ChatColor.GOLD + "/spawners deactivateall" + ChatColor.GREEN + " -> Sets all spawners on the server inactive.",
			ChatColor.GOLD + "/entities create <type>" + ChatColor.GREEN + " -> Creates a new spawnable entity that can be customized.",
			ChatColor.GOLD + "/entities remove [id]" + ChatColor.GREEN + " -> Removes a spawnable entity so it can't be used by spawners.",
			ChatColor.GOLD + "/entities select <id>" + ChatColor.GREEN + " -> Selects an entity so the entity ID doesn't need to be entered every time.",
			ChatColor.GREEN + "* * * * * * * * * * * * * * * *"
	};
	
	final String[] HELP_MESSAGE_5 = {
			ChatColor.GREEN + "* * * CustomSpawners Help Page 5 of 7 * * *",
			ChatColor.GOLD + "/entities settype [id] <type>" + ChatColor.GREEN + " -> Sets the base entity type of a spawnable entity.", //TODO if user inputs spiderjockey, make type spider and set isJockey true.
			ChatColor.GOLD + "/entities seteffect [id] <effect>" + ChatColor.GREEN + " -> Sets the potion effect on an entity. Type \"NONE\" for no effects.", //TODO make "NONE" nullify the effects 
			ChatColor.GOLD + "/entities addeffect [id] <effect>" + ChatColor.GREEN + " -> Adds a potion effect to a spawnable entity.",
			ChatColor.GOLD + "/entities setx [id] <xvelocity>" + ChatColor.GREEN + " -> Sets the X velocity of spawned entities.",
			ChatColor.GOLD + "/entities sety [id] <yvelocity>" + ChatColor.GREEN + " -> Sets the Y velocity of spawned entities.",
			ChatColor.GOLD + "/entities setz [id] <zvelocity>" + ChatColor.GREEN + " -> Sets the Z velocity of spawned entities.",
			ChatColor.GOLD + "/entities setage [id] <age>" + ChatColor.GREEN + " -> Sets the age of a spawned entity.",
			ChatColor.GOLD + "/entities setadult [id]" + ChatColor.GREEN + " -> Makes this entity's age an adult.",
			ChatColor.GREEN + "* * * * * * * * * * * * * * * *"
	};
	
	final String[] HELP_MESSAGE_6 = {
			ChatColor.GREEN + "* * * CustomSpawners Help Page 6 of 7 * * *",
			ChatColor.GOLD + "/entities setbaby [id]" + ChatColor.GREEN + " -> Makes this entity's age a baby.",
			ChatColor.GOLD + "/entities sethealth [id] <health>" + ChatColor.GREEN + " -> Sets the health of spawned entities.", 
			ChatColor.GOLD + "/entities setair [id] <air>" + ChatColor.GREEN + " -> Sets the remaining air for an entity.",
			ChatColor.GOLD + "/entities setprofession [id] <profession>" + ChatColor.GREEN + " -> Sets the profession of a spawned villager.",
			ChatColor.GOLD + "/entities setenderblock [id] <blockID>" + ChatColor.GREEN + " -> Sets the block ID held by spawned endermen.",
			ChatColor.GOLD + "/entities setsaddled [id] <true or false>" + ChatColor.GREEN + " -> Sets whether spawned pigs have saddles.",
			ChatColor.GOLD + "/entities setcharged [id] <true or false>" + ChatColor.GREEN + " -> Sets whether spawned creepers are charged.",
			ChatColor.GOLD + "/entities setjockey [id] <true or false>" + ChatColor.GREEN + " -> Sets whether spawned spiders will have a skeleton rider.",
			ChatColor.GREEN + "* * * * * * * * * * * * * * * *"
	};
	
	final String[] HELP_MESSAGE_7 = {
			ChatColor.GREEN + "* * * CustomSpawners Help Page 7 of 7 * * *",
			ChatColor.GOLD + "/entities settamed [id] <true or false>" + ChatColor.GREEN + " -> Sets whether spawned wolves or ocelots will be tamed or not.", //TODO if tame, set angry false
			ChatColor.GOLD + "/entities setangry [id] <true or false>" + ChatColor.GREEN + " -> Sets whether iron golems, pigmen, or wolves will be angry.", //TODO vice-versa of above
			ChatColor.GOLD + "/entities setsitting [id] <true or false>" + ChatColor.GREEN + " -> Sets whether a cat or wolf will be sitting.", //TODO if sitting is true, set tamed and not angry
			ChatColor.GOLD + "/entities setcattype [id] <type>" + ChatColor.GREEN + " -> Sets the type of cat an ocelot is.", //TODO set tamed and not angry if called
			ChatColor.GOLD + "/entities setenderblock [id] <blockID>" + ChatColor.GREEN + " -> Sets the block ID held by spawned endermen.",
			ChatColor.GOLD + "/entities setslimesize [id] <size>" + ChatColor.GREEN + " -> Sets the size of spawned slimes.",
			ChatColor.GOLD + "/entities setcolor [id] <color>" + ChatColor.GREEN + " -> Sets the color of spawned sheep.",
			ChatColor.GREEN + "* * * * * * * * * * * * * * * *"
	};
	
	final String[] HELP_MESSAGE_NO_COLOR = {
			"* * * CustomSpawners Help * * *",
			"/customspawners help [page] -> Displays this message.", 
			"/customspawners reloadspawners -> Reloads all spawners.", 
			"/spawners removeallmobs -> Kills all mobs created by spawners in the server.",
			"/spawners removemobs <id> -> Kills all mobs created by the spawner with specified ID.",
			"/spawners activateall -> Sets all spawners on the server active.",
			"/spawners deactivateall -> Sets all spawners on the server inactive.",
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
	
	public HelpCommand(CustomSpawners plugin) {
		this.plugin = plugin;
		this.log = plugin.log;
	}

	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		
		Player p = null;
		if(arg0 instanceof Player) {
			p = (Player) arg0;
		}
		
		if(p == null) {
			switch(arg3.length) {
			case 1:
				for(String s : PLUGIN_INFO_NO_COLOR) {
					log.info(s);
					return true;
				}
			case 2:
				for(String s : HELP_MESSAGE_NO_COLOR) {
					log.info(s);
					return true;
				}
			case 3:
				for(String s : HELP_MESSAGE_NO_COLOR) {
					log.info(s);
					return true;
				}
			default:
				return false;
			}
		} else {
			switch(arg3.length) {
			case 1:
				if(p.hasPermission(INFO_PERM)) {
					p.sendMessage(PLUGIN_INFO);
					return true;
				} else {
					p.sendMessage(NO_PERMISSION);
					return true;
				}
			case 2:
				if(p.hasPermission(HELP_PERM)) {
					p.sendMessage(HELP_MESSAGE_1);
					return true;
				} else {
					p.sendMessage(NO_PERMISSION);
					return true;
				}
			case 3:
				if(p.hasPermission(HELP_PERM)) {
					if(!plugin.isInteger(arg3[1])) {
						p.sendMessage(SPECIFY_NUMBER);
					} else { //TODO This will change if more commands are added
						switch(Integer.parseInt(arg3[1])) {
						case 1:
							p.sendMessage(HELP_MESSAGE_1);
							break;
						case 2:
							p.sendMessage(HELP_MESSAGE_2);
							break;
						case 3:
							p.sendMessage(HELP_MESSAGE_3);
							break;
						case 4:
							p.sendMessage(HELP_MESSAGE_4);
							break;
						case 5:
							p.sendMessage(HELP_MESSAGE_5);
							break;
						case 6:
							p.sendMessage(HELP_MESSAGE_6);
							break;
						case 7:
							p.sendMessage(HELP_MESSAGE_7);
							break;
						default:
							p.sendMessage(ChatColor.RED + "Page " + arg3[1] + " does not exist.");
							break;
						}
					}
					return true;
				} else {
					p.sendMessage(NO_PERMISSION);
				}
				break;
			default:
				return false;
			}
		}
		return false;
	}
}
