package com.spacechase0.minecraft.componentequipment.client.render.tileentity;

import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glScalef;
import static org.lwjgl.opengl.GL11.glTranslatef;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.spacechase0.minecraft.componentequipment.ComponentEquipment;
import com.spacechase0.minecraft.componentequipment.client.model.ModificationStandModel;
import com.spacechase0.minecraft.componentequipment.tileentity.ModificationStandTileEntity;
import com.spacechase0.minecraft.spacecore.util.ClientUtils;

import cpw.mods.fml.client.FMLClientHandler;

public class ModificationStandTileEntityRenderer extends TileEntitySpecialRenderer
{
	private static class AnimationInfo
	{
		public float toolRot = 0.f;
	}
	
	@Override
	public void renderTileEntityAt( TileEntity entity, double x, double y, double z, float f )
	{
		ModificationStandTileEntity stand = ( ModificationStandTileEntity ) entity;
		if ( !animInfo.containsKey( stand ) )
		{
			animInfo.put( stand, new AnimationInfo() );
		}
		else if ( !stand.getWorldObj().getBlock( stand.xCoord, stand.yCoord, stand.zCoord ).equals( ComponentEquipment.blocks.modificationStand ) )
		{
			animInfo.remove( stand );
			return;
		}
		
		AnimationInfo anim = animInfo.get( stand );
		updateAnimation( stand, anim );

        GL11.glPushMatrix();
        {
	        GL11.glTranslatef( ( float ) x, ( float ) y, ( float ) z );
	        GL11.glColor4f( 1.0F, 1.0F, 1.0F, 1.0F );

	        ClientUtils.bindTexture( "componentequipment:textures/models/modStand.png" );
	        model.render();
        }
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        {
	        GL11.glTranslatef( ( float ) x + 0.5f, ( float ) y + 0.25f, ( float ) z + 0.5f );

        	glRotatef( anim.toolRot, 0.f, 1.f, 0.f );
	        GL11.glColor4f( 1.0F, 1.0F, 1.0F, 1.0F );

        	float incr = ( 1.f / 16 );
        	glTranslatef( 1.25f * incr, 0.f, 0.5f * incr );
        	//glTranslatef( -4.f * incr, ( 12f * incr ) + incr, -2f * incr );
        	//glRotatef( 90.f, 1.f, 0.f, 0.f );
        	glRotatef( -45.f, 0.f, 0.f, 1.f );
        	glScalef( 0.3f, 0.3f, 0.3f );
        	
	        drawItem( stand );
        }
        GL11.glPopMatrix();
	}
	
	private void drawItem( ModificationStandTileEntity entity )
	{
		ItemStack stack = entity.getStackInSlot( 0 );
		if ( stack == null )
		{
			return;
		}
		/*
        IItemRenderer customRenderer = MinecraftForgeClient.getItemRenderer(stack, IItemRenderer.ItemRenderType.EQUIPPED);

        Block block = null;
        if (stack.getItem() instanceof ItemBlock && stack.itemID < Block.blocksList.length)
        {
            block = Block.blocksList[stack.itemID];
        }
        
        RenderBlocks renderBlocksInstance = getRenderBlocks();
        if ( renderBlocksInstance == null )
        {
        	return;
        }
        
        if (customRenderer != null)
        {
        	FMLClientHandler.instance().getClient().renderEngine.bindTexture(stack.getItemSpriteNumber() == 0 ? "/terrain.png" : "/gui/items.png");
            ForgeHooksClient.renderEquippedItem(customRenderer, renderBlocksInstance, nullentityliving, stack);
        }
        else if (block != null && stack.getItemSpriteNumber() == 0 && RenderBlocks.renderItemIn3d(Block.blocksList[stack.itemID].getRenderType()))
        {
            FMLClientHandler.instance().getClient().renderEngine.bindTexture("/terrain.png");
            renderBlocksInstance.renderBlockAsItem(Block.blocksList[stack.itemID], stack.getItemDamage(), 1.0F);
        }
        else*/
        {
			Item item = stack.getItem();
			for ( int ir = 0; ir < item.getRenderPasses( stack.getItemDamage() ); ++ir )
			{
	            int i = item.getColorFromItemStack(stack, ir);
	            float fr = (float)(i >> 16 & 255) / 255.0F;
	            float fg = (float)(i >> 8 & 255) / 255.0F;
	            float fb = (float)(i & 255) / 255.0F;
	            GL11.glColor4f(fr, fg, fb, 1.0F);
	            
	            IIcon icon = item.getIcon( stack, ir, null, null, 0 );
	
	            if (icon == null)
	            {
	                //GL11.glPopMatrix();
	                return;
	            }
	
	            TextureManager re = FMLClientHandler.instance().getClient().renderEngine;
	            ClientUtils.bindItemTexture( stack.getItemSpriteNumber() );
	
	            Tessellator tessellator = Tessellator.instance;
	            float f = icon.getMinU();
	            float f1 = icon.getMaxU();
	            float f2 = icon.getMinV();
	            float f3 = icon.getMaxV();
	            float f4 = 0.0F;
	            float f5 = 0.3F;
	            GL11.glPushMatrix();
	            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
	            GL11.glTranslatef(-f4, -f5, 0.0F);
	            float f6 = 1.5F;
	            GL11.glScalef(f6, f6, f6);
	            //GL11.glRotatef(50.0F, 0.0F, 1.0F, 0.0F);
	            //GL11.glRotatef(335.0F, 0.0F, 0.0F, 1.0F);
	            GL11.glTranslatef(-0.9375F, -0.0625F, 0.0F);
	            RenderManager.instance.itemRenderer.renderItemIn2D(tessellator, f1, f2, f, f3, icon.getIconWidth(), icon.getIconHeight(), 0.0625F);
	
	            if (stack != null && stack.hasEffect( ir ) && /*par3 == 0*/true)
	            {
	                GL11.glDepthFunc(GL11.GL_EQUAL);
	                GL11.glDisable(GL11.GL_LIGHTING);
	                ClientUtils.bindTexture( "minecraft:textures/misc/enchanted_item_glint.png" );
	                GL11.glEnable(GL11.GL_BLEND);
	                GL11.glBlendFunc(GL11.GL_SRC_COLOR, GL11.GL_ONE);
	                float f7 = 0.76F;
	                GL11.glColor4f(0.5F * f7, 0.25F * f7, 0.8F * f7, 1.0F);
	                GL11.glMatrixMode(GL11.GL_TEXTURE);
	                GL11.glPushMatrix();
	                float f8 = 0.125F;
	                GL11.glScalef(f8, f8, f8);
	                float f9 = (float)(Minecraft.getSystemTime() % 3000L) / 3000.0F * 8.0F;
	                GL11.glTranslatef(f9, 0.0F, 0.0F);
	                GL11.glRotatef(-50.0F, 0.0F, 0.0F, 1.0F);
	                RenderManager.instance.itemRenderer.renderItemIn2D(tessellator, 0.0F, 0.0F, 1.0F, 1.0F, 256, 256, 0.0625F);
	                GL11.glPopMatrix();
	                GL11.glPushMatrix();
	                GL11.glScalef(f8, f8, f8);
	                f9 = (float)(Minecraft.getSystemTime() % 4873L) / 4873.0F * 8.0F;
	                GL11.glTranslatef(-f9, 0.0F, 0.0F);
	                GL11.glRotatef(10.0F, 0.0F, 0.0F, 1.0F);
	                RenderManager.instance.itemRenderer.renderItemIn2D(tessellator, 0.0F, 0.0F, 1.0F, 1.0F, 256, 256, 0.0625F);
	                GL11.glPopMatrix();
	                GL11.glMatrixMode(GL11.GL_MODELVIEW);
	                GL11.glDisable(GL11.GL_BLEND);
	                GL11.glEnable(GL11.GL_LIGHTING);
	                GL11.glDepthFunc(GL11.GL_LEQUAL);
	            }
	            GL11.glPopMatrix();
			}
        }
	}
	
	private void updateAnimation( ModificationStandTileEntity entity, AnimationInfo anim )
	{
		for ( anim.toolRot += 0.25f; anim.toolRot >= 360.f; anim.toolRot -= 360.f );
	}
	
	private static ModificationStandModel model = new ModificationStandModel();
	private Map< ModificationStandTileEntity, AnimationInfo > animInfo = new HashMap< ModificationStandTileEntity, AnimationInfo >();
}
