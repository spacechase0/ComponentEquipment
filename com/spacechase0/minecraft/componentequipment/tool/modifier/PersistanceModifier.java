package com.spacechase0.minecraft.componentequipment.tool.modifier;

import java.util.List;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import com.spacechase0.minecraft.componentequipment.item.EquipmentItem;
import com.spacechase0.minecraft.componentequipment.tool.Modifier;
import com.spacechase0.minecraft.spacecore.util.TranslateUtils;

public class PersistanceModifier extends Modifier
{
	public PersistanceModifier()
	{
		super( "persistance" );
	}
	
	@Override
	public String getName( ItemStack stack )
	{
		return TranslateUtils.translate( "componentequipment:modifier.persistance.name" );
	}
	
	@Override
	public void addInformation( ItemStack stack, List list )
	{
		EquipmentItem item = ( EquipmentItem ) stack.getItem();
		int level = item.equipment.getModifierLevel( stack, type );
		if ( ( level & DAMAGE ) != 0 )
		{
			list.add( PREFIX + EnumChatFormatting.RED + TranslateUtils.translate( "componentequipment:modifier.persistance.damage" ) );
		}
		if ( ( level & TIME ) != 0 )
		{
			list.add( PREFIX + EnumChatFormatting.AQUA + TranslateUtils.translate( "componentequipment:modifier.persistance.time" ) );
		}
		if ( ( level & INVENTORY ) != 0 )
		{
			list.add( PREFIX + EnumChatFormatting.DARK_AQUA + TranslateUtils.translate( "componentequipment:modifier.persistance.inventory" ) );
		}
	}

	@Override
	public String getFormat( ItemStack stack )
	{
		return EnumChatFormatting.DARK_PURPLE.toString();
	}
	
	@Override
	public int getIconColor( int pass )
	{
		int type = pass;
		switch ( type )
		{
			case DAMAGE:    return 0x00FF0000;
			case TIME:      return 0x0000FF88;
			case INVENTORY: return 0x000044FF;
		}
		
		return 0;
	}
	
	@Override
	public int getIconColor( int level, int pass )
	{
		if ( pass == 0 ) return 0x00880088;
		
		int t1 = DAMAGE;
		int t2 = DAMAGE | TIME;
		int t3 = DAMAGE | TIME | INVENTORY;
		
		if ( level <= t1 )
		{
			return super.getIconColor( level, DAMAGE );
		}
		else if ( level <= t2 )
		{
			return super.getIconColor( level - t1, TIME);
		}
		else if ( level <= t3 )
		{
			return super.getIconColor( level - t2, INVENTORY );
		}
		
		return 0;
	}

	@Override
	public int getMaxLevel()
	{
		return ALL;
	}
	
	@Override
	public void entityTick( EntityItem entity )
	{
    	ItemStack stack = entity.getEntityItem();
		EquipmentItem item = ( EquipmentItem ) stack.getItem();
    	if ( ( item.equipment.getModifierLevel( stack, type ) & PersistanceModifier.DAMAGE ) != 0 )
    	{
    		PersistanceEventHandler.setInvulnerable( entity );
    		if ( entity.posY < 0 )
    		{
    			entity.setPosition( entity.posX, 0, entity.posZ );
    		}
    	}
	}
	
	public static final int DAMAGE = 0x01;
	public static final int TIME = 0x02;
	public static final int INVENTORY = 0x4;
	public static final int ALL = ( DAMAGE | TIME | INVENTORY );
	
	private static final String PREFIX = "      - ";
}
