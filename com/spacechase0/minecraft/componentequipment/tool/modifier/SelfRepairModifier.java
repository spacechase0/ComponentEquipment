package com.spacechase0.minecraft.componentequipment.tool.modifier;

import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import com.spacechase0.minecraft.componentequipment.item.EquipmentItem;
import com.spacechase0.minecraft.componentequipment.tool.Modifier;
import com.spacechase0.minecraft.spacecore.util.TranslateUtils;

public class SelfRepairModifier extends Modifier
{
	public SelfRepairModifier()
	{
		super( "selfRepair" );
	}
	
	@Override
	public String getName( ItemStack stack )
	{
		EquipmentItem item = ( EquipmentItem ) stack.getItem();
		return TranslateUtils.translate( "componentequipment:modifier.selfRepair.name" ) + " " + TranslateUtils.translate( "enchantment.level." + item.equipment.getModifierLevel( stack, type ) );
	}
	
	@Override
	public String getFormat( ItemStack stack )
	{
		return EnumChatFormatting.AQUA.toString();
	}
	
	@Override
	public int getIconColor( int pass )
	{
		return ( pass == 0 ) ? 0x00888888 : 0x0000FFFF;
	}
	
	@Override
	public int getMaxLevel()
	{
		return 3;
	}
	
	@Override
	public void itemTick( Entity entity, ItemStack stack )
	{
		if ( entity.worldObj.isRemote )
		{
			return;
		}
		EquipmentItem item = ( EquipmentItem ) stack.getItem();
		
		int chance = 150;
		chance /= item.equipment.getModifierLevel( stack, type );
		chance /= item.equipment.getRepairMultiplier( stack );
		
		if ( rand.nextInt( chance ) == 0 )
		{
			int damage = item.getDamage( stack );
			item.setDamage( stack, damage - 1 );
		}
	}
	
	private Random rand = new Random();
}
