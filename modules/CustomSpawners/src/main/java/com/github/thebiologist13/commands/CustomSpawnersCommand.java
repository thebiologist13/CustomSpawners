package com.github.thebiologist13.commands;

import org.bukkit.command.CommandSender;

import com.github.thebiologist13.CustomSpawners;

/**
 * Superclass for /customspawners commands.
 * 
 * @author thebiologist13
 */
public abstract class CustomSpawnersCommand extends SubCommand {

	public CustomSpawnersCommand(CustomSpawners plugin) {
		super(plugin, "");
	}
	
	public CustomSpawnersCommand(CustomSpawners plugin, String mainPerm) {
		super(plugin, mainPerm);
	}
	
	public abstract void run(CommandSender sender, String subCommand, String[] args);

}
