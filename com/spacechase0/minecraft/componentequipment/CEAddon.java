package com.spacechase0.minecraft.componentequipment;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class CEAddon
{
	public void preInit()
	{
	}
	
	public void init()
	{
	}
	
	public void postInit()
	{
	}
	
	protected static ItemStack getStackFor( String type )
	{
		Item item = ( Item ) Item.itemRegistry.getObject( type );
		if ( item == null )
		{
			CELog.warning( "Unable to find " + type );
			return null;
		}
		
		return new ItemStack( item );
	}
}
