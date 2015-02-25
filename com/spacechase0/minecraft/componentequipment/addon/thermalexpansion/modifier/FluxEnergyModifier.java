package com.spacechase0.minecraft.componentequipment.addon.thermalexpansion.modifier;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import com.spacechase0.minecraft.componentequipment.item.EquipmentItem;
import com.spacechase0.minecraft.componentequipment.tool.Modifier;

public class FluxEnergyModifier extends Modifier
{
	public FluxEnergyModifier()
	{
		super( "fluxEnergy" );
	}
	
	@Override
	public String getName( ItemStack stack )
	{
		EquipmentItem item = ( EquipmentItem ) stack.getItem();
		return StatCollector.translateToLocal( "componentequipment:modifier.fluxEnergy.name" ) + " " + StatCollector.translateToLocal( "enchantment.level." + item.equipment.getModifierLevel( stack, type ) );
	}
	
	@Override
	public void addInformation( ItemStack stack, List list )
	{
		EquipmentItem item = ( EquipmentItem ) stack.getItem();
		list.add( "     " + StatCollector.translateToLocalFormatted( "componentequipment:modifier.fluxEnergy.charge", item.getEnergyStored( stack ), item.getMaxEnergyStored( stack ) ) );
		list.add( "     " + StatCollector.translateToLocalFormatted( "componentequipment:modifier.fluxEnergy.cost", ( int )( 50 / item.equipment.getRepairMultiplier( stack ) ) ) );
	}
	
	@Override
	public String getFormat( ItemStack stack )
	{
		return EnumChatFormatting.WHITE.toString();
	}
	
	@Override
	public int getIconColor( int level, int pass )
	{
		if ( pass == 0 ) return 0x00FF2929;
		
		switch ( level )
		{
			case 1: return 0x00E9BA62;
			case 2: return 0x008897C0;
			case 3: return 0x00CCD1CF;
			case 4: return 0x00F7F694;
			case 5: return 0x00409494;
		}
		
		return 0;
	}

	@Override
	public int getMaxLevel()
	{
		return 5;
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
				int canDrain = item.extractEnergy( stack, perPoint * damage, true );
				int willDrain = canDrain / perPoint;
				if ( willDrain > 0 )
				{
					item.extractEnergy( stack, willDrain * perPoint, false );
					item.damageItemStack( null, stack, -willDrain );
				}
			}
			EquipmentItem.ignoreEnergyRate = false;
		}
		
		// TODO: Charge tools if chestplate like IC2?
		// TODO: Remove modifier if potato capacitor and give user a baked potato?
		//       Also disallow charging, if so
	}
	
	@Override
	public void addDisassemblyResults( ItemStack stack, List< ItemStack > results )
	{
		ItemStack result = getDisassembledSelf( stack );
		if ( stack.getTagCompound().getTag( EquipmentItem.CHARGE_RF ) != null )
		{
			NBTTagCompound tag = new NBTTagCompound();
			tag.setTag( EquipmentItem.CHARGE_RF, stack.getTagCompound().getTag( EquipmentItem.CHARGE_RF ) );
			result.getTagCompound().setTag( "Data", tag );
		}
		
		results.add( result );
	}
}
