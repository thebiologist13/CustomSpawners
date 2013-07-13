package com.github.thebiologist13.commands.entities;

import net.citizensnpcs.Citizens;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.Group;
import com.github.thebiologist13.SpawnableEntity;

public class EntityCreateCommand extends EntityCommand {

	public EntityCreateCommand(CustomSpawners plugin) {
		super(plugin);
	}

	public EntityCreateCommand(CustomSpawners plugin, String mainPerm) {
		super(plugin, mainPerm);
	}

	@Override
	public void run(SpawnableEntity entity, CommandSender sender, String subCommand, String[] args) {

		final String NOT_ALLOWED_ENTITY = ChatColor.RED + "That entity type is disabled for those without permission.";
		final String NONEXISTANT_ENTITY = ChatColor.RED + "That is not a entity type";

		String in = getValue(args, 0, "pig");

		SpawnableEntity newEntity = null;

		boolean hasOverride = true;

		if(sender instanceof Player) {
			hasOverride = ((Player) sender).hasPermission("customspawners.limitoverride");
		}

		int citId = -1;
		if((in.equalsIgnoreCase("player") 
				|| in.equalsIgnoreCase("human")
				|| in.equalsIgnoreCase("npc")
				|| in.equalsIgnoreCase("citizen"))) {
			
			Citizens cp = CustomSpawners.getCitizens();
			
			if(cp == null) {
				PLUGIN.sendMessage(sender, ChatColor.RED + "You must have the Citizens plugin installed " +
						"to use humans in CustomSpawners.");
				return;
			}
			
			String cit = getValue(args, 1, "");
			
			if(cit.isEmpty() || !CustomSpawners.isInteger(cit)) {
				PLUGIN.sendMessage(sender, ChatColor.RED + "You must specify the id number of the citizen.");
				return;
			}
			
			citId = Integer.parseInt(cit);
			
			if(cp.getNPCRegistry().getById(citId) == null) {
				PLUGIN.sendMessage(sender, ChatColor.RED + "That is not a citizen.");
				return;
			}
			
		}

		try {
			newEntity = PLUGIN.createEntity(in, hasOverride);
		} catch(IllegalArgumentException e) {

			if(e.getMessage() == null) {
				e.printStackTrace();
				return;
			}

			if(e.getMessage().equals("Invalid entity type.")) {
				PLUGIN.sendMessage(sender, NONEXISTANT_ENTITY);
				return;
			} else if(e.getMessage().equals("Not allowed entity.")) {
				PLUGIN.sendMessage(sender, NOT_ALLOWED_ENTITY);
				return;
			}
		}

		if(newEntity == null) {
			PLUGIN.sendMessage(sender, NONEXISTANT_ENTITY);
			return;
		}

		if(citId != -1)
			newEntity.setNPC(citId);
		
		CustomSpawners.entities.put(newEntity.getId(), newEntity);

		if(CONFIG.getBoolean("data.autosave") && CONFIG.getBoolean("data.saveOnCreate")) {
			PLUGIN.getFileManager().autosave(newEntity);
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
				&& addTo.getType().equals(Group.Type.ENTITY)) {
			addTo.addItem(addTo);
			PLUGIN.sendMessage(sender, ChatColor.GREEN + "Successfully created a new " + ChatColor.GOLD + 
					in + ChatColor.GREEN + " entity with ID number " + ChatColor.GOLD + newEntity.getId() + 
					ChatColor.GREEN + "! This entity was added to group " + ChatColor.GOLD + PLUGIN.getFriendlyName(addTo)
					+ ChatColor.GREEN + ".");
		} else {
			PLUGIN.sendMessage(sender, ChatColor.GREEN + "Successfully created a new " + ChatColor.GOLD + 
					in + ChatColor.GREEN + " entity with ID number " + ChatColor.GOLD + newEntity.getId() + ChatColor.GREEN + "!");
		}

	}

}
