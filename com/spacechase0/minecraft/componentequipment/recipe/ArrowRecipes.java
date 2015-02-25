package com.spacechase0.minecraft.componentequipment.recipe;

import static net.minecraft.init.Blocks.*;
import static net.minecraft.init.Items.*;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.ShapedOreRecipe;

import com.spacechase0.minecraft.componentequipment.ComponentEquipment;
import com.spacechase0.minecraft.componentequipment.tool.Arrow;
import com.spacechase0.minecraft.componentequipment.tool.ArrowData;

import cpw.mods.fml.common.registry.GameRegistry;

public class ArrowRecipes
{
	public static void addRecipes()
	{
		String[] types = Arrow.getTypes();
		for ( int it = 0; it < types.length; ++it )
		{
			ArrowData data = Arrow.getData( types[ it ] );
			if ( data.getCraftingMaterial() == torch ) continue;

	    	NBTTagCompound tag = new NBTTagCompound();
	    	tag.setString( "Head", types[ it ] );
	    	tag.setBoolean( "Feather", false );
	    	
	    	ItemStack out = new ItemStack( ComponentEquipment.items.arrow, 4 );
	    	out.setTagCompound( tag );
	    	
	    	if ( ComponentEquipment.instance.config.get( "general", "diagonalArrowRecipes", false ).getBoolean( false ) )
	    	{

		    	GameRegistry.addRecipe( new ShapedOreRecipe( out,
		    			                                     new Object[]
		    	    			                             {
	                    	                                 	" ^",
	                    	                                 	"| ",
	                    	                                 	'^', data.getCraftingMaterial(),
	                    	                                 	'|', "stickWood",
		    			                                     } ) );
	    	}
	    	else
	    	{
		    	GameRegistry.addRecipe( new ShapedOreRecipe( out,
		    			                                     new Object[]
		    	    			                             {
	                    	                                 	"^",
	                    	                                 	"|",
	                    	                                 	'^', data.getCraftingMaterial(),
	                    	                                 	'|', "stickWood",
		    			                                     } ) );
	    	}
	    	
	    	tag = ( NBTTagCompound ) tag.copy();
	    	tag.setBoolean( "Feather", true );
	    	out = out.copy();
	    	out.setTagCompound( tag );

	    	if ( ComponentEquipment.instance.config.get( "general", "diagonalArrowRecipes", false ).getBoolean( false ) )
	    	{
		    	GameRegistry.addRecipe( new ShapedOreRecipe( out,
		    			                                     new Object[]
		    			                                     {
		                                                     	"  ^",
		                                                     	" | ",
		                                                     	"~  ",
		                                                     	'^', data.getCraftingMaterial(),
		                                                     	'|', "stickWood",
		                                                     	'~', new ItemStack( feather ),
		    			                                     } ) );
	    	}
	    	else
	    	{
		    	GameRegistry.addRecipe( new ShapedOreRecipe( out,
		    			                                     new Object[]
		    			                                     {
		                                                     	"^",
		                                                     	"|",
		                                                     	"~",
		                                                     	'^', data.getCraftingMaterial(),
		                                                     	'|', "stickWood",
		                                                     	'~', new ItemStack( feather ),
		    			                                     } ) );
	    	}
		}
		
		// Torches
		{
			ArrowData data = Arrow.getData( "torch" );

	    	NBTTagCompound tag = new NBTTagCompound();
	    	tag.setString( "Head", "torch" );
	    	tag.setBoolean( "Feather", false );
	    	
	    	ItemStack out = new ItemStack( ComponentEquipment.items.arrow, 3 );
	    	out.setTagCompound( tag );

	    	if ( ComponentEquipment.instance.config.get( "general", "diagonalArrowRecipes", false ).getBoolean( false ) )
	    	{
		    	GameRegistry.addRecipe( new ShapedOreRecipe( out,
		    			                                     new Object[]
		    	    			                             {
	                    	                                 	"^^",
	                    	                                 	"|^",
	                    	                                 	'^', data.getCraftingMaterial(),
	                    	                                 	'|', "stickWood",
		    			                                     } ) );
	    	}
	    	else
	    	{
		    	GameRegistry.addRecipe( new ShapedOreRecipe( out,
		    			                                     new Object[]
		    	    			                             {
	                    	                                 	"^^^",
	                    	                                 	" | ",
	                    	                                 	'^', data.getCraftingMaterial(),
	                    	                                 	'|', "stickWood",
		    			                                     } ) );
	    	}
	    	
	    	tag = ( NBTTagCompound ) tag.copy();
	    	tag.setBoolean( "Feather", true );
	    	out = out.copy();
	    	out.setTagCompound( tag );

	    	if ( ComponentEquipment.instance.config.get( "general", "diagonalArrowRecipes", false ).getBoolean( false ) )
	    	{
		    	GameRegistry.addRecipe( new ShapedOreRecipe( out,
		    			                                     new Object[]
		    			                                     {
		                                                     	" ^^",
		                                                     	" |^",
		                                                     	"~  ",
		                                                     	'^', data.getCraftingMaterial(),
		                                                     	'|', "stickWood",
		                                                     	'~', new ItemStack( feather ),
		    			                                     } ) );
	    	}
	    	else
	    	{
		    	GameRegistry.addRecipe( new ShapedOreRecipe( out,
		    			                                     new Object[]
		    			                                     {
		                                                     	"^^^",
		                                                     	" | ",
		                                                     	" ~ ",
		                                                     	'^', data.getCraftingMaterial(),
		                                                     	'|', "stickWood",
		                                                     	'~', new ItemStack( feather ),
		    			                                     } ) );
	    	}
		}
	}
}
