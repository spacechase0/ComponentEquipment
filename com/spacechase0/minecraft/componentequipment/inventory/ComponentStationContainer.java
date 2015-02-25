package com.spacechase0.minecraft.componentequipment.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import com.spacechase0.minecraft.componentequipment.tileentity.ComponentStationTileEntity;
import com.spacechase0.minecraft.spacecore.inventory.OutputSlot;
import com.spacechase0.minecraft.spacecore.inventory.ValidatingSlot;

public class ComponentStationContainer extends Container
{
	public ComponentStationContainer( InventoryPlayer player, ComponentStationTileEntity theStation )
	{
		station = theStation;
		
		for ( int i = 0; i < 8/*14*/; ++i )
		{
			int x = 0, y = 0;
			if ( i < 8 )
			{
				x = 10 + ( i * 20 );
				y =  26;//8;
			}
			/*else
			{
				x = 28 + ( ( i - 8 ) * 22 );
				y = 44;
			}*/
			
			addSlotToContainer( new ValidatingSlot( station, ( i * 2 ), x, y ) );
			addSlotToContainer( new OutputSlot( station, ( i * 2 ) + 1, x, y + 18 ) );
		}
		bindPlayerInventory( player );
	}
	
	@Override
	public boolean canInteractWith( EntityPlayer player )
	{
		return station.isUseableByPlayer( player );
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
    		
    		if ( slot < 28 )
    		{
    			// Temporary fix, no shift-clicking :P
    			//if ( true ) { return null; }
    			if ( !mergeItemStack( stackInSlot, 28, 28 + ( InventoryPlayer.getHotbarSize() * 4 ), true ) )
    			{
    				return null;
    			}
    		}
    		else
    		{
    			for ( int i = 0; i < 28; ++i )
    			{
    				if ( station.isItemValidForSlot( i, stackInSlot ) )
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
	
	private final ComponentStationTileEntity station;
}
