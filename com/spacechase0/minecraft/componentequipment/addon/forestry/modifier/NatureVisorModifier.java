package com.spacechase0.minecraft.componentequipment.addon.forestry.modifier;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import com.spacechase0.minecraft.componentequipment.ComponentEquipment;
import com.spacechase0.minecraft.componentequipment.item.ArmorItem;
import com.spacechase0.minecraft.componentequipment.item.EquipmentItem;
import com.spacechase0.minecraft.componentequipment.tool.Modifier;

public class NatureVisorModifier extends Modifier
{
	public NatureVisorModifier()
	{
		super( "natureVisor" );
	}
	
	@Override
	public String getName( ItemStack stack )
	{
		EquipmentItem item = ( EquipmentItem ) stack.getItem();
		return StatCollector.translateToLocal( "componentequipment:modifier.natureVisor.name" );
	}
	
	@Override
	public String getFormat( ItemStack stack )
	{
		return EnumChatFormatting.WHITE.toString();
	}
	
	@Override
	public int getIconColor( int pass )
	{
		return ( pass == 0 ) ? 0x00B4B4B4 : 0x00BFEDFF;
	}

	@Override
	public int getMaxLevel()
	{
		return 1;
	}
	
	@Override
	public boolean canApplyTo( EquipmentItem item )
	{
		return ( item == ComponentEquipment.items.helmet );
	}
	
	// TODO: function in armor
}
