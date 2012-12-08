package com.github.thebiologist13.commands.entities;

import java.util.ArrayList;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.SpawnableEntity;
import com.github.thebiologist13.commands.SpawnerCommand;
import com.github.thebiologist13.serialization.SPotionEffect;

public class EntityClearEffectsCommand extends SpawnerCommand {

	public EntityClearEffectsCommand(CustomSpawners plugin) {
		super(plugin);
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

				s = CustomSpawners.getEntity(CustomSpawners.entitySelection.get(p).toString());
				
			} else if(arg3.length == 1) {
				p.sendMessage(NEEDS_SELECTION);
				return;
			} else if(arg3.length == 2) {

				s = CustomSpawners.getEntity(arg3[1]);

				if(s == null) {
					p.sendMessage(NO_ID);
					return;
				}
				
			} else {
				p.sendMessage(GENERAL_ERROR);
				return;
			}

			//Carry out command
			s.setEffects(new ArrayList<SPotionEffect>());

			//Success
			p.sendMessage(ChatColor.GREEN + "Successfully cleared the potion effects on spawnable entity with ID " 
					+ ChatColor.GOLD + plugin.getFriendlyName(s) + ChatColor.GREEN + "!");
		} else {
			p.sendMessage(NO_PERMISSION);
			return;
		}
		
	}

}
