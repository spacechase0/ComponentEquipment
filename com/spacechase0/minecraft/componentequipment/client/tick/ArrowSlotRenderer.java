package com.spacechase0.minecraft.componentequipment.client.tick;

import java.util.EnumSet;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.spacechase0.minecraft.componentequipment.ComponentEquipment;
import com.spacechase0.minecraft.componentequipment.item.QuiverItem;
import com.spacechase0.minecraft.spacecore.util.ClientUtils;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

public class ArrowSlotRenderer
{
	public ArrowSlotRenderer()
	{
	}
	
	@SubscribeEvent
	public void tick( TickEvent.RenderTickEvent event )
	{
		if ( !event.phase.equals( TickEvent.Phase.END ) )
		{
			return;
		}
		Minecraft mc = FMLClientHandler.instance().getClient();
		
		if ( mc.currentScreen != null || mc.thePlayer == null )
		{
			return;
		}
		GuiIngame gameGui = mc.ingameGUI;

        ScaledResolution scaledRes = new ScaledResolution( mc, mc.displayWidth, mc.displayHeight );
        int sw = scaledRes.getScaledWidth();
        int sh = scaledRes.getScaledHeight();
		
        InventoryPlayer inv = mc.thePlayer.inventory;
        int playerSelected = inv.currentItem;
        ItemStack selected = mc.thePlayer.getCurrentEquippedItem();
        if ( selected == null || !selected.getItem().equals( ComponentEquipment.items.bow ) )
		{
        	return;
		}
        
        ItemStack quiver = QuiverItem.findFirstQuiver( inv );
        if ( quiver == null )
        {
        	return;
        }
        
        ClientUtils.bindTexture( "componentequipment:textures/gui/arrowSlot.png" );
        
        // Render base
        int baseX = ( sw / 2 ) - ( InventoryPlayer.getHotbarSize() * 10 ) + ( playerSelected * 20 );
        int baseY = ( sh - 22 );
        
        for ( int i = 0; i < QuiverItem.SELECTION_SIZE; ++i )
        {
        	GL11.glColor3f( 1.f, 1.f, 1.f );
            gameGui.drawTexturedModalRect( baseX, baseY - 25 - ( i * 20 ), 0, 0, 20, 20 );
        }
        
        int quiverSelected = QuiverItem.getSelected( mc.thePlayer );
    	GL11.glColor3f( 1.f, 1.f, 1.f );
        gameGui.drawTexturedModalRect( baseX - 1, baseY - 25 - ( quiverSelected * 20 ) - 1, 0, 20, 22, 22 );
        
        IInventory quiverInv = QuiverItem.getInventoryOf( quiver );
        
        int count[] = { 0, 0, 0 };
        for ( int ia = 0; ia < QuiverItem.SELECTION_SIZE; ++ia )
        {
        	ItemStack stack = quiverInv.getStackInSlot( ia );
        	if ( stack == null )
        	{
        		continue;
        	}
        	count[ ia ] += stack.stackSize;
        	
	        for ( int is = QuiverItem.SELECTION_SIZE; is < QuiverItem.TOTAL_SIZE; ++is )
	        {
	        	ItemStack otherStack = quiverInv.getStackInSlot( is );
	        	if ( otherStack == null )
	        	{
	        		continue;
	        	}
	        	
	        	if ( stack.isItemEqual( otherStack ) && stack.getTagCompound().equals( otherStack.getTagCompound() ) )
	        	{
	        		count[ ia ] += otherStack.stackSize;
	        	}
	        }
        }
        
        for ( int i = 0; i < QuiverItem.SELECTION_SIZE; ++i )
        {
        	ItemStack stack2 = quiverInv.getStackInSlot( i );
        	if ( stack2 == null )
        	{
        		continue;
        	}
        	stack2 = stack2.copy();
        	stack2.stackSize = count[ i ];

        	GL11.glColor3f( 1.f, 1.f, 1.f );
        	renderItemAt( stack2, baseX + 2, baseY - 25 - ( i * 20 ) + 2 );
        }
        
        GL11.glColor4f( 1.f, 1.f, 1.f, 1.f );
	}
	
	private void renderItemAt( ItemStack stack, int x, int y )
	{
		Minecraft mc = FMLClientHandler.instance().getClient();
		ItemStack itemstack = stack;
		int par2=x;
		int par3=y;
		
        if ( itemstack != null )
        {
            float f1 = (float)stack.animationsToGo - 0;

            if (f1 > 0.0F)
            {
                GL11.glPushMatrix();
                float f2 = 1.0F + f1 / 5.0F;
                GL11.glTranslatef((float)(par2 + 8), (float)(par3 + 12), 0.0F);
                GL11.glScalef(1.0F / f2, (f2 + 1.0F) / 2.0F, 1.0F);
                GL11.glTranslatef((float)(-(par2 + 8)), (float)(-(par3 + 12)), 0.0F);
            }

            itemRenderer.renderItemAndEffectIntoGUI(mc.fontRenderer, mc.renderEngine, itemstack, par2, par3);

            if (f1 > 0.0F)
            {
                GL11.glPopMatrix();
            }

            itemRenderer.renderItemOverlayIntoGUI(mc.fontRenderer, mc.renderEngine, itemstack, par2, par3);
        }
	}
	
	private final RenderItem itemRenderer = new RenderItem();
}
