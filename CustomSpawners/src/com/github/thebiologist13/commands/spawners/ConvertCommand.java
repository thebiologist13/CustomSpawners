package com.github.thebiologist13.commands.spawners;

//import java.lang.reflect.Method;
//import java.util.Map;

import net.minecraft.server.v1_4_6.NBTTagCompound;
import net.minecraft.server.v1_4_6.TileEntity;
import net.minecraft.server.v1_4_6.TileEntityMobSpawner;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_4_6.CraftWorld;
import com.github.thebiologist13.CustomSpawners;
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
				
				/*Class<? extends Object> clazz = NBTTagCompound.class;
		        Method[] methods = clazz.getMethods();
		        for (Method method : methods){
		        	PLUGIN.printDebugMessage("Iterating in methods.");
		        	PLUGIN.printDebugMessage("    Method: " + method.getName());
		            if ((method.getName() == "a") && (method.getParameterTypes().length == 1) 
		            		&& (method.getParameterTypes()[0] == NBTTagCompound.class)) {
		            	PLUGIN.printDebugMessage("Has method.");
		            	method.setAccessible(true);
		                try {
		                	PLUGIN.printDebugMessage("Invoking...");
		                    Object o = method.invoke(testCom);
		                    if(o instanceof Map) {
		                    	PLUGIN.printDebugMessage("Is map.");
		                    	Map<?,?> map = (Map<?,?>) o;
		                    	for(Object o1 : map.keySet()) {
		                    		PLUGIN.printDebugMessage("Iterating in keyset");
		                    		System.out.println(o1.toString());
		                    	}
		                    }
		                } catch (Exception e) {
		                    e.printStackTrace();
		                }
		            }
		        }*/
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
