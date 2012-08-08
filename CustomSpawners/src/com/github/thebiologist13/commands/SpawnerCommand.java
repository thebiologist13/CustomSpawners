package com.github.thebiologist13.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public abstract class SpawnerCommand {
	
	/*
	 * Note:
	 * 
	 * Sub-commands implementing this interface do not need
	 * to check for the length of the argument (at 
	 * least for command validity, they do if it can
	 * be used in multiple ways). They only need to
	 * check if valid values were put in, check for
	 * thrown exceptions, check permissions, and
	 * carry out the command.
	 */
	
	//Strings for errors
	public final static String NO_CONSOLE = "This command can only be used in-game";
	public final static String NO_ID = ChatColor.RED + "Object with that name or ID does not exist.";
	public final static String ID_NOT_NUMBER = ChatColor.RED + "IDs must be a number.";
	public final static String LESS_ARGS = ChatColor.RED + "Not enough arguments.";
	public final static String MORE_ARGS = ChatColor.RED + "Too many arguments.";
	public final static String INVALID_ENTITY = ChatColor.RED + "Can not parse entity type. Using default entity from conifg.";
	public final static String INVALID_CAUSE = ChatColor.RED + "Could not parse damage type.";
	public final static String ENTITY_NONEXISTANT = ChatColor.RED + "There is no spawnable entity with this name or ID.";
	public final static String NOT_ALLOWED_ENTITY = ChatColor.RED + "This is not an allowed entity.";
	public final static String INVALID_BLOCK = ChatColor.RED + "You must be looking a a block.";
	public final static String NEEDS_SELECTION = ChatColor.RED + "To use this command without defining an ID, you must have something selected.";
	public final static String MUST_BE_BOOLEAN = ChatColor.RED + "You must enter a boolean value (true or false).";
	public final static String INVALID_VALUES = ChatColor.RED + "You have entered invalid values for this command. It may be too big/small or be negative.";
	public final static String SPECIFY_NUMBER = ChatColor.RED + "You must specify a number for this command.";
	public final static String GENERAL_ERROR = ChatColor.RED + "An error has occured with this command. Did you type everything right?";
	public final static String NO_PERMISSION = ChatColor.RED + "You do not have permission!";
	public final static String NOT_COMMAND = ChatColor.RED + "That is not a command for CustomSpawners.";
	
	/*public static HashMap<String, CommandExecutor> commands = new HashMap<String, CommandExecutor>();
	
	public static void registerCommand(String name, CommandExecutor executor) {
		commands.put(name.toLowerCase(), executor);
	}
	
	public static void unregisterCommand(String name) {
		commands.remove(name.toLowerCase());
	}
	
	public static CommandExecutor getExecutor(String name) {
		return commands.get(name.toLowerCase());
	}
	
	public static boolean hasCommand(String name) {
		return commands.containsKey(name.toLowerCase());
	}
	
	public static boolean runCommand(String name, CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		if(hasCommand(name)) {
			CommandExecutor executor = getExecutor(name);
			return executor.onCommand(arg0, arg1, arg2, arg3);
		} else {
			return false;
		}
	}*/
	
	public abstract void run(CommandSender arg0, Command arg1, String arg2, String[] arg3);
}
