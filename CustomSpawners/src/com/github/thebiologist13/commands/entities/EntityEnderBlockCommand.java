package com.github.thebiologist13.commands.entities;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.material.MaterialData;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.SpawnableEntity;
import com.github.thebiologist13.commands.SpawnerCommand;

public class EntityEnderBlockCommand extends SpawnerCommand {

	public EntityEnderBlockCommand(CustomSpawners plugin) {
		super(plugin);
	}

	@Override
	public void run(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		
		//Command Syntax = /entities setenderblock [id] <block id>
		//Array Index with selection    0              1
		//Without selection             0        1     2
		
		//Player
		Player p = null;
		//Entity
		SpawnableEntity s = null;
		//ID
		int blockId = 0;
		//Material
		MaterialData enderBlock = null;
		//Permissions
		String perm = "customspawners.entities.setenderblock";
		
		final String MUST_BE_INTEGER = ChatColor.RED + "You must input an integer for the block ID.";
		final String MUST_BE_BLOCK = ChatColor.RED + "Data value must be a block.";
		
		if(!(arg0 instanceof Player)) {
			log.info(NO_CONSOLE);
			return;
		}
		
		p = (Player) arg0;
		
		if(p.hasPermission(perm)) {
			if(CustomSpawners.entitySelection.containsKey(p) && arg3.length == 2) {
				
				s = CustomSpawners.getEntity(CustomSpawners.entitySelection.get(p).toString());
				
				if(!CustomSpawners.isInteger(arg3[1])) {
					p.sendMessage(MUST_BE_INTEGER);
					return;
				}
				
				blockId = Integer.parseInt(arg3[1]);
				
				if(!Material.getMaterial(blockId).isBlock()) {
					p.sendMessage(MUST_BE_BLOCK);
					return;
				}
				
			} else if(arg3.length == 2) {
				p.sendMessage(NEEDS_SELECTION);
				return;
			} else if(arg3.length == 3) {
				
				s = CustomSpawners.getEntity(arg3[1]);

				if(s == null) {
					p.sendMessage(NO_ID);
					return;
				}
				
				if(!CustomSpawners.isInteger(arg3[2])) {
					p.sendMessage(MUST_BE_INTEGER);
					return;
				}
				
				blockId = Integer.parseInt(arg3[2]);
				
				if(!Material.getMaterial(blockId).isBlock()) {
					p.sendMessage(MUST_BE_BLOCK);
					return;
				}
				
			} else {
				p.sendMessage(GENERAL_ERROR);
				return;
			}
			
			//Carry out command
			enderBlock = new MaterialData(blockId);
			s.setEndermanBlock(enderBlock);
			
			//Success
			p.sendMessage(ChatColor.GREEN + "Successfully set spawned endermen's block for entity with ID " +
					ChatColor.GOLD + plugin.getFriendlyName(s) + ChatColor.GREEN + " to " + ChatColor.GOLD + blockId + ChatColor.GREEN + "!");
		} else {
			p.sendMessage(NO_PERMISSION);
			return;
		}
		
	}

}
