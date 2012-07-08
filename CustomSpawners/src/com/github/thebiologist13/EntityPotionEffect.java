package com.github.thebiologist13;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.potion.PotionEffectType;

public class EntityPotionEffect extends ConfigurationSerialization {

	private PotionEffectType type = PotionEffectType.REGENERATION;
	private int duration = 30;
	private int amplifier = 1;
	
	public EntityPotionEffect(Class<? extends ConfigurationSerializable> clazz) {
		super(clazz);
	}

	public Map<String, Object> serialize() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("effect", type.getName());
		map.put("duration", duration);
		map.put("amplifier", amplifier);
		return map;
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
