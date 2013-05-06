package com.github.thebiologist13;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.thebiologist13.commands.*;

/**
 * This executes commands related to the /customspawners command.
 * 
 * @author thebiologist13
 */
public class CustomSpawnersExecutor extends Executor implements CommandExecutor {

	public CustomSpawnersExecutor(CustomSpawners plugin) {
		super(plugin);

		CustomSpawnersCommand hc = new HelpCommand(plugin, "customspawners.help");
		CustomSpawnersCommand rc = new ReloadDataCommand(plugin, "customspawners.reload");
		CustomSpawnersCommand si = new SpawnerIOCommand(plugin, "customspawners.io");

		addCommand("help", hc, new String[] {
				"helpme",
				"sos"
		});
		addCommand("reload", rc, new String[] {
				"reloaddata",
				"refresh"
		});
		addCommand("import", si, new String[] {
				"getspawners",
				"load"
		});
		addCommand("export", si, new String[] {
				"setspawners",
				"save"
		});
	}

	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		
		final String DONORS = CustomSpawners.donors;
		
		final String[] PLUGIN_INFO = {
				ChatColor.GREEN + "* * * " +  ChatColor.GOLD + " CustomSpawners " + PLUGIN.getDescription().getVersion() + ChatColor.GREEN + " by thebiologist13 * * *",
				"",
				ChatColor.GREEN + "Plugin on BukkitDev: ",
				ChatColor.AQUA + "http://dev.bukkit.org/server-mods/customspawners/",
				"",
				ChatColor.GREEN + "thebiologist13 on BukkitDev: ",
				ChatColor.AQUA + "http://dev.bukkit.org/profiles/thebiologist13/",
				"",
				ChatColor.GREEN + "Use \"/customspawners help [page]\" for help on using CustomSpawners",
				"",
				ChatColor.GREEN + "Thanks to all the awesome donors! " + DONORS,
				ChatColor.GREEN + "* * * * * * * * * * * * * * * *"
		};

		if(arg1.getName().equalsIgnoreCase("customspawners")) {

			if(arg3.length == 0) {
				PLUGIN.sendMessage(arg0, PLUGIN_INFO);
				return true;
			}
			
			String sub = arg3[0].toLowerCase();
			
			CustomSpawnersCommand cmd = (CustomSpawnersCommand) super.getCommand(sub);
			
			if(cmd == null) {
				PLUGIN.sendMessage(arg0, ChatColor.RED + "\"" + sub + "\" is not valid for this command.");
				return true;
			}
			
			sub = cmd.getCommand(sub); //Aliases
			
			if(arg0 instanceof Player) {
				Player p = (Player) arg0;
				
				if(!p.hasPermission(cmd.permission)) {
					PLUGIN.sendMessage(p, cmd.NO_PERMISSION);
					return true;
				}
			}
			
			try {
				cmd.run(arg0, sub, makeParams(arg3, 1));
			} catch(ArrayIndexOutOfBoundsException e) {
				PLUGIN.sendMessage(arg0, ChatColor.RED + "You entered invalid parameters.");
				return true;
			} catch(Exception e) {
				PLUGIN.printDebugTrace(e);
				PLUGIN.getFileManager().saveCrash(cmd.getClass(), e);
				PLUGIN.sendMessage(arg0, cmd.GENERAL_ERROR);
				return true;
			}

			return true;

		}

		return false;

	}

}
