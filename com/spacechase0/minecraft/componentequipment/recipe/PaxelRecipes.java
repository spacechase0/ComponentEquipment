package com.spacechase0.minecraft.componentequipment.recipe;

import java.util.ArrayList;
import java.util.List;

import static net.minecraft.init.Blocks.*;
import static net.minecraft.init.Items.*;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;

import com.spacechase0.minecraft.componentequipment.ComponentEquipment;
import com.spacechase0.minecraft.componentequipment.item.HarvesterToolItem;
import com.spacechase0.minecraft.componentequipment.item.ToolItem;
import com.spacechase0.minecraft.componentequipment.tool.Tool;
import com.spacechase0.minecraft.componentequipment.tool.ToolData;

public class PaxelRecipes implements IRecipe
{
	public PaxelRecipes()
	{
	}
	
	@Override
	public boolean matches( InventoryCrafting inv, World world )
	{
    	return ( doCheck( inv ) != null );
	}

	@Override
	public ItemStack getCraftingResult( InventoryCrafting inv )
	{
    	ItemStack item = doCheck( inv );
    	if ( item == null )
    	{
    		return null;
    	}
    	
    	return item;
	}

	@Override
	public int getRecipeSize()
	{
		return 4;
	}

	@Override
	public ItemStack getRecipeOutput()
	{
		return null;
	}
    
    private ItemStack doCheck( InventoryCrafting inv )
    {
    	boolean hasNetherStar = false;
    	ItemStack pick = null, shovel = null, axe = null;
    	for ( int is = 0; is < inv.getSizeInventory(); ++is )
    	{
    		ItemStack stack = inv.getStackInSlot( is );
    		if ( stack == null )
    		{
    			continue;
    		}
    		
    		if ( stack.getItem() instanceof HarvesterToolItem )
    		{
    			HarvesterToolItem tool = ( HarvesterToolItem ) stack.getItem();
    			if ( tool.type.equals( "pickaxe" ) && pick == null )
    			{
    				pick = stack;
    			}
    			else if ( tool.type.equals( "shovel" ) && shovel == null )
    			{
    				shovel = stack;
    			}
    			else if ( tool.type.equals( "axe" ) && axe == null )
    			{
    				axe = stack;
    			}
    			else
    			{
    				return null;
    			}
    		}
    		else if ( stack.getItem().equals( nether_star ) && !hasNetherStar )
    		{
    			hasNetherStar = true;
    		}
    		else
    		{
    			return null;
    		}
    	}
    	
    	if ( pick == null || shovel == null || axe == null || !hasNetherStar )
    	{
    		//System.out.println( "Incomplete recipe." );
    		//Thread.dumpStack();
    		return null;
    	}
    	
    	NBTTagCompound[] data = new NBTTagCompound[] { pick.getTagCompound(), shovel.getTagCompound(), axe.getTagCompound() };

    	String prevHead = null, prevBinding = null, prevHandle = null;
    	NBTTagList prevMods = null;
    	for ( int i = 0; i < data.length; ++i )
    	{
    		NBTTagCompound tag = data[ i ];
    		String head = tag.getString( "head" );
    		String binding = tag.getString( "binding" );
    		String handle = tag.getString( "handle" );
    		NBTTagList mods = tag.getTagList( "Modifiers", 10 );
    		
    		if ( prevHead != null )
    		{
    			if ( !prevHead.equals( head ) || !prevBinding.equals( binding ) || !prevHandle.equals( handle ) || !prevMods.equals( mods ) )
    			{
    				return null;
    			}
    		}
    		
    		prevHead = head;
    		prevBinding = binding;
    		prevHandle = handle;
    		prevMods = mods;
    	}
    	
    	ItemStack paxel = new ItemStack( ComponentEquipment.items.paxel, 1, 0 );
    	paxel.setTagCompound( ( NBTTagCompound ) data[ 0 ].copy() );
    	
    	return paxel;
    }
}
