package com.spacechase0.minecraft.componentequipment.client.gui;

import static net.minecraft.init.Blocks.*;
import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import com.spacechase0.minecraft.componentequipment.ComponentEquipment;
import com.spacechase0.minecraft.componentequipment.inventory.ModificationStandContainer;
import com.spacechase0.minecraft.componentequipment.network.ActivateModificationPacket;
import com.spacechase0.minecraft.componentequipment.tileentity.ModificationStandTileEntity;
import com.spacechase0.minecraft.spacecore.util.ClientUtils;
import com.spacechase0.minecraft.spacecore.util.TranslateUtils;

public class ModificationStandGui extends GuiContainer
{
	public ModificationStandGui( InventoryPlayer player, ModificationStandTileEntity theStand )
	{
		super( new ModificationStandContainer( player, theStand ) );
        stand = theStand;
	}
	
	@Override
	public void initGui()
	{
		super.initGui();
		
		buttonList.clear();
		buttonList.add( new GuiButton( ACTIVATE_BUTTON_ID, guiLeft + 88 - 50, guiTop + 45, 100, 20, "Activate" ) );
	}
	
	@Override
	public void actionPerformed( GuiButton button )
	{
		if ( button.id == ACTIVATE_BUTTON_ID )
		{
			activate();
		}
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer( int param1, int param2 )
	{
		ClientUtils.drawString( TranslateUtils.translate( "container.modificationStand" ), 8, 6, 4210752 );
		ClientUtils.drawString( TranslateUtils.translate( "container.inventory" ), 8, ySize - 96 + 2, 4210752 );
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer( float f, int i, int j )
	{
        GL11.glColor4f( 1.0F, 1.0F, 1.0F, 1.0F );
        ClientUtils.bindTexture( "componentequipment:textures/gui/modStand.png" );
        int x = ( width - xSize ) / 2;
        int y = ( height - ySize ) / 2;
        this.drawTexturedModalRect( x, y, 0, 0, xSize, ySize );
	}
	
	private void activate()
	{
		if ( mc.getNetHandler() == null )
		{
			return;
		}
		
		Block below = stand.getWorldObj().getBlock( stand.xCoord, stand.yCoord - 1, stand.zCoord );
		if ( below == gold_block )
		{
			for ( int i = 0; i < 1; ++i )
			{
				stand.getWorldObj().spawnParticle( "largeexplode", stand.xCoord + 0.5, stand.yCoord + 0.5, stand.zCoord + 0.5, 0, 0, 0 );
			}
		}
		
		ActivateModificationPacket packet = new ActivateModificationPacket( stand.getWorldObj().provider.dimensionId, stand.xCoord, stand.yCoord, stand.zCoord );
		ComponentEquipment.network.sendToServer( packet );
	}
	
	private ModificationStandTileEntity stand;

	public static final int ACTIVATE_BUTTON_ID = 0;
}
