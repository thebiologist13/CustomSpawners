package com.github.thebiologist13.serialization;

import java.io.Serializable;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SPotionEffect implements Serializable {

	private static final long serialVersionUID = 418751498682653727L;
	private int type = PotionEffectType.REGENERATION.getId();
	private int duration = 30;
	private int amplifier = 1;

	public SPotionEffect(PotionEffectType type, int duration, int amplifier) {
		this.type = type.getId();
		this.duration = duration;
		this.amplifier = amplifier;
	}
	
	public SPotionEffect(PotionEffect p) {
		this.type = p.getType().getId();
		this.duration = p.getDuration();
		this.amplifier = p.getAmplifier();
	}

	public PotionEffectType getType() {
		return PotionEffectType.getById(type);
	}

	public void setType(PotionEffectType type) {
		this.type = type.getId();
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
	
	public PotionEffect toPotionEffect() {
		return new PotionEffect(PotionEffectType.getById(type), duration, amplifier);
	}
	
}
