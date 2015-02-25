package com.spacechase0.minecraft.componentequipment.recipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

import com.spacechase0.minecraft.componentequipment.ComponentEquipment;
import com.spacechase0.minecraft.componentequipment.item.ArmorItem;
import com.spacechase0.minecraft.componentequipment.item.EquipmentItem;
import com.spacechase0.minecraft.componentequipment.tool.Armor;
import com.spacechase0.minecraft.componentequipment.tool.MaterialData;

public class EquipmentRepairRecipes implements IRecipe
{
	public EquipmentRepairRecipes( EquipmentItem theEquipment, String[] theRepairables )
	{
		item = theEquipment;
		repairables = theRepairables;
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
    	ItemStack tool = null;
    	for ( int i = 0; i < inv.getSizeInventory(); ++i )
    	{
    		ItemStack stack = inv.getStackInSlot( i );
    		if ( stack == null )
    		{
    			continue;
    		}
    		
    		if ( stack.getItem() == item )
    		{
    			if ( tool == null && ( ( EquipmentItem ) stack.getItem() ) == item )
    			{
    				tool = stack;
    			}
    			else
    			{
    				return null;
    			}
    		}
    	}
    	if ( tool == null )
    	{
    		return null;
    	}
    	
    	boolean wasBroken = ( ( EquipmentItem ) tool.getItem() ).equipment.isBroken( tool );
    	tool = tool.copy();
    	
    	ItemStack mat = null;
    	for ( int i = 0; i < inv.getSizeInventory(); ++i )
    	{
    		ItemStack stack = inv.getStackInSlot( i );
    		if ( stack == null )
    		{
    			continue;
    		}
    		
    		boolean isTool = ( stack.getItem() == item );
			if ( mat == null && !isTool )
			{
				mat = stack;
			}
			else if ( !isTool )
			{
				return null;
			}
    	}
    	if ( mat == null )
    	{
    		return null;
    	}

    	MaterialData valid = null;
    	for ( String repairable : repairables )
    	{
    		MaterialData armorMat = item.equipment.getMaterialOf( tool, repairable );
    		if ( armorMat.canRepairWith( mat ) )
    		{
    			valid = armorMat;
    		}
    	}
    	if ( valid == null ) return null;
    	
    	int repairAmount = ( int )( item.equipment.getRepairMultiplier( tool ) * valid.getBaseDurability() );
    	
    	EquipmentItem toolItem = ( EquipmentItem ) tool.getItem();
    	toolItem.setDamage( tool, toolItem.getDamage( tool ) - repairAmount );
    	
    	if ( wasBroken )
    	{
    		toolItem.equipment.initModifiers( tool );
    	}
    	
    	return tool;
    }
    
    private final EquipmentItem item;
    private final String[] repairables;
}
