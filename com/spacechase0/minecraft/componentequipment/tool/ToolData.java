package com.spacechase0.minecraft.componentequipment.tool;

public class ToolData extends EquipmentData
{
	public ToolData( int theType, int theDamage, String[] theParts )
	{
		super( theParts );
		
		type = theType;
		damage = theDamage;
	}
	
	public int getType()
	{
		return type;
	}
	
	public int getAttackDamage()
	{
		return damage;
	}
	
	public boolean isWeapon()
	{
		return ( ( getType() & Tool.WEAPON ) != 0 );
	}
	
	public boolean isHarvester()
	{
		return ( ( getType() & Tool.HARVEST ) != 0 );
	}
	
	public boolean isBow()
	{
		return ( ( getType() & Tool.BOW ) != 0 );
	}
	
	private final int type;
	private final int damage;
}
