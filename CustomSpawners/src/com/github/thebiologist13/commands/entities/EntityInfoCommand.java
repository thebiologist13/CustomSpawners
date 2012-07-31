package com.github.thebiologist13.commands.entities;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.EntityPotionEffect;
import com.github.thebiologist13.SpawnableEntity;
import com.github.thebiologist13.commands.SpawnerCommand;

public class EntityInfoCommand extends SpawnerCommand {

	private CustomSpawners plugin = null;
	
	public EntityInfoCommand(CustomSpawners plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public void run(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		//Player
		Player p = null;

		//Spawner
		SpawnableEntity s = null;

		//Make sure a player issued command
		if(!(arg0 instanceof Player)) {
			plugin.log.info(NO_CONSOLE);
			return;
		} 

		p = (Player) arg0;

		//Permission check
		if(p.hasPermission("customspawners.entities.info")) {

			//If the player wants to perform command with a selection.
			if(CustomSpawners.entitySelection.containsKey(p) && arg3.length == 1) {

				s = plugin.getEntityById(CustomSpawners.entitySelection.get(p));

			//Arguments are for selection, but none is selected
			} else if(arg3.length == 1) {

				p.sendMessage(NEEDS_SELECTION);
				return;

			//If the player wants to perform command on a specific entity
			} else if(arg3.length == 2) {

				if(!plugin.isInteger(arg3[1])) {
					p.sendMessage(ID_NOT_NUMBER);
					return;
				}

				if(!plugin.isValidEntity(Integer.parseInt(arg3[1]))) {
					p.sendMessage(NO_ID);
					return;
				}

				s = plugin.getEntityById(Integer.parseInt(arg3[1]));

			//General error
			} else {

				p.sendMessage(GENERAL_ERROR);
				return;

			}

			String effectMessage = ""; 
			ArrayList<EntityPotionEffect> effects = s.getEffects();
			for(int i = 0; i < effects.size(); i++) {
				if(i == 0) {
					effectMessage += effects.get(i).getType().getName() + " " + effects.get(i).getAmplifier();
				} else {
					effectMessage += ", " + effects.get(i).getType().getName() + " " + effects.get(i).getAmplifier();
				}
			}
			
			if(effectMessage.isEmpty()) {
				effectMessage = "No effects.";
			}
			
			String header = ChatColor.GREEN + "Information on entity with ID " + ChatColor.GOLD + String.valueOf(s.getId());
			
			if(!s.getName().isEmpty()) {
				header += " (" + s.getName() + ")";
			}
			
			//Send info
			String[] message = {
					"",
					header + ChatColor.GREEN + ": ",
					"",
					ChatColor.GOLD + "Type: " + s.getType().getName(),
					ChatColor.GOLD + "Effects: " + effectMessage,
					ChatColor.GOLD + "X Velocity: " + s.getXVelocity(),
					ChatColor.GOLD + "Y Velocity: " + s.getYVelocity(),
					ChatColor.GOLD + "Z Velocity: " + s.getZVelocity(),
					ChatColor.GOLD + "Age: " + s.getAge(),
					ChatColor.GOLD + "Health: " + s.getHealth(),
					ChatColor.GOLD + "Air: " + s.getAir(),
					ChatColor.GOLD + "Profession: " + s.getProfession().toString(),
					ChatColor.GOLD + "Enderman Block ID: " + s.getEndermanBlock().getItemTypeId(),
					ChatColor.GOLD + "Saddled: " + s.isSaddled(),
					ChatColor.GOLD + "Charged: " + s.isCharged(),
					ChatColor.GOLD + "Jockey: " + s.isJockey(),
					ChatColor.GOLD + "Tamed: " + s.isTamed(),
					ChatColor.GOLD + "Sitting: " + s.isSitting(),
					ChatColor.GOLD + "Angry: " + s.isAngry(),
					ChatColor.GOLD + "Cat Type: " + s.getCatType(),
					ChatColor.GOLD + "Slime Size: " + s.getSlimeSize(),
					ChatColor.GOLD + "Color: " + s.getColor()
			};

			p.sendMessage(message);
			
		} else {
			p.sendMessage(NO_PERMISSION);
			return;
		}
	}

}
