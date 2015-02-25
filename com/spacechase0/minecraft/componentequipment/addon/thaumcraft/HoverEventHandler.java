package com.spacechase0.minecraft.componentequipment.addon.thaumcraft;

import java.lang.reflect.Method;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;

import com.spacechase0.minecraft.componentequipment.ComponentEquipment;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class HoverEventHandler
{
	@SubscribeEvent
	public void update( LivingUpdateEvent event )
	{
		if ( !( event.entity instanceof EntityPlayer ) )
		{
			return;
		}
		EntityPlayer player = ( EntityPlayer ) event.entity;
		
		ItemStack chestplate = player.inventory.armorItemInSlot( 2 );
		if ( !player.capabilities.isFlying && chestplate != null && chestplate.getItem() == ComponentEquipment.items.chestplate )
        {
			try
			{
				Class c = Class.forName( "thaumcraft.common.items.armor.Hover" );
				Method m = c.getMethod( "handleHoverArmor", EntityPlayer.class, ItemStack.class );
				m.invoke( null, player, chestplate );
			}
			catch ( Exception exception )
			{
				exception.printStackTrace();
			}
        }
	}
	
}
