package com.github.thebiologist13.commands.entities;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.SpawnableEntity;
import com.github.thebiologist13.commands.SubCommand;

/**
 * Superclass for /entities commands.
 * 
 * @author thebiologist13
 */
public abstract class EntityCommand extends SubCommand {
	
	public final String NO_ENTITY = ChatColor.RED + "You must have a entity selected, or define an entity.";
	
	private boolean needsObject = true;

	public EntityCommand(CustomSpawners plugin) {
		super(plugin, "");
	}
	
	public EntityCommand(CustomSpawners plugin, String mainPerm) {
		super(plugin, mainPerm);
	}
	
	public  String getSuccessMessage(SpawnableEntity entity, String value) {
		return ChatColor.GREEN + "Successfully set " + ChatColor.GOLD + value + ChatColor.GREEN + 
				" to entity " + ChatColor.GOLD + PLUGIN.getFriendlyName(entity) + ChatColor.GREEN + "!";
	}
	
	public String getSuccessMessage(SpawnableEntity entity, String prop, String value) {
		return ChatColor.GREEN + "Successfully set property \"" + ChatColor.GOLD + prop + ChatColor.GREEN + 
				"\" to " + ChatColor.GOLD + value + ChatColor.GREEN + " on entity " + ChatColor.GOLD + 
				PLUGIN.getFriendlyName(entity) + ChatColor.GREEN + "!";
	}
	
	public boolean needsObject() {
		return needsObject;
	}
	
	public boolean permissibleForObject(CommandSender sender, String perm, SpawnableEntity object) {
		
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
	
	public abstract void run(SpawnableEntity entity, CommandSender sender, String subCommand, String[] args);

	public void setNeedsObject(boolean needsObject) {
		this.needsObject = needsObject;
	}

}
