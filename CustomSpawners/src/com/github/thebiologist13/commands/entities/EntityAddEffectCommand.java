package com.github.thebiologist13.commands.entities;

import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.EntityPotionEffect;
import com.github.thebiologist13.SpawnableEntity;
import com.github.thebiologist13.commands.SpawnerCommand;

public class EntityAddEffectCommand extends SpawnerCommand {

	private CustomSpawners plugin = null;
	
	private Logger log = null;
	
	public EntityAddEffectCommand(CustomSpawners plugin) {
		this.plugin = plugin;
		this.log = plugin.log;
	}
	
	@Override
	public void run(CommandSender arg0, Command arg1, String arg2, String[] arg3) {

		//Command Syntax = /customspawners addeffect [id] <type> <level> <duration in seconds>
		//Array Index with selection           0             1      2              3
		//Without selection                    0       1     2      3              4
		
		//Player
		Player p = null;
		//Entity
		SpawnableEntity s = null;
		//Effect
		EntityPotionEffect effect = null;
		//PotionEffectType
		PotionEffectType effectType = null;
		//Amplifier
		int amplifier = 0;
		//Duration
		int duration = 0;
		//Permissions
		String perm = "customspawners.entities.addeffect";

		if(!(arg0 instanceof Player)) {
			log.info(NO_CONSOLE);
			return;
		}

		p = (Player) arg0;

		if(p.hasPermission(perm)) {
			if(CustomSpawners.entitySelection.containsKey(p) && arg3.length == 4) {

				s = plugin.getEntityById(CustomSpawners.entitySelection.get(p));
				
				effectType = PotionEffectType.getByName(arg3[1]);
				
				if(effectType == null) {
					p.sendMessage(ChatColor.RED + arg3[1] + " is not a valid potion effect.");
					return;
				}
				
				if(!plugin.isInteger(arg3[2])) {
					p.sendMessage(ChatColor.RED + "The potion effect level must be an integer.");
					return;
				}
				
				amplifier = Integer.parseInt(arg3[2]);
				
				if(amplifier < 1) {
					p.sendMessage(ChatColor.RED + "The potion effect level must be greater than or equal to one.");
					return;
				}
				
				if(!plugin.isInteger(arg3[3])) {
					p.sendMessage(ChatColor.RED + "The potion effect duration must be an integer.");
					return;
				}
				
				duration = Integer.parseInt(arg3[3]);
				
				if(duration < 0) {
					p.sendMessage(ChatColor.RED + "The potion effect duration must be greater than 0.");
					return;
				}

				effect = new EntityPotionEffect(effectType, duration, amplifier);
				
			} else if(arg3.length == 4) {
				p.sendMessage(NEEDS_SELECTION);
				return;
			} else if(arg3.length == 5) {

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

				effectType = PotionEffectType.getByName(arg3[2]);
				
				if(effectType == null) {
					p.sendMessage(ChatColor.RED + arg3[2] + " is not a valid potion effect.");
					return;
				}
				
				if(!plugin.isInteger(arg3[3])) {
					p.sendMessage(ChatColor.RED + "The potion effect level must be an integer.");
					return;
				}
				
				amplifier = Integer.parseInt(arg3[3]);
				
				if(amplifier < 1) {
					p.sendMessage(ChatColor.RED + "The potion effect level must be greater than or equal to one.");
					return;
				}
				
				if(!plugin.isInteger(arg3[4])) {
					p.sendMessage(ChatColor.RED + "The potion effect duration must be an integer.");
					return;
				}
				
				duration = Integer.parseInt(arg3[4]);
				
				if(duration < 0) {
					p.sendMessage(ChatColor.RED + "The potion effect duration must be greater than 0.");
					return;
				}

				effect = new EntityPotionEffect(effectType, duration, amplifier);
				
			} else {
				p.sendMessage(GENERAL_ERROR);
				return;
			}

			//Carry out command
			s.addPoitionEffect(effect);

			//Success
			p.sendMessage(ChatColor.GREEN + "Successfully added a potion effect to spawnable entity with ID " 
					+ ChatColor.GOLD + s.getId() + ChatColor.GREEN + "! Effect Details: " + ChatColor.GOLD 
					+ effect.getType().getName() + " " + effect.getAmplifier() + ChatColor.GREEN + ".");
		} else {
			p.sendMessage(NO_PERMISSION);
			return;
		}
		
	}

}
