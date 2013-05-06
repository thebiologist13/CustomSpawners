package com.github.thebiologist13.commands.spawners;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.SpawnableEntity;
import com.github.thebiologist13.Spawner;
import com.github.thebiologist13.commands.SubCommand;

public abstract class SpawnerCommand extends SubCommand {

	public final String NO_SPAWNER = ChatColor.RED + "You must have a spawner selected, or define a spawner.";
	public final String NO_OVERRIDE = ChatColor.RED + "Your entered values are not permissible.";
	
	private boolean needsObject = true;
	
	public SpawnerCommand(CustomSpawners plugin) {
		super(plugin, "");
	}
	
	public SpawnerCommand(CustomSpawners plugin, String mainPerm) {
		super(plugin, mainPerm);
	}
	
	public void checkRecursiveSpawns(SpawnableEntity type, CommandSender sender) {
		if(type.getSpawnerData() != null) {
			Spawner data = type.getSpawnerData();
			for(Integer i : data.getTypeData()) {
				SpawnableEntity s = CustomSpawners.getEntity(i.intValue());
				if(s == null)
					continue;
				if(s.getSpawnerData() != null) {
					PLUGIN.sendMessage(sender, ChatColor.GOLD + "Warning! This entity may recursively spawn mobs! " +
							"Make sure you know what you are doing or the server might crash from mobs spawning too fast!");
				}
			}
		}
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
	
	public boolean permissibleForObject(CommandSender sender, String perm, Spawner object) {

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
	
	public abstract void run(Spawner spawner, CommandSender sender, String subCommand, String[] args);

	public void setNeedsObject(boolean needsObject) {
		this.needsObject = needsObject;
	}

}
