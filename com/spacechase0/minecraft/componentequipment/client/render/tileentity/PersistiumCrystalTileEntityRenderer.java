package com.spacechase0.minecraft.componentequipment.client.render.tileentity;

import net.minecraft.tileentity.TileEntity;

import org.lwjgl.opengl.GL11;

import com.spacechase0.minecraft.spacecore.client.render.tileentity.ModelTileEntityRenderer;

public class PersistiumCrystalTileEntityRenderer extends ModelTileEntityRenderer
{
	public PersistiumCrystalTileEntityRenderer()
	{
		super( "crystal", "componentequipment" );
	}
	
	@Override
	public void renderTileEntityAt( TileEntity entity, double x, double y, double z, float f )
	{
        GL11.glPushMatrix();
        {
        	// TODO: Currently goes faster if multiple are visible. Fix that.
        	bob += Math.pow( Math.cbrt( 0.165 - Math.abs( bob ) ), 9.5 ) * bobDir;
        	if ( Math.abs( bob ) > 0.1f )
        	{
        		bob = 0.1f * bobDir;
        		bobDir = -bobDir;
        	}
        	
	        GL11.glTranslatef( 0.f, bob + 0.15f, 0.f );
	        //GL11.glScalef( 1.5f, 1.5f, 1.5f );
	        super.renderTileEntityAt( entity, x, y, z, f );
        }
        GL11.glPopMatrix();
	}
	
	public static float bob = 0.f;
	public static int bobDir = 1;
}
