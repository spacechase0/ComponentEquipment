package com.spacechase0.minecraft.componentequipment.addon.forestry.modifier;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import com.spacechase0.minecraft.componentequipment.item.ArmorItem;
import com.spacechase0.minecraft.componentequipment.item.EquipmentItem;
import com.spacechase0.minecraft.componentequipment.tool.Modifier;

public class BeeSuitModifier extends Modifier
{
	public BeeSuitModifier()
	{
		super( "beeSuit" );
	}
	
	@Override
	public String getName( ItemStack stack )
	{
		EquipmentItem item = ( EquipmentItem ) stack.getItem();
		return StatCollector.translateToLocal( "componentequipment:modifier.beeSuit.name" );
	}
	
	@Override
	public String getFormat( ItemStack stack )
	{
		return EnumChatFormatting.WHITE.toString();
	}
	
	@Override
	public int getIconColor( int pass )
	{
		return ( pass == 0 ) ? 0x00FFFFFF : 0x00000000;
	}

	@Override
	public int getMaxLevel()
	{
		return 1;
	}
	
	@Override
	public boolean canApplyTo( EquipmentItem item )
	{
		return ( item instanceof ArmorItem );
	}
	
	// TODO: function in armor
}
