package com.github.thebiologist13;

import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.configuration.file.FileConfiguration;

import com.github.thebiologist13.commands.SubCommand;

/**
 * Superclass for command executors.
 * 
 * @author thebiologist13
 */
public class Executor {

	public final FileConfiguration CONFIG;
	public final CustomSpawners PLUGIN;
	private ConcurrentHashMap<String, SubCommand> commands = new ConcurrentHashMap<String, SubCommand>();
	
	public Executor(CustomSpawners plugin) {
		this.PLUGIN = plugin;
		this.CONFIG = plugin.getCustomConfig();
	}
	
	public void addCommand(String name, SubCommand executor) {
		commands.put(name, executor);
	}
	
	public void addCommand(String name, SubCommand executor, String[] aliases) {
		commands.put(name, executor);
		for(String alias : aliases) {
			commands.put(alias, executor);
			executor.addAlias(alias, name);
		}
	}
	
	public SubCommand getCommand(String command) {
		return commands.get(command);
	}
	
	public String[] makeParams(String[] baseParams, int offset) {
		int pLength = baseParams.length - offset;
		
		if(pLength < 1)
			return new String[0];
		
		String[] newParams = new String[pLength];
		for(int i = 0; i < pLength; i++) {
			newParams[i] = baseParams[i + offset];
		}
		
		return newParams;
	} 
	
	public void removeCommand(String name) {
		commands.remove(name);
	}
	
}
