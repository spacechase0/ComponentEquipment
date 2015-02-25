package com.spacechase0.minecraft.componentequipment.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import com.spacechase0.minecraft.componentequipment.tileentity.ModificationStandTileEntity;
import com.spacechase0.minecraft.spacecore.inventory.ValidatingSlot;

public class ModificationStandContainer extends Container
{
	public ModificationStandContainer( InventoryPlayer player, ModificationStandTileEntity theStand )
	{
		stand = theStand;

		addSlotToContainer( new ValidatingSlot( stand, 0, 80, 25 ) );
		bindPlayerInventory( player );
	}
	
	@Override
	public boolean canInteractWith( EntityPlayer player )
	{
		return stand.isUseableByPlayer( player );
	}
	
    @Override
    public ItemStack transferStackInSlot( EntityPlayer player, int slot )
    {
    	ItemStack stack = null;
    	Slot slotObj = ( Slot ) inventorySlots.get( slot );
    	
    	if ( slotObj != null && slotObj.getHasStack() )
    	{
    		ItemStack stackInSlot = slotObj.getStack();
    		stack = stackInSlot.copy();
    		
    		if ( slot < 1 )
    		{
    			if ( !mergeItemStack( stackInSlot, 1, 1 + ( InventoryPlayer.getHotbarSize() * 4 ), true ) )
    			{
    				return null;
    			}
    		}
    		else
    		{
    			for ( int i = 0; i < 1; ++i )
    			{
    				if ( stand.isItemValidForSlot( i, stackInSlot ) )
	        		{
	        			if ( !mergeItemStack( stackInSlot, i, i + 1, false ) )
	        			{
	        				return null;
	        			}
	        		}
    			}
    		}
    		
    		if ( stackInSlot.stackSize == 0 )
    		{
    			slotObj.putStack( null );
    		}
    		else
    		{
    			slotObj.onSlotChanged();
    		}
    		
    		if ( stackInSlot.stackSize == stack.stackSize )
    		{
    			return null;
    		}
    		
    		slotObj.onPickupFromSlot( player,  stackInSlot );
    	}
    	
    	return stack;
    }
    protected void bindPlayerInventory( InventoryPlayer player )
    {
		for (int iy = 0; iy < 3; iy++)
		{
			for (int ix = 0; ix < InventoryPlayer.getHotbarSize(); ix++)
			{
				addSlotToContainer( new Slot( player, ix + iy * InventoryPlayer.getHotbarSize() + InventoryPlayer.getHotbarSize(), 8 + ix * 18, 84 + iy * 18 ) );
			}
		}
		
		for (int ix = 0; ix < InventoryPlayer.getHotbarSize(); ix++)
		{
			addSlotToContainer( new Slot( player, ix, 8 + ix * 18, 142 ) );
		}
	}
	
	private final ModificationStandTileEntity stand;
}
