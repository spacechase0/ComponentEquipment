package com.spacechase0.minecraft.componentequipment.client.render.tool;

import static net.minecraft.init.Blocks.*;
import static net.minecraft.init.Items.*;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelChest;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.spacechase0.minecraft.componentequipment.ComponentEquipment;
import com.spacechase0.minecraft.componentequipment.client.ClientProxy;
import com.spacechase0.minecraft.spacecore.util.ClientUtils;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/*
        this.bipedHead.rotateAngleY = par4 / (180F / (float)Math.PI);
        this.bipedHead.rotateAngleX = par5 / (180F / (float)Math.PI);
        */

public class JukeboxRenderer implements IModifierRenderer
{
	@Override
	public void render( EntityLivingBase entity, ItemStack stack, String modName, float par2, float par3, float par4, float par5, float par6, float par7 )
	{
		TextureManager re = Minecraft.getMinecraft().getTextureManager();

		re.bindTexture( TextureMap.locationBlocksTexture );
		
		RenderBlocks rb = new RenderBlocks();
		rb.blockAccess = new DummyBlockAccess();

		//GL11.glTranslatef( 0, -0.5f, 0 );
		GL11.glRotatef( par5, 0, 1, 0 );
		GL11.glRotatef( par6, 1, 0, 0 );
		GL11.glRotatef( 180, 1, 0, 0 );
		GL11.glTranslatef( 0, 0.5f, 0 );
		GL11.glScalef( 0.35f, 0.35f, 0.35f );
		
		rb.useInventoryTint = true;
		rb.renderBlockAsItem( jukebox, 0, 1 );
		
		GL11.glRotatef( 90, 0, 1, 0 );
		GL11.glScalef( 0.6f, 0.6f, 0.6f );
		GL11.glTranslatef( 0.6f, 0.5f, 0 );
		drawItem();
	}

	
	private void drawItem()
	{
		ItemStack stack = ( ( ClientProxy ) ComponentEquipment.proxy ).playingRecord;
		//System.out.println(stack);
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
	            ClientUtils.bindTexture( stack.getItemSpriteNumber() );
	
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
	
	private static class DummyBlockAccess implements IBlockAccess
	{
		@Override
		public Block getBlock( int x, int y, int z )
		{
			return air;
		}

		@Override
		public TileEntity getTileEntity( int x, int y, int z )
		{
			return null;
		}

		@Override
		public int getLightBrightnessForSkyBlocks( int x, int y, int z, int l )
		{
			return 0xFFFFFFFF;
		}

		@Override
		public int getBlockMetadata( int x, int y, int z )
		{
			return 0;
		}

		@Override
		public boolean isAirBlock( int x, int y, int z )
		{
			return true;
		}

		@Override
		public BiomeGenBase getBiomeGenForCoords( int x, int z )
		{
			return BiomeGenBase.ocean;
		}

		@Override
		public int getHeight()
		{
			return 1;
		}

		@Override
		public boolean extendedLevelsInChunkCache()
		{
			return false; // ?
		}

		@Override
		public int isBlockProvidingPowerTo( int x, int y, int z, int l )
		{
			return 0;
		}

		@Override
		public boolean isSideSolid( int x, int y, int z, ForgeDirection side, boolean _default )
		{
			return false;
		}
	}
}
