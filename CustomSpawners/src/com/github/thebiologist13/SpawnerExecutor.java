package com.github.thebiologist13;

import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

@SuppressWarnings("unused")
public class SpawnerExecutor implements CommandExecutor {

	/*
	 * This class executes the /customspawner command.
	 * You will notice very little code other than
	 * something like this:
	 * 
	 * SomeClass.run(arg0, arg1, arg2, arg3);
	 * 
	 * This is because each sub-command 
	 * (in the com.github.thebiologist13.commands package)
	 * has 100+ lines of code to execute. Therefore
	 * I put each sub-command's code in a 
	 * separate class.
	 */

	private CustomSpawners plugin;
	
	private Logger log;
	
	//Sub-Commands
	
	public SpawnerExecutor(CustomSpawners plugin) {
		this.plugin = plugin;
		this.log = plugin.log;
	}
	
	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		
		if(arg1.getName().equalsIgnoreCase("spawners")) {
			
			//How many arguments
			int argumentLength = arg3.length;
			
			//Catch any unhandled errors with commands.
			try {
				return true;
			} catch(Exception e) {
				e.printStackTrace();
				return true;
			}
		}
		return false;
	}
}
