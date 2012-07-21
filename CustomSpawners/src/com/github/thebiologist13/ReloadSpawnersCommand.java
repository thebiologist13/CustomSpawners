package com.github.thebiologist13;

import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.thebiologist13.commands.SpawnerCommand;

public class ReloadSpawnersCommand extends SpawnerCommand implements CommandExecutor {

	private CustomSpawners plugin = null;
	
	private Logger log = null;
	
	final String RELOAD_PERM = "customspawners.reloadspawners";
	
	public ReloadSpawnersCommand(CustomSpawners plugin) {
		this.plugin = plugin;
		this.log = plugin.log;
	}

	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		
		Player p = null;
		if(arg0 instanceof Player) {
			p = (Player) arg0;
		}
		
		if(p == null) {
			if(arg3.length == 1) {
				reloadSpawners();
				return true;
			} else {
				log.info(MORE_ARGS);
			}
		} else {
			if(p.hasPermission(RELOAD_PERM)) {
				if(arg3.length == 1) {
					reloadSpawners();
					return true;
				} else {
					log.info(MORE_ARGS);
				}
			}
		}
		return false;
	}

	private void reloadSpawners() {
		plugin.saveEntities();
		plugin.saveSpawners();
		plugin.loadEntities();
		plugin.loadSpawners();
	}
}
