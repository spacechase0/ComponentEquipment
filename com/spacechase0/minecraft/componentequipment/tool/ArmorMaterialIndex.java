package com.spacechase0.minecraft.componentequipment.tool;

import java.util.HashMap;
import java.util.Map;

import static net.minecraft.init.Items.*;
import net.minecraft.item.ItemArmor;

public class ArmorMaterialIndex
{
	public static void set( ItemArmor item, String mat )
	{
		index.put( item, mat );
	}
	
	public static String get( ItemArmor item )
	{
		return index.get( item );
	}
	
	public static void initialize()
	{
		set( diamond_helmet, "diamond" );
		set( diamond_chestplate, "diamond" );
		set( diamond_leggings, "diamond" );
		set( diamond_boots, "diamond" );
		set( iron_helmet, "iron" );
		set( iron_chestplate, "iron" );
		set( iron_leggings, "iron" );
		set( iron_boots, "iron" );
		set( golden_helmet, "gold" );
		set( golden_chestplate, "gold" );
		set( golden_leggings, "gold" );
		set( golden_boots, "gold" );
		set( leather_helmet, "leather" );
		set( leather_chestplate, "leather" );
		set( leather_leggings, "leather" );
		set( leather_boots, "leather" );
	}
	
	private static Map< ItemArmor, String > index = new HashMap< ItemArmor, String >();
}
