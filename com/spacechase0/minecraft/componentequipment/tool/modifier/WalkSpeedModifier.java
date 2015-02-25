package com.spacechase0.minecraft.componentequipment.tool.modifier;

import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import com.spacechase0.minecraft.componentequipment.ComponentEquipment;
import com.spacechase0.minecraft.componentequipment.item.EquipmentItem;
import com.spacechase0.minecraft.componentequipment.tool.Modifier;

public class WalkSpeedModifier extends ItemAttributeModifier
{
	public WalkSpeedModifier()
	{
		super( "walkSpeed", EnumEnchantmentType.armor_feet, EnumChatFormatting.GRAY, 0x00FFFFFF, 0x000066CC, 4, SharedMonsterAttributes.movementSpeed, 2, 0.125, "CB3F55D3-645C-4F38-A597-9C13A33DB5CF" );
	}
	
	@Override
	public boolean canApplyTo( EquipmentItem item )
	{
		return ( item == ComponentEquipment.items.boots || item == ComponentEquipment.items.leggings );
	}
}
