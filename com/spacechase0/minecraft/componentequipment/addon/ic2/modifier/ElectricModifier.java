package com.spacechase0.minecraft.componentequipment.addon.ic2.modifier;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import com.spacechase0.minecraft.componentequipment.item.EquipmentItem;
import com.spacechase0.minecraft.componentequipment.tool.Modifier;

public class ElectricModifier extends Modifier
{
	public ElectricModifier()
	{
		super( "electric" );
	}
	
	@Override
	public String getName( ItemStack stack )
	{
		EquipmentItem item = ( EquipmentItem ) stack.getItem();
		return StatCollector.translateToLocal( "componentequipment:modifier.electric.name" ) + " " + StatCollector.translateToLocal( "enchantment.level." + item.equipment.getModifierLevel( stack, type ) );
	}
	
	@Override
	public void addInformation( ItemStack stack, List list )
	{
		EquipmentItem item = ( EquipmentItem ) stack.getItem();
		list.add( "     " + StatCollector.translateToLocalFormatted( "componentequipment:modifier.electric.charge", item.getManager( stack ).getCharge( stack ), item.getMaxCharge( stack ) ) );
		list.add( "     " + StatCollector.translateToLocalFormatted( "componentequipment:modifier.electric.cost", ( int )( 50 / item.equipment.getRepairMultiplier( stack ) ) ) );
	}
	
	@Override
	public String getFormat( ItemStack stack )
	{
		return EnumChatFormatting.WHITE.toString();
	}
	
	@Override
	public int getIconColor( int level, int pass )
	{
		if ( pass == 0 ) return 0x00FFFF00;
		
		switch ( level )
		{
			case 1: return 0x00FF0000;
			case 2: return 0x0000007E;
			case 3: return 0x00FF8080;
			case 4: return 0x0000FF00;
		}
		
		return 0;
	}

	@Override
	public int getMaxLevel()
	{
		return 4;
	}
	
	@Override
	public boolean canApplyTo( EquipmentItem item )
	{
		return true;
	}
	
	@Override
	public void itemTick( Entity entity, ItemStack stack )
	{
		EquipmentItem item = ( EquipmentItem ) stack.getItem();
		
		int damage = item.getDamage( stack );
		if ( damage > 0 )
		{
			int perPoint = 50;
			perPoint /= item.equipment.getRepairMultiplier( stack );
			
			EquipmentItem.ignoreEnergyRate = true;
			{
				int canDrain = ( int ) item.getManager( stack ).discharge( stack, perPoint * damage, item.equipment.getModifierLevel( stack, type ), false, false, true );
				int willDrain = canDrain / perPoint;
				System.out.println(canDrain);
				if ( willDrain > 0 )
				{
					item.getManager( stack ).use( stack, willDrain * perPoint, ( EntityLivingBase ) entity ); //item.equipment.getModifierLevel( stack, type ), false, false );
					item.damageItemStack( null, stack, -willDrain );
				}
			}
			EquipmentItem.ignoreEnergyRate = false;
		}
	}
	
	@Override
	public void addDisassemblyResults( ItemStack stack, List< ItemStack > results )
	{
		ItemStack result = getDisassembledSelf( stack );
		if ( stack.getTagCompound().getTag( EquipmentItem.CHARGE_EU ) != null )
		{
			NBTTagCompound tag = new NBTTagCompound();
			tag.setTag( EquipmentItem.CHARGE_EU, stack.getTagCompound().getTag( EquipmentItem.CHARGE_EU ) );
			result.getTagCompound().setTag( "Data", tag );
		}
		
		results.add( result );
	}
}
