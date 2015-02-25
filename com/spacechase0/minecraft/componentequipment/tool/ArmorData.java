package com.spacechase0.minecraft.componentequipment.tool;

public class ArmorData extends EquipmentData
{
	public ArmorData( int theType, int theDurMult, int theDivisor, String[] theParts )
	{
		super( theParts );
		
		type = theType;
		durMult = theDurMult;
		divisor = theDivisor;
	}
	
	public int getType()
	{
		return type;
	}
	
	public int getDurabilityMultiplier()
	{
		return durMult;
	}
	
	public int getRepairDivisor()
	{
		return divisor;
	}

	private final int type;
	private final int durMult;
	private final int divisor;
}
