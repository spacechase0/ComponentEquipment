package com.spacechase0.minecraft.componentequipment.tool.modifier;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import com.spacechase0.minecraft.componentequipment.item.EquipmentItem;
import com.spacechase0.minecraft.componentequipment.tool.Modifier;
import com.spacechase0.minecraft.spacecore.util.TranslateUtils;

public class CheaperRepairModifier extends Modifier
{
	public CheaperRepairModifier()
	{
		super( "cheapRepair" );
	}
	
	@Override
	public String getName( ItemStack stack )
	{
		EquipmentItem item = ( EquipmentItem ) stack.getItem();
		return TranslateUtils.translate( "componentequipment:modifier.cheapRepair.name" ) + " " + TranslateUtils.translate( "enchantment.level." + item.equipment.getModifierLevel( stack, type ) );
	}

	@Override
	public String getFormat( ItemStack stack )
	{
		return EnumChatFormatting.GOLD.toString();
	}
	
	@Override
	public int getIconColor( int pass )
	{
		return ( pass == 0 ) ? 0x00664411 : 0x00CCAA00;
	}

	@Override
	public int getMaxLevel()
	{
		return 3;
	}

	@Override
	public float getRepairModifier( ItemStack stack )
	{
		EquipmentItem item = ( EquipmentItem ) stack.getItem();
		return 1.f + ( item.equipment.getModifierLevel( stack, type ) * 0.334f );
	}
}
