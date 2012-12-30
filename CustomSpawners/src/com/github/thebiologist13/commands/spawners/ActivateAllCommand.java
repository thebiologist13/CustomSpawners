package com.github.thebiologist13.commands.spawners;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.Spawner;
import com.github.thebiologist13.commands.SubCommand;

public class ActivateAllCommand extends SubCommand {

	public ActivateAllCommand(CustomSpawners plugin) {
		super(plugin);
	}

	public void run(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		//Player 
		Player p = null;
		
		//Check if player
		if(arg0 instanceof Player) {
			p = (Player) arg0;
		}
		
		if(p == null) {
			activateSpawners();
			
			plugin.log.info("All spawners set active.");
		} else {
			if(p.hasPermission("customspawners.spawners.activateall")) {
				activateSpawners();
				
				p.sendMessage(ChatColor.GREEN + "All spawners set active.");
			} else {
				p.sendMessage(SubCommand.NO_PERMISSION);
				return;
			}
		}
	}
	
	private void activateSpawners() {
		for(Spawner s : CustomSpawners.spawners.values()) {
			s.setActive(true);
		}
	}
}
