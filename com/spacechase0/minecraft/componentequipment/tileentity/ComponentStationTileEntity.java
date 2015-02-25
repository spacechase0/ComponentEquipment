package com.spacechase0.minecraft.componentequipment.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

import com.spacechase0.minecraft.componentequipment.ComponentEquipment;
import com.spacechase0.minecraft.componentequipment.tool.Material;
import com.spacechase0.minecraft.componentequipment.tool.MaterialData;

public class ComponentStationTileEntity extends TileEntity implements IInventory
{
	@Override
	public void updateEntity()
	{
		updateSlots();
	}
	
	@Override
	public int getSizeInventory()
	{
		return 16;
	}

	@Override
	public ItemStack getStackInSlot( int slot )
	{
		return stacks[ slot ];
	}

	@Override
	public ItemStack decrStackSize( int slot, int amt )
	{
		ItemStack stack = getStackInSlot( slot );
		if ( stack == null )
		{
			return null;
		}
		
		ItemStack ret = stack.copy();
		ret.stackSize = Math.min( amt, stack.stackSize );
		
		stack.stackSize -= ret.stackSize;

		/*
		if ( slot % 2 == 1 && amt > 0 )
		{
			decrStackSize( slot - 1, 1 );
		}
		//*/
		
		if ( stack.stackSize <= 0 )
		{
			setInventorySlotContents( slot, null );
		}
		
		markDirty(); // onInventoryChanged?
		worldObj.markBlockForUpdate( xCoord, yCoord, zCoord );
		
		return ret;
	}

	@Override
	public ItemStack getStackInSlotOnClosing( int slot )
	{
		return null;
	}

	@Override
	public void setInventorySlotContents( int slot, ItemStack stack )
	{
		if ( isItemValidForSlot( slot, stack ) )
		{
			ItemStack old = getStackInSlot( slot );
			stacks[ slot ] = stack;
		}
	}
	
	@Override
	public boolean isItemValidForSlot( int i, ItemStack item )
	{
		if ( item == null )
		{
			return true;
		}
		
		// TO DO

		return true;
	}
	
	@Override
	public String getInventoryName()
	{
		return "Component Station";
	}

	@Override
	public boolean hasCustomInventoryName()
	{
		return false;
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 1;
	}

	@Override
	public boolean isUseableByPlayer( EntityPlayer player )
	{
		// Copied from chest code :P
		return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : player.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 64.0D;
	}

	@Override
	public void openInventory()
	{
	}

	@Override
	public void closeInventory()
	{
	}
	
    @Override
    public void readFromNBT( NBTTagCompound tag )
    {
		super.readFromNBT( tag );
		
		for ( int i = 0; i < stacks.length; ++i )
		{
			stacks[ i ] = ItemStack.loadItemStackFromNBT( ( NBTTagCompound ) tag.getTag( "Item" + i ) );
		}
    }

    @Override
    public void writeToNBT( NBTTagCompound tag )
    {
		super.writeToNBT( tag );
		
		for ( int i = 0; i < stacks.length; ++i )
		{
			NBTTagCompound itemTag = new NBTTagCompound();
			if ( stacks[ i ] != null )
			{
				stacks[ i ].writeToNBT( itemTag );
			}
			tag.setTag( "Item" + i, itemTag );
		}
    }
    
    @Override
    public Packet getDescriptionPacket()
    {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        this.writeToNBT(nbttagcompound);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, -1, nbttagcompound);
    }
    
    @Override
    public void onDataPacket( NetworkManager net, S35PacketUpdateTileEntity pkt )
    {
    	if ( worldObj.isRemote )
    	{
    		readFromNBT( pkt.func_148857_g() );
    	}
    }
    
    private void updateSlots()
    {
    	if ( updating /*|| worldObj.isRemote*/ )
    	{
    		return;
    	}
    	updating = true;
    	
    	/*
    	if ( slot % 2 == 1 && old != null && old.stackSize != 0 && curr == null )
    	{
    		decrStackSize( slot - 1, 1 );
    	}
    	*/
    	
    	for ( int i = 0; i < 8/*14*/; ++i )
    	{
			ItemStack item = getStackInSlot( i * 2 );
    		if ( item != null )
    		{
    			// Why did I have to reverse the i checks?
				/*if ( ( item.stackSize == 1 && i >= 8 ) || ( item.stackSize == 2 && i < 8 ) )
				{
					continue;
				}*/
				
    			String part = null;
    			int count = 1;
    			switch ( i )
    			{
					case  0: part = "handle"; count = 2; break;
					case  1: part = "binding"; count = 2; break;
					case  2: part = "pickaxeHead"; break;
					case  3: part = "axeHead"; break;
					case  4: part = "shovelHead"; break;
					case  5: part = "hoeHead"; break;
					case  6: part = "swordHead"; break;
					case  7: part = "bowHead"; break;
					/*
					case  8: part = "helm"; break;
					case  9: part = "chest"; break;
					case 10: part = "arm"; break;
					case 11: part = "belt"; break;
					case 12: part = "leg"; break;
					case 13: part = "boot"; break;*/
    			}
    			
    			String mat = null;
    			String[] mats = Material.getTypes();
    			for ( int im = 0; im < mats.length; ++im )
    			{
    				MaterialData data = Material.getData( mats[ im ] );
    				if ( data.canRepairWith( item ) )
    				{
    					mat = mats[ im ];
    				}
    			}
    			
    			if ( part != null && mat != null && count > 0 )
    			{
    				ItemStack outCurr = getStackInSlot( ( i * 2 ) + 1 );
    				if ( outCurr != null/* && outCurr.stackSize != count*/ )
    				{
    					continue;
    				}
    				
    				setInventorySlotContents( ( i * 2 ), null );
    				
					NBTTagCompound tag = new NBTTagCompound();
					tag.setString( "Part", part );
					tag.setString( "Material", mat );
	    			
	    			ItemStack out = new ItemStack( ComponentEquipment.items.part, count, 0 );
	    			out.setTagCompound( tag );
	    			setInventorySlotContents( ( i * 2 ) + 1, out );
    			}
    		}
    		else
    		{
    			//setInventorySlotContents( ( i * 2 ) + 1, null );
    		}
    	}
    	
    	updating = false;
    }
    
    private ItemStack[] stacks = new ItemStack[ 8/*14*/ * 2 ];
    private boolean updating = false;
}
