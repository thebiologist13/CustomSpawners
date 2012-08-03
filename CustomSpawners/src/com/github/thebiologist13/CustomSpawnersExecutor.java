package com.github.thebiologist13;

import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.thebiologist13.commands.*;

public class CustomSpawnersExecutor implements CommandExecutor {

	//private CustomSpawners plugin = null;
	
	private Logger log = null;
	
	private HelpCommand hc = null;
	private ReloadDataCommand rc = null;
	
	public CustomSpawnersExecutor(CustomSpawners plugin) {
		//this.plugin = plugin;
		this.log = plugin.log;
		
		hc = new HelpCommand(plugin);
		rc = new ReloadDataCommand(plugin);
	}

	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		
		Player p = null;
		
		if(arg0 instanceof Player) {
			p = (Player) arg0;
		}
		
		if(arg1.getName().equalsIgnoreCase("customspawners")) {
			
			try {
				if(arg3.length == 0) {
					
					hc.run(arg0, arg1, arg2, arg3);
					return true;
					
				} else if(arg3.length == 1) {
					
					if(arg3[0].equalsIgnoreCase("help")) {
						hc.run(arg0, arg1, arg2, arg3);
						return true;
					} else if(arg3[0].equalsIgnoreCase("reload")) {
						rc.run(arg0, arg1, arg2, arg3);
						return true;
					}
					
				} else if(arg3.length == 2) {
					
					if(arg3[0].equalsIgnoreCase("help")) {
						hc.run(arg0, arg1, arg2, arg3);
						return true;
					}
					
				} else {
					if(arg0 instanceof Player) {
						p = (Player) arg0;
						p.sendMessage(SpawnerCommand.GENERAL_ERROR);
					} else {
						log.info("An error has occured with this command. Did you type everything right?");
					}
					return true;
				}
				
				return false;
			} catch(Exception e) {
				if(arg0 instanceof Player) {
					p = (Player) arg0;
					p.sendMessage(SpawnerCommand.GENERAL_ERROR);
					e.printStackTrace();
				} else {
					log.info("An error has occured with this command. Did you type everything right?");
				}
			}
			
			return true;
		}
		return false;
	}

}
