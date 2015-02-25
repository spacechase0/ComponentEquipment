package com.spacechase0.minecraft.componentequipment.recipe;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import com.spacechase0.minecraft.componentequipment.ComponentEquipment;
import com.spacechase0.minecraft.componentequipment.item.ArmorItem;
import com.spacechase0.minecraft.componentequipment.tool.Armor;
import com.spacechase0.minecraft.componentequipment.tool.ArmorData;
import com.spacechase0.minecraft.componentequipment.tool.ArmorMaterialIndex;

public class ArmorRecipes implements IRecipe
{
	public ArmorRecipes( ArmorItem theArmor )
	{
		armor = theArmor;
		type = armor.type;
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
		return ( type.equals( "bow" ) ? 4 : 3);
	}

	@Override
	public ItemStack getRecipeOutput()
	{
		return null;
	}
    
    private ItemStack doCheck( InventoryCrafting inv )
    {
    	boolean foundStation = false;
    	ItemArmor vanArmor = null;
        for ( int i = 0; i < inv.getSizeInventory(); ++i )
        {
            ItemStack stack = inv.getStackInSlot( i );
            if ( stack == null )
            {
            	continue;
            }
            
            if ( stack.getItem() instanceof ItemArmor && stack.getItemDamage() == 0 && vanArmor == null )
            {
            	vanArmor = ( ItemArmor ) stack.getItem();
            	
            }
            else if ( stack.getItem() == Item.getItemFromBlock( ComponentEquipment.blocks.componentStation ) && !foundStation )
            {
            	foundStation = true;
            }
            else
            {
            	return null;
            }
        }
        
        if ( !foundStation || vanArmor == null ) return null;

        String armorType = null;
        switch ( vanArmor.armorType )
        {
	        case 0: armorType = "helmet"; break;
	        case 1: armorType = "chestplate"; break;
	        case 2: armorType = "leggings"; break;
	        case 3: armorType = "boots"; break;
        }

        ArmorData data = Armor.instance.getData( type );
        if ( !type.equals( armorType ) )
        {
        	return null;
        }
        
        String mat = ArmorMaterialIndex.get( vanArmor );
        if ( mat == null )
        {
        	return null;
        }

        NBTTagCompound tag = new NBTTagCompound();
        tag.setString( data.getParts()[ 0 ], mat );
        
        ItemStack stack = new ItemStack( armor );
        stack.setTagCompound( tag );
        
        armor.equipment.init( stack );
        
        return stack;
    }
    
    private final ArmorItem armor;
    private final String type;
}
