package com.github.thebiologist13;

import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.github.thebiologist13.commands.*;

public class CustomSpawnersExecutor implements CommandExecutor {

	private ConcurrentHashMap<String, SpawnerCommand> commands = new ConcurrentHashMap<String, SpawnerCommand>();
	
	private CustomSpawners plugin;
	
	public CustomSpawnersExecutor(CustomSpawners plugin) {
		this.plugin = plugin;
		
		HelpCommand hc = new HelpCommand(plugin);
		ReloadDataCommand rc = new ReloadDataCommand(plugin);
		
		commands.put("help", hc);
		commands.put("reload", rc);
	}

	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		
		if(arg1.getName().equalsIgnoreCase("customspawners")) {
			
			try {
				
				if(arg3.length == 0) {
					commands.get("help").run(arg0, arg1, arg2, arg3);
					return true;
				}
				
				SpawnerCommand runThis = commands.get(arg3[0].toLowerCase());
				
				if(runThis == null) {
					plugin.sendMessage(arg0, ChatColor.RED + "\"" + arg3[0].toLowerCase() + "\" is not a valid command for CustomSpawners.");
				} else {
					runThis.run(arg0, arg1, arg2, arg3);
				}

			} catch(ArrayIndexOutOfBoundsException e) {
				plugin.sendMessage(arg0, ChatColor.RED + "You entered an invalid number or arguments. Make sure you entered all the right parameters.");
			} catch(Exception e) {
				
				e.printStackTrace();
				plugin.sendMessage(arg0, SpawnerCommand.GENERAL_ERROR);
				
			}
			
			return true;
			
		}
		
		return false;
		
	}

}
