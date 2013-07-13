package com.github.thebiologist13;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.thebiologist13.commands.SelectionParser;
import com.github.thebiologist13.commands.groups.*;

/**
 * This executes commands related to /groups command.
 * 
 * @author thebiologist13
 */
public class GroupsExecutor extends Executor implements CommandExecutor{

	public GroupsExecutor(CustomSpawners plugin) {
		super(plugin);
		
		GroupCommand addObj = new GroupAddObjectCommand(plugin, "customspawners.groups.addobject");
		GroupCommand clear = new GroupClearCommand(plugin, "customspawners.groups.clear");
		GroupCommand create = new GroupCreateCommand(plugin, "customspawners.groups.create");
		GroupCommand find = new GroupFindCommand(plugin, "customspawners.groups.find");
		GroupCommand remove = new GroupRemoveCommand(plugin, "customspawners.groups.remove");
		GroupCommand remObj = new GroupRemoveObjectCommand(plugin, "customspawners.groups.removeobject");
		GroupCommand type = new GroupTypeCommand(plugin, "customspawners.groups.type");
		GroupCommand list = new GroupListCommand(plugin, "customspawners.groups.list");
		GroupCommand info = new GroupInfoCommand(plugin, "customspawners.groups.info");
		GroupCommand name = new GroupNameCommand(plugin, "customspawners.groups.name");
		GroupCommand select = new GroupSelectCommand(plugin, "customspawners.groups.select"); //TODO WIKI: Add group select.
		
		create.setNeedsObject(false);
		list.setNeedsObject(false);
		find.setNeedsObject(false);
		select.setNeedsObject(false);
		
		addCommand("creategroup", create, new String[] {
				"create",
				"new",
				"makenew"
		});
		
		addCommand("removegroup", remove, new String[] {
				"remove",
				"rem",
				"deletegroup",
				"delete",
				"del"
		});
		
		addCommand("addobject", addObj, new String[] {
				"addobj",
				"add",
				"putobject",
				"putobj",
				"put"
		});
		
		addCommand("clear", clear, new String[] {
				"cleargroup",
				"empty",
				"removeall",
				"eraseall"
		});
		
		addCommand("find", find, new String[] {
				"findobject",
				"findobj",
				"findingroup",
				"ingroup"
		});
		
		addCommand("removeobject", remObj, new String[] {
				"remobject",
				"removeobj",
				"remobj"
		});
		
		addCommand("settype", type, new String[] {
				"type",
				"setgrouptype",
				"grouptype"
		});
		
		addCommand("listallgroups", list, new String[] {
				"listall",
				"list",
				"showgroups",
				"displaygroups"
		});
		
		addCommand("info", info, new String[] {
				"showinfo",
				"displayinfo",
				"show",
				"display"
		});
		
		addCommand("setname", name, new String[] {
				"name",
				"callit",
				"displayname"
		});
		
		addCommand("selectgroup", select, new String[] {
				"select",
				"sel",
				"choose"
		});
	}

	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		
		final String INVALID_PARAMS = ChatColor.RED + "You entered invalid parameters.";
		
		if(arg1.getName().equalsIgnoreCase("groups")) {
			
			if(arg3.length < 1) {
				PLUGIN.sendMessage(arg0, ChatColor.GREEN + "This is the command used for group " +
						"modification. See the wiki for commands!");
				return true;
			}
			
			Group groupRef = null;
			String sub = arg3[0].toLowerCase();
			String objId = "";
			String[] params;
			
			if(arg3.length > 1)
				objId = arg3[1];

			if(arg3.length == 0) {
				PLUGIN.sendMessage(arg0, ChatColor.RED + "You must enter a command.");
				return true;
			}
			
			GroupCommand cmd = (GroupCommand) super.getCommand(sub);
			
			if(cmd == null) {
				PLUGIN.sendMessage(arg0, ChatColor.RED + "\"" + sub + "\" is not valid for the groups command.");
				return true;
			}
			
			sub = cmd.getCommand(sub); //Aliases
			
			if(!cmd.permissible(arg0, null)) {
				PLUGIN.sendMessage(arg0, cmd.NO_PERMISSION);
				return true;
			}
			
			if(!cmd.permissibleForObject(arg0, null, groupRef)) {
				PLUGIN.sendMessage(arg0, cmd.NO_PERMISSION);
				return true;
			}
			
			if(cmd.needsObject()) {
				try {
					groupRef = SelectionParser.getGroupSelection(objId, arg0);
				} catch (ParentChildException e) {
					PLUGIN.sendMessage(arg0, cmd.PARENT_CHILD);
					return true;
				} catch (TypeException e) {
					PLUGIN.sendMessage(arg0, cmd.NOT_SAME_TYPE);
					return true;
				}
				
				if(arg0 instanceof Player) {
					Player p = (Player) arg0;

					if(!CustomSpawners.groupSelection.containsKey(p) || objId.startsWith("t:")) {
						params = makeParams(arg3, 2);
					} else {
						params = makeParams(arg3, 1);
					}
				} else {

					if(CustomSpawners.consoleGroup == -1 || objId.startsWith("t:")) {
						params = makeParams(arg3, 2);
					} else {
						params = makeParams(arg3, 1);
					}
				}
				
				if(groupRef == null) {
					PLUGIN.sendMessage(arg0, cmd.NO_GROUP);
					return true;
				}
			} else {
				params = makeParams(arg3, 1);
				
				try {
					cmd.run(null, arg0, sub, params);
				} catch(Exception e) {
					cmd.crash(e, arg0);
				}
				
				return true;
			}
			
			try {
				cmd.run(groupRef, arg0, sub, params);
			} catch(ArrayIndexOutOfBoundsException e) {
				PLUGIN.sendMessage(arg0, INVALID_PARAMS);
				return true;
			} catch(IllegalArgumentException e) {
				if(e.getMessage().equals("Containment")) {
					PLUGIN.sendMessage(arg0, ChatColor.RED + "The group you entered has " +
							"one or more children not in the parent group!");
					return true;
				} else if(e.getMessage().equals("Type")) {
					PLUGIN.sendMessage(arg0, ChatColor.RED + "That group is not the right type!");
					return true;
				}
			} catch(Exception e) {
				cmd.crash(e, arg0);
				return true;
			}
			
			if(CONFIG.getBoolean("data.autosave") && CONFIG.getBoolean("data.saveOnCommand")) {
				PLUGIN.getFileManager().autosaveAll();
			}
			
			return true;
		}
		
		return false;
	}

}
