package com.github.thebiologist13;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class EntitiesExecutor implements CommandExecutor {

	public EntitiesExecutor(CustomSpawners plugin) {
		
	}

	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		if(arg1.getName().equalsIgnoreCase("entities")) {
			return true;
		}
		return false;
	}

}
