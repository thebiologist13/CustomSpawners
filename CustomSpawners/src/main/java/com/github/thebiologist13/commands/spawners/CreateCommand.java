package com.github.thebiologist13.commands.spawners;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.Group;
import com.github.thebiologist13.SpawnableEntity;
import com.github.thebiologist13.Spawner;

public class CreateCommand extends SpawnerCommand {

	public CreateCommand(CustomSpawners plugin) {
		super(plugin);
	}

	public CreateCommand(CustomSpawners plugin, String mainPerm) {
		super(plugin, mainPerm);
	}

	@Override
	public void run(Spawner spawner, CommandSender sender, String subCommand, String[] args) {

		if(!(sender instanceof Player)) {
			PLUGIN.sendMessage(sender, NO_CONSOLE);
			return;
		}

		Player player = (Player) sender;

		SpawnableEntity entity = null;

		String in = getValue(args, 0, "");

		if(in.isEmpty()) {
			Integer selection = CustomSpawners.entitySelection.get(player);
			if(selection == null) {
				PLUGIN.sendMessage(sender, NO_ID);
				return;
			}
			entity = CustomSpawners.getEntity(selection);
		} else {
			entity = CustomSpawners.getEntity(in);
		}

		if(entity == null) {
			PLUGIN.sendMessage(sender, NO_ID);
			return;
		}

		Block target = player.getTargetBlock(CustomSpawners.transparent, CONFIG.getInt("players.maxDistance", 5));

		if(target == null) {
			PLUGIN.sendMessage(player, ChatColor.RED + "You must look at a block to make a spawner there.");
			return;
		}

		if(target.getType().equals(Material.AIR)) {
			PLUGIN.sendMessage(player, ChatColor.RED + "You must look at a block to make a spawner there.");
			return;
		}

		Spawner newSpawner = PLUGIN.createSpawner(entity, target.getLocation());

		if(CONFIG.getBoolean("data.autosave") && CONFIG.getBoolean("data.saveOnCreate")) {
			PLUGIN.getFileManager().autosave(newSpawner);
		}
		
		//TODO Add this to wiki.
		Group addTo = null;
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(CustomSpawners.groupSelection.containsKey(p))
				addTo = CustomSpawners.getGroup(CustomSpawners.groupSelection.get((Player) sender));
		} else {
			if(CustomSpawners.consoleGroup != -1)
				addTo = CustomSpawners.getGroup(CustomSpawners.consoleGroup);
		}

		if(addTo != null && CONFIG.getBoolean("players.groupAutoAdd", false) 
				&& addTo.getType().equals(Group.Type.SPAWNER)) {
			addTo.addItem(newSpawner);
			PLUGIN.sendMessage(sender, ChatColor.GREEN + "Successfully created a new " + ChatColor.GOLD + 
					in + ChatColor.GREEN + " spawner with ID number " + ChatColor.GOLD + newSpawner.getId() + 
					ChatColor.GREEN + "! This spawner was added to group " + ChatColor.GOLD + PLUGIN.getFriendlyName(addTo)
					+ ChatColor.GREEN + ".");
		}

		PLUGIN.sendMessage(player, ChatColor.GREEN + "Successfully created a " + ChatColor.GOLD + 
				PLUGIN.getFriendlyName(entity) + ChatColor.GREEN + " spawner with ID " + 
				ChatColor.GOLD + PLUGIN.getFriendlyName(newSpawner) + ChatColor.GREEN + "!");

	}
}
