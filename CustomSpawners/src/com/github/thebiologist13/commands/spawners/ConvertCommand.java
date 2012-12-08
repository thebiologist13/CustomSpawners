package com.github.thebiologist13.commands.spawners;

import net.minecraft.server.TileEntity;
import net.minecraft.server.TileEntityMobSpawner;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.Player;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.NBTManager;
import com.github.thebiologist13.NotTileEntityException;
import com.github.thebiologist13.Spawner;
import com.github.thebiologist13.commands.SpawnerCommand;

public class ConvertCommand extends SpawnerCommand {

	public ConvertCommand(CustomSpawners plugin) {
		super(plugin);
	}

	@Override
	public void run(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		
		/*
		 * Command Usage -> /css convert [id]
		 *   Arg#:           CMD    0      1          
		 *   Purposes:
		 *     CMD: Command itself.
		 *     0: The base command signaling this class.
		 *     1: ID of custom spawner to convert if none is selected.
		 *     2*: Type of conversion
		 *       full: All available properties converted. Includes surrounding mechanisms for light.
		 *       block: All available properties converted.
		 *       part: Basic properties only (type, delay, range). Includes copy of CustomSpawners spawner.
		 *       base: Basic properties only (type, delay, range).
		 *       
		 *   *Unimplemented
		 */
		
		//Player
		Player p = null;
		//Spawner
		Spawner s = null;
		//Perm
		String perm = "customspawners.spawners.convert";
		
		if(arg0 instanceof Player) {
			p = (Player) arg0;
		}
		
		if(p == null) {
			
			if(CustomSpawners.consoleSpawner != -1 && arg3.length == 1) {
				
				s = CustomSpawners.getSpawner(String.valueOf(CustomSpawners.consoleSpawner));
				
			} else if(arg3.length == 1) {
				
				plugin.sendMessage(arg0, NEEDS_SELECTION);
				return;
				
			} else if(arg3.length == 2) {
				
				s = CustomSpawners.getSpawner(arg3[1]);

				if(s == null) {
					plugin.sendMessage(arg0, NO_ID);
					return;
				}
				
			} else {
				
				plugin.sendMessage(arg0, GENERAL_ERROR);
				return;
				
			}
			
			convert(s);
			
		} else {
			
			if(p.hasPermission(perm)) {
				
				Block target = p.getTargetBlock(null, 4);
				
				if(arg3.length == 1 && target.getTypeId() == 52) {
					
					s = plugin.getSpawnerAt(target.getLocation());
					
					if(s == null) {
						plugin.sendMessage(p, ChatColor.RED + "You must look at a mob spawner with CustomSpawner's data to convert it!");
					}
					
				} else if(CustomSpawners.spawnerSelection.containsKey(p)  && arg3.length == 1) {
					
					s = CustomSpawners.getSpawner(String.valueOf(CustomSpawners.spawnerSelection.get(p)));
					
				} else if(arg3.length == 1) {
					
					plugin.sendMessage(p, NEEDS_SELECTION);
					return;
					
				} else if(arg3.length == 2) {
					
					s = CustomSpawners.getSpawner(arg3[1]);

					if(s == null) {
						plugin.sendMessage(p, NO_ID);
						return;
					}
					
				} else {
					
					plugin.sendMessage(p, GENERAL_ERROR);
					return;
					
				}
				
				convert(s);
				
			}
			
		}
		
	}
	
	private void convert(Spawner s) {
		
		CraftWorld cw = (CraftWorld) s.getLoc().getWorld();
		TileEntity te = cw.getTileEntityAt(s.getLoc().getBlockX(), s.getLoc().getBlockY(), s.getLoc().getBlockZ());
		Block blk = cw.getBlockAt(s.getLoc());
		
		if(s.isConverted()) { //If converting back to a CustomSpawner
			
			//TODO Maybe add a "locked" property so it can't be set active when converted?
			s.setActive(true); //Sets the CustomSpawner's spawner to be inactive so double spawns don't occur 
			blk.setTypeIdAndData(s.getBlock().getTypeId(), s.getBlock().getData(), true);
			
		} else { //If converting to a mob spawner block
			
			if(!(te instanceof TileEntityMobSpawner)) {
				blk.setTypeIdAndData(52, (byte) 0, true);
				te = cw.getTileEntityAt(s.getLoc().getBlockX(), s.getLoc().getBlockY(), s.getLoc().getBlockZ());
			}
			
			NBTManager nbtMan = new NBTManager();
			s.setActive(false);
			try {
				nbtMan.setTileEntityMobSpawnerNBT(s.getLoc().getBlock(), nbtMan.getSpawnerNBT(s));
			} catch (NotTileEntityException e) {
				e.printStackTrace();
			}
			
			//Save to world
			plugin.saveCustomSpawnerToWorld(s);
			
		}
		
		s.setConverted(!s.isConverted());
		
	}

}
