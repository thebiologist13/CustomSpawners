package com.github.thebiologist13.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.thebiologist13.CustomSpawners;

public class ReloadDataCommand extends SpawnerCommand {

	public ReloadDataCommand(CustomSpawners plugin) {
		super(plugin);
	}

	@Override
	public void run(CommandSender arg0, Command arg1, String arg2, String[] arg3) {

		Player p = null;
		
		final String RELOAD_PERM = "customspawners.reload";
		
		if(arg0 instanceof Player) {
			p = (Player) arg0;
		}
		
		if(p == null) {
			if(arg3.length == 1) {
				try {
					plugin.getFileManager().reloadData();
				} catch (Exception e) {
					e.printStackTrace();
				}
				log.info("Spawners and Entities reloaded.");
			} else {
				log.info(MORE_ARGS);
			}
		} else {
			if(p.hasPermission(RELOAD_PERM)) {
				if(arg3.length == 1) {
					try {
						plugin.getFileManager().reloadData();
					} catch (Exception e) {
						e.printStackTrace();
					}
					p.sendMessage(ChatColor.GREEN + "Spawners and Entities reloaded.");
				} else {
					log.info(MORE_ARGS);
				}
			} else {
				p.sendMessage(NO_PERMISSION);
			}
		}
	}
}
