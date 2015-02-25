package com.spacechase0.minecraft.componentequipment.client.render.item;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import com.spacechase0.minecraft.componentequipment.client.model.ModificationStandModel;
import com.spacechase0.minecraft.spacecore.util.ClientUtils;

public class ModificationStandItemRenderer implements IItemRenderer
{
	public ModificationStandItemRenderer( ModificationStandModel theModel )
	{
		model = theModel;
	}
	
	@Override
	public boolean handleRenderType( ItemStack item, ItemRenderType type )
	{
		return true;
	}

	@Override
	public boolean shouldUseRenderHelper( ItemRenderType type, ItemStack item, ItemRendererHelper helper )
	{
		return ( helper == IItemRenderer.ItemRendererHelper.ENTITY_ROTATION || helper == IItemRenderer.ItemRendererHelper.ENTITY_BOBBING );
	}

	@Override
	public void renderItem( ItemRenderType type, ItemStack stack, Object... data )
	{
		if ( data[ 0 ] instanceof RenderBlocks )
		{
			RenderBlocks blocks = ( RenderBlocks ) data[ 0 ];
			re = blocks.minecraftRB.renderEngine;
		}
		else if ( data.length > 1 && data[ 1 ] instanceof TextureManager )
		{
			re = ( TextureManager ) data[ 1 ];
		}

		GL11.glPushMatrix();
		{
			if ( type == IItemRenderer.ItemRenderType.ENTITY )
			{
				GL11.glTranslatef( -0.5f, 0.f, -0.5f );
			}
			else if ( type == IItemRenderer.ItemRenderType.EQUIPPED )
			{
				GL11.glRotatef( -45.f, 1.f, 0.f, 0.f );
				GL11.glRotatef( 45.f / 2, 0.f, 0.f, 1.f );
			}
			else if ( type == IItemRenderer.ItemRenderType.INVENTORY )
			{
				GL11.glScalef( 12.f, 12.f, 12.f );
				GL11.glRotatef( 135.f + ( 45.f / 4 ), 1.f, 0.f, 0.f );
				GL11.glRotatef( 45.f, 0.f, 1.f, 0.f );
				//GL11.glRotatef( 135.f, 0.f, 0.f, 1.f );
				GL11.glTranslatef( 0.f, -1.25f, 0.f );
			}

			ClientUtils.bindTexture( "componentequipment:textures/models/modStand.png" );
			model.render();
		}
		GL11.glPopMatrix();
		GL11.glColor3f( 1.f, 1.f, 1.f );
		
		//re.resetBoundTexture();
	}
	
	private TextureManager re;
	private ModificationStandModel model;
}
