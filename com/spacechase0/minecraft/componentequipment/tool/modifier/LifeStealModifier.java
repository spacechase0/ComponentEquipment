package com.spacechase0.minecraft.componentequipment.tool.modifier;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import com.spacechase0.minecraft.componentequipment.item.EquipmentItem;
import com.spacechase0.minecraft.componentequipment.item.SwordItem;
import com.spacechase0.minecraft.componentequipment.item.ToolItem;
import com.spacechase0.minecraft.componentequipment.tool.Modifier;
import com.spacechase0.minecraft.spacecore.util.TranslateUtils;

public class LifeStealModifier extends Modifier
{
	public LifeStealModifier()
	{
		super( "lifeSteal" );
	}
	
	@Override
	public String getName( ItemStack stack )
	{
		EquipmentItem item = ( EquipmentItem ) stack.getItem();
		return TranslateUtils.translate( "componentequipment:modifier.lifeSteal.name" ) + " " + TranslateUtils.translate( "enchantment.level." + item.equipment.getModifierLevel( stack, type ) );
	}
	
	@Override
	public String getFormat( ItemStack stack )
	{
		return EnumChatFormatting.BLACK.toString();
	}
	
	@Override
	public int getIconColor( int pass )
	{
		return ( pass == 0 ) ? 0x00000000 : 0x00FF4444;
	}
	
	@Override
	public boolean canApplyTo( EquipmentItem item )
	{
		return ( item instanceof SwordItem );
	}
	
	@Override
	public int getMaxLevel()
	{
		return 2;
	}
	
	@Override
	public void hitEntity( EntityLivingBase attacker, EntityLivingBase target, ItemStack stack )
	{
		ToolItem item = ( ToolItem ) stack.getItem();
		int damage = item.tool.getAttackDamage( ( ToolItem ) stack.getItem(), stack );
		float amt = ( float ) damage / getMaxLevel();
		int stolen = ( int )( amt * item.tool.getModifierLevel( stack, type ) );
		
		attacker.heal( stolen );
	}
}
