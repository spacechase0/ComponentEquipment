package com.spacechase0.minecraft.componentequipment.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import com.spacechase0.minecraft.componentequipment.item.ArrowItem;
import com.spacechase0.minecraft.componentequipment.item.QuiverItem;
import com.spacechase0.minecraft.spacecore.inventory.ItemStackContainer;
import com.spacechase0.minecraft.spacecore.inventory.ItemStackInventory;
import com.spacechase0.minecraft.spacecore.inventory.StaticSlot;
import com.spacechase0.minecraft.spacecore.inventory.ValidatingSlot;

public class QuiverContainer extends ItemStackContainer
{
	public QuiverContainer( InventoryPlayer thePlayer, ItemStack theQuiver )
	{
		super( thePlayer );
		
		quiver = theQuiver;
		inv = new QuiverInventory( quiver );
		
		for ( int i = 0; i < QuiverItem.TOTAL_SIZE; ++i )
		{
			int x = 62 + ( ( i % QuiverItem.SELECTION_SIZE ) * 18 );
			int y = 16 + ( ( i / QuiverItem.SELECTION_SIZE ) * 18 );
			
			int is = QuiverItem.TOTAL_SIZE - i - 1;
			if ( is < QuiverItem.SELECTION_SIZE )
			{
				y += 2;
			}
			
			addSlotToContainer( new ValidatingSlot( inv, is, x, y ) );
		}
		bindPlayerInventory( player );
	}
	
	@Override
    public void onContainerClosed( EntityPlayer player )
	{
		// Um.... Why do I have to do this?
		getItemInventory().markDirty(); // onInventoryChanged?
		player.inventory.mainInventory[ player.inventory.currentItem ] = getItemInventory().invStack;
	}
	
	@Override
	protected ItemStackInventory getItemInventory()
	{
		return inv;
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
    		
    		if ( slot < QuiverItem.TOTAL_SIZE )
    		{
    			if ( !mergeItemStack( stackInSlot, QuiverItem.TOTAL_SIZE, QuiverItem.TOTAL_SIZE + ( InventoryPlayer.getHotbarSize() * 4 ), true ) )
    			{
    				return null;
    			}
    		}
    		else if ( stackInSlot != null && stackInSlot.getItem() instanceof ArrowItem )
    		{
    			if ( !mergeItemStack( stackInSlot, 0, QuiverItem.TOTAL_SIZE, false ) )
    			{
    				return null;
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
			if ( ix == player.currentItem )
			{
				addSlotToContainer( new StaticSlot( player, ix, 8 + ix * 18, 142 ) );
				continue;
			}
			
			addSlotToContainer( new Slot( player, ix, 8 + ix * 18, 142 ) );
		}
	}
    
	private final ItemStack quiver;
	private final ItemStackInventory inv;
}
