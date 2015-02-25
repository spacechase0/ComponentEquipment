package com.spacechase0.minecraft.componentequipment.tool;

public class EquipmentData
{
	public EquipmentData( String[] theParts )
	{
		parts = theParts;
	}

	public String[] getParts()
	{
		return parts;
	}
	
	protected final String[] parts;
}