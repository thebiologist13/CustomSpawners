package com.github.thebiologist13.serialization.meta;

import java.io.Serializable;
import java.util.List;

import org.bukkit.FireworkEffect;

public class SFireworkEffect implements Serializable {

	private static final long serialVersionUID = -7672805346329108992L;
	private List<Color> colors;
	private List<Color> fade;
	private String type;
	private boolean flicker;
	private boolean trail;
	
	public SFireworkEffect(FireworkEffect effect) {
		for(org.bukkit.Color c : effect.getColors()) {
			colors.add(new Color(c));
		}
		for(org.bukkit.Color c : effect.getFadeColors()) {
			fade.add(new Color(c));
		}
		type = effect.getType().toString();
		flicker = effect.hasFlicker();
		trail = effect.hasTrail();
	}

	public List<Color> getColors() {
		return colors;
	}

	public List<Color> getFade() {
		return fade;
	}

	public FireworkEffect.Type getType() {
		return FireworkEffect.Type.valueOf(type);
	}

	public boolean isFlicker() {
		return flicker;
	}

	public boolean isTrail() {
		return trail;
	}

	public void setColors(List<Color> colors) {
		this.colors = colors;
	}

	public void setFade(List<Color> fade) {
		this.fade = fade;
	}

	public void setType(FireworkEffect.Type type) {
		this.type = type.toString();
	}

	public void setFlicker(boolean flicker) {
		this.flicker = flicker;
	}

	public void setTrail(boolean trail) {
		this.trail = trail;
	}

}
