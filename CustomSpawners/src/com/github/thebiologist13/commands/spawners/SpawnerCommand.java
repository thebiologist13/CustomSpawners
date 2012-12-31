package com.github.thebiologist13.commands.spawners;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.Spawner;
import com.github.thebiologist13.commands.SubCommand;

public abstract class SpawnerCommand extends SubCommand {

	public final String NO_SPAWNER = ChatColor.RED + "You must have a entity selected, or define an entity.";
	public final String NO_OVERRIDE = ChatColor.RED + "Your entered values are not permissible.";
	
	private boolean needsObject = true;
	
	public SpawnerCommand(CustomSpawners plugin) {
		super(plugin, "");
	}
	
	public SpawnerCommand(CustomSpawners plugin, String mainPerm) {
		super(plugin, mainPerm);
	}
	
	public  String getSuccessMessage(Spawner spawner, String value) {
		return ChatColor.GREEN + "Successfully set " + ChatColor.GOLD + value + ChatColor.GREEN + 
				" to spawner " + ChatColor.GOLD + PLUGIN.getFriendlyName(spawner) + ChatColor.GREEN + "!";
	}
	
	public String getSuccessMessage(Spawner spawner, String prop, String value) {
		return ChatColor.GREEN + "Successfully set property \"" + ChatColor.GOLD + prop + ChatColor.GREEN + 
				"\" to " + ChatColor.GOLD + value + ChatColor.GREEN + " on spawner " + ChatColor.GOLD + 
				PLUGIN.getFriendlyName(spawner) + ChatColor.GREEN + "!";
	}
	
	public boolean needsObject() {
		return needsObject;
	}
	
	public abstract void run(Spawner spawner, CommandSender sender, String subCommand, String[] args);

	public void setNeedsObject(boolean needsObject) {
		this.needsObject = needsObject;
	}

}
