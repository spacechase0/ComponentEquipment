package com.spacechase0.minecraft.componentequipment.client.render.entity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderPlayerEvent;

import com.spacechase0.minecraft.componentequipment.client.model.ArmorModel;
import com.spacechase0.minecraft.componentequipment.item.ArmorItem;

import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class PlayerArmorRenderer
{
	public PlayerArmorRenderer()
	{
	}
	
	@SubscribeEvent
	public void onArmorRender( RenderPlayerEvent.SetArmorModel event )
	{
		if ( event.stack == null || !( event.stack.getItem() instanceof ArmorItem ) )
		{
			return;
		}
		event.result = 1;
		ArmorItem item = ( ArmorItem ) event.stack.getItem();
		
		ArmorModel model = ( ArmorModel ) item.getArmorModel( event.entityLiving, event.stack, event.slot );
		model.isSneak = event.entityLiving.isSneaking();
		if ( event.entityLiving instanceof EntityPlayer )
		{
			ItemStack held = event.entityPlayer.inventory.getCurrentItem();
			model.heldItemRight = ( held != null ) ? 0 : 1;
			if ( held != null )
			{
				model.aimedBow = event.entityPlayer.getItemInUseCount() > 0 && held.getItemUseAction().equals( EnumAction.bow );
			}
		}
		event.renderer.setRenderPassModel( model );
	}
}
