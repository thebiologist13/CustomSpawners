package com.github.thebiologist13;

import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import com.github.thebiologist13.commands.SpawnerCommand;
import com.github.thebiologist13.commands.spawners.*;

public class SpawnerExecutor implements CommandExecutor {

	private ConcurrentHashMap<String, SpawnerCommand> commands = new ConcurrentHashMap<String, SpawnerCommand>();
	
	private FileConfiguration config = null;
	
	private CustomSpawners plugin;
	
	public SpawnerExecutor(CustomSpawners plugin) {
		this.plugin = plugin;
		this.config = plugin.getCustomConfig();
		
		SpawnerCommand hic = new HiddenCommand(plugin);
		SpawnerCommand aac = new ActivateAllCommand(plugin);
		SpawnerCommand atc = new AddTypeCommand(plugin);
		SpawnerCommand dac = new DeactivateAllCommand(plugin);
		SpawnerCommand fsc = new ForceSpawnCommand(plugin);
		SpawnerCommand rmc = new RemoveMobsCommand(plugin);
		SpawnerCommand nac = new NameCommand(plugin);
		SpawnerCommand lnc = new ListNearCommand(plugin);
		SpawnerCommand lac = new ListAllCommand(plugin);
		SpawnerCommand cc = new CreateCommand(plugin);
		SpawnerCommand sc = new SelectCommand(plugin);
		SpawnerCommand rc = new RemoveCommand(plugin);
		SpawnerCommand stc = new SetTypeCommand(plugin);
		SpawnerCommand llc = new LightLevelCommand(plugin);
		SpawnerCommand mmc = new MaxMobsCommand(plugin);
		SpawnerCommand psc = new PerSpawnCommand(plugin);
		SpawnerCommand pdc = new PlayerDistanceCommand(plugin);
		SpawnerCommand slc = new SetLocationCommand(plugin);
		SpawnerCommand src = new SetRadiusCommand(plugin);
		SpawnerCommand srac = new SetRateCommand(plugin);
		SpawnerCommand srec = new SetRedstoneCommand(plugin);
		SpawnerCommand ic = new InfoCommand(plugin);
		SpawnerCommand ac = new ActiveCommand(plugin);
		SpawnerCommand pc = new PositionCommand(plugin);
		SpawnerCommand sac = new SpawnAreaCommand(plugin);
		SpawnerCommand coc = new ConvertCommand(plugin);
		
		this.commands.put("create", cc);
		this.commands.put("remove", rc);
		this.commands.put("select", sc);
		this.commands.put("info", ic);
		this.commands.put("sel", sc);
		this.commands.put("rem", rc);
		this.commands.put("listnear", lnc);
		this.commands.put("near", lnc);
		this.commands.put("listall", lac);
		this.commands.put("list", lac);
		this.commands.put("setactive", ac);
		this.commands.put("setinactive", ac);
		this.commands.put("sethidden", hic);
		this.commands.put("setunhidden", hic);
		this.commands.put("setlocation", slc);
		this.commands.put("location", slc);
		this.commands.put("puthere", slc);
		this.commands.put("activateall", aac);
		this.commands.put("allon", aac);
		this.commands.put("deactivateall", dac);
		this.commands.put("alloff", dac);
		this.commands.put("forcespawn", fsc);
		this.commands.put("force", fsc);
		this.commands.put("spawn", fsc);
		this.commands.put("removeallmobs", rmc);
		this.commands.put("invadersmustdie", rmc);
		this.commands.put("removemobs", rmc);
		this.commands.put("destroythemwithlasers", rmc);
		this.commands.put("knifeparty", rmc);
		this.commands.put("pos1", pc);
		this.commands.put("pos2", pc);
		this.commands.put("convert", coc);
		this.commands.put("togglestate", coc);
		this.commands.put("setmaxlight", llc);
		this.commands.put("setminlight", llc);
		this.commands.put("setmaxmobs", mmc);
		this.commands.put("maxmobs", mmc);
		this.commands.put("setmobsperspawn", psc);
		this.commands.put("mobsperspawn", psc);
		this.commands.put("mps", psc);
		this.commands.put("perspawn", psc);
		this.commands.put("setmaxdistance", pdc);
		this.commands.put("setmindistance", pdc);
		this.commands.put("setredstone", srec);
		this.commands.put("redstone", srec);
		this.commands.put("setradius", src);
		this.commands.put("radius", src);
		this.commands.put("rad", src);
		this.commands.put("setrate", srac);
		this.commands.put("rate", srac);
		this.commands.put("addtype", atc);
		this.commands.put("addt", atc);
		this.commands.put("setname", nac);
		this.commands.put("name", nac);
		this.commands.put("setspawnarea", sac);
		this.commands.put("spawnarea", sac);
		this.commands.put("sa", sac);
		this.commands.put("settype", stc);
		this.commands.put("type", stc);
	}
	
	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		
		if(arg1.getName().equalsIgnoreCase("spawners")) {
			
			try {
				
				SpawnerCommand runThis = commands.get(arg3[0].toLowerCase());
				
				if(runThis == null) {
					plugin.sendMessage(arg0, ChatColor.RED + "\"" + arg3[0].toLowerCase() + "\" is not a valid command for CustomSpawners.");
				} else {
					runThis.run(arg0, arg1, arg2, arg3);
				}

			} catch(Exception e) {
				
				e.printStackTrace();
				plugin.sendMessage(arg0, SpawnerCommand.GENERAL_ERROR);
				
			}
			
			if(config.getBoolean("data.autosave") && config.getBoolean("data.saveOnCommand")) {
				plugin.getFileManager().autosaveAll();
			}

			return true;
			
		}
		
		return false;
		
	}
	
}
