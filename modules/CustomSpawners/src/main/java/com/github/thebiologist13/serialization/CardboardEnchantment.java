package com.github.thebiologist13.serialization;

import java.io.Serializable;

import org.bukkit.enchantments.Enchantment;

import com.github.thebiologist13.api.ICardboardEnchantment;

/**
 * A serializable Enchantment
 * 
 * @author NuclearW
 */
public class CardboardEnchantment implements Serializable, ICardboardEnchantment {
	
	private static final long serialVersionUID = 4256877106564545842L;
	private final int id;
	 
    public CardboardEnchantment(Enchantment enchantment) {
        this.id = enchantment.getId();
    }
 
    @Override
    public Enchantment unbox() {
        return Enchantment.getById(this.id);
    }
    
}
