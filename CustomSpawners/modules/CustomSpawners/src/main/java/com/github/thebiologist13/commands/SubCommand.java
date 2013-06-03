package com.github.thebiologist13.commands;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.api.IObject;

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
	public final String WARN_LAG = ChatColor.GOLD + "WARNING! Running this command could cause a lot of lag or crashes " +
			"very fast if used improperly. Enter the command again to confirm.";
	
	public final FileConfiguration CONFIG;
	public final Logger LOG;
	public final CustomSpawners PLUGIN;
	
	public String permission;
	
	private Map<String, String> aliases = new HashMap<String, String>();
	private boolean needsObject;
	
	public SubCommand(CustomSpawners plugin, String mainPerm) {
		this.PLUGIN = plugin;
		this.CONFIG = plugin.getCustomConfig();
		this.LOG = plugin.log;
		this.permission = mainPerm;
		this.needsObject = true;
	}

	public void addAlias(String alias, String mainCommand) {
		this.aliases.put(alias.toLowerCase(), mainCommand.toLowerCase());
	}
	
	public void crash(Exception e, CommandSender arg0) {
		PLUGIN.printDebugTrace(e);
		PLUGIN.sendMessage(arg0, ChatColor.RED + "An error has occurred. Crash report saved to " + 
				PLUGIN.getFileManager().saveCrash(this.getClass(), e));
		PLUGIN.sendMessage(arg0, GENERAL_ERROR);
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
	
	public double handleDynamic(String in, double current) throws IllegalArgumentException {
		in = replace(in, current);
		return CustomSpawners.evaluate(in);
	}
	
	public float handleDynamic(String in, float current) throws IllegalArgumentException {
		in = replace(in, current);
		return (float) CustomSpawners.evaluate(in);
	}
	
	public int handleDynamic(String in, int current) throws IllegalArgumentException {
		in = replace(in, current);
		return (int) CustomSpawners.evaluate(in);
	}
	
	public boolean needsObject() {
		return needsObject;
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
	
	public boolean permissibleForObject(CommandSender sender, String perm, IObject object) {

		if(object == null)
			return permissible(sender, null);
		
		if(perm == null)
			perm = this.permission;
		
		if(perm.isEmpty())
			perm = this.permission;
		
		if(permissible(sender, null))
			return true;
		
		perm += "." + object.getId();
		
		return permissible(sender, perm);
	}

	public void setAliases(Map<String, String> aliases) {
		this.aliases = aliases;
	}
	
	public void setNeedsObject(boolean needsObject) {
		this.needsObject = needsObject;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}
	
	//TODO WIKI: Lag warning.
	//TODO WIKI: New Config Items
	public boolean warnLag(CommandSender sender) {
		
		if(CONFIG.getBoolean("players.warnLag", true)) {
			if(sender instanceof Player) {
				Player p = (Player) sender;
				if(CustomSpawners.warn.containsKey(p))
					return false;
				if(p.hasPermission("customspawners.spawners.limitoverride"))
					return false;
				CustomSpawners.warn.put((Player) sender, CONFIG.getInt("players.timeout", 5));
			}
			PLUGIN.sendMessage(sender, WARN_LAG);
			return true;
		}
		return false;
	}
	
	private String replace(String in, double current) {
		in = in.replaceAll("@current", "" + current);
		in = in.replaceAll("@cur", "" + current);
		in = in.replaceAll("@c", "" + current);
		return in;
	}
	
}
