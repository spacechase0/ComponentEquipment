package com.spacechase0.minecraft.componentequipment.tool;

import java.util.HashMap;
import java.util.Map;

import static net.minecraft.init.Blocks.*;
import static net.minecraft.init.Items.*;
import net.minecraft.item.ItemStack;

public class Arrow
{
	public static void addType( String type, Object stack, float damage, boolean infinity )
	{
		types.put( type, new ArrowData( stack, damage, infinity ) );
	}
	
	public static String[] getTypes()
	{
		return types.keySet().toArray( new String[] {} );
	}
	
	public static ArrowData getData( String type )
	{
		return types.get( type );
	}
	
	private static Map< String, ArrowData > types = new HashMap< String, ArrowData >();
	
	static
	{
		addType( "wood", "plankWood", 0.75f, true );
		addType( "stone", "cobblestone", 1.25f, true );
		addType( "flint", flint, 1.75f, true );
		addType( "iron", iron_ingot, 2.25f, true );
		addType( "gold", gold_ingot, 0.75f, true );
		addType( "diamond", diamond, 3.75f, true );

		addType( "bonemeal", new ItemStack( dye, 1, 15 ), 0, false );
		addType( "enderPearl", ender_pearl, 1, false );
		//addType( "ice", Block.ice, 1.5f, false );
		addType( "torch", torch, 0, false );
		addType( "tnt", tnt, 0, false );
	}
}
