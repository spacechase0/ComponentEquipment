package com.spacechase0.minecraft.componentequipment.inventory;

import net.minecraft.item.ItemStack;

import com.spacechase0.minecraft.componentequipment.item.ArrowItem;
import com.spacechase0.minecraft.componentequipment.item.QuiverItem;
import com.spacechase0.minecraft.spacecore.inventory.ItemStackInventory;

public class QuiverInventory extends ItemStackInventory
{
	public QuiverInventory( ItemStack theInvStack )
	{
		super( theInvStack, QuiverItem.TOTAL_SIZE );
		//for(int i=0;i<100;++i)System.out.println("BROKEN,FIXME");
	}

	@Override
	public ItemStack decrStackSize( int slot, int amt )
	{
		ItemStack result = super.decrStackSize( slot, amt );
		if ( slot >= QuiverItem.SELECTION_SIZE )
		{
			return result;
		}
		
		//*
		ItemStack curr = getStackInSlot( slot );
		if ( curr != null && curr.stackSize < 64 )
		{
			int diff = 64 - curr.stackSize;
			for ( int i = QuiverItem.SELECTION_SIZE; i < QuiverItem.TOTAL_SIZE; ++i )
			{
				ItemStack here = getStackInSlot( i );
				if ( here == null )
				{
					continue;
				}
				
				if ( curr.getItem() == here.getItem() && curr.getItemDamage() == here.getItemDamage() && curr.getTagCompound().equals( here.getTagCompound() ) )
				{
					ItemStack extra = decrStackSize( i, diff );
					curr.stackSize += extra.stackSize;
					break;
				}
			}
			markDirty(); // onInventoryChanged ?
		}
		//*/
		
		return result;
	}

	@Override
	public String getInventoryName()
	{
		return "quiver";
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
		
		return ( stack.getItem() instanceof ArrowItem );
	}
}
