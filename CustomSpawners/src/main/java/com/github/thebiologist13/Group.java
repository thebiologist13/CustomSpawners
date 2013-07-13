package com.github.thebiologist13;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.bukkit.ChatColor;

import com.github.thebiologist13.api.IObject;

public class Group implements Serializable, IObject {

	public enum Type {
		SPAWNER,
		ENTITY;
		
		public static Type fromName(String name) {
			name = name.toLowerCase();
			if(name.equals("spawner")) {
				return Type.SPAWNER;
			} else if(name.equals("entity") 
					|| name.equals("spawnableentity")) {
				return Type.ENTITY;
			} else {
				return null;
			}
		}
	}

	private static final long serialVersionUID = 835761381987304038L;
	
	//Data
	private Map<String, Object> data;
	
	private Map<IObject, Integer> group = new HashMap<IObject, Integer>();
	
	public Group(int id, Type type) {
		this(id, type, "");
	}
	
	public Group(int id, Type type, String name) {
		this.data = new HashMap<String, Object>();
		this.data.put("id", id);
		this.data.put("name", name);
		this.data.put("type", type);
	}

	public void addItem(IObject obj) {
		
		if(obj instanceof Spawner && !getType().equals(Type.SPAWNER)) {
			throw new IllegalArgumentException("Must add spawners to a group of spawners.");
		} else if(obj instanceof SpawnableEntity && !getType().equals(Type.ENTITY)) {
			throw new IllegalArgumentException("Must add entities to a group of entities.");
		} else if(obj instanceof Group) {
			Group g = (Group) obj;
			
			if(g.getType().equals(Type.SPAWNER) && !getType().equals(Type.SPAWNER)) {
				throw new IllegalArgumentException("Must add groups of spawners to a group of spawners.");
			} else if(g.getType().equals(Type.ENTITY) && !getType().equals(Type.ENTITY)) {
				throw new IllegalArgumentException("Must add groups of entities to a group of entities.");
			}
		}
		
		group.put(obj, obj.getId());
	}
	
	public boolean contains(IObject obj) {
		for(Integer i : group.values()) {
			if(i.intValue() == obj.getId())
				return true;
		}
		return false;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Group))
			return false;
		return ((Group) obj).getId() == getId();
	}
	
	public Map<IObject, Integer> getGroup() {
		Map<IObject, Integer> map = new HashMap<IObject, Integer>();
		for(IObject i : group.keySet()) {
			int id = i.getId();
			if(i instanceof Spawner) {
				map.put(CustomSpawners.getSpawner(id), id);
			} else if(i instanceof SpawnableEntity) {
				map.put(CustomSpawners.getEntity(id), id);
			} else if(i instanceof Group) {
				map.put(CustomSpawners.getGroup(id), id);
			}
		}
		return map;
	}

	@Override
	public int getId() {
		return (Integer) this.data.get("id");
	}
	
	public String getName() {
		return (String) this.data.get("name");
	}
	
	public Type getType() {
		return (Type) this.data.get("type");
	}

	public void removeItem(IObject obj) {
		group.remove(obj);
	}

	public void setGroup(Map<IObject, Integer> group) {
		for(IObject obj : group.keySet()) {
			addItem(obj);
		}
	}

	public void setName(String name) {
		name = ChatColor.translateAlternateColorCodes('&', name);
		
		name = name.replaceAll("__", " ");
		
		this.data.put("name", name);
	}

	public void setType(Type type) {
		Validate.notNull(type, "Cannot be null type");
		this.data.put("type", type);
	}

}
