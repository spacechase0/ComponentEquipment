package com.spacechase0.minecraft.componentequipment.client.render.tileentity;

import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glScalef;
import static org.lwjgl.opengl.GL11.glTranslatef;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.spacechase0.minecraft.componentequipment.ComponentEquipment;
import com.spacechase0.minecraft.componentequipment.tileentity.PersistiumInfuserTileEntity;
import com.spacechase0.minecraft.spacecore.client.render.tileentity.ModelTileEntityRenderer;
import com.spacechase0.minecraft.spacecore.util.ClientUtils;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class PersistiumInfuserTileEntityRenderer extends ModelTileEntityRenderer
{
	private static class AnimationInfo
	{
		public boolean activating = false;
		
		public float doorOffset = 0.f;
		
		public float toolRot = 0.f;
		public float toolOffset = 0.f;
		public float orbFade = 0.f;
		public float orbRot = ( new Random() ).nextInt( 360 );
		
		public float beaconOffset = 0.f;
		public float beamLength = 0.f;
	}
	
	public PersistiumInfuserTileEntityRenderer()
	{
		super( "persistiumInfuser", "componentequipment" );
		
		orbModel = AdvancedModelLoader.loadModel( new ResourceLocation( "componentequipment:models/infuserOrb.obj" ) );
	}
	
	@Override
	public void renderTileEntityAt( TileEntity tileEntity, double x, double y, double z, float f )
	{
		//System.out.println(f);
		PersistiumInfuserTileEntity entity = ( PersistiumInfuserTileEntity ) tileEntity;
		if ( !animInfo.containsKey( entity ) )
		{
			animInfo.put( entity, new AnimationInfo() );
		}
		else if ( !entity.getWorldObj().getBlock( entity.xCoord, entity.yCoord, entity.zCoord ).equals( ComponentEquipment.blocks.persistiumInfuser ) )
		{
			animInfo.remove( entity );
			return;
		}
		
		AnimationInfo anim = animInfo.get( entity );
		if ( ( entity.getProgressNeeded() > 0 ) != anim.activating && animationEnded( entity, anim ) )
		{
			anim.activating = ( entity.getProgressNeeded() > 0 );
		}
		updateAnimation( entity, anim );
		
		ClientUtils.bindTexture( "componentequipment:textures/models/" + modelName + ".png" );
		
        glPushMatrix();
        {
	        glTranslatef( ( float ) x + 0.5f, ( float ) y, ( float ) z + 0.5f );
	        GL11.glColor4f( 1.0F, 1.0F, 1.0F, 1.0F );
	        GL11.glEnable( GL11.GL_CULL_FACE );
	        
	        model.renderPart( "base" );

	        // Doors
	        if ( anim.doorOffset <= 0.245f )
	        {
	        	glPushMatrix();
	        	
	        	glTranslatef( 0.f, 0.f, anim.doorOffset );
		        model.renderPart( "door1" );
		        
	        	glTranslatef( 0.f, 0.f, -anim.doorOffset * 2 );
		        model.renderPart( "door2" );

		        glPopMatrix();
	        }
	        
	        // Beacon
	        glPushMatrix();
	        Tessellator tess = Tessellator.instance;
	        tess.setColorOpaque_F( 1.f, 1.f, 1.f );
	        tess.startDrawingQuads();
	        {
	        	Minecraft.getMinecraft().renderEngine.bindTexture( TextureMap.locationBlocksTexture );
	        	
	        	GL11.glColor4f( 1.f, 1.f, 1.f, 1.f );
	        	glTranslatef( -0.25f, anim.beaconOffset, -0.25f );
	        	glScalef( 0.5f, 0.5f, 0.5f );
	        	
		        RenderBlocks rb = new RenderBlocks( entity.getWorldObj() );
		        rb.renderBlockBeacon( net.minecraft.init.Blocks.beacon, ( int ) 0, ( int ) 0, ( int ) 0 );
	        }
	        tess.draw();
	        drawBeaconBeam( entity, anim );
	        glPopMatrix();

	        GL11.glCullFace( GL11.GL_BACK );
	        //GL11.glDisable( GL11.GL_CULL_FACE );
	        // Item
	        glPushMatrix();
	        {
	        	//anim.orbRot+=f*5;
	        	//System.out.println(f);
	        	float par3 = anim.orbRot + f;
	        	float par4 = ( float ) Math.pow( MathHelper.sin(par3 * 0.01F) / 2.0F + 0.2F, 2 );

	        	// Beams
	        	float bob = PersistiumCrystalTileEntityRenderer.bob;
	        	float num = ( PersistiumCrystalTileEntityRenderer.bobDir > 0 ) ? bob : bob;
	        	//System.out.println(num*10);
		        for ( int ip = 0; ip < 4 && anim.orbFade > 0.01f; ++ip )
		        {
			        glPushMatrix();
		        	float par9=0.f;
		        	float Y = (float) y + anim.toolOffset - 1.f;

		        	glTranslatef( 0, 1 + anim.toolOffset, 0.f );
		        	glRotatef( 90 * ip, 0.f, 1.f, 0.f );
		        	glRotatef( 66f + ( num * 20 ), 0.f, 0.f, 1.f );
		        	glRotatef( 47.5f, 1.f, 0.f, 0.f );
		        	
		        	//System.out.println(par3);
		        	float ticks = -par3 / 20f;
		        	Tessellator tessellator = Tessellator.instance;
		            float f2 = (float)anim.orbRot + par9;
		            float f3 = MathHelper.sin(f2 * 0.2F) / 2.0F + 0.5F;
		            f3 = (f3 * f3 + f3) * 0.2F;
		            float f4 = (float)x;
		            float f5 = (float)Y;
		            float f6 = (float)z;
		            float f7 = 0;//MathHelper.sqrt_float(f4 * f4 + f6 * f6);
		            float f8 = 4.45f;//MathHelper.sqrt_float(f4 * f4 + f5 * f5 + f6 * f6);
		            float f9 = 0.0F - (ticks) * 0.01F;
		            float f10 = MathHelper.sqrt_float(f4 * f4 + f5 * f5 + f6 * f6) / 32.0F - (ticks) * 0.01F;
		            
		            byte b0 = 8;
		            GL11.glShadeModel(GL11.GL_SMOOTH);
		            GL11.glDisable(GL11.GL_CULL_FACE);
		            //GL11.glEnable( GL11.GL_BLEND );
		            tessellator.startDrawing(5);
		            RenderHelper.disableStandardItemLighting();
		            Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation("minecraft:textures/entity/endercrystal/endercrystal_beam.png"));
		            for (int i = 0; i <= b0; ++i)
		            {
		                float f11 = par4*2 + MathHelper.sin((float)(i % b0) * (float)Math.PI * 2.0F / (float)b0) * 0.75F;
		                float f12 = MathHelper.cos((float)(i % b0) * (float)Math.PI * 2.0F / (float)b0) * 0.75F;
		                float f13 = (float)(i % b0) * 1.0F / (float)b0;
		                tessellator.setColorRGBA_I(0xFFFFFF, ( int )( anim.orbFade * 255));
		                tessellator.addVertexWithUV((double)(f11*0.5f), (double)(f12*0.5f), 0.0D, (double)f13, (double)f9);
		                tessellator.setColorRGBA_I(0x000000, ( int )( anim.orbFade * 255));
		                tessellator.addVertexWithUV((double)f11*0.2f, (double)f12*0.2f, (double)f8, (double)f13, (double)f10);
		            }
		            tessellator.draw();
		            GL11.glEnable(GL11.GL_CULL_FACE);
		            //GL11.glDisable( GL11.GL_BLEND );
		            GL11.glShadeModel(GL11.GL_FLAT);
		            RenderHelper.enableStandardItemLighting();
			        glPopMatrix();
		        }
		        
		        GL11.glColor4f( 1.f, 1.f, 1.f, 1.f );
	        	
	        	if ( anim.orbFade > 0.f )
	        	{
	        		glTranslatef( 0.f, par4, 0.f );
	        	}
	        	
	        	float incr = ( 1.f / 16 );
	        	glTranslatef( -4.f * incr, ( 12f * incr ) + incr, -2f * incr );
	        	glTranslatef( 0.f, anim.toolOffset, ( anim.toolRot / 90.f ) * 0.15f );
	        	glRotatef( 90.f, 1.f, 0.f, 0.f );
	        	glRotatef( 180.f, 0.f, 1.f, 0.f );
	        	glRotatef( anim.toolRot, 1.f, 0.f, 0.f );
	        	glScalef( 0.4f, 0.4f, 0.4f );
	        	glPushMatrix();
	        	{
	        		drawItem( entity );
	        	}
	        	glPopMatrix();

	        	glTranslatef( -0.75f, 0.25f, 0.f );
	        	glScalef( 2.5f, 2.5f, 2.5f );
	        	
		        // Orb
		        glPushMatrix();
		        if ( anim.orbFade > 0.f )
		        {
		        	Minecraft.getMinecraft().renderEngine.bindTexture( new ResourceLocation("componentequipment:textures/models/" + modelName + ".png" ));
		        	
		        	GL11.glEnable( GL11.GL_BLEND );
		        	
		        	//glTranslatef( 0.5f, -1.5f, 0.f );
		        	GL11.glColor4f( 1.f, 1.f, 1.f, anim.orbFade );
		        	
		            GL11.glRotatef(par3, 0.0F, 1.0F, 0.0F);
		            //GL11.glTranslatef(0.0F, par4, 0.0F);
		            GL11.glRotatef(60.0F, 0.7071F, 0.0F, 0.7071F);
		            orbModel.renderPart( "center_cop" );
		            
		            float f6 = 0.875F;
		            GL11.glScalef(f6, f6, f6);
		            GL11.glRotatef(60.0F, 0.7071F, 0.0F, 0.7071F);
		            GL11.glRotatef(par3, 0.0F, 1.0F, 0.0F);
		            orbModel.renderPart( "center_cop" );
		            
		            GL11.glScalef(f6, f6, f6);
		            GL11.glRotatef(60.0F, 0.7071F, 0.0F, 0.7071F);
		            GL11.glRotatef(par3, 0.0F, 1.0F, 0.0F);
		            orbModel.renderPart( "center" );
		        	
		        	GL11.glDisable( GL11.GL_BLEND );
		        }
		        glPopMatrix();
		        
		        //glPopMatrix();glPushMatrix();
	        }
	        glPopMatrix();
        }
        glPopMatrix();
	}
	
	private void drawItem( PersistiumInfuserTileEntity entity )
	{
		ItemStack stack = entity.getStackInSlot( PersistiumInfuserTileEntity.TOOL_SLOT );
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
	            IIcon icon = item.getIcon( stack, ir, null, null, 0 );
	
	            if (icon == null)
	            {
	                //GL11.glPopMatrix();
	                return;
	            }
	
	            TextureManager re = FMLClientHandler.instance().getClient().renderEngine;
	            ClientUtils.bindItemTexture( item.getSpriteNumber() );
	
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
	
	            if (stack != null && stack.hasEffect() && /*par3 == 0*/true)
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
	
	private void drawBeaconBeam( PersistiumInfuserTileEntity entity, AnimationInfo anim )
	{
		if ( anim.beaconOffset < 0.75f )
		{
			return;
		}
		
		float par2 = 0, par4 = 0, par6 = 0, par8 = 0;
		float f1 = ( float ) Math.max( 0, ( anim.beamLength * 2 ) - 0.5 ) / 256.f;

        Tessellator tessellator = Tessellator.instance;
        ClientUtils.bindTexture( "minecraft:textures/entity/beacon_beam.png" );
        GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, 10497.0F);
        GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, 10497.0F);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDepthMask(true);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
        float f2 = (float) entity.getWorldObj().getTotalWorldTime() + par8;
        float f3 = -f2 * 0.2F - (float)MathHelper.floor_float(-f2 * 0.1F);
        byte b0 = 1;
        double d3 = (double)f2 * 0.025D * (1.0D - (double)(b0 & 1) * 2.5D);
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA(255, 255, 255, 32);
        double d4 = (double)b0 * 0.2D;
        double d5 = 0.5D + Math.cos(d3 + 2.356194490192345D) * d4;
        double d6 = 0.5D + Math.sin(d3 + 2.356194490192345D) * d4;
        double d7 = 0.5D + Math.cos(d3 + (Math.PI / 4D)) * d4;
        double d8 = 0.5D + Math.sin(d3 + (Math.PI / 4D)) * d4;
        double d9 = 0.5D + Math.cos(d3 + 3.9269908169872414D) * d4;
        double d10 = 0.5D + Math.sin(d3 + 3.9269908169872414D) * d4;
        double d11 = 0.5D + Math.cos(d3 + 5.497787143782138D) * d4;
        double d12 = 0.5D + Math.sin(d3 + 5.497787143782138D) * d4;
        double d13 = (double)(256.0F * f1);
        double d14 = 0.0D;
        double d15 = 1.0D;
        double d16 = (double)(-1.0F + f3);
        double d17 = (double)(256.0F * f1) * (0.5D / d4) + d16;
        tessellator.addVertexWithUV(par2 + d5, par4 + d13, par6 + d6, d15, d17);
        tessellator.addVertexWithUV(par2 + d5, par4, par6 + d6, d15, d16);
        tessellator.addVertexWithUV(par2 + d7, par4, par6 + d8, d14, d16);
        tessellator.addVertexWithUV(par2 + d7, par4 + d13, par6 + d8, d14, d17);
        tessellator.addVertexWithUV(par2 + d11, par4 + d13, par6 + d12, d15, d17);
        tessellator.addVertexWithUV(par2 + d11, par4, par6 + d12, d15, d16);
        tessellator.addVertexWithUV(par2 + d9, par4, par6 + d10, d14, d16);
        tessellator.addVertexWithUV(par2 + d9, par4 + d13, par6 + d10, d14, d17);
        tessellator.addVertexWithUV(par2 + d7, par4 + d13, par6 + d8, d15, d17);
        tessellator.addVertexWithUV(par2 + d7, par4, par6 + d8, d15, d16);
        tessellator.addVertexWithUV(par2 + d11, par4, par6 + d12, d14, d16);
        tessellator.addVertexWithUV(par2 + d11, par4 + d13, par6 + d12, d14, d17);
        tessellator.addVertexWithUV(par2 + d9, par4 + d13, par6 + d10, d15, d17);
        tessellator.addVertexWithUV(par2 + d9, par4, par6 + d10, d15, d16);
        tessellator.addVertexWithUV(par2 + d5, par4, par6 + d6, d14, d16);
        tessellator.addVertexWithUV(par2 + d5, par4 + d13, par6 + d6, d14, d17);
        tessellator.draw();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDepthMask(false);
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA(255, 255, 255, 32);
        double d18 = 0.2D;
        double d19 = 0.2D;
        double d20 = 0.8D;
        double d21 = 0.2D;
        double d22 = 0.2D;
        double d23 = 0.8D;
        double d24 = 0.8D;
        double d25 = 0.8D;
        double d26 = (double)(256.0F * f1);
        double d27 = 0.0D;
        double d28 = 1.0D;
        double d29 = (double)(-1.0F + f3);
        double d30 = (double)(256.0F * f1) + d29;
        tessellator.addVertexWithUV(par2 + d18, par4 + d26, par6 + d19, d28, d30);
        tessellator.addVertexWithUV(par2 + d18, par4, par6 + d19, d28, d29);
        tessellator.addVertexWithUV(par2 + d20, par4, par6 + d21, d27, d29);
        tessellator.addVertexWithUV(par2 + d20, par4 + d26, par6 + d21, d27, d30);
        tessellator.addVertexWithUV(par2 + d24, par4 + d26, par6 + d25, d28, d30);
        tessellator.addVertexWithUV(par2 + d24, par4, par6 + d25, d28, d29);
        tessellator.addVertexWithUV(par2 + d22, par4, par6 + d23, d27, d29);
        tessellator.addVertexWithUV(par2 + d22, par4 + d26, par6 + d23, d27, d30);
        tessellator.addVertexWithUV(par2 + d20, par4 + d26, par6 + d21, d28, d30);
        tessellator.addVertexWithUV(par2 + d20, par4, par6 + d21, d28, d29);
        tessellator.addVertexWithUV(par2 + d24, par4, par6 + d25, d27, d29);
        tessellator.addVertexWithUV(par2 + d24, par4 + d26, par6 + d25, d27, d30);
        tessellator.addVertexWithUV(par2 + d22, par4 + d26, par6 + d23, d28, d30);
        tessellator.addVertexWithUV(par2 + d22, par4, par6 + d23, d28, d29);
        tessellator.addVertexWithUV(par2 + d18, par4, par6 + d19, d27, d29);
        tessellator.addVertexWithUV(par2 + d18, par4 + d26, par6 + d19, d27, d30);
        tessellator.draw();
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDepthMask(true);
	}
	
	private boolean animationEnded( PersistiumInfuserTileEntity entity, AnimationInfo anim )
	{
		/*if ( true )*/ return true;
		/*
		boolean result = true;
		if ( !anim.activating )
		{
			result = result && ( anim.doorOffset <= 0.f );
			result = result && ( anim.toolRot <= 0.f );
			result = result && ( anim.toolOffset <= 0.f );
			result = result && ( anim.beaconOffset <= 0.f );
		}
		else
		{
			result = result && ( anim.doorOffset >= 0.25f );
			result = result && ( anim.toolRot >= 90.f );
			result = result && ( anim.toolOffset >= 1.5f );
			result = result && ( anim.beaconOffset >= 0.5f );
		}
		
		return result;
		*/
	}
	
	private void updateAnimation( PersistiumInfuserTileEntity entity, AnimationInfo anim )
	{
		//anim.doorOffset=anim.toolRot=anim.toolOffset=anim.orbFade=anim.beaconOffset=anim.beamLength=0.00f;
		if ( anim.activating )
		{
			if ( anim.doorOffset < DOOR_MAX )
			{
				anim.doorOffset += 0.001f;
			}
			else if ( anim.beaconOffset < BEACON_MAX && anim.toolOffset > 1.25f )
			{
				anim.beaconOffset += 0.0025f;
			}
			else if ( anim.beaconOffset >= BEACON_MAX && anim.beamLength < BEAM_MAX && anim.toolOffset >= TOOL_OFF_MAX )
			{
				anim.beamLength += 0.075f;
			}
			
			if ( anim.toolRot < TOOL_ROT_MAX )
			{
				anim.toolRot += 0.25f;
			}
			
			if ( anim.toolOffset < TOOL_OFF_MAX )
			{
				anim.toolOffset += 0.005f;
			}
			else if ( anim.orbFade < ORB_MAX && anim.beamLength >= BEAM_MAX )
			{
				anim.orbFade += 0.05f;
			}
			else if ( anim.orbFade >= ORB_MAX )
			{
				// ...
			}
		}
		else
		{
			if ( anim.beamLength > 0 && anim.orbFade <= 0.f )
			{
				anim.beamLength -= 0.075f;
			}
			else if ( anim.beaconOffset > 0 && anim.beamLength <= 0 )
			{
				anim.beaconOffset -= 0.0025f;
			}
			else if ( anim.doorOffset > 0 )
			{
				anim.doorOffset -= 0.001f;
			}
			
			if ( anim.toolRot > 0 )
			{
				anim.toolRot -= 0.25f;
			}
			
			if ( anim.toolOffset > 0 && anim.beamLength <= 0 )
			{
				anim.toolOffset -= 0.005f;
			}
			else if ( anim.orbFade > 0 )
			{
				anim.orbFade -= 0.05f;
			}
			else if ( anim.orbFade >= ORB_MAX )
			{
				// ...
			}
		}
		
		anim.orbRot += 1.5;
		/*
		final float MAX = 10000;
		for ( anim.orbRot += 1.5; anim.orbRot >= MAX; anim.orbRot -= MAX );
		for (                   ; anim.orbRot <    0; anim.orbRot += MAX );
		//*/
	}
	
	private RenderBlocks getRenderBlocks()
	{
        RenderBlocks renderBlocksInstance;
        try
        {
	        Field field = ItemRenderer.class.getDeclaredFields()[ 4 ];
	        field.setAccessible( true );
	        renderBlocksInstance = ( RenderBlocks ) field.get( RenderManager.instance.itemRenderer );
        }
        catch ( Exception exception )
        {
        	exception.printStackTrace();
        	return null;
        }
        
        return renderBlocksInstance;
	}
	
	private Map< PersistiumInfuserTileEntity, AnimationInfo > animInfo = new HashMap< PersistiumInfuserTileEntity, AnimationInfo >();
	private IModelCustom orbModel;

	private static final float DOOR_MAX = 0.25f;
	private static final float TOOL_ROT_MAX = 90.f;
	private static final float TOOL_OFF_MAX = 3.f;
	private static final float BEACON_MAX = 0.75f;
	private static final float BEAM_MAX = TOOL_OFF_MAX + 0.1f;
	private static final float ORB_MAX = 0.75f;
}
