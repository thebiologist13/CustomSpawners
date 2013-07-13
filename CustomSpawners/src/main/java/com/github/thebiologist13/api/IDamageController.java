package com.github.thebiologist13.api;

import org.bukkit.event.entity.EntityDamageEvent;

public interface IDamageController {
	
	public double getModifiedDamage(EntityDamageEvent ev);
	
}
