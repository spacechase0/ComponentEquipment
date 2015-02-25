package com.spacechase0.minecraft.componentequipment.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import com.spacechase0.minecraft.componentequipment.inventory.JukeboxContainer;
import com.spacechase0.minecraft.componentequipment.inventory.PersistiumInfuserContainer;
import com.spacechase0.minecraft.componentequipment.item.EquipmentItem;
import com.spacechase0.minecraft.componentequipment.network.ActivateInfuserPacket;
import com.spacechase0.minecraft.componentequipment.tileentity.PersistiumInfuserTileEntity;
import com.spacechase0.minecraft.componentequipment.tool.modifier.PersistanceModifier;
import com.spacechase0.minecraft.spacecore.util.ClientUtils;
import com.spacechase0.minecraft.spacecore.util.TranslateUtils;

public class JukeboxGui extends GuiContainer
{
	public JukeboxGui( InventoryPlayer player, ItemStack theHelmet )
	{
		super( new JukeboxContainer( player, theHelmet ) );
        helmet = theHelmet;
	}
	
	@Override
	public void initGui()
	{
		super.initGui();
		
		// TODO:
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer( int param1, int param2 )
	{
		ClientUtils.drawString( TranslateUtils.translate( "container.portableJukebox" ), 8, 6, 4210752 );
		ClientUtils.drawString( TranslateUtils.translate( "container.inventory" ), 8, ySize - 96 + 2, 4210752 );
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer( float f, int i, int j )
	{
        GL11.glColor4f( 1.0F, 1.0F, 1.0F, 1.0F );
        ClientUtils.bindTexture( "componentequipment:textures/gui/portableJukebox.png" );
        int x = ( width - xSize ) / 2;
        int y = ( height - ySize ) / 2;
        this.drawTexturedModalRect( x, y, 0, 0, xSize, ySize );
	}
	
	@Override
	public void actionPerformed( GuiButton button )
	{
		// TODO:
	}
	
	private ItemStack helmet;
}
