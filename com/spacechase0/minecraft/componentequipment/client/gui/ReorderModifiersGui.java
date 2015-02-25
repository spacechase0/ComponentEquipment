package com.spacechase0.minecraft.componentequipment.client.gui;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.spacechase0.minecraft.componentequipment.ComponentEquipment;
import com.spacechase0.minecraft.componentequipment.inventory.ReorderModifiersContainer;
import com.spacechase0.minecraft.componentequipment.item.EquipmentItem;
import com.spacechase0.minecraft.componentequipment.network.ReorderModifiersPacket;
import com.spacechase0.minecraft.spacecore.client.gui.ReorderGui;
import com.spacechase0.minecraft.spacecore.client.gui.ScrollbarGui;
import com.spacechase0.minecraft.spacecore.util.ClientUtils;
import com.spacechase0.minecraft.spacecore.util.INotifiable;

import cpw.mods.fml.common.Loader;

public class ReorderModifiersGui extends GuiContainer implements INotifiable
{
	public ReorderModifiersGui( InventoryPlayer player )
	{
		super( new ReorderModifiersContainer( player ) );
		( ( ReorderModifiersContainer )( inventorySlots ) ).pleaseNotify( this );
	}
	
	@Override
	public void initGui()
	{
		super.initGui();
		
		resetReorder();
	}
	
	@Override
	public void notify( Object obj, Object... data )
	{
		if ( obj == reorder )
		{
			if ( mc.getNetHandler() == null )
			{
				return;
			}
			
			ReorderModifiersPacket packet = new ReorderModifiersPacket( ( Integer ) data[ 0 ], ( Integer ) data[ 1 ] );
			ComponentEquipment.network.sendToServer( packet );
		}
		else
		{
			resetReorder();
		}
	}
	
	@Override
	public void mouseClicked( int mouseX, int mouseY, int button )
	{
		super.mouseClicked( mouseX, mouseY, button );
		
		mouseX -= guiLeft;
		mouseY -= guiTop;
		
		if ( reorder != null && mouseX >= 30 && mouseY >= 8 && mouseX < 30 + 98 && mouseY < 8 + 70 )
		{
			reorder.mouseClicked( mouseX, mouseY + getOffsetFromScrollbar(), button );
		}
	}
	
	@Override
	public void mouseClickMove( int mouseX, int mouseY, int button, long timeSinceClick )
	{
		if ( scrollbar != null )
		{
			scrollbar.mouseClickMove( mouseX - guiLeft, mouseY - guiTop, button, timeSinceClick );
		}
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer( int param1, int param2 )
	{
        //fontRenderer.drawString( StatCollector.translateToLocal( "container.modificationStand" ), 8, 6, 4210752 );
        //fontRenderer.drawString( StatCollector.translateToLocal( "container.inventory" ), 8, ySize - 96 + 2, 4210752 );
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer( float f, int i, int j )
	{
        GL11.glColor4f( 1.0F, 1.0F, 1.0F, 1.0F );
        ClientUtils.bindTexture( "componentequipment:textures/gui/reorderMods.png" );
        int x = ( width - xSize ) / 2;
        int y = ( height - ySize ) / 2;
        this.drawTexturedModalRect( x, y, 0, 0, xSize, ySize );

		GL11.glPushMatrix();
		GL11.glTranslatef( x, y, 0 );
		if ( reorder != null )
		{
			GL11.glEnable( GL11.GL_SCISSOR_TEST );
			//GL11.glScissor( 30, 8, 98, 70 );
			GL11.glPushMatrix();
			GL11.glTranslatef( 0, -getOffsetFromScrollbar(), 0 );
			{
				ScaledResolution sr = new ScaledResolution( mc, mc.displayWidth, mc.displayHeight );
				GL11.glScissor( ( guiLeft + 30 ) * sr.getScaleFactor(), ( height - ( guiTop + 8 + 70 ) ) * sr.getScaleFactor(), 98 * sr.getScaleFactor(), 70 * sr.getScaleFactor() );
				reorder.draw( mc.fontRenderer, i - x, j - y + getOffsetFromScrollbar() );
			}
			GL11.glPopMatrix();
			GL11.glDisable( GL11.GL_SCISSOR_TEST );
			
			if ( scrollbar != null )
			{
				scrollbar.draw( mc.fontRenderer, i - x, j - x );
			}
		}
		GL11.glPopMatrix();
	}
	
	private void resetReorder()
	{
		ItemStack stack = inventorySlots.getSlot( 1 ).getStack();
		if ( stack == null )
		{
			reorder = null;
			scrollbar = null;
			return;
		}

		List< String > data = null;
		if ( stack.getItem() instanceof EquipmentItem )
		{
			EquipmentItem item = ( EquipmentItem ) stack.getItem();
			data = item.equipment.getModifiers( stack );;
		}
		else
		{
			try
			{
				if ( Loader.isModLoaded( "TConstruct" ) )
				{
					Class c = Class.forName( "tconstruct.library.tools.ToolCore" );
					boolean isTool = c.isAssignableFrom( stack.getItem().getClass() );
					
					if ( isTool )
					{
						NBTTagCompound tag = stack.getTagCompound().getCompoundTag( "InfiTool" );
						
						data = new ArrayList< String >();
						for ( int i = 1; i <= 6; ++i )
						{
							if ( tag.hasKey( "Effect" + i ) )
							{
								data.add( "Effect" + i );
							}
						}
					}
				}
			}
			catch ( Exception exception )
			{
				return;
			}
		}
		reorder = new ReorderGui( 32, 10, 94, data );
		reorder.pleaseNotify( this );
		
		scrollbar = null;
		if ( ( data.size() * 9 ) + 8 > 70 )
		{
			scrollbar = new ScrollbarGui( 134, 8, 70 );
		}
	}
	
	private int getOffsetFromScrollbar()
	{
        if ( scrollbar != null && scrollbar.pos != 0 )
        {
        	float percent = scrollbar.pos / ( float ) scrollbar.height;
        	
        	int total = ( 10 * 9 ) + 4 - 70;
        	return ( int )( total * percent );
        }
        
        return 0;
	}
	
	private ReorderGui reorder;
	private ScrollbarGui scrollbar;
}