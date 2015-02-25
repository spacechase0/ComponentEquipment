package com.spacechase0.minecraft.componentequipment.client.render.tileentity;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.spacechase0.minecraft.componentequipment.client.model.MysteriousOrbModel;
import com.spacechase0.minecraft.componentequipment.tileentity.MysteriousOrbTileEntity;

public class MysteriousOrbTileEntityRenderer extends TileEntitySpecialRenderer
{
	@Override
	public void renderTileEntityAt( TileEntity entity, double x, double y, double z, float f )
	{
		if ( !( entity instanceof MysteriousOrbTileEntity ) )
		{
			return;
		}
		
		MysteriousOrbTileEntity orb = ( MysteriousOrbTileEntity ) entity;

        GL11.glPushMatrix();
        GL11.glTranslatef( ( float ) x, ( float ) y, ( float ) z );
        GL11.glColor4f( 1.0F, 1.0F, 1.0F, 1.0F );

		model.render();
        
        GL11.glPopMatrix();
	}
	
	private static MysteriousOrbModel model = new MysteriousOrbModel();
}
