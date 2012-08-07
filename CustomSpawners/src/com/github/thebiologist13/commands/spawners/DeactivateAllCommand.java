package com.github.thebiologist13.commands.spawners;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.Spawner;
import com.github.thebiologist13.commands.SpawnerCommand;

public class DeactivateAllCommand extends SpawnerCommand {

	private CustomSpawners plugin;
	
	public DeactivateAllCommand(CustomSpawners plugin) {
		this.plugin = plugin;
	}
	
	public void run(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		//Player
		Player p = null;
		
		//Check if player
		if(arg0 instanceof Player) {
			p = (Player) arg0;
		}
		
		if(p == null) {
			deactivateSpawners();
			
			plugin.log.info("All spawners set inactive.");
		} else {
			if(p.hasPermission("customspawners.spawners.deactivateall")) {
				deactivateSpawners();
				
				p.sendMessage(ChatColor.GREEN + "All spawners set inactive.");
			}  else {
				p.sendMessage(NO_PERMISSION);
				return;
			}
		}
	}
	
	private void deactivateSpawners() {
		for(Spawner s : CustomSpawners.spawners.values()) {
			s.setActive(false);
		}
	}
}
