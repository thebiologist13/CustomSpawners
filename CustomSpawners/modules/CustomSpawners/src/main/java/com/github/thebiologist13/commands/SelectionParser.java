package com.github.thebiologist13.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.thebiologist13.CustomSpawners;
import com.github.thebiologist13.Group;
import com.github.thebiologist13.api.IObject;
import com.github.thebiologist13.commands.groups.ParentChildException;
import com.github.thebiologist13.commands.groups.TypeException;

public class SelectionParser {

	/**
	 * Gets a spawner or a group of spawners.
	 * 
	 * @param input The input string.
	 * @return An IObject that is either a spawner or a group of spawners.
	 * @throws TypeException 
	 * @throws ParentChildException 
	 */
	public static IObject getSpawnerSelection(String input, CommandSender sender) 
			throws ParentChildException, TypeException {
		
		IObject ref = getObjectNoSelection(input, Group.Type.SPAWNER);
		
		if(isTargeted(input))
			return ref;

		if(sender instanceof Player) {
			Player p = (Player) sender;

			if(CustomSpawners.spawnerSelection.containsKey(p))
				ref = CustomSpawners.getSpawner(CustomSpawners.spawnerSelection.get(p));
		} else {
			if(CustomSpawners.consoleSpawner != -1)
				ref = CustomSpawners.getSpawner(CustomSpawners.consoleSpawner);
		}

		return ref;

	}

	/**
	 * Gets an entity or a group of entities.
	 * 
	 * @param input The input string.
	 * @return An IObject that is either an entity or a group of entities.
	 * @throws TypeException 
	 * @throws ParentChildException 
	 */
	public static IObject getEntitySelection(String input, CommandSender sender) 
			throws ParentChildException, TypeException {

		IObject ref = getObjectNoSelection(input, Group.Type.ENTITY);
		
		if(isTargeted(input))
			return ref;

		if(sender instanceof Player) {
			Player p = (Player) sender;

			if(CustomSpawners.entitySelection.containsKey(p))
				ref = CustomSpawners.getEntity(CustomSpawners.entitySelection.get(p));
		} else {
			if(CustomSpawners.consoleEntity != -1)
				ref = CustomSpawners.getSpawner(CustomSpawners.consoleEntity);
		}

		return ref;
	}

	/**
	 * Gets an entity or a group of entities.
	 * 
	 * @param input The input string.
	 * @return A CSObject that is either an entity or a group of entities.
	 * @throws TypeException 
	 * @throws ParentChildException 
	 */
	public static Group getGroupSelection(String input, CommandSender sender) 
			throws ParentChildException, TypeException {

		Group ref = null;

		if(input.startsWith("t:")) { //Target
			ref = getGroup(input.substring(2));
			return ref;
		} else {
			ref = getGroup(input);
		}

		if(sender instanceof Player) {
			Player p = (Player) sender;

			if(CustomSpawners.groupSelection.containsKey(p))
				ref = CustomSpawners.getGroup(CustomSpawners.groupSelection.get(p));
		} else {
			if(CustomSpawners.consoleGroup != -1)
				ref = CustomSpawners.getGroup(CustomSpawners.consoleGroup);
		}

		return ref;
	}

	/**
	 * Handles groups with parent/child format.
	 * 
	 * @param input What to parse.
	 * @return Parsed group.
	 */
	public static Group getGroup(String input) throws ParentChildException, TypeException {

		String[] split = input.split("\\.");

		if(split.length == 1) {
			return CustomSpawners.getGroup(split[0]);
		} else {

			Group group = CustomSpawners.getGroup(split[0]);

			if(group == null)
				return null;

			for(int i = 1; i < split.length; i++) {

				Group test = CustomSpawners.getGroup(split[i]);

				if(test == null)
					return null;

				if(!group.contains(test))
					throw new ParentChildException();

				if(!test.getType().equals(group.getType()))
					throw new TypeException();

				group = test;
			}

			return group;
		}
	}

	/**
	 * Handles groups with parent/child or set format.
	 * 
	 * @param input What to parse.
	 * @return Parsed group.
	 * @throws TypeException 
	 * @throws ParentChildException 
	 */
	public static Group getGroupOrSet(String input, Group.Type type) 
			throws ParentChildException, TypeException {
		String[] split = input.split("\\.");
		int index = split.length - 1;
		String[] dash = split[index].split("-");
		boolean setNotation = (dash.length == 2);

		if(setNotation) {
			Group parent = null;

			for(int i = 0; i < split.length; i++) {
				Group test = CustomSpawners.getGroup(split[i]);

				if(test == null && i == index) {
					//Gotta try to get a dashed group
					if(!CustomSpawners.isInteger(dash[0])
							|| !CustomSpawners.isInteger(dash[1]))
						return null;

					int id1 = Integer.parseInt(dash[0]);
					int id2 = Integer.parseInt(dash[1]);
					int diff = Math.abs(id1 - id2);
					Group set = new Group(-1, type);
					boolean hasParent = (split.length == 1) ? false : (parent != null);

					if(id1 > id2) {
						for(int j = id2; j < diff + id2; j++) {
							IObject add = getCorrect(j, type);
							if(add != null) {
								if(hasParent && !parent.contains(add))
									continue;
								set.addItem(add);
							}
						}
					} else if(id1 < id2) {
						for(int j = id1; j < diff + id1; j++) {
							IObject add = getCorrect(j, type);
							if(add != null) {
								if(hasParent && !parent.contains(add))
									continue;
								set.addItem(add);
							}
						}
					} else {
						IObject add = getCorrect(id1, type);
						if(add != null) {
							if(hasParent && !parent.contains(add))
								continue;
							set.addItem(add);
						}
					}
					
					return set;
				}

				if(i != 0) {
					if(!parent.contains(test))
						throw new ParentChildException();

					if(!test.getType().equals(parent.getType()))
						throw new TypeException();
				}

				parent = test;
			}

			return parent;
		} else {
			return getGroup(input);
		}
	}

	public static IObject getObjectNoSelection(String input, Group.Type type) 
			throws ParentChildException, TypeException {
		IObject ref = null;
		
		if(input.startsWith("t:")) { //Object Target
			ref = getCorrect(input.substring(2), type);
			return ref;
		} else if(input.startsWith("g:")) { //Group Target
			/*
			 * Can be any of the following formats:
			 * 
			 * 1. g:<main group>.<child group>.<spawner0>-<spawner1>
			 * 2. g:<main group>.<child group>.<spawner>
			 * 3. g:<main group>.<child group>
			 * 4. g:<main group>
			 * 5. g:<spawner0>-<spawner1>
			 * 
			 * Dashed ones have spawner1 excluded.
			 */

			if(type == null)
				return null;
			
			String parse = input.substring(2);

			return getGroupOrSet(parse, type);
		} else {
			ref = getCorrect(input, type);
		}
		
		return ref;
	}
	
	public static IObject getCorrect(int id, Group.Type type) {
		if(type == null)
			return CustomSpawners.getGroup(id);

		switch(type) {
		case SPAWNER:
			return CustomSpawners.getSpawner(id);
		case ENTITY:
			return CustomSpawners.getEntity(id);
		default:
			return null;
		}
	}
	
	public static IObject getCorrect(String id, Group.Type type) {
		if(type == null)
			return CustomSpawners.getGroup(id);

		switch(type) {
		case SPAWNER:
			return CustomSpawners.getSpawner(id);
		case ENTITY:
			return CustomSpawners.getEntity(id);
		default:
			return null;
		}
	}
	
	public static IObject get(String in, Group.Type type, Group.Type makeType) 
			throws ParentChildException, TypeException {
		
		if(type == null)
			return getGroupOrSet(in, makeType);

		switch(type) {
		case SPAWNER:
			return CustomSpawners.getSpawner(in);
		case ENTITY:
			return CustomSpawners.getEntity(in);
		default:
			return null;
		}
	}
	
	public static boolean isTargeted(String input) {
		if(input.startsWith("t:") || input.startsWith("g:"))
			return true;
		return false;
	}

}
