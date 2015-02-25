package com.spacechase0.minecraft.componentequipment.tool;

import java.util.HashMap;
import java.util.Map;

import static net.minecraft.init.Items.*;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import com.spacechase0.minecraft.componentequipment.tool.material.EnderMaterial;
import com.spacechase0.minecraft.componentequipment.tool.material.ExtraModMaterial;
import com.spacechase0.minecraft.componentequipment.tool.material.OreDictionaryMaterial;

public class Material
{
	public static void addType( String type, Block block, int baseDur, float toolMult, int armorDurMult, float speed, int level, int dmg, int totalArmor, EnumChatFormatting format )
	{
		types.put( type, new MaterialData( type, new ItemStack( block ), baseDur, toolMult, armorDurMult, speed, level, dmg, totalArmor, format.toString() ) );
	}
	
	public static void addType( String type, Item item, int baseDur, float toolMult, int armorDurMult, float speed, int level, int dmg, int totalArmor, EnumChatFormatting format )
	{
		types.put( type, new MaterialData( type, new ItemStack( item ), baseDur, toolMult, armorDurMult, speed, level, dmg, totalArmor, format.toString() ) );
	}
	
	public static void addType( MaterialData data )
	{
		types.put( data.getType(), data );
	}
	
	public static String[] getTypes()
	{
		return types.keySet().toArray( new String[] {} );
	}
	
	public static MaterialData getData( String type )
	{
		MaterialData data = types.get( type );
		return ( data == null ) ? types.get( "paper" ) : data;
	}
	
	private static Map< String, MaterialData > types = new HashMap< String, MaterialData >();
	
	static
	{
		addType( new OreDictionaryMaterial( "wood",  "plankWood",   150, 0.75f,  2,  2.0f, 0, 0, 3, EnumChatFormatting.GOLD ) );
		addType( new OreDictionaryMaterial( "stone", "cobblestone", 200, 0.50f,  3,  4.0f, 1, 1, 5, EnumChatFormatting.DARK_GRAY ) );
		addType( "iron",    iron_ingot,    300, 1.00f, 15,  6.0f, 2, 2, 15, EnumChatFormatting.WHITE );
		addType( "gold",    gold_ingot,    150, 0.25f,  7, 20.0f, 0, 0, 11, EnumChatFormatting.YELLOW );
		addType( "diamond", diamond,      750, 2.00f, 33,  8.0f, 3, 3, 20, EnumChatFormatting.AQUA );
		addType( "leather", leather,      200, 0.25f,  6,  1.0f, 0, 0, 7, EnumChatFormatting.GOLD );
		addType( new ExtraModMaterial( "paper", new ItemStack( paper ), 50, 0.10f,  1,  0.1f, 0, 0, 1, EnumChatFormatting.WHITE ) );
		addType( new EnderMaterial(            100, 1.25f, 20,  5.0f, 1, 1, 10, EnumChatFormatting.BLUE ) );
	}
}
