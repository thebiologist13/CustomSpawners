package com.github.thebiologist13.commands;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import com.github.thebiologist13.CustomSpawners;

/**
 * Superclass for all commands.
 * 
 * @author thebiologist13
 */
public class SubCommand {
	
	public final String GENERAL_ERROR = ChatColor.RED + "An error has occured with this command. Did you type everything right?";
	public final String NO_CONSOLE = "This command can only be used in-game";
	public final String NO_ID = ChatColor.RED + "Object with that name or ID does not exist.";
	public final String NO_PERMISSION = ChatColor.RED + "You do not have permission!";
	public final String NOT_COMMAND = ChatColor.RED + "That is not a command for CustomSpawners.";
	public final String NOT_INT_AMOUNT = ChatColor.RED + "You must input an integer for the amount.";
	
	public final FileConfiguration CONFIG;
	public final Logger LOG;
	public final CustomSpawners PLUGIN;
	
	public String permission;
	
	private Map<String, String> aliases = new HashMap<String, String>();
	
	public SubCommand(CustomSpawners plugin, String mainPerm) {
		this.PLUGIN = plugin;
		this.CONFIG = plugin.getCustomConfig();
		this.LOG = plugin.log;
		this.permission = mainPerm;
	}

	public void addAlias(String alias, String mainCommand) {
		this.aliases.put(alias.toLowerCase(), mainCommand.toLowerCase());
	}

	public Map<String, String> getAliases() {
		return aliases;
	}
	
	public String getCommand(String alias) {
		alias = alias.toLowerCase();
		return (this.aliases.containsKey(alias)) ? this.aliases.get(alias) : alias;
	}
	
	public String getPermission() {
		return permission;
	}
	
	public String getValue(String[] input, int index, Object defVal) {
		if(input.length > index) {
			return input[index];
		} else {
			return String.valueOf(defVal).toLowerCase();
		}
	}
	
	public void removeAlias(String alias) {
		this.aliases.remove(alias.toLowerCase());
	}
	
	public boolean permissible(CommandSender sender, String perm) {
		if(sender instanceof Player) {
			return ((Player) sender).hasPermission(perm);
		} else {
			return true;
		}
	}

	public void setAliases(Map<String, String> aliases) {
		this.aliases = aliases;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}
	
//	/*
//	 * Note:
//	 * 
//	 * Sub-commands implementing this interface do not need
//	 * to check for the length of the argument (at 
//	 * least for command validity, they do if it can
//	 * be used in multiple ways). They only need to
//	 * check if valid values were put in, check for
//	 * thrown exceptions, check permissions, and
//	 * carry out the command.
//	 */
//	
//	public final static String GENERAL_ERROR = ChatColor.RED + "An error has occured with this command. Did you type everything right?";
//	public final static String ID_NOT_NUMBER = ChatColor.RED + "IDs must be a number.";
//	public final static String INVALID_BLOCK = ChatColor.RED + "You must be looking a a block.";
//	public final static String INVALID_CAUSE = ChatColor.RED + "Could not parse damage type.";
//	public final static String INVALID_ENTITY = ChatColor.RED + "Can not parse entity type. Using default entity from conifg.";
//	public final static String INVALID_ITEM = ChatColor.RED + "That is not an item or is in the wrong format for item types. Use <Item ID>:<Item Damage> [amount]";
//	public final static String INVALID_VALUES = ChatColor.RED + "You have entered invalid values for this command. It may be too big/small or be negative.";
//	public final static String LESS_ARGS = ChatColor.RED + "Not enough arguments.";
//	public final static String MORE_ARGS = ChatColor.RED + "Too many arguments.";
//	public final static String MUST_BE_BOOLEAN = ChatColor.RED + "You must enter a boolean value (true or false).";
//	public final static String NEEDS_SELECTION = ChatColor.RED + "To use this command without defining an ID, you must have something selected.";
//	public final static String NO_CONSOLE = "This command can only be used in-game";
//	public final static String NO_ID = ChatColor.RED + "Object with that name or ID does not exist.";
//	public final static String NO_PERMISSION = ChatColor.RED + "You do not have permission!";
//	public final static String NOT_ALLOWED_ENTITY = ChatColor.RED + "This is not an allowed entity.";
//	public final static String NOT_COMMAND = ChatColor.RED + "That is not a command for CustomSpawners.";
//	public final static String SPECIFY_NUMBER = ChatColor.RED + "You must specify a number for this command.";
//	
//	//Alias, Main Command
//	public Map<String, String> aliases = new HashMap<String, String>();
//	public FileConfiguration config = null;
//	public Logger log = null;
//	
//	public CustomSpawners plugin = null;
//	
//	public SpawnerCommand(CustomSpawners plugin) {
//		this.plugin = plugin;
//		this.log = plugin.log;
//		this.config = plugin.getCustomConfig();
//	}
//	
//	public void addAlias(String alias, String mainCommand) {
//		this.aliases.put(alias, mainCommand);
//	}
//
//	public Map<String, String> getAliases() {
//		return aliases;
//	}
//
//	public String getAssignedCommand(String[] args) {
//		return (this.aliases.containsKey(args[0])) ? this.aliases.get(args[0]) : args[0];	
//	}
//	
//	public SpawnableEntity getSelectedEntity(CommandSender sender) {
//		if(sender instanceof Player) {
//			Player p = (Player) sender;
//			
//			if(!CustomSpawners.entitySelection.containsKey(p)) {
//				return null;
//			}
//			
//			return CustomSpawners.getEntity(CustomSpawners.entitySelection.get(p).toString());
//		} else {
//			
//			if(CustomSpawners.consoleEntity == -1) {
//				return null;
//			}
//			
//			return CustomSpawners.getEntity(String.valueOf(CustomSpawners.consoleEntity));
//		}
//	}
//	
//	public Spawner getSelectedSpawner(CommandSender sender) {
//		if(sender instanceof Player) {
//			Player p = (Player) sender;
//			
//			if(!CustomSpawners.spawnerSelection.containsKey(p)) {
//				return null;
//			}
//			
//			return CustomSpawners.getSpawner(CustomSpawners.spawnerSelection.get(p).toString());
//		} else {
//			
//			if(CustomSpawners.consoleSpawner == -1) {
//				return null;
//			}
//			
//			return CustomSpawners.getSpawner(String.valueOf(CustomSpawners.consoleSpawner));
//		}
//	}
//	
//	public abstract void run(CommandSender arg0, Command arg1, String arg2, String[] arg3);
//	
//	public void setAliases(Map<String, String> aliases) {
//		this.aliases = aliases;
//	}
	
}
