package com.spacechase0.minecraft.componentequipment.recipe;

import java.util.ArrayList;
import java.util.List;

import static net.minecraft.init.Blocks.*;
import static net.minecraft.init.Items.*;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import com.spacechase0.minecraft.componentequipment.ComponentEquipment;
import com.spacechase0.minecraft.componentequipment.item.EquipmentItem;
import com.spacechase0.minecraft.componentequipment.item.ToolItem;
import com.spacechase0.minecraft.componentequipment.tool.Equipment;
import com.spacechase0.minecraft.componentequipment.tool.Modifier;
import com.spacechase0.minecraft.componentequipment.tool.Tool;
import com.spacechase0.minecraft.componentequipment.tool.ToolData;

public class ModifierRecipes implements IRecipe
{
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
    	ItemStack modEssence = null;
    	ItemStack equipmentStack = null;
    	boolean extra = false;
    	for ( int is = 0; is < inv.getSizeInventory(); ++is )
    	{
    		ItemStack stack = inv.getStackInSlot( is );
    		if ( stack == null )
    		{
    			continue;
    		}
    		Item item = stack.getItem();
    		
    		if ( item instanceof EquipmentItem )
    		{
    			if ( equipmentStack == null ) equipmentStack = stack;
    			else extra = true;
    		}
    		else if ( item == ComponentEquipment.items.modifierEssence )
    		{
    			if ( modEssence == null ) modEssence = stack;
    			else extra = true;
    		}
    		else
    		{
    			extra = true;
    		}
    	}
    	
    	if ( modEssence == null || equipmentStack == null || extra )
    	{
    		return null;
    	}
    	
    	Modifier mod = Modifier.getModifier( modEssence.getTagCompound().getString( "Modifier" ) );
    	EquipmentItem item = ( EquipmentItem ) equipmentStack.getItem();
    	Equipment equipment = item.equipment;
    	
    	if ( equipment.getModifiersRemaining( equipmentStack ) < mod.getModifierCost() || 
    	     equipment.getModifierLevel( equipmentStack, mod.type ) > 0 ||
    	     !mod.canAdd( equipmentStack ) || !mod.canApplyTo( item ) )
    	{
    		return null;
    	}
    	
    	ItemStack result = equipmentStack.copy();
    	equipment.setModifierLevel( result, mod.type, modEssence.getTagCompound().getInteger( "Level" ) );
    	equipment.initModifiers( equipmentStack );
    	
    	NBTTagCompound data = modEssence.getTagCompound().getCompoundTag( "Data" );
    	for ( Object keyObj : data.func_150296_c() )
    	{
    		String key = ( String ) keyObj;
    		NBTBase tag = ( NBTBase ) data.getTag( key );
    		result.getTagCompound().setTag( key, tag );
    	}
    	
    	return result;
    }
}
