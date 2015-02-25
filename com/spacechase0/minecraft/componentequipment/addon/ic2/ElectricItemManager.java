package com.spacechase0.minecraft.componentequipment.addon.ic2;

import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItemManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

import com.spacechase0.minecraft.componentequipment.item.EquipmentItem;

public class ElectricItemManager implements IElectricItemManager
{
	@Override
	public double charge( ItemStack stack, double amt, int tier, boolean ignoreTransferLimit, boolean simulate )
	{
		EquipmentItem armor = ( EquipmentItem ) stack.getItem();
		
		if ( tier < armor.getTier( stack ) )
		{
			return 0;
		}
		
		double max = ignoreTransferLimit ? Integer.MAX_VALUE : armor.getTransferLimit( stack );
		
		double toCharge = Math.min( amt, Math.min( armor.getMaxCharge( stack ) - getCharge( stack ), max ) );
		if ( !simulate )
		{
			stack.getTagCompound().setDouble( EquipmentItem.CHARGE_EU, getCharge( stack ) + toCharge );
		}
		
		return toCharge;
	}

	@Override
	public double discharge( ItemStack stack, double amt, int tier, boolean ignoreTransferLimit, boolean externally, boolean simulate )
	{
		EquipmentItem armor = ( EquipmentItem ) stack.getItem();
		
		if ( tier < armor.getTier( stack ) )
		{
			return 0;
		}
		
		double max = ignoreTransferLimit ? Integer.MAX_VALUE : armor.getTransferLimit( stack );
		
		double toDischarge = Math.min( amt, Math.min( getCharge( stack ), max ) );
		if ( !simulate )
		{
			stack.getTagCompound().setDouble( EquipmentItem.CHARGE_EU, getCharge( stack ) - toDischarge );
		}
		
		return toDischarge;
	}

	@Override
	public double getCharge( ItemStack stack )
	{
		return stack.getTagCompound().getDouble( EquipmentItem.CHARGE_EU );
	}

	@Override
	public boolean canUse( ItemStack stack, double amt )
	{
		return ElectricItem.rawManager.canUse( stack, amt );
	}

	@Override
	public boolean use( ItemStack stack, double amt, EntityLivingBase entity )
	{
		return ElectricItem.rawManager.use( stack, amt, entity );
	}

	@Override
	public void chargeFromArmor( ItemStack stack, EntityLivingBase entity )
	{
		ElectricItem.rawManager.chargeFromArmor( stack, entity );
	}

	@Override
	public String getToolTip( ItemStack stack )
	{
		return ElectricItem.rawManager.getToolTip( stack );
	}
}
