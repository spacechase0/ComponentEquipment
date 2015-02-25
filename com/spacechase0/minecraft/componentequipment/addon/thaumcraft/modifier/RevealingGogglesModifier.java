package com.spacechase0.minecraft.componentequipment.addon.thaumcraft.modifier;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import com.spacechase0.minecraft.componentequipment.ComponentEquipment;
import com.spacechase0.minecraft.componentequipment.item.EquipmentItem;
import com.spacechase0.minecraft.componentequipment.tool.Modifier;

public class RevealingGogglesModifier extends Modifier
{
	public RevealingGogglesModifier()
	{
		super( "seeAuraNodes" );
	}
	
	@Override
	public String getName( ItemStack stack )
	{
		EquipmentItem item = ( EquipmentItem ) stack.getItem();
		return StatCollector.translateToLocal( "componentequipment:modifier.seeAuraNodes.name" );
	}
	
	@Override
	public String getFormat( ItemStack stack )
	{
		return EnumChatFormatting.LIGHT_PURPLE.toString();
	}
	
	@Override
	public int getIconColor( int pass )
	{
		return ( pass == 0 ) ? 0x00F2AE00 : 0x003F1171;
	}
	
	@Override
	public boolean canApplyTo( EquipmentItem item )
	{
		return ( item == ComponentEquipment.items.helmet );
	}
}
