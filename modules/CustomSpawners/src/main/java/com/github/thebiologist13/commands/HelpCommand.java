package com.github.thebiologist13.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.github.thebiologist13.CustomSpawners;


/**
 * This command displays the help message.
 * 
 * @author thebiologist13
 */
public class HelpCommand extends CustomSpawnersCommand {

	public HelpCommand(CustomSpawners plugin) {
		super(plugin);
	}
	
	public HelpCommand(CustomSpawners plugin, String perm) {
		super(plugin, perm);
	}
	
	@Override
	public void run(CommandSender sender, String subCommand, String[] args) {
		
		final String[] HELP_MESSAGE_0 = {
				ChatColor.GREEN + "* * * CustomSpawners Help * * *",
				ChatColor.GOLD + "Use /customspawners help [page #] to see a help page.",
				ChatColor.GOLD + "Arguments in " + ChatColor.AQUA + "[] are optional" + ChatColor.GOLD + ", they are often to identify a",
				ChatColor.GOLD + "object by ID number. Prefix these IDs with \"t:\". Arguments in " + ChatColor.DARK_RED + "<> are required" +
				ChatColor.GOLD + ". They are usually the value to set.",
				ChatColor.GREEN + "* * * * * * * * * * * * * * * *"
		};
		
		final String[] HELP_MESSAGE_MAIN = {
				ChatColor.GOLD + "/customspawners help [page]" + ChatColor.GREEN + " -> Displays this message.", 
				ChatColor.GOLD + "/customspawners" + ChatColor.GREEN + " -> Displays the plugin info.", 
				ChatColor.GOLD + "/customspawners reloadspawners" + ChatColor.GREEN + " -> Reloads spawners from file.",
				ChatColor.GOLD + "/customspawners import" + ChatColor.GREEN + " -> Imports CustomSpawners from world folder.",
				ChatColor.GOLD + "/customspawners export" + ChatColor.GREEN + " -> Exports CustomSpawners to world folder.",
				ChatColor.GOLD + "/spawners create <type>" + ChatColor.GREEN + " -> Creates a new spawner of the specified entity.",
				ChatColor.GOLD + "/spawners setname [id] <name>" + ChatColor.GREEN + " -> Names or renames the spawner for easier selection.",
				ChatColor.GOLD + "/spawners remove [id]" + ChatColor.GREEN + " -> Removes a spawner.",
				ChatColor.GOLD + "/spawners select <id>" + ChatColor.GREEN + " -> Selects a spawner so the ID does not need to be entered every time.",
				ChatColor.GOLD + "/spawners listnear" + ChatColor.GREEN + " -> Displays nearby spawners with IDs and locations.",
				ChatColor.GOLD + "/spawners listall" + ChatColor.GREEN + " -> Displays all spawners with IDs and locations.",
				ChatColor.GOLD + "/spawners setactive [id]" + ChatColor.GREEN + " -> Makes a spawner active so it spawns.",
				ChatColor.GOLD + "/spawners setinactive [id]" + ChatColor.GREEN + " -> Makes a spawner inactive so it doesn't spawn.",
				ChatColor.GOLD + "/spawners info [id]" + ChatColor.GREEN + " -> Displays information on a spawner.",
				ChatColor.GOLD + "/spawners setmaxlight [id] <light level>" + ChatColor.GREEN + " -> Sets the maximum light before spawning.",
				ChatColor.GOLD + "/spawners setminlight [id] <light level>" + ChatColor.GREEN + " -> Sets the minimum light before spawning.",
				ChatColor.GOLD + "/spawners setmaxmobs [id] <max mobs>" + ChatColor.GREEN + " -> Sets the maximum mobs a spawner will spawn.",
				ChatColor.GOLD + "/spawners setmobsperspawn [id] <mobs per spawn>" + ChatColor.GREEN + " -> Sets mobs spawned per rate.",
				ChatColor.GOLD + "/spawners settype [id] <new type>" + ChatColor.GREEN + " -> Sets the type of mob to spawn.",
				ChatColor.GOLD + "/spawners addtype [id] <type>" + ChatColor.GREEN + " -> Adds a type of mob to spawn.",
				ChatColor.GOLD + "/spawners setredstone [id] <true or false>" + ChatColor.GREEN + " -> Sets whether a spawner must be powered.", 
				ChatColor.GOLD + "/spawners setrate [id] <rate>" + ChatColor.GREEN + " -> Sets how fast in ticks to spawn.",
				ChatColor.GOLD + "/spawners setradius [id] <radius>" + ChatColor.GREEN + " -> Sets the spawn area radius.",
				ChatColor.GOLD + "/spawners setlocation [id]" + ChatColor.GREEN + " -> Sets the location of a spawner.",
				ChatColor.GOLD + "/spawners setmaxdistance [id] <distance>" + ChatColor.GREEN + " -> Sets maximum distance a player can be.",
				ChatColor.GOLD + "/spawners setmindistance [id] <distance>" + ChatColor.GREEN + " -> Sets minimum distance a player can be.",
				ChatColor.GOLD + "/spawners forcespawn [id]" + ChatColor.GREEN + " -> Forces a spawner to spawn mobs regardless of conditions.",
				ChatColor.GOLD + "/spawners removeallmobs" + ChatColor.GREEN + " -> Kills all mobs created by spawners in the server.", 
				ChatColor.GOLD + "/spawners removemobs <id>" + ChatColor.GREEN + " -> Kills all mobs created by the spawner with specified ID.",
				ChatColor.GOLD + "/spawners activateall" + ChatColor.GREEN + " -> Sets all spawners on the server active.",
				ChatColor.GOLD + "/spawners deactivateall" + ChatColor.GREEN + " -> Sets all spawners on the server inactive.",
				ChatColor.GOLD + "/spawners setname [id] <name>" + ChatColor.GREEN + " -> Gives a name to the spawner.",
				ChatColor.GOLD + "/spawners convert [id]" + ChatColor.GREEN + " -> Converts a CustomSpawners spawner to a mob spawner block and back.",
				ChatColor.GOLD + "/spawners clone [id]" + ChatColor.GREEN + " -> Clones a spawner to a new location.",
				ChatColor.GOLD + "/spawners sethidden [id]" + ChatColor.GREEN + " -> Makes a spawner hidden.", 
				ChatColor.GOLD + "/spawners setunhidden [id]" + ChatColor.GREEN + " -> Unhides a spawner.",
				ChatColor.GOLD + "/spawners spawnonpower [id] <true or false>" + ChatColor.GREEN + " -> Whether to spawn on power.",
				ChatColor.GOLD + "/spawners wand" + ChatColor.GREEN + " -> Toggle the selection wand.",
				ChatColor.GOLD + "/spawners settimes [id] <time,...,time>" + ChatColor.GREEN + " -> Force times to spawn at.",
				ChatColor.GOLD + "/spawners addtime [id] <time,...,time>" + ChatColor.GREEN + " -> Add time to spawn at.",
				ChatColor.GOLD + "/spawners cleartime [id]" + ChatColor.GREEN + " -> Clears spawn times.",
				ChatColor.GOLD + "/spawners addmod [id] <property>=<expression>" + ChatColor.GREEN + " -> Adds a modifier.",
				ChatColor.GOLD + "/spawners setmod [id] <property>=<expression>" + ChatColor.GREEN + " -> Sets the modifier.",
				ChatColor.GOLD + "/spawners clearmods [id]" + ChatColor.GREEN + " -> Clears the modifiers.",
				ChatColor.GOLD + "/entities create <type>" + ChatColor.GREEN + " -> Creates a new spawnable entity that can be customized.",
				ChatColor.GOLD + "/entities remove [id]" + ChatColor.GREEN + " -> Removes a spawnable entity so it can't be used by spawners.",
				ChatColor.GOLD + "/entities select <id>" + ChatColor.GREEN + " -> Selects an entity so the entity ID doesn't need to be entered every time.",
				ChatColor.GOLD + "/entities setvelocity [id] <x velocity,y velocity,z velocity>" + ChatColor.GREEN + " -> Sets the velocity of spawned entities.",
				ChatColor.GOLD + "/entities setenderblock [id] <blockID>" + ChatColor.GREEN + " -> Sets the block ID held by spawned endermen.",
				ChatColor.GOLD + "/entities settype [id] <type>" + ChatColor.GREEN + " -> Sets the base entity type of a spawnable entity.",
				ChatColor.GOLD + "/entities setname [id] <name>" + ChatColor.GREEN + " -> Names or renames the entity for easier selection.",
				ChatColor.GOLD + "/entities showname [id] <true or false>" + ChatColor.GREEN + " -> Whether to show the name of the entity above their head.",
				ChatColor.GOLD + "/entities setenderblock [id] <blockID>" + ChatColor.GREEN + " -> Sets the block ID held by spawned endermen.",
				ChatColor.GOLD + "/entities setsaddled [id] <true or false>" + ChatColor.GREEN + " -> Sets whether spawned pigs have saddles.",
				ChatColor.GOLD + "/entities setcharged [id] <true or false>" + ChatColor.GREEN + " -> Sets whether spawned creepers are charged.",
				ChatColor.GOLD + "/entities setjockey [id] <true or false>" + ChatColor.GREEN + " -> Sets whether spawned spiders will have a skeleton rider.",
				ChatColor.GOLD + "/entities settamed [id] <true or false>" + ChatColor.GREEN + " -> Sets whether spawned wolves or ocelots will be tamed or not.",
				ChatColor.GOLD + "/entities setangry [id] <true or false>" + ChatColor.GREEN + " -> Sets whether iron golems, pigmen, or wolves will be angry.",
				ChatColor.GOLD + "/entities setsitting [id] <true or false>" + ChatColor.GREEN + " -> Sets whether a cat or wolf will be sitting.",
				ChatColor.GOLD + "/entities setcattype [id] <type>" + ChatColor.GREEN + " -> Sets the type of cat an ocelot is.",
				ChatColor.GOLD + "/entities setslimesize [id] <size>" + ChatColor.GREEN + " -> Sets the size of spawned slimes.",
				ChatColor.GOLD + "/entities setcolor [id] <color>" + ChatColor.GREEN + " -> Sets the color of spawned sheep.",
				ChatColor.GOLD + "/entities setpassive [id] <true or false>" + ChatColor.GREEN + " -> Sets if the entity will be passive.",
				ChatColor.GOLD + "/entities setblacklist [id] <true or false>" + ChatColor.GREEN + " -> Should it use a damage blacklist?",
				ChatColor.GOLD + "/entities setwhitelist [id] <true or false>" + ChatColor.GREEN + " -> Should it use a damage whitelist?",
				ChatColor.GOLD + "/entities addblacklistitem [id] <damage type>" + ChatColor.GREEN + " -> Add a damage typ to blacklist.",
				ChatColor.GOLD + "/entities addwhitelistitem [id] <damage type>" + ChatColor.GREEN + " -> Add a damage typ to whitelist.",
				ChatColor.GOLD + "/entities clearblacklist [id] " + ChatColor.GREEN + " -> Clears the damage blacklist.",
				ChatColor.GOLD + "/entities clearwhitelist [id] " + ChatColor.GREEN + " -> Clears the damage whitelist.",
				ChatColor.GOLD + "/entities additem [id] <item ID>" + ChatColor.GREEN + " -> Add an item to the itemlist.",
				ChatColor.GOLD + "/entities clearitems [id] " + ChatColor.GREEN + " -> Clears the itemlist.",
				ChatColor.GOLD + "/entities adddrop [id] <item:damage> <amount>" + ChatColor.GREEN + " -> Add an item to the drops.",
				ChatColor.GOLD + "/entities setdrops [id] <item:damage> <amount>" + ChatColor.GREEN + " -> Sets the mob's dropped item.",
				ChatColor.GOLD + "/entities cleardrops [id]" + ChatColor.GREEN + " -> Clears the drops.",
				ChatColor.GOLD + "/entities setusingdrops [id] <true or false>" + ChatColor.GREEN + " -> Set a entity to use custom drops.",
				ChatColor.GOLD + "/entities setcustomdamage [id] <true or false>" + ChatColor.GREEN + " -> Sets whether customized damage will be used.",
				ChatColor.GOLD + "/entities setdamageamount [id] <amount>" + ChatColor.GREEN + " -> Sets the damage amount.",
				ChatColor.GOLD + "/entities setfuselength [id] <length>" + ChatColor.GREEN + " -> Sets how long the fuse on TNT is in ticks.",
				ChatColor.GOLD + "/entities setincendiary [id] <true or false> " + ChatColor.GREEN + " -> Sets whether the explosive kills with fire.",
				ChatColor.GOLD + "/entities setyield [id] <yeild>" + ChatColor.GREEN + " -> Sets the power of an explosive. Five is normal.",
				ChatColor.GOLD + "/entities setitemtype [id] <item:damage>" + ChatColor.GREEN + " -> Sets the type of item or falling block to be.",
				ChatColor.GOLD + "/entities setpotiontype [id] <effect>" + ChatColor.GREEN + " -> Sets the type of potion a potion entity is.",
				ChatColor.GOLD + "/entities setexp [id] <dropped exp>" + ChatColor.GREEN + " -> Sets the dropped experience.",
				ChatColor.GOLD + "/entities clearinventroy [id]" + ChatColor.GREEN + " -> Sets the dropped experience.",
				ChatColor.GOLD + "/entities sethand [id] <dropped exp>" + ChatColor.GREEN + " -> Sets the dropped experience.",
				ChatColor.GOLD + "/entities sethelmet [id] <dropped exp>" + ChatColor.GREEN + " -> Sets the dropped experience.",
				ChatColor.GOLD + "/entities setchest [id] <dropped exp>" + ChatColor.GREEN + " -> Sets the dropped experience.",
				ChatColor.GOLD + "/entities setpants [id] <dropped exp>" + ChatColor.GREEN + " -> Sets the dropped experience.",
				ChatColor.GOLD + "/entities setboots [id] <dropped exp>" + ChatColor.GREEN + " -> Sets the dropped experience.",
				ChatColor.GOLD + "/entities addinv [id] <dropped exp>" + ChatColor.GREEN + " -> Sets the dropped experience.",
				ChatColor.GOLD + "/entities setwither [id] <dropped exp>" + ChatColor.GREEN + " -> Sets the dropped experience.",
				ChatColor.GOLD + "/entities seteffect [id] <effect>" + ChatColor.GREEN + " -> Sets the potion effect on an entity.",
				ChatColor.GOLD + "/entities cleareffects [id]" + ChatColor.GREEN + " -> Clear all the potion effects on a spawner.",
				ChatColor.GOLD + "/entities addeffect [id] <effect>" + ChatColor.GREEN + " -> Adds a potion effect to a spawnable entity.",
				ChatColor.GOLD + "/entities zombievillager [id] <effect>" + ChatColor.GREEN + " -> Makes a zombie a zombie villager.",
				ChatColor.GOLD + "/entities sethealth [id] <health>" + ChatColor.GREEN + " -> Sets the health of spawned entities.", 
				ChatColor.GOLD + "/entities setair [id] <air>" + ChatColor.GREEN + " -> Sets the remaining air for an entity.",
				ChatColor.GOLD + "/entities setprofession [id] <profession>" + ChatColor.GREEN + " -> Sets the profession of a spawned villager.",
				ChatColor.GOLD + "/entities setage [id] <age>" + ChatColor.GREEN + " -> Sets the age of a spawned entity.",
				ChatColor.GOLD + "/entities setrider [id] <entity ID>" + ChatColor.GREEN + " -> Sets the spawnable entity riding this mob.",
				ChatColor.GOLD + "/entities setminecartspeed [id] <speed>" + ChatColor.GREEN + " -> Sets the minecart speed."
		};
		
		String pIn = getValue(args, 0, 0);
		int page = 0;
		
		if(!CustomSpawners.isInteger(pIn)) {
			PLUGIN.sendMessage(sender, ChatColor.RED + "Specify a number for a help page.");
			return;
		}
		
		page = Integer.parseInt(pIn);
		
		if(page == 0) {
			PLUGIN.sendMessage(sender, HELP_MESSAGE_0);
			return;
		}
		
		int startIndex = (page - 1) * 8;
		
		if(startIndex > HELP_MESSAGE_MAIN.length) {
			PLUGIN.sendMessage(sender, ChatColor.RED + "That page is out of range.");
			return;
		}
		
		int totalPages = 0;
		int length = 0;
		int remain = HELP_MESSAGE_MAIN.length % 8;
		if(remain != 0) {
			length = HELP_MESSAGE_MAIN.length + (8 - remain); 
		} else {
			length = HELP_MESSAGE_MAIN.length;
		}
		totalPages = length / 8;
		
		int arraySize = 10;
		if((HELP_MESSAGE_MAIN.length - startIndex) < 8) {
			arraySize = (HELP_MESSAGE_MAIN.length - startIndex) + 1;
		}
		String[] showThis = new String[arraySize];
		showThis[0] = ChatColor.GREEN + "* * * CustomSpawners Help Page " + page + " of " + totalPages + " * * *";
		showThis[arraySize - 1] = ChatColor.GREEN + "* * * * * * * * * * * * * * * *";
		for(int i = 1; i < (arraySize - 1); i++) {
			if((startIndex + i) > (HELP_MESSAGE_MAIN.length - 1))
				break;
			showThis[i] = HELP_MESSAGE_MAIN[startIndex + i];
		}
		
		PLUGIN.sendMessage(sender, showThis);
		
	}
	
}
