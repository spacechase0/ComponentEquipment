package com.spacechase0.minecraft.componentequipment.client.render.tool;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public interface IModifierRenderer
{
	public void render( EntityLivingBase entity, ItemStack stack, String modName, float par2, float par3, float par4, float par5, float par6, float par7 );
}
