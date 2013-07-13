package com.github.thebiologist13;

import java.util.HashMap;
import java.util.Map;

public class HorseData {

	public enum Type {
		NORMAL(0, "Normal"),
		DONKEY(1, "Donkey"),
		MULE(2, "Mule"),
		SKELETON(3, "Skeleton"),
		UNDEAD(4, "Undead");
		
		private final int ID;
		private final String NAME;
		private static final Map<Integer, Type> ID_MAP = new HashMap<Integer, Type>();
		private static final Map<String, Type> NAME_MAP = new HashMap<String, Type>();
		
		static {
			for(Type t : values()) {
				ID_MAP.put(t.getId(), t);
				NAME_MAP.put(t.getName().toLowerCase(), t);
			}
		}
		
		Type(int id, String name) {
			ID = id;
			NAME = name;
		}
		
		public int getId() {
			return ID;
		}
		
		public String getName() {
			return NAME;
		}
		
		public static Type fromId(int id) {
			return ID_MAP.get(id);
		}
		
		public static Type fromName(String name) {
			return NAME_MAP.get(name.toLowerCase());
		}
		
	}
	
	public enum Color {
		WHITE(0, "White"),
		CREAMY(1, "Creamy"),
		CHESTNUT(2, "Chestnut"),
		BROWN(3, "Brown"),
		BLACK(4, "Black"),
		GRAY(5, "Gray"),
		DARK_BROWN(6, "DarkBrown");
		
		private final int ID;
		private final String NAME;
		private static final Map<Integer, Color> ID_MAP = new HashMap<Integer, Color>();
		private static final Map<String, Color> NAME_MAP = new HashMap<String, Color>();
		
		static {
			for(Color c : values()) {
				ID_MAP.put(c.getId(), c);
				NAME_MAP.put(c.getName().toLowerCase(), c);
			}
		}
		
		Color(int id, String name) {
			ID = id;
			NAME = name;
		}
		
		public int getId() {
			return ID;
		}
		
		public String getName() {
			return NAME;
		}
		
		public static Color fromId(int id) {
			return ID_MAP.get(id);
		}
		
		public static Color fromName(String name) {
			return NAME_MAP.get(name.toLowerCase());
		}
	}
	
	public enum Variant {
		NONE(0, "None"),
		WHITE(1, "White"),
		WHITE_FIELD(2, "WhiteField"),
		WHITE_DOTS(3, "WhiteDots"),
		BLACK_DOTS(4, "BlackDots");
		
		private final int ID;
		private final String NAME;
		private static final Map<Integer, Variant> ID_MAP = new HashMap<Integer, Variant>();
		private static final Map<String, Variant> NAME_MAP = new HashMap<String, Variant>();
		
		static {
			for(Variant c : values()) {
				ID_MAP.put(c.getId(), c);
				NAME_MAP.put(c.getName().toLowerCase(), c);
			}
		}
		
		Variant(int id, String name) {
			ID = id;
			NAME = name;
		}
		
		public int getId() {
			return ID;
		}
		
		public String getName() {
			return NAME;
		}
		
		public static Variant fromId(int id) {
			return ID_MAP.get(id);
		}
		
		public static Variant fromName(String name) {
			return NAME_MAP.get(name.toLowerCase());
		}
		
	}

}
