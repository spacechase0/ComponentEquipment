package com.spacechase0.minecraft.componentequipment.tool.modifier;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import com.spacechase0.minecraft.componentequipment.ComponentEquipment;
import com.spacechase0.minecraft.componentequipment.item.EquipmentItem;
import com.spacechase0.minecraft.componentequipment.tool.Modifier;
import com.spacechase0.minecraft.spacecore.util.TranslateUtils;

public class WaterWalkModifier extends Modifier
{
	public WaterWalkModifier()
	{
		super( "waterWalk" );
	}
	
	@Override
	public String getName( ItemStack stack )
	{
		EquipmentItem item = ( EquipmentItem ) stack.getItem();
		return TranslateUtils.translate( "componentequipment:modifier.waterWalk.name" ) + " " + TranslateUtils.translate( "enchantment.level." + item.equipment.getModifierLevel( stack, type ) );
	}
	
	@Override
	public String getFormat( ItemStack stack )
	{
		return EnumChatFormatting.AQUA.toString();
	}
	
	@Override
	public int getIconColor( int pass )
	{
		return ( pass == 0 ) ? 0x000000FF : 0x000273FF;
	}
	
	@Override
	public boolean canApplyTo( EquipmentItem item )
	{
		return ( item == ComponentEquipment.items.boots );
	}
	
	@Override
	public int getMaxLevel()
	{
		return 1;
	}
	
	private Random rand = new Random();
	private Map< EntityLivingBase, Integer > prevs = new HashMap< EntityLivingBase, Integer >();
}
