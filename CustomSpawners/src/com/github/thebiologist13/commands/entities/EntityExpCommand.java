package com.github.thebiologist13.commands.entities;

import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.SpawnableEntity;
import com.github.thebiologist13.commands.SpawnerCommand;

public class EntityExpCommand extends SpawnerCommand {

	private CustomSpawners plugin = null;
	
	private Logger log = null;
	
	public EntityExpCommand(CustomSpawners plugin) {
		this.plugin = plugin;
		this.log = plugin.log;
	}
	
	@Override
	public void run(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		//Player
		Player p = null;
		//SpawnableEntity
		SpawnableEntity s = null;
		//Perm
		String perm = "customspawners.entities.setexp";
		//Exp
		int exp = 0;
		
		final String MUST_BE_INTEGER = ChatColor.RED + "The dropped experience must be an integer.";

		if(!(arg0 instanceof Player)) {
			log.info(NO_CONSOLE);
			return;
		}

		p = (Player) arg0;
		
		if(p.hasPermission(perm)) {
			if(CustomSpawners.entitySelection.containsKey(p) && arg3.length == 2) {

				s = plugin.getEntity(CustomSpawners.entitySelection.get(p).toString());
				
				if(!plugin.isInteger(arg3[1])) {
					p.sendMessage(MUST_BE_INTEGER);
					return;
				}
				
				exp = Integer.parseInt(arg3[1]);
				
			} else if(arg3.length == 2) {
				p.sendMessage(NEEDS_SELECTION);
				return;
			} else if(arg3.length == 3) {

				s = plugin.getEntity(arg3[1]);

				if(s == null) {
					p.sendMessage(NO_ID);
					return;
				}
				
				if(!plugin.isInteger(arg3[2])) {
					p.sendMessage(MUST_BE_INTEGER);
					return;
				}
				
				exp = Integer.parseInt(arg3[2]);
				
			} else {
				p.sendMessage(GENERAL_ERROR);
				return;
			}
			
			//Set
			s.setDroppedExp(exp);
			
			//Success
			p.sendMessage(ChatColor.GREEN + "Successfully set the dropped experience of spawnable entity with ID " 
					+ ChatColor.GOLD + plugin.getFriendlyName(s) + ChatColor.GREEN + " to " + ChatColor.GOLD 
					+ exp + ChatColor.GREEN + "!");
		}
		
	}

}
