package com.github.thebiologist13.commands.spawners;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.Spawner;
import com.github.thebiologist13.commands.SpawnerCommand;

public class ListNearCommand extends SpawnerCommand {

	public ListNearCommand(CustomSpawners plugin) {
		super(plugin);
	}

	@Override
	public void run(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		Player p = null;

		if(!(arg0 instanceof Player)) {
			plugin.log.info(NO_CONSOLE);
			return;
		}

		p = (Player) arg0;

		if(p.hasPermission("customspawners.spawners.listnear")) {
			ArrayList<Spawner> nearbySpawners = new ArrayList<Spawner>();
			for(Spawner s : CustomSpawners.spawners.values()) {
				if(!p.hasPermission("customspawners.spawners.listnear.hidden")) {
					if(s.getLoc().distance(p.getLocation()) < 25 && !s.isHidden()) { //TODO add config option for this nearby distance (the 25)
						 nearbySpawners.add(s);
					}
				} else {
					if(s.getLoc().distance(p.getLocation()) < 25) { //TODO add config option for this nearby distance (the 25)
						 nearbySpawners.add(s);
					}
				}
			}

			if(nearbySpawners.size() == 0) {
				p.sendMessage(ChatColor.GREEN + "There are no spawners within " + String.valueOf(25) + " blocks."); //TODO here too.
				return;
			} else {
				p.sendMessage(ChatColor.GOLD + "Nearby Spawners: ");
				for(Spawner s : nearbySpawners) {
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
