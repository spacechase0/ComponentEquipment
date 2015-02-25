package com.spacechase0.minecraft.componentequipment.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import com.spacechase0.minecraft.componentequipment.tileentity.PersistiumInfuserTileEntity;
import com.spacechase0.minecraft.spacecore.inventory.CustomSmelterContainer;
import com.spacechase0.minecraft.spacecore.inventory.ValidatingSlot;

public class PersistiumInfuserContainer extends CustomSmelterContainer
{
	public PersistiumInfuserContainer( InventoryPlayer player, PersistiumInfuserTileEntity theInfuser )
	{
		super( theInfuser );
		infuser = theInfuser;
		
		addSlotToContainer( new ValidatingSlot( infuser, 0, 8, 17 ) ); // Block of Persistium
		addSlotToContainer( new ValidatingSlot( infuser, 1, 8, 53 ) ); // Fuel
		addSlotToContainer( new ValidatingSlot( infuser, 2, 150, 35 ) ); // Tool
		bindPlayerInventory( player );
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
    		
    		if ( slot < 3 )
    		{
    			if ( !mergeItemStack( stackInSlot, 3, 3 + ( InventoryPlayer.getHotbarSize() * 4 ), true ) )
    			{
    				return null;
    			}
    		}
    		else
    		{
    			for ( int i = 0; i < 3; ++i )
    			{
    				if ( infuser.isItemValidForSlot( i, stackInSlot ) )
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
    		
    		slotObj.onPickupFromSlot( player, stackInSlot );
    	}
    	
    	return stack;
    }
	
	private final PersistiumInfuserTileEntity infuser;
}
