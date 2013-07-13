package com.github.thebiologist13.v1_6_R2;

import net.minecraft.server.v1_6_R2.EntityMinecartMobSpawner;
import net.minecraft.server.v1_6_R2.NBTTagCompound;
import net.minecraft.server.v1_6_R2.World;

public class CSMinecart extends EntityMinecartMobSpawner {
	
	public CSMinecart(World arg0) {
		super(arg0);
	}

	public CSMinecart(World arg0, double arg1, double arg2, double arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public NBTTagCompound getNBT() {
		NBTTagCompound nbt = new NBTTagCompound();
		b(nbt);
		return nbt;
	}
	
	public void setNBT(NBTTagCompound nbt) {
		a(nbt);
	}
	
}
