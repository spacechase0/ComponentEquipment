package com.spacechase0.minecraft.componentequipment.client.render.item;

import static com.spacechase0.minecraft.componentequipment.client.render.tileentity.PersistiumCrystalTileEntityRenderer.bob;
import static com.spacechase0.minecraft.componentequipment.client.render.tileentity.PersistiumCrystalTileEntityRenderer.bobDir;
import net.minecraft.item.ItemStack;

import org.lwjgl.opengl.GL11;

import com.spacechase0.minecraft.spacecore.client.render.item.ModelItemRenderer;

public class PersistiumCrystalItemRenderer extends ModelItemRenderer
{
	public PersistiumCrystalItemRenderer()
	{
		super( "crystal", "componentequipment" );
	}

	@Override
	public void renderItem( ItemRenderType type, ItemStack stack, Object... data )
	{
        GL11.glPushMatrix();
        {
        	bob += Math.pow( Math.cbrt( 0.165 - Math.abs( bob ) ), 9.5 ) * bobDir;
        	if ( Math.abs( bob ) > 0.1f )
        	{
        		bob = 0.1f * bobDir;
        		bobDir = -bobDir;
        	}
        	
	        GL11.glTranslatef( 0.f, bob + 0.15f, 0.f );
	        //GL11.glScalef( 1.5f, 1.5f, 1.5f );
	        super.renderItem( type, stack, data );
        }
        GL11.glPopMatrix();
	}
}
