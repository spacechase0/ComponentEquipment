package com.spacechase0.minecraft.componentequipment.inventory;

import net.minecraft.item.ItemRecord;
import net.minecraft.item.ItemStack;

import com.spacechase0.minecraft.spacecore.inventory.ItemStackInventory;

public class JukeboxInventory extends ItemStackInventory
{
	public JukeboxInventory( ItemStack theInvStack )
	{
		super( theInvStack, 3 * 3 );
	}

	@Override
	public String getInventoryName()
	{
		return "gui.componentequipment:portableJukebox.name";
	}

	@Override
	public boolean hasCustomInventoryName()
	{
		return false;
	}

	@Override
	public boolean isItemValidForSlot( int slot, ItemStack stack )
	{
		if ( stack == null )
		{
			return true;
		}
		
		return ( stack.getItem() instanceof ItemRecord );
	}
}
