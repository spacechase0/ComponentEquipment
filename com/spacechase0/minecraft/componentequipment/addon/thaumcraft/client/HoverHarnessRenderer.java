package com.spacechase0.minecraft.componentequipment.addon.thaumcraft.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import thaumcraft.api.ItemApi;

import com.spacechase0.minecraft.componentequipment.client.render.tool.IModifierRenderer;

public class HoverHarnessRenderer implements IModifierRenderer
{
	@Override
	public void render( EntityLivingBase entity, ItemStack stack, String modName, float par2, float par3, float par4, float par5, float par6, float par7 )
	{
		TextureManager re = Minecraft.getMinecraft().getTextureManager();
		
		ItemStack harnessStack = ItemApi.getItem( "itemHoverHarness", 0 );
		Item harnessItem = harnessStack.getItem();
		
		if ( entity instanceof EntityPlayer )
		{
			EntityPlayer player = ( EntityPlayer ) entity;

			harnessStack.setTagCompound( stack.getTagCompound() );
			
			ItemStack old = player.inventory.armorInventory[ 2 ];
			player.inventory.armorInventory[ 2 ] = harnessStack;
			try
			{
				re.bindTexture( new ResourceLocation( harnessItem.getArmorTexture( harnessStack, entity, 1, "overlay" ) ) );
				harnessItem.getArmorModel( entity, harnessStack, 1 ).render( entity, 0, 0, 0, 0, 0, par7 );
			}
			finally
			{
				player.inventory.armorInventory[ 2 ] = old;
			}
		}
	}
}
