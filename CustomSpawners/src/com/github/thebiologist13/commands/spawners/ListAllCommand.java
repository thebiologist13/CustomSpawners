package com.github.thebiologist13.commands.spawners;

import java.util.ArrayList;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.Spawner;
import com.github.thebiologist13.commands.SpawnerCommand;

public class ListAllCommand extends SpawnerCommand {

	public ListAllCommand(CustomSpawners plugin) {
		super(plugin);
	}

	@Override
	public void run(CommandSender arg0, Command arg1, String arg2, String[] arg3) {

		final String NO_SPAWNERS = "No spawners have been created yet.";
		
		Player p = null;
		
		if(arg0 instanceof Player) {
			p = (Player) arg0;
		}
		
		if(p == null) {
			if(CustomSpawners.spawners.size() == 0) {
				log.info(NO_SPAWNERS);
			} else {
				log.info("Spawners: ");
				for(Spawner s : CustomSpawners.spawners.values()) {
					if(!s.getName().isEmpty()) {
						log.info(String.valueOf(s.getId()) + " with name " + s.getName() + " at location ("  
								+ s.getLoc().getBlockX() + ", " + s.getLoc().getBlockY() + ", " + s.getLoc().getBlockZ() + ")");
					} else {
						log.info(String.valueOf(s.getId()) + " at location (" + s.getLoc().getBlockX() + ", " 
								+ s.getLoc().getBlockY() + ", " + s.getLoc().getBlockZ() + ")");
					}
				}
			}
		} else {
			if(p.hasPermission("customspawners.spawners.listall")) {

				ArrayList<Spawner> unhiddenSpawners = new ArrayList<Spawner>();
				for(Spawner s : CustomSpawners.spawners.values()) {
					if(!s.isHidden()) {
						unhiddenSpawners.add(s);
					}
				}

				if(p.hasPermission("customspawners.spawners.listall.hidden")) {
					if(CustomSpawners.spawners.size() == 0) {
						p.sendMessage(ChatColor.RED + NO_SPAWNERS);
					} else {
						p.sendMessage(ChatColor.GOLD + "Spawners: ");
						for(Spawner s : CustomSpawners.spawners.values()) {
							if(!s.getName().isEmpty()) {
								p.sendMessage(ChatColor.GOLD + String.valueOf(s.getId()) + ChatColor.GREEN + " with name " + ChatColor.GOLD + s.getName() + ChatColor.GREEN +
										" at location (" + s.getLoc().getBlockX() + ", " + s.getLoc().getBlockY() + ", " + s.getLoc().getBlockZ() + ")");
							} else {
								p.sendMessage(ChatColor.GOLD + String.valueOf(s.getId()) + ChatColor.GREEN + " at location (" + s.getLoc().getBlockX() + ", " 
										+ s.getLoc().getBlockY() + ", " + s.getLoc().getBlockZ() + ")");
							}
						}
					}
				} else {
					if(unhiddenSpawners.size() == 0) {
						p.sendMessage(ChatColor.RED + NO_SPAWNERS);
					} else {
						p.sendMessage(ChatColor.GOLD + "Spawners: ");
						for(Spawner s : unhiddenSpawners) {
							if(!s.getName().isEmpty()) {
								p.sendMessage(ChatColor.GOLD + String.valueOf(s.getId()) + ChatColor.GREEN + " with name " + ChatColor.GOLD + s.getName() + ChatColor.GREEN +
										" at location (" + s.getLoc().getBlockX() + ", " + s.getLoc().getBlockY() + ", " + s.getLoc().getBlockZ() + ")");
							} else {
								p.sendMessage(ChatColor.GOLD + String.valueOf(s.getId()) + ChatColor.GREEN + " at location (" + s.getLoc().getBlockX() + ", " 
										+ s.getLoc().getBlockY() + ", " + s.getLoc().getBlockZ() + ")");
							}
						}
					}
				}
			}
		}
	}
}
