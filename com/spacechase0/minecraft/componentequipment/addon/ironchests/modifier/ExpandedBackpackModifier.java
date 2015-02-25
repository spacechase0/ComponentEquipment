package com.spacechase0.minecraft.componentequipment.addon.ironchests.modifier;

import com.spacechase0.minecraft.componentequipment.tool.modifier.BackpackModifier;

public class ExpandedBackpackModifier extends BackpackModifier
{
	public ExpandedBackpackModifier()
	{
		super( false );
	}
	
	@Override
	public int getMaxLevel()
	{
		return 4;
	}
	
	@Override
	public int getSlotCount( int level )
	{
		return level * 3 * 9;
	}
	
	@Override
	public String getGuiTexture( int level )
	{
		switch ( level )
		{
			case 1: return super.getGuiTexture( level );
			case 2: return "ironchest:textures/gui/ironcontainer.png";
			case 3: return "ironchest:textures/gui/goldcontainer.png";
			case 4: return "ironchest:textures/gui/diamondcontainer.png";
		}
		
		return "";
	}
	
	public String getModelTexture( int level )
	{
		switch ( level )
		{
			case 1: return super.getModelTexture( level );
			case 2: return "ironchest:textures/model/ironchest.png";
			case 3: return "ironchest:textures/model/goldchest.png";
			case 4: return "ironchest:textures/model/diamondchest.png";
		}
		
		return "";
	}
}
