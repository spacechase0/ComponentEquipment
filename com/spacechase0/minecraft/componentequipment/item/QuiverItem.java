package com.spacechase0.minecraft.componentequipment.item;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.spacechase0.minecraft.componentequipment.ComponentEquipment;
import com.spacechase0.minecraft.componentequipment.inventory.QuiverInventory;

public class QuiverItem extends SimpleItem
{
	public QuiverItem()
	{
		super( "quiver" );
		
		setMaxStackSize( 1 );
	}
	
	@Override
    public ItemStack onItemRightClick( ItemStack stack, World world, EntityPlayer player )
    {
		if ( !world.isRemote )
		{
	        player.openGui( ComponentEquipment.instance, ComponentEquipment.QUIVER_GUI_ID, world, ( int ) player.posX, ( int ) player.posY, ( int ) player.posZ );
		}
		
        return stack;
    }
	
	public static ItemStack findFirstQuiver( IInventory inv )
	{
		for ( int i = 0; i < inv.getSizeInventory(); ++i )
		{
			ItemStack stack = inv.getStackInSlot( i );
			if ( stack == null )
			{
				continue;
			}
			
			if ( stack.getItem() == ComponentEquipment.items.quiver )
			{
				return stack;
			}
		}
		
		return null;
	}
	
	public static IInventory getInventoryOf( ItemStack stack )
	{
		if ( stack == null || stack.getItem() != ComponentEquipment.items.quiver )
		{
			return null;
		}
		
		return new QuiverInventory( stack );
	}
	
	public static int getSelected( EntityPlayer player )
	{
		Integer result = selecteds.get( player );
		return ( result == null ) ? 0 : result;
	}
	
	public static void setSelected( EntityPlayer player, int selected )
	{
		selecteds.put( player, selected );
	}
	
	public static final int SELECTION_SIZE = 3;
	public static final int TOTAL_SIZE = 9;
	
	private static final Map< EntityPlayer, Integer > selecteds = new HashMap< EntityPlayer, Integer >();
}
