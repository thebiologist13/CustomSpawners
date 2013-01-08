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
		
		if(perm == null)
			perm = this.permission;
		
		if(perm.isEmpty())
			perm = this.permission;
		
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
	
}
