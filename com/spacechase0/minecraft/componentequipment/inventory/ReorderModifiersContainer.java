package com.spacechase0.minecraft.componentequipment.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import com.spacechase0.minecraft.componentequipment.item.EquipmentItem;
import com.spacechase0.minecraft.spacecore.inventory.DummyContainer;
import com.spacechase0.minecraft.spacecore.inventory.OutputSlot;
import com.spacechase0.minecraft.spacecore.inventory.ValidatingSlot;
import com.spacechase0.minecraft.spacecore.util.INotifiable;

import cpw.mods.fml.common.Loader;

public class ReorderModifiersContainer extends DummyContainer
{
	public ReorderModifiersContainer( InventoryPlayer player )
	{
		super( 2 );
		inventorySlots.clear();
		inventoryItemStacks.clear();
		
		addSlotToContainer( new ValidatingSlot( this, 0, 8, 32 ) );
		addSlotToContainer( new OutputSlot( this, 1, 152, 32 ) );
		bindPlayerInventory( player );
	}
	
	public void pleaseNotify( INotifiable obj )
	{
		toNotify = obj;
	}
	
	@Override
	public boolean isItemValidForSlot( int slot, ItemStack stack )
	{
		if ( stack == null )
		{
			return true;
		}
		
		boolean validFirstSlot = stack.getItem() instanceof EquipmentItem;
		try
		{
			if ( Loader.isModLoaded( "TConstruct" ) )
			{
				Class c = Class.forName( "tconstruct.library.tools.ToolCore" );
				validFirstSlot = validFirstSlot || c.isAssignableFrom( stack.getItem().getClass() );
			}
		}
		catch ( Exception exception )
		{
		}
		
		return ( ( slot == 0 ) ? validFirstSlot : true );
	}
    
    @Override
    public void onCraftMatrixChanged( IInventory inv )
    {
        super.onCraftMatrixChanged( inv );
        
        if ( inv == this )
        {
        	updateOutputSlot();
        	prevStacks = stacks.clone();
        	
        	if ( toNotify != null )
        	{
        		toNotify.notify( this );
        	}
        }
    }
	
	@Override
    public void onContainerClosed( EntityPlayer player )
    {
        super.onContainerClosed( player );

        if ( !player.worldObj.isRemote )
        {
        	if ( stacks[ 0 ] != null )
        	{
        		player.entityDropItem( stacks[ 0 ], 0 );
        	}
        }
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
    		
    		if ( slot < 2 )
    		{
    			if ( !mergeItemStack( stackInSlot, 2, 2 + ( InventoryPlayer.getHotbarSize() * 4 ), true ) )
    			{
    				return null;
    			}
    		}
    		else
    		{
    			for ( int i = 0; i < 1; ++i )
    			{
    				if ( isItemValidForSlot( i, stackInSlot ) )
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
    
    private void updateOutputSlot()
    {
    	if ( stacks[ 0 ] == prevStacks[ 0 ] && stacks[ 1 ] != prevStacks[ 1 ] )
    	{
    		// If left is the same, and right is different
    		if ( stacks[ 1 ] == null )
    		{
    			stacks[ 0 ] = null;
    		}
    	}
    	else if ( stacks[ 0 ] != prevStacks[ 0 ] && stacks[ 1 ] == prevStacks[ 1 ] )
    	{
    		// If left is different, and right is same
    		stacks[ 1 ] = ( stacks[ 0 ] == null ) ? null : stacks[ 0 ].copy();
    	}
    }
    
    private ItemStack prevStacks[] = new ItemStack[ 2 ];
    private INotifiable toNotify;
}
