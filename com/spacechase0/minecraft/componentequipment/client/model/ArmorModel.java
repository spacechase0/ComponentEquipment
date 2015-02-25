package com.spacechase0.minecraft.componentequipment.client.model;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.spacechase0.minecraft.componentequipment.client.render.tool.IModifierRenderer;
import com.spacechase0.minecraft.componentequipment.item.ArmorItem;
import com.spacechase0.minecraft.componentequipment.tool.Armor;
import com.spacechase0.minecraft.componentequipment.tool.ArmorData;
import com.spacechase0.minecraft.spacecore.util.ClientUtils;

import cpw.mods.fml.client.FMLClientHandler;

public class ArmorModel extends ModelBiped
{
	public ArmorModel( EntityLivingBase entity, String theType )
	{
		super( ( theType.equals( "chestplate" ) ) ? 1.f : 0.5f );
		living = entity;
		type = theType;
		
		Armor armor = Armor.instance;
		ArmorData data = armor.getData( type );
		
		isChild = false;
		
        bipedHead.showModel = ( data.getType() == Armor.HELMET );
        bipedHeadwear.showModel = ( data.getType() == Armor.HELMET );
        bipedBody.showModel = ( data.getType() == Armor.CHESTPLATE || data.getType() == Armor.LEGGINGS );
        bipedRightArm.showModel = ( data.getType() == Armor.CHESTPLATE );
        bipedLeftArm.showModel = ( data.getType() == Armor.CHESTPLATE );
        bipedRightLeg.showModel = ( data.getType() == Armor.LEGGINGS || data.getType() == Armor.BOOTS );
        bipedLeftLeg.showModel = ( data.getType() == Armor.LEGGINGS || data.getType() == Armor.BOOTS );
	}
	
	@Override
    public void render( Entity entity, float par2, float par3, float par4, float par5, float par6, float par7 )
    {
		if ( !( entity instanceof EntityLivingBase ) )
		{
			return;
		}
		EntityLivingBase living = ( EntityLivingBase ) entity;
		
		Armor armor = Armor.instance;
		ArmorData data = armor.getData( type );
		ItemStack stack = living.getEquipmentInSlot( 1 + 3 - data.getType() );
		if ( stack == null || !( stack.getItem() instanceof ArmorItem ) )
		{
			return;
		}
		ArmorItem item = ( ArmorItem ) stack.getItem();
		
		TextureManager re = FMLClientHandler.instance().getClient().renderEngine;
		
		String[] parts = data.getParts();
		for ( int ip = 0; ip < parts.length; ++ip )
		{
			String part = parts[ ip ];
			String mat = armor.getMaterialOf( stack, part ).getType();
			if ( item.equipment.getCasing( stack, part ) != null )
			{
				mat = item.equipment.getCasing( stack, part );
			}
			String tex = "componentequipment:textures/armor/" + type + "/parts/" + part + "/" + mat + ".png";
			ClientUtils.bindTexture( tex );
			
			boolean right = part.endsWith( "Right" );
			boolean left = part.endsWith( "Left" );
			boolean neither = ( !right && !left );
			
			bipedLeftArm.showModel = bipedLeftLeg.showModel = right || neither;
			bipedRightArm.showModel = bipedRightLeg.showModel = left || neither;
			
			GL11.glPushMatrix();
			{
				super.render( entity, par2, par3, par4, par5, par6, par7 );
			}
			GL11.glPopMatrix();
		}
		
		if ( type.equals( "chestplate" ) )
		{
			bipedLeftArm.showModel = bipedRightArm.showModel = true;
			bipedLeftLeg.showModel = bipedRightLeg.showModel = false;
		}
		else if ( type.equals( "boots" ) )
		{
			bipedLeftArm.showModel = bipedRightArm.showModel = false;
			bipedLeftLeg.showModel = bipedRightLeg.showModel = true;
		}
		
		String[] mods = armor.getModifiers( stack ).toArray( new String[] {} );
		for ( int im = 0; im < mods.length; ++im )
		{
			GL11.glColor4f( 1.f, 1.f, 1.f, 1.f );
			
			String modName = mods[ im ];
			if ( customModRenderers.get( modName ) != null )
			{
				GL11.glPushMatrix();
				try
				{
					customModRenderers.get( modName ).render( living, stack, modName, par2, par3, par4, par5, par6, par7 );
				}
				finally
				{
					GL11.glPopMatrix();
				}
			}
			else
			{
				int level = armor.getModifierLevel( stack, modName );
				String tex = "componentequipment:textures/armor/modifiers/" + mods[ im ] + "/" + level + "_" + ( item.type.equals( "leggings" ) ? 2 : 1 ) + ".png";
				ClientUtils.bindTexture( tex );
				
				GL11.glPushMatrix();
				{
					super.render( entity, par2, par3, par4, par5, par6, par7 );
				}
				GL11.glPopMatrix();
			}
		}
    }
	
	public static void registerModifierRenderer( String modName, IModifierRenderer renderer )
	{
		customModRenderers.put( modName, renderer );
	}
	
	private final EntityLivingBase living;
	private final String type;
	
	private static final Map< String, IModifierRenderer > customModRenderers = new HashMap< String, IModifierRenderer >();
}
