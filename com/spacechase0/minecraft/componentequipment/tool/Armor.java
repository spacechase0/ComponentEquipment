package com.spacechase0.minecraft.componentequipment.tool;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.item.ItemStack;

import com.spacechase0.minecraft.componentequipment.item.ArmorItem;
import com.spacechase0.minecraft.componentequipment.item.EquipmentItem;

public class Armor extends Equipment
{
	public void addType( String type, int theType, int durMult, int divisor, String[] parts )
	{
		ArmorData data = new ArmorData( theType, durMult, divisor, parts );
		types.put( type, data );
		baseTypes.put( type, data );
	}
	
	public String[] getTypes()
	{
		return types.keySet().toArray( new String[] {} );
	}
	
	public ArmorData getData( String type )
	{
		return types.get( type );
	}
	
	@Override
	public int getMaxDamage( EquipmentItem item, ItemStack stack )
	{
		ArmorData data = getData( item.type );
		int pieceMult = data.getDurabilityMultiplier();
		
		String part = data.getParts()[ 0 ];
		MaterialData mat = getMaterialOf( stack, part );
		
		return ( int )( pieceMult * mat.getArmorDurabilityMultiplier() );
	}

	@Override
	public float getRepairMultiplier( ItemStack stack )
	{
		int mults[] = new int[] { 11, 16, 15, 13 };
		
		ArmorItem armor = ( ArmorItem ) stack.getItem();
		
		return ( 2.25f / getData( armor.type ).getRepairDivisor() ) * super.getRepairMultiplier( stack );
	}
	
	public double getProtectionOf( ItemStack stack )
	{
		if ( isBroken( stack ) )
		{
			return 0;
		}
		
		double mult = 0;
		
		ArmorItem item = ( ArmorItem ) stack.getItem();
		if ( item.type.equals( "helmet" ) || item.type.equals( "boots" ) )
		{
			mult = 0.15;
		}
		else if ( item.type.equals( "chestplate" ) )
		{
			mult = 0.4;
		}
		else if ( item.type.equals( "leggings" ) )
		{
			mult = 0.3;
		}
		
		return ( mult * getMaterialOf( stack, getData( item.type ).getParts()[ 0 ] ).getTotalArmor() ) / 20 * 0.8;
	}
	
	public int getDisplayProtectionOf( ItemStack stack )
	{
		return ( int )( getProtectionOf( stack ) / 0.8 * 20 );
	}
	
	private static Map< String, ArmorData > types = new HashMap< String, ArmorData >();
	public static final Armor instance = new Armor();

	private Armor()
	{
		addType( "helmet", HELMET, 11, 4, new String[] { "helm" } );
		addType( "chestplate", CHESTPLATE, 16, 6, new String[] { "chest" } );
		addType( "leggings", LEGGINGS, 15, 5, new String[] { "leggings" } );
		addType( "boots", BOOTS, 13, 3, new String[] { "boots" } );
	}

	public static final int HELMET = 0;
	public static final int CHESTPLATE = 1;
	public static final int LEGGINGS = 2;
	public static final int BOOTS = 3;
}
