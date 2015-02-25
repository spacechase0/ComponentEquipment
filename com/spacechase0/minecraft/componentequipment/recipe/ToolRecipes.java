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
import com.spacechase0.minecraft.componentequipment.item.ToolItem;
import com.spacechase0.minecraft.componentequipment.tool.Tool;
import com.spacechase0.minecraft.componentequipment.tool.ToolData;

public class ToolRecipes implements IRecipe
{
	public ToolRecipes( ToolItem theTool )
	{
		tool = theTool;
		type = tool.type;
		headType = type + "Head";
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
    	int extra = 0;
    	List< ItemStack > parts = new ArrayList< ItemStack >();
        for ( int i = 0; i < inv.getSizeInventory(); ++i )
        {
            ItemStack stack = inv.getStackInSlot( i );
            if ( stack == null )
            {
            	continue;
            }
            
            if ( stack.getItem().equals( ComponentEquipment.items.part ) )
            {
            	//System.out.println( "found " + stack.getTagCompound() );
            	parts.add( stack );
            }
            else if ( type.equals( "bow" ) && stack.getItem().equals( string ) )
            {
            	++extra;
            }
            else
            {
            	return null;
            }
        }

        ToolData data = Tool.instance.getData( type );
        if ( parts.size() != data.getParts().length )
        {
        	return null;
        }
        
        NBTTagCompound tag = new NBTTagCompound();
        int foundParts = 0;
        for ( int i = 0; i < data.getParts().length; ++i )
        {
        	String part = data.getParts()[ i ];
        	if ( part.equals( "core" ) )
        	{
        		part = "head";
        	}
        	
        	for ( int ip = 0; ip < parts.size(); ++ip )
        	{
        		ItemStack stack = parts.get( ip );
        		String type = stack.getTagCompound().getString( "Part" );
        		if ( type.equals( headType ) )
        		{
        			type = "head";
        		}
        		
        		if ( !type.equals( part ) )
        		{
        			continue;
        		}
        		++foundParts;
        		
        		tag.setString( part, stack.getTagCompound().getString( "Material" ) );
        		
        		parts.remove( ip );
        		break;
        	}
        }
        
        if ( foundParts != data.getParts().length )
        {
        	return null;
        }
        
        if ( type.equals( "bow" ) && extra != 1 )
        {
        	return null;
        }
        
        ItemStack stack = new ItemStack( tool );
        stack.setTagCompound( tag );
        
        tool.equipment.init( stack );
        
        return stack;
    }
    
    private final ToolItem tool;
    private final String type;
    private final String headType;
}
