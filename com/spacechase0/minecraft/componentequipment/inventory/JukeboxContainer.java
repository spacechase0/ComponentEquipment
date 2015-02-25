package com.spacechase0.minecraft.componentequipment.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import com.spacechase0.minecraft.componentequipment.tool.Armor;
import com.spacechase0.minecraft.spacecore.inventory.ItemStackContainer;
import com.spacechase0.minecraft.spacecore.inventory.ItemStackInventory;
import com.spacechase0.minecraft.spacecore.inventory.ValidatingSlot;

public class JukeboxContainer extends ItemStackContainer
{
	public JukeboxContainer( InventoryPlayer thePlayer, ItemStack theHelmet )
	{
		super( thePlayer );
		
		helmet = theHelmet;
		inv = new JukeboxInventory( helmet );
		
		int level = Armor.instance.getModifierLevel( helmet, "portableJukebox" );

        for ( int ir = 0; ir < 3; ++ir )
        {
            for ( int ic = 0; ic < 3; ++ic )
            {
                addSlotToContainer( new ValidatingSlot( inv, ic + ir * 3, 61 + ic * 18, 17 + ir * 18 ) );
            }
        }
		bindPlayerInventory( player );
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
    		
    		if ( slot < 9 )
    		{
    			if ( !mergeItemStack( stackInSlot, 9, 9 + ( InventoryPlayer.getHotbarSize() * 4 ), true ) )
    			{
    				return null;
    			}
    		}
    		else if ( stackInSlot != null )
    		{
    			if ( !mergeItemStack( stackInSlot, 0, 9, false ) )
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
			addSlotToContainer( new Slot( player, ix, 8 + ix * 18, 142 ) );
		}
	}
    
	private final ItemStack helmet;
	private final ItemStackInventory inv;
}
