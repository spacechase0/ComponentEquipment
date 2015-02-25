package com.spacechase0.minecraft.componentequipment.client.render.tool;

import static net.minecraft.init.Blocks.*;
import static net.minecraft.init.Items.*;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelChest;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.BlockFluidBase;

import org.lwjgl.opengl.GL11;

import com.spacechase0.minecraft.componentequipment.item.EquipmentItem;
import com.spacechase0.minecraft.componentequipment.tool.Modifier;
import com.spacechase0.minecraft.componentequipment.tool.modifier.BackpackModifier;
import com.spacechase0.minecraft.spacecore.util.ClientUtils;

public class WaterWalkRenderer implements IModifierRenderer
{
	@Override
	public void render( EntityLivingBase entity, ItemStack stack, String modName, float par2, float par3, float par4, float par5, float par6, float par7 )
	{
		TextureManager re = Minecraft.getMinecraft().getTextureManager();
		
		ClientUtils.bindTexture( "componentequipment:textures/armor/modifiers/waterWalk/footstep.png" );
		
		boolean render = false;
		{
			int x = ( int ) entity.posX;
			int y = ( int ) entity.boundingBox.minY - 1;
			int z = ( int ) entity.posZ;
			
			Block block = entity.worldObj.getBlock( x, y, z );
			Block above = entity.worldObj.getBlock( x, y + 1, z );
			
			render = render || ( block instanceof BlockLiquid );
			
			if ( block instanceof BlockFluidBase )
			{
				render = render || ( ( ( BlockFluidBase ) block ).getFilledPercentage( entity.worldObj, x, y, z ) >= 1.0 );
			}
			
			render = render && ( !( above instanceof BlockLiquid ) && !( above instanceof BlockFluidBase ) );
		}
		if ( !render ) return;
		
		GL11.glEnable( GL11.GL_BLEND );
		GL11.glDisable( GL11.GL_LIGHTING );

		GL11.glPushMatrix();
		{
			float alpha = System.currentTimeMillis() % 6000 / 3000.f;
			if ( alpha > 1 ) alpha = 2 - alpha;
			
			GL11.glTranslatef( -0.725f, 1.55f, -0.35f );
			GL11.glScalef( 0.7f, 0.75f, 0.7f );
			GL11.glColor4f( 1.f, 1.f, 1.f, alpha );
			
			Tessellator tess = Tessellator.instance;
			tess.startDrawingQuads();
			tess.addVertexWithUV( 0, 0, 0, 0, 0 );
			tess.addVertexWithUV( 2, 0, 0, 1, 0 );
			tess.addVertexWithUV( 2, 0, 1, 1, 1 );
			tess.addVertexWithUV( 0, 0, 1, 0, 1 );
			tess.draw();
		}
		GL11.glPopMatrix();
		
		GL11.glDisable( GL11.GL_BLEND );
	}
}
