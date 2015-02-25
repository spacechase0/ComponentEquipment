package com.spacechase0.minecraft.componentequipment.recipe;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import com.spacechase0.minecraft.componentequipment.ComponentEquipment;
import com.spacechase0.minecraft.componentequipment.item.ArmorItem;
import com.spacechase0.minecraft.componentequipment.item.EquipmentItem;
import com.spacechase0.minecraft.componentequipment.tool.Armor;
import com.spacechase0.minecraft.componentequipment.tool.ArmorData;
import com.spacechase0.minecraft.componentequipment.tool.Equipment;

public class CaseToolRecipes implements IRecipe
{
	public CaseToolRecipes()
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
		return 2;
	}

	@Override
	public ItemStack getRecipeOutput()
	{
		return null;
	}
    
    private ItemStack doCheck( InventoryCrafting inv )
    {
    	boolean toolFirst = false;
    	ItemStack toolStack = null;
    	ItemStack casingPart = null;
        for ( int i = 0; i < inv.getSizeInventory(); ++i )
        {
            ItemStack stack = inv.getStackInSlot( i );
            if ( stack == null )
            {
            	continue;
            }
            
            if ( stack.getItem() instanceof EquipmentItem && toolStack == null )
            {
            	toolStack = stack;
            	if ( casingPart == null ) toolFirst = true;
            }
            else if ( stack.getItem() == ComponentEquipment.items.partCasing && casingPart == null )
            {
            	casingPart = stack;
            	if ( toolStack == null ) toolFirst = false;
            }
            else
            {
            	return null;
            }
        }
        
        if ( toolStack == null || casingPart == null )
        {
        	return null;
        }
        
        EquipmentItem toolItem = ( EquipmentItem ) toolStack.getItem();
        
        String realPart = casingPart.getTagCompound().getString( "Part" );
        if ( realPart.endsWith( "Head" ) && realPart.substring( 0, realPart.indexOf( "Head" ) ).equals( toolItem.type ) )
        {
        	// axeHead -> head, etc.
        	realPart = "head";
        }
        
        if ( !toolStack.getTagCompound().hasKey( realPart ) )
        {
        	realPart += toolFirst ? "Right" : "Left";
        	if ( !toolStack.getTagCompound().hasKey( realPart ) )
        	{
        		return null;
        	}
        }
        
        if ( toolItem.equipment.getCasing( toolStack, realPart ) != null )
        {
        	return null;
        }
        
        ItemStack stack = toolStack.copy();
        toolItem.equipment.setCasing( stack, realPart, casingPart.getTagCompound().getString( "Material" ) );
        
        return stack;
    }
}
