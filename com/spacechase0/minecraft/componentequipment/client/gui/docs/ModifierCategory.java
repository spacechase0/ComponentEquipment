package com.spacechase0.minecraft.componentequipment.client.gui.docs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import com.spacechase0.minecraft.componentequipment.client.gui.DocumentationGui;
import com.spacechase0.minecraft.componentequipment.tool.Modifier;
import com.spacechase0.minecraft.componentequipment.tool.ModifierRecipes;
import com.spacechase0.minecraft.componentequipment.tool.ModifierRecipes.Recipe;
import com.spacechase0.minecraft.componentequipment.tool.ModifierRecipes.Recipe.StackType;
import com.spacechase0.minecraft.spacecore.recipe.RecipeSimplifier;
import com.spacechase0.minecraft.spacecore.recipe.RecipeSimplifier.DisplayStack;
import com.spacechase0.minecraft.spacecore.util.ClientUtils;
import com.spacechase0.minecraft.spacecore.util.ListUtils;
import com.spacechase0.minecraft.spacecore.util.TranslateUtils;

public class ModifierCategory extends DocumentationCategory
{
	public ModifierCategory( DocumentationGui theGui )
	{
		super( theGui );
	}
	
	@Override
	public String getName()
	{
		return "modifiers";
	}

	@Override
	public List< String > getSections()
	{
		return Arrays.asList( Modifier.getTypes() );
	}
	
	@Override
	public String getSectionTitle( String section )
	{
		return TranslateUtils.translate( "componentequipment:modifier." + section + ".name" );
	}

	@Override
	public int getPageCount( String section )
	{
		int amt = 1;
		List< Integer > found = new ArrayList< Integer >();
		for ( Recipe recipe : ModifierRecipes.recipes )
		{
			if ( recipe.mod.equals( section ) && !found.contains( recipe.level ) )
			{
				amt += 1;
				found.add( recipe.level );
			}
		}
		
		return amt;
	}

	@Override
	public void draw( String section, int page, List< DisplayStack > stacksToDraw )
	{
		Modifier mod = Modifier.getModifier( section );

		if ( page == 0 )
		{
			drawString( 20, 17 + 0 * 10, "name", TranslateUtils.translate( "componentequipment:modifier." + section + ".name" ) );
			drawString( 20, 17 + 1 * 10, "category", TranslateUtils.translate( "componentequipment:modifier." + section + ".category" ) );
			drawString( 20, 17 + 2 * 10, "apply", TranslateUtils.translate( "componentequipment:modifier." + section + ".apply" ) );
			drawString( 20, 17 + 3 * 10, "modCost", mod.getModifierCost() );
			ClientUtils.drawSplitString( TranslateUtils.translate( "componentequipment:modifier." + section + ".desc" ).replace( "\\n", "\n" ), gui.guiLeft + 20, gui.guiTop + 17 + 4 * 10 + 10, DocumentationGui.BOOK_WIDTH - 20 * 2, 0 );
		}
		else
		{
			int recipeIndex = page - 1;
			
			drawString( DocumentationGui.BOOK_WIDTH / 2 - 16, 32, "level", TranslateUtils.translate( "enchantment.level." + ( recipeIndex + 1 ) ) );
			
			int done = 0;
			for ( Recipe recipe : ModifierRecipes.recipes )
			{
				if ( !recipe.mod.equals( section ) || recipe.level != recipeIndex + 1 )
				{
					continue;
				}
				
				int x = ( int )( gui.guiLeft + DocumentationGui.BOOK_WIDTH / 2 );
				int y = gui.guiTop + ( int )( DocumentationGui.BOOK_HEIGHT * 0.425 ) - 16 + ( done * 32 );
				
				x -= ( ( recipe.ingredients.size() % 3 ) * 20 - 2 ) / 2;
				
				for ( int ii = 0; ii < recipe.ingredients.size(); ++ii )
				{
					StackType ingredient = recipe.ingredients.get( ii );
					
					int ix = ii % 3;
					int iy = ii / 3;
					
					List< ItemStack > stacks = new ArrayList< ItemStack >();
					for ( ItemStack stack : RecipeSimplifier.getAliases( ingredient.stacks.get( 0 ) ) )
					{
						stack = stack.copy();
						stack.stackSize = ingredient.amt;
						stacks.add( stack );
					}
					for ( int is = 1; is < ingredient.stacks.size(); ++is )
					{
						for ( ItemStack stack : RecipeSimplifier.getAliases( ingredient.stacks.get( is ) ) )
						{
							stack = stack.copy();
							stack.stackSize = ingredient.amt;
							stacks.add( stack );
						}
					}
					
					stacksToDraw.add( new DisplayStack( x + ix * 20, y + iy * 20, stacks ) );
				}
				
				if ( done > 0 )
				{
					Tessellator tess = Tessellator.instance;
					GL11.glDisable( GL11.GL_TEXTURE_2D );
					tess.startDrawing( GL11.GL_LINES );
					{
						tess.setColorOpaque(0, 0, 0);
						tess.addVertex( x + 8 - DocumentationGui.BOOK_WIDTH / 4, y - 8, 0 );
						tess.addVertex( x + 8 + DocumentationGui.BOOK_WIDTH / 4, y - 8, 0 );
					}
					tess.draw();
					GL11.glEnable( GL11.GL_TEXTURE_2D );
				}
				
				++done;
			}
		}
	}
	
	private void drawString( int x, int y, String str, Object... params )
	{
		ClientUtils.drawString( TranslateUtils.translate( "gui.componentequipment:docBook.modifiers." + str, params ), gui.guiLeft + x, gui.guiTop + y, 0 );
	}
}
