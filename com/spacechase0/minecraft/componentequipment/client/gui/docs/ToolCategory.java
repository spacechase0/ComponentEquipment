package com.spacechase0.minecraft.componentequipment.client.gui.docs;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.oredict.OreDictionary;

import com.spacechase0.minecraft.componentequipment.client.gui.DocumentationGui;
import com.spacechase0.minecraft.componentequipment.item.PartItem;
import com.spacechase0.minecraft.componentequipment.tool.Armor;
import com.spacechase0.minecraft.componentequipment.tool.Tool;
import com.spacechase0.minecraft.componentequipment.tool.ToolData;
import com.spacechase0.minecraft.spacecore.recipe.RecipeSimplifier.DisplayStack;
import com.spacechase0.minecraft.spacecore.util.ClientUtils;
import com.spacechase0.minecraft.spacecore.util.TranslateUtils;

public class ToolCategory extends DocumentationCategory
{
	public ToolCategory( DocumentationGui theGui )
	{
		super( theGui );
	}
	
	@Override
	public String getName()
	{
		return "tools";
	}

	@Override
	public List< String > getSections()
	{
		List< String > sections = new ArrayList< String >();
		
		for ( String tool : Tool.instance.getTypes() )
		{
			sections.add( tool );
		}
		for ( String armor : Armor.instance.getTypes() )
		{
			sections.add( armor );
		}
		
		return sections;
	}
	
	@Override
	public String getSectionTitle( String section )
	{
		return TranslateUtils.translate( "item." + section + ".name" );
	}

	@Override
	public int getPageCount( String section )
	{
		return 1;
	}

	@Override
	public void draw( String section, int page, List< DisplayStack > stacksToDraw )
	{
		int mult = 0;
		if ( Tool.instance.getData( section ) != null )
		{
			ToolData tool = Tool.instance.getData( section );
			if ( tool.getAttackDamage() > 0 )
			{
				drawString( 20, 20, "damage", tool.getAttackDamage() );
				mult = 1;
			}
		}

		ClientUtils.drawSplitString( TranslateUtils.translate( "item." + section + ".desc" ), gui.guiLeft + 20, gui.guiTop + 17 + mult * 20, DocumentationGui.BOOK_WIDTH - 20 * 2, 0 );

		String[] parts = null;
		if ( Tool.instance.getData( section ) != null && !section.equals( "paxel" ) )
		{
			parts = Tool.instance.getData( section ).getParts();
		}
		else if ( Armor.instance.getData( section ) != null )
		{
			parts = Armor.instance.getData( section ).getParts();
		}
		else
		{
			parts = new String[ 0 ];
		}
		
		int total = parts.length + ( section.equals( "bow" ) ? 1 : 0 );
		
		int baseX = gui.guiLeft + DocumentationGui.BOOK_WIDTH / 2 - total * 18 / 2;
		int baseY = gui.guiTop + DocumentationGui.BOOK_HEIGHT - 20 - 18;
		for ( int i = 0; i < total; ++i )
		{
			ItemStack[] show = null;
			if ( i == parts.length )
			{
				show = new ItemStack[] { new ItemStack( net.minecraft.init.Items.string ) };
			}
			else
			{
				String part = parts[ i ];
				
				if ( part.equals( "head" ) )
				{
					part = section + "Head";
				}
				else if ( part.endsWith( "Left" ) )
				{
					part = part.substring( 0, part.length() - 4 );
				}
				else if ( part.endsWith( "Right" ) )
				{
					part = part.substring( 0, part.length() - 5 );
				}
				
				show = PartItem.getAllStacksOfType( part );
			}
			
			DisplayStack display = new DisplayStack( baseX, baseY, show );
			display.x += i * 18;
			
			stacksToDraw.add( display );
		}
	}
	
	private void drawString( int x, int y, String str, Object... params )
	{
		ClientUtils.drawString( TranslateUtils.translate( "gui.componentequipment:docBook.tools." + str, params ), gui.guiLeft + x, gui.guiTop + y, 0 );
	}
}
