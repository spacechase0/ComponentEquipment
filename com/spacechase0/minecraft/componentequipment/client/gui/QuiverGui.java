package com.spacechase0.minecraft.componentequipment.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import com.spacechase0.minecraft.componentequipment.inventory.QuiverContainer;
import com.spacechase0.minecraft.spacecore.util.ClientUtils;
import com.spacechase0.minecraft.spacecore.util.TranslateUtils;

public class QuiverGui extends GuiContainer
{
	public QuiverGui( InventoryPlayer player, ItemStack theQuiver )
	{
		super( new QuiverContainer( player, theQuiver ) );
		quiver = theQuiver;
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer( int param1, int param2 )
	{
		ClientUtils.drawString( TranslateUtils.translate( "container.quiver" ), 8, 6, 4210752 );
        ClientUtils.drawString( TranslateUtils.translate( "container.inventory" ), 8, ySize - 96 + 2, 4210752 );
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer( float f, int i, int j )
	{
        GL11.glColor4f( 1.0F, 1.0F, 1.0F, 1.0F );
        ClientUtils.bindTexture( "componentequipment:textures/gui/quiver.png" );
        int x = ( width - xSize ) / 2;
        int y = ( height - ySize ) / 2;
        this.drawTexturedModalRect( x, y, 0, 0, xSize, ySize );
	}
	
	private final ItemStack quiver;
}
