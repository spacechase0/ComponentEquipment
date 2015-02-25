package com.spacechase0.minecraft.componentequipment.client.gui.docs;

import java.util.Arrays;
import java.util.List;

import static net.minecraft.init.Blocks.*;
import static net.minecraft.init.Items.*;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import com.spacechase0.minecraft.componentequipment.ComponentEquipment;
import com.spacechase0.minecraft.componentequipment.client.gui.DocumentationGui;
import com.spacechase0.minecraft.componentequipment.item.IngotItem;
import com.spacechase0.minecraft.componentequipment.item.ModifierItem;
import com.spacechase0.minecraft.componentequipment.tileentity.PersistiumInfuserTileEntity;
import com.spacechase0.minecraft.spacecore.recipe.RecipeSimplifier;
import com.spacechase0.minecraft.spacecore.recipe.RecipeSimplifier.DisplayStack;
import com.spacechase0.minecraft.spacecore.util.ClientUtils;
import com.spacechase0.minecraft.spacecore.util.TranslateUtils;

public class GettingStartedCategory extends DocumentationCategory
{
	public GettingStartedCategory( DocumentationGui theGui )
	{
		super( theGui );
	}
	
	@Override
	public String getName()
	{
		return "gettingStarted";
	}

	@Override
	public List< String > getSections()
	{
		return Arrays.asList( new String[] { "makingTools", "addingModifiers", "persistance", "disassembly", "casing" } );
	}
	
	@Override
	public String getSectionTitle( String section )
	{
		return TranslateUtils.translate( "gui.componentequipment:docBook.gettingStarted." + section + ".title" );
	}

	@Override
	public int getPageCount( String section )
	{
		if ( section.equals( "persistance" ) )
		{
			return 3;
		}
		else
		{
			return 2;
		}
	}

	@Override
	public void draw( String section, int page, List<DisplayStack> stacksToDraw )
	{
		String text = "gui.componentequipment:docBook.gettingStarted." + section + ".text";
		Object[] params = new Object[ 0 ];
		if ( section.equals( "makingTools" ) )
		{
			text += ( page + 1 );
			
			Block block = ComponentEquipment.blocks.componentStation;
			params = new Object[] { Item.getItemFromBlock( block ).getItemStackDisplayName( new ItemStack( block ) ) };
		}
		else if ( section.equals( "addingModifiers" ) )
		{
			text += ( page + 1 );
			
			Block block1 = ComponentEquipment.blocks.modificationStand;
			Block block2 = cobblestone_wall;
			Item item = item_frame;
			params = new Object[] { Item.getItemFromBlock( block1 ).getItemStackDisplayName( new ItemStack( block1 ) ),
			                        Item.getItemFromBlock( block2 ).getItemStackDisplayName( new ItemStack( block2 ) ),
			                        item.getItemStackDisplayName( new ItemStack( item ) ), };
		}
		else if ( section.equals( "persistance" ) )
		{
			text += ( page + 1 );
			if ( page == 0 )
			{
				Block block = ComponentEquipment.blocks.ingot;
				params = new Object[] { Item.getItemFromBlock( block ).getItemStackDisplayName( new ItemStack( block, 1, IngotItem.PERSISTIUM ) ) };
			}
			else if ( page == 1 )
			{
				Block block = ComponentEquipment.blocks.ingot;
				params = new Object[] { Item.getItemFromBlock( block ).getItemStackDisplayName( new ItemStack( block, 1, IngotItem.PERSISTIUM ) ), PersistiumInfuserTileEntity.BASE_BURN_TIME / 20 };
			}
			else if ( page == 2 )
			{
				text = null;
			}
		}
		else if ( section.equals( "disassembly" ) )
		{
			text += ( page + 1 );
			
			if ( page == 0 )
			{
				Block block1 = ComponentEquipment.blocks.modificationStand;
				Block block2 = gold_block;
				params = new Object[] { Item.getItemFromBlock( block1 ).getItemStackDisplayName( new ItemStack( block1 ) ),
				                        Item.getItemFromBlock( block2 ).getItemStackDisplayName( new ItemStack( block2 ) ),
				                        Item.getItemFromBlock( block2 ).getItemStackDisplayName( new ItemStack( block2 ) ), };
			}
			
			DisplayStack stack = new DisplayStack( gui.width / 2 - 8, gui.height - 64, ModifierItem.getAllStacks() );
			stacksToDraw.add( stack );
		}
		else if ( section.equals( "casing" ) )
		{
			text += ( page + 1 );
		}
		
		if ( text != null )
		{
			ClientUtils.drawSplitString( TranslateUtils.translate( text, params ).replace( "\\n", "\n" ), gui.guiLeft + 20, gui.guiTop + 17, DocumentationGui.BOOK_WIDTH - 20 * 2, 0 );
		}
		
		if ( section.equals( "persistance" ) && page == 2 )
		{
			final int START_LEFT = gui.guiLeft + 20 + DocumentationGui.BOOK_WIDTH / 2 - ( 7 * 16 / 2 ) / 2;
			final int START_TOP = gui.guiTop + 20 + DocumentationGui.BOOK_HEIGHT / 4;// - ( ( 7 + 3 ) * 16 / 4 ) / 2;
			
			int highestZ = 0;
			for ( int ix = 6; ix >= 0; --ix )
			{
				for ( int iy = 6; iy >= 0; --iy )
				{
					int x = ( ix - iy ) * 16 / 2;
					int y = ( ix + iy ) * 16 / 4;
					
					x += START_LEFT;
					y += START_TOP;
					
					DisplayStack display = new DisplayStack( x, y, new ItemStack( obsidian ) );
					display.z = ( int )( y * 1.5 );
					stacksToDraw.add( display );
					
					highestZ = Math.max( highestZ, display.z );
				}
			}
			
			for ( int i = 0; i < 4; ++i )
			{
				for ( int ih = 0; ih < 3; ++ih )
				{
					int ix = ( i % 2 ) * 6;
					int iy = ( i / 2 ) * 6;
					
					int x = ( ix - iy ) * 16 / 2;
					int y = ( ix + iy ) * 16 / 4 - 8 * ( ih + 1 );
					
					x += START_LEFT;
					y += START_TOP;
					
					Block block = quartz_block;
					int meta = 2;
					if ( ih == 2 )
					{
						block = ComponentEquipment.blocks.persistiumCrystal;
						meta = 0;
					}
					
					DisplayStack display = new DisplayStack( x, y, new ItemStack( block, 1, meta ) );
					display.z = highestZ + i;
					stacksToDraw.add( display );
					
					highestZ = Math.max( highestZ, display.z );
				}
			}
			
			{
				int ih = 0;
				
				int ix = 3;
				int iy = 3;
				
				int x = ( ix - iy ) * 16 / 2;
				int y = ( ix + iy ) * 16 / 4 - 8 * ( ih + 1 );
				
				x += START_LEFT;
				y += START_TOP;
				
				DisplayStack display = new DisplayStack( x, y, new ItemStack( ComponentEquipment.blocks.ingot, 1, IngotItem.PERSISTIUM ) );
				display.z = highestZ - 10;// + 1;
				stacksToDraw.add( display );
				
				highestZ = display.z;
			}
			{
				int ih = 1;
				
				int ix = 3;
				int iy = 3;
				
				int x = ( ix - iy ) * 16 / 2;
				int y = ( ix + iy ) * 16 / 4 - 8 * ( ih + 1 );
				
				x += START_LEFT;
				y += START_TOP;
				
				DisplayStack display = new DisplayStack( x, y, new ItemStack( beacon, 1 ) );
				display.z = highestZ + 1;
				stacksToDraw.add( display );
			}
			
			String str = "(7x7)";
			ClientUtils.drawString( str, gui.guiLeft + DocumentationGui.BOOK_WIDTH / 2 - ClientUtils.getStringWidth( str ) / 2, gui.guiTop + DocumentationGui.BOOK_HEIGHT / 4 * 3, 0 );
		}
	}
}
