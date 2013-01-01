package com.github.thebiologist13.commands.spawners;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.SpawnableEntity;
import com.github.thebiologist13.Spawner;

public class InfoCommand extends SpawnerCommand {

	public InfoCommand(CustomSpawners plugin) {
		super(plugin);
	}

	public InfoCommand(CustomSpawners plugin, String mainPerm) {
		super(plugin, mainPerm);
	}

	@Override
	public void run(Spawner spawner, CommandSender sender, String subCommand, String[] args) {
		
		if(spawner.isHidden() && !permissible(sender, "customspawners.spawners.info.hidden")) {
			PLUGIN.sendMessage(sender, ChatColor.RED + "You are not allowed to view info on that spawner!");
			return;
		}
		
		PLUGIN.sendMessage(sender, getInfo(spawner));
		
	}

	private String[] getInfo(Spawner s) {
		String typesMessage = "";
		ArrayList<SpawnableEntity> types = new ArrayList<SpawnableEntity>();
		for(Integer e : s.getTypeData()) {
			types.add(CustomSpawners.getEntity(e.toString()));
		}
		
		for(int i = 0; i < types.size(); i++) {
			if(i == 0) {
				if(types.get(i).getName().isEmpty()) {
					typesMessage += types.get(i).getId();
				} else {
					typesMessage += types.get(i).getId() + " (" + types.get(i).getName() + ")";
				}
			} else {
				if(types.get(i).getName().isEmpty()) {
					typesMessage += ", " + types.get(i).getId();
				} else {
					typesMessage += ", " + types.get(i).getId() + " (" + types.get(i).getName() + ")";
				}
			}
		}
		
		String header = ChatColor.GREEN + "Information on spawner with ID " + ChatColor.GOLD + String.valueOf(s.getId());
		
		if(!s.getName().isEmpty()) {
			header += " (" + s.getName() + ")";
		}
		
		String converted = "";
		if(s.isConverted()) {
			converted = ChatColor.RED + String.valueOf(s.isConverted());
		} else {
			converted = ChatColor.GREEN + String.valueOf(s.isConverted());
		}
		
		String[] message = {
				"",
				header + ChatColor.GREEN + ": ",
				"",
				ChatColor.GOLD + "Active: " + String.valueOf(s.isActive()),
				ChatColor.GOLD + "Converted: " + converted,
				ChatColor.GOLD + "Hidden: " + String.valueOf(s.isHidden()),
				ChatColor.GOLD + "Types: " + typesMessage,
				ChatColor.GOLD + "Location: " + "(" + s.getLoc().getBlockX() + ", " + s.getLoc().getBlockY() + ", " + s.getLoc().getBlockZ() + ")",
				ChatColor.GOLD + "Spawn Rate: " + String.valueOf(s.getMobsPerSpawn()) + " per " + String.valueOf(s.getRate()) + " ticks",
				ChatColor.GOLD + "Spawn Radius: " + String.valueOf(s.getRadius()),
				ChatColor.GOLD + "Maximum Mobs: " + String.valueOf(s.getMaxMobs()),
				ChatColor.GOLD + "Maximum Light: " + String.valueOf(s.getMaxLightLevel()),
				ChatColor.GOLD + "Minimum Light: " + String.valueOf(s.getMinLightLevel()),
				ChatColor.GOLD + "Maximum Distance: " + String.valueOf(s.getMaxPlayerDistance()),
				ChatColor.GOLD + "Minimum Distance: " + String.valueOf(s.getMinPlayerDistance()),
				ChatColor.GOLD + "Redstone Triggered: " + String.valueOf(s.isRedstoneTriggered()),
				ChatColor.GOLD + "Spawn When Powered: " + String.valueOf(s.isSpawnOnRedstone()),
				ChatColor.GOLD + "Uses Spawn Area: " + String.valueOf(s.isUsingSpawnArea()),
				ChatColor.GOLD + "Spawn Area Locations: ",
				ChatColor.GOLD + "  Point 1 - (" + s.getAreaPoints()[0].getBlockX() + "," +
						s.getAreaPoints()[0].getBlockY() + "," + s.getAreaPoints()[0].getBlockZ() + ") ",
				ChatColor.GOLD + "  Point 2 - (" + s.getAreaPoints()[1].getBlockX() + "," + 
						s.getAreaPoints()[1].getBlockY() + "," + s.getAreaPoints()[1].getBlockZ() + ")"
				};
		
		return message;
		
	}
	
}
