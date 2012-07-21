package com.github.thebiologist13;

import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.thebiologist13.commands.HelpCommand;
import com.github.thebiologist13.commands.SpawnerCommand;

public class CustomSpawnersExecutor implements CommandExecutor {

	private CustomSpawners plugin = null;
	
	private Logger log = null;
	
	private HelpCommand hc = null;
	private ReloadSpawnersCommand rc = null;
	
	public CustomSpawnersExecutor(CustomSpawners plugin) {
		this.plugin = plugin;
		this.log = plugin.log;
		
		hc = new HelpCommand(plugin);
		rc = new ReloadSpawnersCommand(plugin);
		
		SpawnerCommand.registerCommand("help", hc);
		SpawnerCommand.registerCommand("reloadspawners", rc);
	}

	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		
		Player p = null;
		
		if(arg0 instanceof Player) {
			p = (Player) arg0;
		}
		
		if(arg1.getName().equalsIgnoreCase("customspawners")) {
			
			switch(arg3.length) {
			case 0:
				return SpawnerCommand.runCommand("help", arg0, arg1, arg2, arg3);
			case 1:
				if(arg3[0].equalsIgnoreCase("help")) {
					return SpawnerCommand.runCommand("help", arg0, arg1, arg2, arg3);
				} else if(arg3[0].equalsIgnoreCase("reloadspawners")) {
					return SpawnerCommand.runCommand("reloadspawners", arg0, arg1, arg2, arg3);
				}
			case 2:
				return SpawnerCommand.runCommand("help", arg0, arg1, arg2, arg3);
			default:
				if(p == null) {
					
				}
			}
			
			return false;
		}
		return false;
	}

}
