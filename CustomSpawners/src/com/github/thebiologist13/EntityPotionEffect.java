package com.github.thebiologist13;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.potion.PotionEffectType;

@SerializableAs("Effect")
public class EntityPotionEffect implements ConfigurationSerializable {

	private PotionEffectType type = PotionEffectType.REGENERATION;
	private int duration = 30;
	private int amplifier = 1;

	public EntityPotionEffect(PotionEffectType type, int duration, int amplifier) {
		this.type = type;
		this.duration = duration;
		this.amplifier = amplifier;
	}
	
	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("effect", type.getName());
		map.put("duration", duration);
		map.put("amplifier", amplifier);
		return map;
	}
	
	//DESERIALIZE
	public static EntityPotionEffect deserialize(Map<String, Object> map) {
		String name = (String) map.get("effect");
		int dur = (Integer) map.get("duration");
		int amp = (Integer) map.get("amplifier");
		
		return new EntityPotionEffect(PotionEffectType.getByName(name), dur, amp);
	}
	
	public PotionEffectType getType() {
		return type;
	}

	public void setType(PotionEffectType type) {
		this.type = type;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public int getAmplifier() {
		return amplifier;
	}

	public void setAmplifier(int amplifier) {
		this.amplifier = amplifier;
	}
}
