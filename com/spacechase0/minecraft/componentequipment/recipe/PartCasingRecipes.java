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
import net.minecraft.world.World;

import com.spacechase0.minecraft.componentequipment.ComponentEquipment;
import com.spacechase0.minecraft.componentequipment.item.ArmorItem;
import com.spacechase0.minecraft.componentequipment.tool.Armor;
import com.spacechase0.minecraft.componentequipment.tool.ArmorData;

public class PartCasingRecipes implements IRecipe
{
	public PartCasingRecipes()
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
    	ItemStack paperPart = null;
    	ItemStack otherPart = null;
        for ( int i = 0; i < inv.getSizeInventory(); ++i )
        {
            ItemStack stack = inv.getStackInSlot( i );
            if ( stack == null )
            {
            	continue;
            }
            
            if ( stack.getItem() == ComponentEquipment.items.part && otherPart == null)
            {
            	otherPart = stack;
            }
            else if ( stack.getItem().equals( paper ) && paperPart == null )
            {
            	paperPart = stack;
            }
            else
            {
            	return null;
            }
        }
        
        if ( paperPart == null || otherPart == null )
        {
        	return null;
        }
        
        /*
        if ( !paperPart.getTagCompound().getString( "Part" ).equals( otherPart.getTagCompound().getString( "Part" ) ) )
        {
        	return null;
        }
        */
        
        ItemStack stack = new ItemStack( ComponentEquipment.items.partCasing, otherPart.stackSize, otherPart.getItemDamage() );
        stack.stackTagCompound = ( NBTTagCompound ) otherPart.stackTagCompound.copy();
        
        return stack;
    }
}
