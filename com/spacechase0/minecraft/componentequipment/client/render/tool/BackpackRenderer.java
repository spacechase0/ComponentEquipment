package com.spacechase0.minecraft.componentequipment.client.render.tool;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelChest;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.spacechase0.minecraft.componentequipment.item.EquipmentItem;
import com.spacechase0.minecraft.componentequipment.tool.Modifier;
import com.spacechase0.minecraft.componentequipment.tool.modifier.BackpackModifier;
import com.spacechase0.minecraft.spacecore.util.ClientUtils;

public class BackpackRenderer implements IModifierRenderer
{
	@Override
	public void render( EntityLivingBase entity, ItemStack stack, String modName, float par2, float par3, float par4, float par5, float par6, float par7 )
	{
		TextureManager re = Minecraft.getMinecraft().getTextureManager();

		EquipmentItem equipment = ( EquipmentItem ) stack.getItem();
		BackpackModifier mod = ( BackpackModifier ) Modifier.getModifier( modName );
		
		ClientUtils.bindTexture( mod.getModelTexture( equipment.equipment.getModifierLevel( stack, modName ) ) );
		ModelChest chest = new ModelChest();
		
		GL11.glScalef( 0.5f, 0.5f, 0.5f );
		GL11.glRotatef( 180, 0, 1, 0 );
		GL11.glTranslatef( -0.5f, 0, -0.75f );
		chest.renderAll();
	}
}
