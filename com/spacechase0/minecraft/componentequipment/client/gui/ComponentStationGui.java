package com.spacechase0.minecraft.componentequipment.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.spacechase0.minecraft.componentequipment.inventory.ComponentStationContainer;
import com.spacechase0.minecraft.componentequipment.tileentity.ComponentStationTileEntity;
import com.spacechase0.minecraft.spacecore.util.ClientUtils;

public class ComponentStationGui extends GuiContainer
{
	public ComponentStationGui( InventoryPlayer player, ComponentStationTileEntity theStation )
	{
		super( new ComponentStationContainer( player, theStation ) );
        station = theStation;
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer( int param1, int param2 )
	{
        //fontRenderer.drawString( StatCollector.translateToLocal( "container.inventory" ), 8, ySize - 96 + 2, 4210752 );
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer( float f, int i, int j )
	{
        GL11.glColor4f( 1.0F, 1.0F, 1.0F, 1.0F );
        ClientUtils.bindTexture( "componentequipment:textures/gui/parts.png" );
        int x = ( width - xSize ) / 2;
        int y = ( height - ySize ) / 2;
        this.drawTexturedModalRect( x, y, 0, 0, xSize, ySize );
	}
	
	private ComponentStationTileEntity station;
}
