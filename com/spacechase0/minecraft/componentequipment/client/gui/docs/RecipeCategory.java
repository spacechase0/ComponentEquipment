package com.spacechase0.minecraft.componentequipment.client.gui.docs;

import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glTranslatef;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.minecraft.init.Blocks.*;
import static net.minecraft.init.Items.*;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import com.spacechase0.minecraft.componentequipment.ComponentEquipment;
import com.spacechase0.minecraft.componentequipment.client.gui.DocumentationGui;
import com.spacechase0.minecraft.spacecore.recipe.RecipeSimplifier;
import com.spacechase0.minecraft.spacecore.recipe.RecipeSimplifier.DisplayStack;
import com.spacechase0.minecraft.spacecore.util.ClientUtils;
import com.spacechase0.minecraft.spacecore.util.ReflectionUtils;
import com.spacechase0.minecraft.spacecore.util.TranslateUtils;

public class RecipeCategory extends DocumentationCategory
{
	public RecipeCategory( DocumentationGui theGui )
	{
		super( theGui );
		loadRecipeData();
	}
	
	@Override
	public String getName()
	{
		return "recipes";
	}

	@Override
	public List< String > getSections()
	{
		return Arrays.asList( ( String[] ) recipeData.keySet().toArray( new String[] {} ) );
	}
	
	@Override
	public String getSectionTitle( String section )
	{
		return TranslateUtils.translate( "gui.componentequipment:docBook.recipes." + section );
	}

	@Override
	public int getPageCount( String section )
	{
		return recipeData.get( section ).size();
	}

	@Override
	public void draw( String section, int page, List<DisplayStack> stacksToDraw )
	{
		List< IRecipe > recipes = recipeData.get( section );
    	
    	final int craftLeft = gui.guiLeft + DocumentationGui.BOOK_WIDTH / 2 - 54 / 2;
    	final int craftTop = gui.guiTop + 40;
    	
    	ClientUtils.bindTexture( CRAFTING_TEX );
    	gui.drawTexturedModalRect( craftLeft, craftTop, 29, 16, 54, 54 );

    	glPushMatrix();
    	{
    		glTranslatef( craftLeft + 36 + 4, craftTop + 54, 0.f );
    		glRotatef( 90.f, 0.f, 0.f, 1.f );
    		
    		gui.drawTexturedModalRect( 0, 0, 83, 30, 36, 26 );
    	}
    	glPopMatrix();
    	
    	gui.drawTexturedModalRect( craftLeft + 18 - 4, craftTop + 90, 119, 30, 26, 26 );
    	
    	IRecipe curr = recipes.get( page );
    	RecipeSimplifier.handleRecipe( stacksToDraw, curr, craftLeft, craftTop );
    	
    	if ( curr instanceof ShapedRecipes || curr instanceof ShapedOreRecipe )
    	{
    		String str = TranslateUtils.translate( "gui.componentequipment:docBook.recipes.shaped" );
    		int w = ClientUtils.getStringWidth( str );
    		
    		ClientUtils.drawString( str, gui.guiLeft + DocumentationGui.BOOK_WIDTH / 2 - w / 2, gui.guiTop + 24, 0x00000000 );
    	}
    	else if ( curr instanceof ShapelessRecipes || curr instanceof ShapelessOreRecipe )
    	{
    		String str = TranslateUtils.translate( "gui.componentequipment:docBook.recipes.shapeless" );
    		int w = ClientUtils.getStringWidth( str );
    		
    		ClientUtils.drawString( str, gui.guiLeft + DocumentationGui.BOOK_WIDTH / 2 - w / 2, gui.guiTop + 24, 0x00000000 );
    	}
	}

	private void loadRecipeData()
	{
		String path = "/assets/componentequipment/docs/recipes.txt";
		try
		{
			InputStream stream = ComponentEquipment.class.getResourceAsStream( path );
			InputStreamReader reader = new InputStreamReader( stream );
			BufferedReader buffer = new BufferedReader( reader );
			
			recipeData.clear();
			
			while ( true )
			{
				String line = buffer.readLine();
				if ( line == null || line.indexOf( '=' ) == -1 )
				{
					break;
				}
				
				String pre = line.substring( 0, line.indexOf( '=' ) );
				String post = line.substring( line.indexOf( '=' ) + 1 );
				
				List< IRecipe > recipes = new ArrayList< IRecipe >();
				
				String[] items = post.split( "," );
				for ( String item : items )
				{
					if ( item.indexOf( '.' ) == -1 ) continue;

					// Identify the result we want
					String cat = item.substring( 0, item.indexOf( '.' ) );
					String specific = item.substring( item.indexOf( '.' ) + 1 );
					int data = OreDictionary.WILDCARD_VALUE;
					if ( specific.indexOf( ':' ) != -1 )
					{
						data = Integer.valueOf( specific.substring( specific.indexOf( ':' ) + 1 ) );
						specific = specific.substring( 0, specific.indexOf( ':' ) );
					}
					
					ItemStack stack = new ItemStack( stick );
					if ( cat.equals( "block" ) )
					{
						stack = new ItemStack( ( Block ) ReflectionUtils.getObj( ComponentEquipment.blocks, specific ), 1, data );
					}
					else if ( cat.equals( "item" ) )
					{
						stack = new ItemStack( ( Item ) ReflectionUtils.getObj( ComponentEquipment.items, specific ), 1, data );
					}
					else
					{
						throw new IllegalArgumentException( "Bad line in recipes.txt for documentation: Bad category for '" + item + "'" );
					}
					
					// Look for the recipe
					for ( Object obj : CraftingManager.getInstance().getRecipeList() )
					{
						IRecipe recipe = ( IRecipe ) obj;
						if ( recipe instanceof ShapedRecipes )
						{
							ShapedRecipes r = ( ShapedRecipes ) recipe;
							if ( RecipeSimplifier.matches( stack, r.getRecipeOutput() ) )
							{
								recipes.add( recipe );
							}
						}
						else if ( recipe instanceof ShapedOreRecipe )
						{
							ShapedOreRecipe r = ( ShapedOreRecipe ) recipe;
							if ( RecipeSimplifier.matches( stack, r.getRecipeOutput() ) )
							{
								recipes.add( recipe );
							}
						}
						else if ( recipe instanceof ShapelessRecipes )
						{
							ShapelessRecipes r = ( ShapelessRecipes ) recipe;
							if ( RecipeSimplifier.matches( stack, r.getRecipeOutput() ) )
							{
								recipes.add( recipe );
							}
						}
						else if ( recipe instanceof ShapelessOreRecipe )
						{
							ShapelessOreRecipe r = ( ShapelessOreRecipe ) recipe;
							if ( RecipeSimplifier.matches( stack, r.getRecipeOutput() ) )
							{
								recipes.add( recipe );
							}
						}
					}
				}
				
				recipeData.put( pre, recipes );
			}
		}
		catch ( Exception exception )
		{
			exception.printStackTrace();
		}
	}
	
	private Map< String, List< IRecipe > > recipeData = new HashMap< String, List< IRecipe > >();
	
    private static final String CRAFTING_TEX = "minecraft:textures/gui/container/crafting_table.png";
}
