package com.spacechase0.minecraft.componentequipment.client.model;

import java.nio.FloatBuffer;
import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.spacechase0.minecraft.spacecore.util.ClientUtils;

import cpw.mods.fml.client.FMLClientHandler;

public class MysteriousOrbModel extends ModelBase
{
	public MysteriousOrbModel()
	{
		base = new ModelRenderer( this, 0, 0 );
		base.addBox( 4, 4, 4, 8, 8, 8 );
	}
	
	public void render()
	{
		base = new ModelRenderer( this, 0, 0 );
		base.addBox( 4, 4, 4, 8, 8, 8 );
		
		GL11.glTranslatef( 0.25f, 0.25f, 0.25f );
		
		{
			float par2=0,par4=0,par6=0;
	        float f1 = (float)-FMLClientHandler.instance().getClient().thePlayer.posY;//(float)this.tileEntityRenderer.playerX;
	        float f2 = (float)-FMLClientHandler.instance().getClient().thePlayer.posZ;//(float)this.tileEntityRenderer.playerY;
	        float f3 = (float)FMLClientHandler.instance().getClient().thePlayer.posX;//(float)this.tileEntityRenderer.playerZ;
	        GL11.glDisable(GL11.GL_LIGHTING);
	        Random random = new Random(31100L);
	        float f4 = 0.5F;

	        for (int i = 0; i < 16; ++i)
	        {
	            GL11.glPushMatrix();
	            float f5 = (float)(16 - i);
	            float f6 = 0.0625F / 2;
	            float f7 = 1.0F / (f5 + 1.0F);

	            if (i == 0)
	            {
	                ClientUtils.bindTexture( "minecraft:textures/environment/end_sky.png" );
	                f7 = 0.1F;
	                f5 = 65.0F;
	                f6 = 0.125F;
	                GL11.glEnable(GL11.GL_BLEND);
	                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	            }

	            if (i == 1)
	            {
	            	ClientUtils.bindTexture( "minecraft:textures/entity/end_portal.png" );
	                GL11.glEnable(GL11.GL_BLEND);
	                GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
	                f6 = 0.5F;
	            }

	            float f8 = (float)(-(par4 + (double)f4));
	            float f9 = f8 + ActiveRenderInfo.objectY;
	            float f10 = f8 + f5 + ActiveRenderInfo.objectY;
	            float f11 = f9 / f10;
	            f11 += (float)(par4 + (double)f4);
	            GL11.glTranslatef(f1, f11, f3);
	            GL11.glTexGeni(GL11.GL_S, GL11.GL_TEXTURE_GEN_MODE, GL11.GL_OBJECT_LINEAR);
	            GL11.glTexGeni(GL11.GL_T, GL11.GL_TEXTURE_GEN_MODE, GL11.GL_OBJECT_LINEAR);
	            GL11.glTexGeni(GL11.GL_R, GL11.GL_TEXTURE_GEN_MODE, GL11.GL_OBJECT_LINEAR);
	            GL11.glTexGeni(GL11.GL_Q, GL11.GL_TEXTURE_GEN_MODE, GL11.GL_EYE_LINEAR);
	            GL11.glTexGen(GL11.GL_S, GL11.GL_OBJECT_PLANE, this.func_76907_a(1.0F, 0.0F, 0.0F, 0.0F));
	            GL11.glTexGen(GL11.GL_T, GL11.GL_OBJECT_PLANE, this.func_76907_a(0.0F, 0.0F, 1.0F, 0.0F));
	            GL11.glTexGen(GL11.GL_R, GL11.GL_OBJECT_PLANE, this.func_76907_a(0.0F, 0.0F, 0.0F, 1.0F));
	            GL11.glTexGen(GL11.GL_Q, GL11.GL_EYE_PLANE, this.func_76907_a(0.0F, 1.0F, 0.0F, 0.0F));
	            GL11.glEnable(GL11.GL_TEXTURE_GEN_S);
	            GL11.glEnable(GL11.GL_TEXTURE_GEN_T);
	            GL11.glEnable(GL11.GL_TEXTURE_GEN_R);
	            GL11.glEnable(GL11.GL_TEXTURE_GEN_Q);
	            GL11.glPopMatrix();
	            GL11.glMatrixMode(GL11.GL_TEXTURE);
	            GL11.glPushMatrix();
	            GL11.glLoadIdentity();
	            GL11.glTranslatef(0.0F, (float)(Minecraft.getSystemTime() % 700000L) / 700000.0F, 0.0F);
	            //GL11.glTranslatef(ActiveRenderInfo.objectX*f5/f9, ActiveRenderInfo.objectY*f5/f9, ActiveRenderInfo.objectZ*f5/f9);
	            GL11.glScalef(f6, f6, f6);
	            GL11.glTranslatef(0.5F, 0.5F, 0.0F);
	            GL11.glRotatef((float)(i * i * 4321 + i * 9) * 2.0F, 0.0F, 0.0F, 1.0F);
	            GL11.glTranslatef(-0.5F, -0.5F, 0.0F);
	            GL11.glTranslatef(-f1, -f3, -f2);
	            f9 = f8 + ActiveRenderInfo.objectY;
	            GL11.glTranslatef(ActiveRenderInfo.objectX * f5 / f9, ActiveRenderInfo.objectZ * f5 / f9, -f2);
	            Tessellator tessellator = Tessellator.instance;
	            tessellator.startDrawingQuads();
	            f11 = random.nextFloat() * 0.5F + 0.1F;
	            float f12 = random.nextFloat() * 0.5F + 0.4F;
	            float f13 = random.nextFloat() * 0.5F + 0.5F;

	            if (i == 0)
	            {
	                f13 = 1.0F;
	                f12 = 1.0F;
	                f11 = 1.0F;
	            }

	            tessellator.setColorRGBA_F(f11 * f7, f12 * f7, f13 * f7, 1.0F);
	            // Top
	            //GL11.glMatrixMode(GL11.GL_PROJECTION);
	            //GL11.glScalef(2.f,2.f,2.f);
	            //*
	            tessellator.addVertex(par2, par4 + (double)f4, par6);
	            tessellator.addVertex(par2, par4 + (double)f4, par6 + 0.50D);
	            tessellator.addVertex(par2 + 0.50D, par4 + (double)f4, par6 + 0.50D);
	            tessellator.addVertex(par2 + 0.50D, par4 + (double)f4, par6);
	            
	            // Bottom
	            tessellator.addVertex(par2, par4 + (double)f4*0, par6);
	            tessellator.addVertex(par2, par4 + (double)f4*0, par6 + 0.50D);
	            tessellator.addVertex(par2 + 0.50D, par4 + (double)f4*0, par6 + 0.50D);
	            tessellator.addVertex(par2 + 0.50D, par4 + (double)f4*0, par6);
	            //*/
	            // side1
	            for ( int im = 0; im < 2; ++im )
	            {
	            	//if(im==0)continue;
	            	float tx=-0.5f,ty=0,tz=0.0f;
	            	if ( im == 1 ) tessellator.addTranslation(tx,ty,tz);
		            tessellator.addVertex(par2+f4, par4, par6);
		            tessellator.addVertex(par2+f4, par4+0.5D, par6);
		            tessellator.addVertex(par2+f4, par4+0.5D, par6 + 0.50D);
		            tessellator.addVertex(par2+f4, par4, par6+0.5D);
	            	if ( im == 1 ) tessellator.addTranslation(-tx,-ty,-tz);
	            }
	            
	            // side2
	            for ( int im = 0; im < 2; ++im )
	            {
	            	//if(im==0)continue;
	            	float tx=0,ty=0,tz=-0.5f;
	            	if ( im == 1 ) tessellator.addTranslation(tx,ty,tz);
		            tessellator.addVertex(par2, par4, par6+f4);
		            tessellator.addVertex(par2+0.5D, par4, par6+f4);
		            tessellator.addVertex(par2+0.5D, par4+0.5D, par6+f4);
		            tessellator.addVertex(par2, par4+0.5D, par6+f4);
	            	if ( im == 1 ) tessellator.addTranslation(-tx,-ty,-tz);
	            }
	            
	            GL11.glDisable(GL11.GL_CULL_FACE);
	            tessellator.draw();
	            GL11.glEnable(GL11.GL_CULL_FACE);
	            GL11.glPopMatrix();
	            GL11.glMatrixMode(GL11.GL_MODELVIEW);
	        }

	        GL11.glDisable(GL11.GL_BLEND);
	        GL11.glDisable(GL11.GL_TEXTURE_GEN_S);
	        GL11.glDisable(GL11.GL_TEXTURE_GEN_T);
	        GL11.glDisable(GL11.GL_TEXTURE_GEN_R);
	        GL11.glDisable(GL11.GL_TEXTURE_GEN_Q);
	        GL11.glEnable(GL11.GL_LIGHTING);
		}
		
		// Base
		//binder.bindTexture( "/mods/componentequipment/textures/models/armorStandBase.png" );
		//base.render( 1.f / 16 );
	}

    private FloatBuffer func_76907_a(float par1, float par2, float par3, float par4)
    {
        this.field_76908_a.clear();
        this.field_76908_a.put(par1).put(par2).put(par3).put(par4);
        this.field_76908_a.flip();
        return this.field_76908_a;
    }
	
	private ModelRenderer base;
    FloatBuffer field_76908_a = GLAllocation.createDirectFloatBuffer(16);
}
