package com.github.thebiologist13.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.thebiologist13.CustomSpawners;

public class ReloadCommand extends SpawnerSubCommand {
	
	private CustomSpawners plugin;
	
	public ReloadCommand(CustomSpawners plugin) {
		this.plugin = plugin;
	}
	public void run(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		//Player
		Player p = null;
		
		//Check if player or console
		if(arg0 instanceof Player) {
			p = (Player) arg0;
		}
		
		if(p == null) {
			plugin.reloadSpawners();
		} else {
			if(p.hasPermission("customspawners.reloadspawners")) {
				p.sendMessage(ChatColor.GREEN + "Reloading spawners... ");
				plugin.reloadSpawners();
				p.sendMessage(ChatColor.GREEN + "Reload Complete!");
			}
		}
	}
}
