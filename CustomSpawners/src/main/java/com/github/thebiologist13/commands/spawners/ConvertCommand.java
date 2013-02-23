package com.github.thebiologist13.commands.spawners;

import net.minecraft.server.v1_4_R1.NBTTagCompound;
import net.minecraft.server.v1_4_R1.TileEntity;
import net.minecraft.server.v1_4_R1.TileEntityMobSpawner;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_4_R1.CraftWorld;
import org.bukkit.entity.EntityType;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.SpawnableEntity;
import com.github.thebiologist13.Spawner;
import com.github.thebiologist13.nbt.NBTManager;
import com.github.thebiologist13.nbt.NotTileEntityException;

public class ConvertCommand extends SpawnerCommand {

	public ConvertCommand(CustomSpawners plugin) {
		super(plugin);
	}

	public ConvertCommand(CustomSpawners plugin, String mainPerm) {
		super(plugin, mainPerm);
	}

	@Override
	public void run(Spawner spawner, CommandSender sender, String subCommand, String[] args) {
		
		String in = getValue(args, 0, "");
		
		if(in.equals("minecart")) {
			SpawnableEntity minecart = new SpawnableEntity(EntityType.MINECART, PLUGIN.getNextEntityId());
			minecart.setSpawnerData(spawner);
			
			CustomSpawners.entities.put(minecart.getId(), minecart);
			
			if(CONFIG.getBoolean("data.autosave") && CONFIG.getBoolean("data.saveOnCreate")) {
				PLUGIN.getFileManager().autosave(minecart);
			}
			
			PLUGIN.sendMessage(sender, ChatColor.GREEN + "Successfully converted spawner " + ChatColor.GOLD + 
					PLUGIN.getFriendlyName(spawner) + ChatColor.GREEN + " to a new minecart entity with ID number " + ChatColor.GOLD + 
					minecart.getId() + ChatColor.GREEN + "!");
			
			return;
		} else if(in.equals("falling_block") || in.equals("fallingblock")) {
			SpawnableEntity falling = new SpawnableEntity(EntityType.FALLING_BLOCK, PLUGIN.getNextEntityId());
			falling.setSpawnerData(spawner);
			
			CustomSpawners.entities.put(falling.getId(), falling);
			
			if(CONFIG.getBoolean("data.autosave") && CONFIG.getBoolean("data.saveOnCreate")) {
				PLUGIN.getFileManager().autosave(falling);
			}
			
			PLUGIN.sendMessage(sender, ChatColor.GREEN + "Successfully converted spawner " + ChatColor.GOLD + 
					PLUGIN.getFriendlyName(spawner) + ChatColor.GREEN + " to a new falling block entity with ID number " + ChatColor.GOLD + 
					falling.getId() + ChatColor.GREEN + "!");
			
		}
		
		CraftWorld cw = (CraftWorld) spawner.getLoc().getWorld();
		TileEntity te = cw.getTileEntityAt(spawner.getLoc().getBlockX(), spawner.getLoc().getBlockY(), spawner.getLoc().getBlockZ());
		Block blk = cw.getBlockAt(spawner.getLoc());

		if(spawner.isConverted()) { //If converting back to a CustomSpawner

			blk.setTypeIdAndData(spawner.getBlockId(), spawner.getBlockData(), false);

		} else { //If converting to a mob spawner block

			if(!(te instanceof TileEntityMobSpawner)) {
				blk.setTypeIdAndData(52, (byte) 0, true);
				te = cw.getTileEntityAt(spawner.getLoc().getBlockX(), spawner.getLoc().getBlockY(), spawner.getLoc().getBlockZ());
			}

			NBTManager nbtMan = new NBTManager();
			spawner.setActive(false);
			try {
				NBTTagCompound nbt = nbtMan.getSpawnerNBT(spawner);
				
				if(nbt == null) {
					PLUGIN.sendMessage(sender, ChatColor.RED + "Could not convert spawner. " +
							"The entity might have been invalid, or an error may have occurred.");
					return;
				}
				
				nbtMan.setTileEntityMobSpawnerNBT(spawner.getLoc().getBlock(), nbt);
			} catch (NotTileEntityException e) {
				
				PLUGIN.printDebugMessage(e.getMessage(), this.getClass());
				PLUGIN.sendMessage(sender, ChatColor.RED + "Could not find mob spawner block. Please report this to plugin author.");
				return;
			}

		}

		spawner.setConverted(!spawner.isConverted());
		
		PLUGIN.sendMessage(sender, ChatColor.GREEN + "Successfully converted spawner with ID " + ChatColor.GOLD + 
				PLUGIN.getFriendlyName(spawner) + ChatColor.GREEN + "!");
		
	}

}
