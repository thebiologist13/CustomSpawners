package com.github.thebiologist13;

import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import com.github.thebiologist13.commands.SpawnerCommand;
import com.github.thebiologist13.commands.entities.*;

public class EntitiesExecutor implements CommandExecutor {
	
	private ConcurrentHashMap<String, SpawnerCommand> commands = new ConcurrentHashMap<String, SpawnerCommand>();
	
	private FileConfiguration config = null;
	
	private CustomSpawners plugin = null;
	
	public EntitiesExecutor(CustomSpawners plugin) {
		this.plugin = plugin;
		this.config = plugin.getCustomConfig();
		
		SpawnerCommand aec = new EntityAddEffectCommand(plugin);
		SpawnerCommand agc = new EntityAgeCommand(plugin);
		SpawnerCommand aic = new EntityAirCommand(plugin);
		SpawnerCommand anc = new EntityAngryCommand(plugin);
		SpawnerCommand ctc = new EntityCatTypeCommand(plugin);
		SpawnerCommand chc = new EntityChargedCommand(plugin);
		SpawnerCommand cec = new EntityClearEffectsCommand(plugin);
		SpawnerCommand coc = new EntityColorCommand(plugin);
		SpawnerCommand crc = new EntityCreateCommand(plugin);
		SpawnerCommand ebc = new EntityEnderBlockCommand(plugin);
		SpawnerCommand hec = new EntityHealthCommand(plugin);
		SpawnerCommand ifc = new EntityInfoCommand(plugin);
		SpawnerCommand joc = new EntityJockeyCommand(plugin);
		SpawnerCommand nac = new EntityNameCommand(plugin);
		SpawnerCommand prc = new EntityProfessionCommand(plugin);
		SpawnerCommand rec = new EntityRemoveCommand(plugin);
		SpawnerCommand sac = new EntitySaddledCommand(plugin);
		SpawnerCommand slc = new EntitySelectCommand(plugin);
		SpawnerCommand sec = new EntitySetEffectCommand(plugin);
		SpawnerCommand stc = new EntitySetTypeCommand(plugin);
		SpawnerCommand sic = new EntitySittingCommand(plugin);
		SpawnerCommand ssc = new EntitySlimeSizeCommand(plugin);
		SpawnerCommand tac = new EntityTamedCommand(plugin);
		SpawnerCommand vec = new EntityVelocityCommand(plugin);
		SpawnerCommand lac = new EntityListAllCommand(plugin);
		SpawnerCommand pac = new EntityPassiveCommand(plugin);
		SpawnerCommand ftc = new EntityFireTicksCommand(plugin);
		SpawnerCommand blc = new EntityBlackListCommand(plugin);
		SpawnerCommand wlc = new EntityWhiteListCommand(plugin);
		SpawnerCommand ilc = new EntityItemListCommand(plugin);
		SpawnerCommand dac = new EntityDamageCommand(plugin);
		SpawnerCommand drc = new EntityDropsCommand(plugin);
		SpawnerCommand edc = new EntityExpCommand(plugin);
		SpawnerCommand fuc = new EntityFuseCommand(plugin);
		SpawnerCommand inc = new EntityIncendiaryCommand(plugin);
		SpawnerCommand yic = new EntityYieldCommand(plugin);
		SpawnerCommand itc = new EntityItemTypeCommand(plugin);
		SpawnerCommand ptc = new EntityPotionTypeCommand(plugin);
		SpawnerCommand eic = new EntityInvulnerableCommand(plugin);
		SpawnerCommand einc = new EntityInventoryCommand(plugin);
		
		this.commands.put("create", crc);
		this.commands.put("select", slc);
		this.commands.put("remove", rec);
		this.commands.put("info", ifc);
		this.commands.put("sel", slc);
		this.commands.put("rem", rec);
		this.commands.put("cleareffects", cec);
		this.commands.put("noeffects", cec);
		this.commands.put("list", lac);
		this.commands.put("listall", lac);
		this.commands.put("show", lac);
		this.commands.put("clearitems", ilc);
		this.commands.put("noitems", ilc);
		this.commands.put("clearblacklist", blc);
		this.commands.put("noblacklist", blc);
		this.commands.put("blc", blc);
		this.commands.put("clearwhitelist", wlc);
		this.commands.put("nowhitelist", wlc);
		this.commands.put("wlc", wlc);
		this.commands.put("cleardrops", drc);
		this.commands.put("nodrops", drc);
		this.commands.put("drc", drc);
		this.commands.put("setair", aic);
		this.commands.put("air", aic);
		this.commands.put("setage", agc);
		this.commands.put("age", agc);
		this.commands.put("setangry", anc);
		this.commands.put("angry", anc);
		this.commands.put("setcattype", ctc);
		this.commands.put("cattype", ctc);
		this.commands.put("cat", ctc);
		this.commands.put("setcharged", chc);
		this.commands.put("setcolor", coc);
		this.commands.put("color", coc);
		this.commands.put("setenderblock", ebc);
		this.commands.put("enderblock", ebc);
		this.commands.put("eb", ebc);
		this.commands.put("setendermanblock", ebc);
		this.commands.put("sethealth", hec);
		this.commands.put("health", hec);
		this.commands.put("hp", hec);
		this.commands.put("setjockey", joc);
		this.commands.put("jockey", joc);
		this.commands.put("jock", joc);
		this.commands.put("setname", nac);
		this.commands.put("name", nac);
		this.commands.put("setprofession", prc);
		this.commands.put("profession", prc);
		this.commands.put("villager", prc);
		this.commands.put("setsaddled", sac);
		this.commands.put("saddle", sac);
		this.commands.put("settype", stc);
		this.commands.put("type", stc);
		this.commands.put("mob", stc);
		this.commands.put("setsitting", sic);
		this.commands.put("sitting", sic);
		this.commands.put("sit", sic);
		this.commands.put("setslimesize", ssc);
		this.commands.put("setsize", ssc);
		this.commands.put("size", ssc);
		this.commands.put("settamed", tac);
		this.commands.put("tamed", tac);
		this.commands.put("setvelocity", vec);
		this.commands.put("setvector", vec);
		this.commands.put("velocity", vec);
		this.commands.put("vector", vec);
		this.commands.put("launch", vec);
		this.commands.put("setpassive", pac);
		this.commands.put("passive", pac);
		this.commands.put("setfireticks", ftc);
		this.commands.put("fireticks", ftc);
		this.commands.put("fire", ftc);
		this.commands.put("addblacklistitem", blc);
		this.commands.put("setblacklist", blc);
		this.commands.put("addwhitelistitem", wlc);
		this.commands.put("setwhitelist", wlc);
		this.commands.put("additem", ilc);
		this.commands.put("setcustomdamage", dac);
		this.commands.put("setdamageamount", dac);
		this.commands.put("setusingdrops", drc);
		this.commands.put("usedrops", drc);
		this.commands.put("drops", drc);
		this.commands.put("setexp", edc);
		this.commands.put("setxp", edc);
		this.commands.put("exp", edc);
		this.commands.put("xp", edc);
		this.commands.put("setfuselength", fuc);
		this.commands.put("setfuseticks", fuc);
		this.commands.put("fuselength", fuc);
		this.commands.put("fuseticks", fuc);
		this.commands.put("fuse", fuc);
		this.commands.put("setincendiary", inc);
		this.commands.put("incendiary", inc);
		this.commands.put("napalm", inc);
		this.commands.put("setitemtype", itc);
		this.commands.put("itemtype", itc);
		this.commands.put("setyield", yic);
		this.commands.put("yield", yic);
		this.commands.put("boomsize", yic);
		this.commands.put("setinvulnerable", eic);
		this.commands.put("invulnerable", eic);
		this.commands.put("invincible", eic);
		this.commands.put("dontdie", eic);
		this.commands.put("adddrop", drc);
		this.commands.put("addd", drc);
		this.commands.put("setdrops", drc);
		this.commands.put("setdrop", drc);
		this.commands.put("setd", drc);
		this.commands.put("addeffect", aec);
		this.commands.put("adde", aec);
		this.commands.put("seteffect", sec);
		this.commands.put("sete", sec);
		this.commands.put("setpotiontype", ptc);
		this.commands.put("potiontype", ptc);
		this.commands.put("clearinventory", einc);
		this.commands.put("clearinv", einc);
		einc.addAlias("clearinv", "clearinventory");
		this.commands.put("noinv", einc);
		einc.addAlias("noinv", "clearinventory");
		this.commands.put("addinventoryitem", einc);
		this.commands.put("addinventory", einc);
		einc.addAlias("addinventory", "addinventoryitem");
		this.commands.put("addinv", einc);
		einc.addAlias("addinv", "addinventoryitem");
		this.commands.put("setinventory", einc);
		this.commands.put("setinv", einc);
		einc.addAlias("setinv", "setinventory");
		this.commands.put("sethand", einc);
		this.commands.put("sethelmet", einc);
		this.commands.put("sethelm", einc);
		einc.addAlias("sethelm", "sethelmet");
		this.commands.put("sethat", einc);
		einc.addAlias("sethat", "sethelmet");
		this.commands.put("setchest", einc);
		this.commands.put("setshirt", einc);
		einc.addAlias("setshirt", "setchest");
		this.commands.put("setleggings", einc);
		this.commands.put("setpants", einc);
		einc.addAlias("setpants", "setleggings");
		this.commands.put("setboots", einc);
		this.commands.put("setshoes", einc);
		einc.addAlias("setshoes", "setboots");
	}

	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		
		if(arg1.getName().equalsIgnoreCase("entities")) {
			try {
				
				SpawnerCommand runThis = commands.get(arg3[0].toLowerCase());
				
				if(runThis == null) {
					plugin.sendMessage(arg0, ChatColor.RED + "\"" + arg3[0].toLowerCase() + "\" is not a valid command for CustomSpawners.");
				} else {
					runThis.run(arg0, arg1, arg2, arg3);
				}
				
			} catch(Exception e) {
				
				plugin.sendDebugStack(arg0, e.getStackTrace());
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
