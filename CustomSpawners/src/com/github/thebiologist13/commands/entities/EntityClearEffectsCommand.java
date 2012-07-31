package com.github.thebiologist13.commands.entities;

import java.util.ArrayList;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.EntityPotionEffect;
import com.github.thebiologist13.SpawnableEntity;
import com.github.thebiologist13.commands.SpawnerCommand;

public class EntityClearEffectsCommand extends SpawnerCommand {

	private CustomSpawners plugin = null;
	
	private Logger log = null;
	
	public EntityClearEffectsCommand(CustomSpawners plugin) {
		this.plugin = plugin;
		this.log = plugin.log;
	}
	
	@Override
	public void run(CommandSender arg0, Command arg1, String arg2, String[] arg3) {

		//Command Syntax = /customspawners cleareffects [id]
		//Array Index with selection            0   
		//Without selection                     0         1
		
		//Player
		Player p = null;
		//Entity
		SpawnableEntity s = null;
		//Permissions
		String perm = "customspawners.entities.cleareffects";

		if(!(arg0 instanceof Player)) {
			log.info(NO_CONSOLE);
			return;
		}

		p = (Player) arg0;

		if(p.hasPermission(perm)) {
			if(CustomSpawners.entitySelection.containsKey(p) && arg3.length == 1) {

				s = plugin.getEntityById(CustomSpawners.entitySelection.get(p));
				
			} else if(arg3.length == 1) {
				p.sendMessage(NEEDS_SELECTION);
				return;
			} else if(arg3.length == 2) {

				int id = 0;

				//Check that the ID entered is a number
				if(!plugin.isInteger(arg3[1])) {
					p.sendMessage(ID_NOT_NUMBER);
					return;
				}

				id = Integer.parseInt(arg3[1]);

				//Check if the ID entered is the ID of a entity
				if(!plugin.isValidEntity(id)) {
					p.sendMessage(NO_ID);
					return;
				}

				s = plugin.getEntityById(id);
				
			} else {
				p.sendMessage(GENERAL_ERROR);
				return;
			}

			//Carry out command
			s.setEffects(new ArrayList<EntityPotionEffect>());

			//Success
			p.sendMessage(ChatColor.GREEN + "Successfully cleared the potion effects on spawnable entity with ID " 
					+ ChatColor.GOLD + s.getId() + ChatColor.GREEN + "!");
		} else {
			p.sendMessage(NO_PERMISSION);
			return;
		}
		
	}

}
