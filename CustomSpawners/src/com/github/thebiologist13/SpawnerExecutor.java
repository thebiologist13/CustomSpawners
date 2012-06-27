package com.github.thebiologist13;

import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.thebiologist13.commands.ActiveCommand;
import com.github.thebiologist13.commands.CreateCommand;
import com.github.thebiologist13.commands.HelpCommand;
import com.github.thebiologist13.commands.HiddenCommand;
import com.github.thebiologist13.commands.InfoCommand;
import com.github.thebiologist13.commands.LightLevelCommand;
import com.github.thebiologist13.commands.ListAllCommand;
import com.github.thebiologist13.commands.ListNearCommand;
import com.github.thebiologist13.commands.MaxMobsCommand;
import com.github.thebiologist13.commands.PerSpawnCommand;
import com.github.thebiologist13.commands.PlayerDistanceCommand;
import com.github.thebiologist13.commands.ReloadCommand;
import com.github.thebiologist13.commands.RemoveCommand;
import com.github.thebiologist13.commands.SelectCommand;
import com.github.thebiologist13.commands.SetLocationCommand;
import com.github.thebiologist13.commands.SetRadiusCommand;
import com.github.thebiologist13.commands.SetRateCommand;
import com.github.thebiologist13.commands.SetRedstoneCommand;
import com.github.thebiologist13.commands.SetTypeCommand;

public class SpawnerExecutor implements CommandExecutor {

	/*
	 * This class executes the /customspawner command.
	 * You will notice very little code other than
	 * something like this:
	 * 
	 * SomeClass.run(arg0, arg1, arg2, arg3);
	 * 
	 * This is because each sub-command 
	 * (in the com.github.thebiologist13.commands package)
	 * has 100+ lines of code to execute. Therefore
	 * I put each sub-command's code in a 
	 * separate class.
	 */
	
	final String GENERAL_ERROR = ChatColor.RED + "An error has occured with this command. Did you type everything right?";
	final String GENERAL_ERROR_NO_COLOR = "An error has occured with this command. Did you type everything right?";
	
	@SuppressWarnings("unused")
	private CustomSpawners plugin;
	
	private Logger log;
	
	//Sub-Commands
	private HelpCommand hc;
	private ListNearCommand lnc;
	private ListAllCommand lac;
	private CreateCommand cc;
	private SelectCommand sc;
	private RemoveCommand rc;
	private SetTypeCommand stc;
	private ActiveCommand ac;
	private HiddenCommand hic;
	private LightLevelCommand llc;
	private MaxMobsCommand mmc;
	private PerSpawnCommand psc;
	private PlayerDistanceCommand pdc;
	private ReloadCommand rlc;
	private SetLocationCommand slc;
	private SetRadiusCommand src;
	private SetRateCommand srac;
	private SetRedstoneCommand srec;
	private InfoCommand ic;
	
	public SpawnerExecutor(CustomSpawners plugin) {
		this.plugin = plugin;
		log = plugin.log;
		hc = new HelpCommand(plugin);
		lnc = new ListNearCommand(plugin);
		lac = new ListAllCommand(plugin);
		cc = new CreateCommand(plugin);
		sc = new SelectCommand(plugin);
		rc = new RemoveCommand(plugin);
		stc = new SetTypeCommand(plugin);
		llc = new LightLevelCommand(plugin);
		mmc = new MaxMobsCommand(plugin);
		psc = new PerSpawnCommand(plugin);
		pdc = new PlayerDistanceCommand(plugin);
		rlc = new ReloadCommand(plugin);
		slc = new SetLocationCommand(plugin);
		src = new SetRadiusCommand(plugin);
		srac = new SetRateCommand(plugin);
		srec = new SetRedstoneCommand(plugin);
		ic = new InfoCommand(plugin);
		ac = new ActiveCommand(plugin);
	}
	
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		
		if(arg1.getName().equalsIgnoreCase("customspawner")) {
			
			//How many arguments
			int argumentLength = arg3.length;
			
			//Catch any unhandled errors with commands.
			try {
				//If the argument length is 0, display the help message
				if(argumentLength == 0) {
					
					hc.run(arg0, arg1, arg2, arg3);
					return true;
					
				/*
				 * Commands with 1 Argument
				 */
					
				} else if(argumentLength == 1) {
					
					if(arg3[0].equalsIgnoreCase("help")) { //Help command
						
						hc.run(arg0, arg1, arg2, arg3);
						return true;
						
					} else if(arg3[0].equalsIgnoreCase("listnear")) { //List nearby spawners command
						
						lnc.run(arg0, arg1, arg2, arg3);
						return true;
						
					} else if(arg3[0].equalsIgnoreCase("listall")) { //List all spawners command
						
						lac.run(arg0, arg1, arg2, arg3);
						return true;
						
					} else if(arg3[0].equalsIgnoreCase("remove")) {
						
						rc.run(arg0, arg1, arg2, arg3);
						return true;
						
					} else if(arg3[0].equalsIgnoreCase("setactive")) {
						
						ac.run(arg0, arg1, arg2, arg3);
						return true;
						
					} else if(arg3[0].equalsIgnoreCase("setinactive")) {
						
						ac.run(arg0, arg1, arg2, arg3);
						return true;
						
					} else if(arg3[0].equalsIgnoreCase("sethidden")) {
						
						hic.run(arg0, arg1, arg2, arg3);
						return true;
						
					} else if(arg3[0].equalsIgnoreCase("setunhidden")) {
						
						hic.run(arg0, arg1, arg2, arg3);
						return true;
						
					} else if(arg3[0].equalsIgnoreCase("reloadspawners")) {
						
						rlc.run(arg0, arg1, arg2, arg3);
						return true;
						
					} else if(arg3[0].equalsIgnoreCase("info")) {
						
						ic.run(arg0, arg1, arg2, arg3);
						return true;
						
					} else if(arg3[0].equalsIgnoreCase("setlocation")) {
						
						slc.run(arg0, arg1, arg2, arg3);
						return true;
						
					} else {
						
						sendInvalidArgumentMessage(arg0, arg3[0]);
						return true;
						
					}
					
				/*
				 * Commands with 2 Arguments
				 */
					
				} else if(argumentLength == 2) {
					
					if(arg3[0].equalsIgnoreCase("create")) { //Create Command
						
						cc.run(arg0, arg1, arg2, arg3);
						return true;
						
					} else if(arg3[0].equalsIgnoreCase("select")) { //Selection command
						
						sc.run(arg0, arg1, arg2, arg3);
						return true;
						
					} else if(arg3[0].equalsIgnoreCase("remove")) {
						
						rc.run(arg0, arg1, arg2, arg3);
						return true;
						
					} else if(arg3[0].equalsIgnoreCase("settype")) {
						
						stc.run(arg0, arg1, arg2, arg3);
						return true;
						
					} else if(arg3[0].equalsIgnoreCase("setactive")) {
						
						ac.run(arg0, arg1, arg2, arg3);
						return true;
						
					} else if(arg3[0].equalsIgnoreCase("setinactive")) {
						
						ac.run(arg0, arg1, arg2, arg3);
						return true;
						
					} else if(arg3[0].equalsIgnoreCase("sethidden")) {
						
						hic.run(arg0, arg1, arg2, arg3);
						return true;
						
					} else if(arg3[0].equalsIgnoreCase("setunhidden")) {
						
						hic.run(arg0, arg1, arg2, arg3);
						return true;
						
					} else if(arg3[0].equalsIgnoreCase("info")) {
						
						ic.run(arg0, arg1, arg2, arg3);
						return true;
						
					} else if(arg3[0].equalsIgnoreCase("setmaxlight")) {
						
						llc.run(arg0, arg1, arg2, arg3);
						return true;
						
					} else if(arg3[0].equalsIgnoreCase("setminlight")) {
						
						llc.run(arg0, arg1, arg2, arg3);
						return true;
						
					} else if(arg3[0].equalsIgnoreCase("setmaxmobs")) {
						
						mmc.run(arg0, arg1, arg2, arg3);
						return true;
						
					} else if(arg3[0].equalsIgnoreCase("setmobsperspawn")) {
						
						psc.run(arg0, arg1, arg2, arg3);
						return true;
						
					} else if(arg3[0].equalsIgnoreCase("setmaxdistance")) {
						
						pdc.run(arg0, arg1, arg2, arg3);
						return true;
						
					} else if(arg3[0].equalsIgnoreCase("setmindistance")) {
						
						pdc.run(arg0, arg1, arg2, arg3);
						return true;
						
					} else if(arg3[0].equalsIgnoreCase("setlocation")) {
						
						slc.run(arg0, arg1, arg2, arg3);
						return true;
						
					} else if(arg3[0].equalsIgnoreCase("setredstone")) {
						
						srec.run(arg0, arg1, arg2, arg3);
						return true;
						
					} else if(arg3[0].equalsIgnoreCase("setradius")) {
						
						src.run(arg0, arg1, arg2, arg3);
						return true;
						
					} else if(arg3[0].equalsIgnoreCase("setrate")) {
						
						srac.run(arg0, arg1, arg2, arg3);
						return true;
						
					} else {
						
						sendInvalidArgumentMessage(arg0, arg3[0]);
						return true;
						
					}
					
				/*
				 * Commands with 3 Arguments
				 */
					
				} else if(argumentLength == 3) {
					if(arg3[0].equalsIgnoreCase("setmaxlight")) {
						
						llc.run(arg0, arg1, arg2, arg3);
						return true;
						
					} else if(arg3[0].equalsIgnoreCase("setminlight")) {
						
						llc.run(arg0, arg1, arg2, arg3);
						return true;
						
					} else if(arg3[0].equalsIgnoreCase("setmaxmobs")) {
						
						mmc.run(arg0, arg1, arg2, arg3);
						return true;
						
					} else if(arg3[0].equalsIgnoreCase("setmobsperspawn")) {
						
						psc.run(arg0, arg1, arg2, arg3);
						return true;
						
					} else if(arg3[0].equalsIgnoreCase("setmaxdistance")) {
						
						pdc.run(arg0, arg1, arg2, arg3);
						return true;
						
					} else if(arg3[0].equalsIgnoreCase("setmindistance")) {
						
						pdc.run(arg0, arg1, arg2, arg3);
						return true;
						
					} else if(arg3[0].equalsIgnoreCase("setlocation")) {
						
						slc.run(arg0, arg1, arg2, arg3);
						return true;
						
					} else if(arg3[0].equalsIgnoreCase("setradius")) {
						
						src.run(arg0, arg1, arg2, arg3);
						return true;
						
					} else if(arg3[0].equalsIgnoreCase("setrate")) {
						
						srac.run(arg0, arg1, arg2, arg3);
						return true;
						
					} else if(arg3[0].equalsIgnoreCase("setredstone")) {
						
						srec.run(arg0, arg1, arg2, arg3);
						return true;
						
					} else if(arg3[0].equalsIgnoreCase("settype")) {
						
						stc.run(arg0, arg1, arg2, arg3);
						return true;
						
					} else if(arg3[0].equalsIgnoreCase("setradius")) {
						
						src.run(arg0, arg1, arg2, arg3);
						return true;
						
					} else if(arg3[0].equalsIgnoreCase("setrate")) {
						
						srac.run(arg0, arg1, arg2, arg3);
						return true;
						
					} else {
						
						sendInvalidArgumentMessage(arg0, arg3[0]);
						return true;
						
					}
				}
				return true;
			} catch(Exception e) {
				/*
				if(arg0 instanceof Player) {
					Player p = (Player) arg0;
					p.sendMessage(GENERAL_ERROR);
				}
				log.severe(GENERAL_ERROR_NO_COLOR);
				*/
				e.printStackTrace();
				return true;
			}
		}
		return false;
	}
	
	//Send invalid arguments message (when arguments are specified, but cannot be handled/executed)
	public void sendInvalidArgumentMessage(CommandSender arg0, String arg) {
		final String MESSAGE = ChatColor.RED + arg + " is not a usable argument for this command.";
		final String MESSAGE_NO_COLOR = arg + " is not a usable argument for this command.";
		
		if(arg0 instanceof Player) {
			Player p = (Player) arg0;
			p.sendMessage(MESSAGE);
		} else {
			log.info(MESSAGE_NO_COLOR);
		}
	}
}
