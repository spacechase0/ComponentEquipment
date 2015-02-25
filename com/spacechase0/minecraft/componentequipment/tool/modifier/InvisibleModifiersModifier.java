package com.spacechase0.minecraft.componentequipment.tool.modifier;

import java.util.List;
import java.util.UUID;

import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import com.spacechase0.minecraft.componentequipment.ComponentEquipment;
import com.spacechase0.minecraft.componentequipment.item.EquipmentItem;
import com.spacechase0.minecraft.componentequipment.tool.Modifier;
import com.spacechase0.minecraft.spacecore.util.TranslateUtils;

public class InvisibleModifiersModifier extends Modifier
{
	public InvisibleModifiersModifier()
	{
		super( "invisibleModifiers" );
	}
	
	@Override
	public String getName( ItemStack stack )
	{
		return TranslateUtils.translate( "componentequipment:modifier.invisibleModifiers.name" );
	}
	
	@Override
	public String getFormat( ItemStack stack )
	{
		return EnumChatFormatting.WHITE.toString();
	}
	
	@Override
	public int getIconColor( int pass )
	{
		return ( pass == 0 ) ? 0 : 0x004488CC;
	}
	
	@Override
	public boolean canApplyTo( EquipmentItem item )
	{
		return true;
	}
	
	@Override
	public int getModifierCost()
	{
		return 0;
	}
}
