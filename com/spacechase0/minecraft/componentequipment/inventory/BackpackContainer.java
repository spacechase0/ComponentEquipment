package com.spacechase0.minecraft.componentequipment.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import com.spacechase0.minecraft.componentequipment.tool.Armor;
import com.spacechase0.minecraft.componentequipment.tool.Modifier;
import com.spacechase0.minecraft.componentequipment.tool.modifier.BackpackModifier;
import com.spacechase0.minecraft.spacecore.inventory.ItemStackContainer;
import com.spacechase0.minecraft.spacecore.inventory.ItemStackInventory;

public class BackpackContainer extends ItemStackContainer
{
	public BackpackContainer( InventoryPlayer thePlayer, ItemStack theChestplate )
	{
		super( thePlayer );
		
		chestplate = theChestplate;
		inv = new BackpackInventory( chestplate );

		BackpackModifier mod = ( BackpackModifier ) Modifier.getModifier( "chestBackpack" );
		int level = Armor.instance.getModifierLevel( chestplate, "chestBackpack" );

		int xBase = ( level == 1 ) ?  8 : 12;
		int yBase = ( level == 1 ) ? 18 : 8;
		int cols = ( ( level == 4 ) ? 12 : 9 );
        for ( int ir = 0; ir < mod.getSlotCount( level ) / cols; ++ir )
        {
            for ( int ic = 0; ic < cols; ++ic )
            {
                addSlotToContainer( new Slot( inv, ic + ir * cols, xBase + ic * 18, yBase + ir * 18 ) );
            }
        }
        if ( level > 1 )
        {
        	if ( level == 4 ) xBase += 18 * 1.5;
        	
        	Slot last = ( Slot ) inventorySlots.toArray()[ inventorySlots.size() - 1 ];
        	bindPlayerInventory( player, xBase, last.yDisplayPosition + 18 + 4 );
        }
        else
        {
        	bindPlayerInventory( player, xBase, 84 );
        }
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
    		
    		if ( slot < 27 )
    		{
    			if ( !mergeItemStack( stackInSlot, 27, 27 + ( InventoryPlayer.getHotbarSize() * 4 ), true ) )
    			{
    				return null;
    			}
    		}
    		else if ( stackInSlot != null )
    		{
    			if ( !mergeItemStack( stackInSlot, 0, 27, false ) )
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
    
    protected void bindPlayerInventory( InventoryPlayer player, int xBase, int h )
    {
		for (int iy = 0; iy < 3; iy++)
		{
			for (int ix = 0; ix < InventoryPlayer.getHotbarSize(); ix++)
			{
				addSlotToContainer( new Slot( player, ix + iy * InventoryPlayer.getHotbarSize() + InventoryPlayer.getHotbarSize(), xBase + ix * 18, h + iy * 18 ) );
			}
		}
		
		for (int ix = 0; ix < InventoryPlayer.getHotbarSize(); ix++)
		{
			addSlotToContainer( new Slot( player, ix, xBase + ix * 18, h + 58 ) );
		}
	}
    
	private final ItemStack chestplate;
	private final ItemStackInventory inv;
}
