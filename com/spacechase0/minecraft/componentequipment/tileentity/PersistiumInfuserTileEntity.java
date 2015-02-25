package com.spacechase0.minecraft.componentequipment.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.AxisAlignedBB;

import com.spacechase0.minecraft.componentequipment.ComponentEquipment;
import com.spacechase0.minecraft.componentequipment.item.EquipmentItem;
import com.spacechase0.minecraft.componentequipment.item.IngotItem;
import com.spacechase0.minecraft.componentequipment.tool.modifier.PersistanceModifier;
import com.spacechase0.minecraft.spacecore.block.CustomSmelterBlock;
import com.spacechase0.minecraft.spacecore.tileentity.CustomSmelterTileEntity;

public class PersistiumInfuserTileEntity extends CustomSmelterTileEntity
{
	@Override
    public AxisAlignedBB getRenderBoundingBox()
    {
		return TileEntity.INFINITE_EXTENT_AABB;
    }
	
	@Override
	public int getSizeInventory()
	{
		return 3;
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
			stacks[ slot ] = stack;
		}
	}
	
	@Override
	public boolean isItemValidForSlot( int slot, ItemStack item )
	{
		if ( item == null )
		{
			return true;
		}
		
		if ( slot == FUEL_SLOT )
		{
			return ( TileEntityFurnace.getItemBurnTime( item ) > 0 );
		}
		else if ( slot == BLOCK_SLOT )
		{
			return ( item.getItem() == Item.getItemFromBlock( ComponentEquipment.blocks.ingot ) && item.getItemDamage() == IngotItem.PERSISTIUM );
		}
		else if ( slot == TOOL_SLOT )
		{
			return ( item.getItem() instanceof EquipmentItem );
		}

		return true;
	}
	
	@Override
	public String getInventoryName()
	{
		return "tile.persistiumInfuser.name";
	}

	@Override
	public boolean hasCustomInventoryName()
	{
		return false;
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 64;
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
		
		active = tag.getInteger( "Active" );
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
		
		tag.setInteger( "Active", active );
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

	@Override
	protected CustomSmelterBlock getBlock()
	{
		return ComponentEquipment.blocks.persistiumInfuser;
	}

	@Override
	public int getFuelSlot()
	{
		return 1;
	}

	@Override
	protected void updateProgressNeeded()
	{
		ItemStack toolStack = getStackInSlot( TOOL_SLOT );
		if ( getStackInSlot( BLOCK_SLOT ) == null || toolStack == null )
		{
			progressNeeded = 0;
			return;
		}
		EquipmentItem item = ( EquipmentItem ) toolStack.getItem();
		
		int level = item.equipment.getModifierLevel( toolStack, "persistance" );
		if ( level >= PersistanceModifier.ALL )
		{
			progressNeeded = 0;
			return;
		}
		
		if ( item.equipment.getModifiersRemaining( toolStack ) <= 0 && item.equipment.getModifierLevel( toolStack, "persistance" ) == 0 )
		{
			progressNeeded = 0;
			return;
		}
		
		if ( active <= 0 || active >= PersistanceModifier.ALL )
		{
			progressNeeded = 0;
			return;
		}
		
		progressNeeded = BASE_BURN_TIME;
	}

	@Override
	protected void recipeCompleted()
	{
		if ( active == -1 )
		{
			return;
		}
		
		decrStackSize( BLOCK_SLOT, 1 );
		
		ItemStack toolItem = getStackInSlot( TOOL_SLOT );
		EquipmentItem item = ( EquipmentItem ) toolItem.getItem();
		item.equipment.setModifierLevel( toolItem, "persistance", item.equipment.getModifierLevel( toolItem, "persistance" ) | active );
		
		active = -1;
	}
	
	// I can't figure out why, but otherwise it flickers constantly.
	@Override
	public boolean shouldRenderInPass( int pass )
	{
		return true;
	}
	
	public ItemStack[] stacks = new ItemStack[ 3 ];
	public int active = -1;
	
	public static final int BLOCK_SLOT = 0;
	public static final int FUEL_SLOT = 1;
	public static final int TOOL_SLOT = 2;
	public static final int BASE_BURN_TIME = 3000;
}
