package com.spacechase0.minecraft.componentequipment.tool;


public class ArrowData
{
	public ArrowData( Object theMat, float theDamage, boolean theInfinity )
	{
		mat = theMat;
		damage = theDamage;
		infinity = theInfinity;
	}
	
	public Object getCraftingMaterial()
	{
		return mat;
	}
	
	public float getDamage()
	{
		return damage;
	}
	
	public boolean affectedByInfinity()
	{
		return infinity;
	}
	
	private final Object mat;
	private final float damage;
	private final boolean infinity;
}
