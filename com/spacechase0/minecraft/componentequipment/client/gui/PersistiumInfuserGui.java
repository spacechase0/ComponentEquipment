package com.spacechase0.minecraft.componentequipment.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import com.spacechase0.minecraft.componentequipment.ComponentEquipment;
import com.spacechase0.minecraft.componentequipment.inventory.PersistiumInfuserContainer;
import com.spacechase0.minecraft.componentequipment.item.EquipmentItem;
import com.spacechase0.minecraft.componentequipment.network.ActivateInfuserPacket;
import com.spacechase0.minecraft.componentequipment.tileentity.PersistiumInfuserTileEntity;
import com.spacechase0.minecraft.componentequipment.tool.modifier.PersistanceModifier;
import com.spacechase0.minecraft.spacecore.util.ClientUtils;
import com.spacechase0.minecraft.spacecore.util.TranslateUtils;

public class PersistiumInfuserGui extends GuiContainer
{
	public PersistiumInfuserGui( InventoryPlayer player, PersistiumInfuserTileEntity theInfuser )
	{
		super( new PersistiumInfuserContainer( player, theInfuser ) );
        infuser = theInfuser;
	}
	
	@Override
	public void initGui()
	{
		super.initGui();
		addButtons();
	}
	
	@Override
    public void drawScreen( int par1, int par2, float par3 )
	{
		super.drawScreen( par1, par2, par3 );
		
		ItemStack toolSlot = infuser.getStackInSlot( PersistiumInfuserTileEntity.TOOL_SLOT );
		if ( toolSlot != prevInSlot )
		{
			prevInSlot = toolSlot;
			addButtons();
		}
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer( int param1, int param2 )
	{
        //fontRenderer.drawString( "Persistium Infuser", 8, 6, 4210752 );
		ClientUtils.drawString( TranslateUtils.translate( "container.inventory" ), 8, ySize - 96 + 2, 4210752 );
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer( float f, int i, int j )
	{
        GL11.glColor4f( 1.0F, 1.0F, 1.0F, 1.0F );
        ClientUtils.bindTexture( "componentequipment:textures/gui/persistiumInfuser.png" );
        int x = ( width - xSize ) / 2;
        int y = ( height - ySize ) / 2;
        this.drawTexturedModalRect( x, y, 0, 0, xSize, ySize );

        if ( infuser.getBurnTimeLeft() > 0 )
        {
        	float scale = infuser.getBurnTimeLeft() / ( ( float )( infuser.getBurnTimeTotal() ) );
        	int size = ( int ) Math.ceil( 14 * scale );
        	
            this.drawTexturedModalRect( x + 8, y + 36 + ( 14 - size ), 176, 14 - size, 14, size );
        }
        
        if ( infuser.getProgressAmount() > 0 )
        {
        	float scale = infuser.getProgressAmount() / ( ( float ) PersistiumInfuserTileEntity.BASE_BURN_TIME );
        	int size = ( int ) Math.ceil( 24 * scale );
        	
            this.drawTexturedModalRect( x + 119, y + 34, 176, 14, size, 17 );
        }
	}
	
	@Override
	public void actionPerformed( GuiButton button )
	{
		//System.out.println( "Pressed button " + button.id );
		if ( button.id == 1 || ( button.id % 2 ) == 0 ) // Power of 2 is what I should check for
		{
			if ( mc.getNetHandler() == null )
			{
				return;
			}
			
			ActivateInfuserPacket packet = new ActivateInfuserPacket( infuser.getWorldObj().provider.dimensionId, infuser.xCoord, infuser.yCoord, infuser.zCoord );
			packet.level = button.id;
			ComponentEquipment.network.sendToServer( packet );
		}
	}
	
	private void addButtons()
	{
		buttonList.clear();
		
		if ( prevInSlot == null )
		{
			return;
		}

		EquipmentItem item = ( EquipmentItem ) prevInSlot.getItem();
		int level = item.equipment.getModifierLevel( prevInSlot, "persistance" );
		
		int t1 = ( PersistanceModifier.DAMAGE );
		if ( ( level & t1 ) != t1 )
		{
			if ( ( level & PersistanceModifier.DAMAGE ) == 0 )
			{
		        buttonList.add( new GuiButton( PersistanceModifier.DAMAGE, guiLeft + 29, guiTop + 7 + ( 22 * 1 ), 87, 20, "Damage" ) );
			}
			return;
		}
        
		int t2 = ( PersistanceModifier.TIME );
		if ( ( level & t2 ) != t2 )
		{
			if ( ( level & PersistanceModifier.TIME ) == 0 )
			{
				buttonList.add( new GuiButton( PersistanceModifier.TIME, guiLeft + 29, guiTop + 7 + ( 22 * 1 ), 87, 20, "Time" ) );
			}
			return;
		}
        
		int t3 = ( PersistanceModifier.INVENTORY );
		if ( ( level & t3 ) != t3 )
		{
			if ( ( level & PersistanceModifier.INVENTORY ) == 0 )
			{
				buttonList.add( new GuiButton( PersistanceModifier.INVENTORY, guiLeft + 29, guiTop + 7 + ( 22 * 1 ), 87, 20, "Inventory" ) );
			}
			return;
		}
	}
	
	private PersistiumInfuserTileEntity infuser;
	private ItemStack prevInSlot;
}
