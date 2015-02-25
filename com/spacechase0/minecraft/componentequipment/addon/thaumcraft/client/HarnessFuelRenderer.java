package com.spacechase0.minecraft.componentequipment.addon.thaumcraft.client;

import java.lang.reflect.Method;
import java.util.EnumSet;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import thaumcraft.api.ItemApi;

import com.spacechase0.minecraft.componentequipment.ComponentEquipment;
import com.spacechase0.minecraft.componentequipment.item.ArmorItem;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.RenderTickEvent;

public class HarnessFuelRenderer
{
	public HarnessFuelRenderer()
	{
		try
		{
			Class c = Class.forName( "thaumcraft.client.lib.ClientTickEventsFML" );
			renderObj = c.getConstructor().newInstance();
			renderMethod = c.getMethod( "renderHoverHUD", float.class, EntityPlayer.class, long.class, ItemStack.class );
		}
		catch ( Exception exception )
		{
			exception.printStackTrace();
		}
	}
	

	@SubscribeEvent
	public void tickEnd( RenderTickEvent event )
	{
		if ( !event.phase.equals( TickEvent.Phase.END ) )
		{
			return;
		}
		Minecraft mc = FMLClientHandler.instance().getClient();
		
		if ( mc.currentScreen != null || renderObj == null || renderMethod == null )
		{
			return;
		}
		
		EntityPlayer player = mc.thePlayer;
		
		ItemStack chestplate = player.getCurrentArmor( 2 );
		if ( chestplate == null || chestplate.getItem() != ComponentEquipment.items.chestplate )
		{
			return;
		}
		ArmorItem armor = ( ArmorItem ) chestplate.getItem();
		
		if ( armor.armor.getModifierLevel( chestplate, "hoverHarness" ) <= 0 )
		{
			return;
		}
		
		ItemStack harnessStack = ItemApi.getItem( "itemHoverHarness", 0 );
		harnessStack.setTagCompound( chestplate.getTagCompound() );
		
		try
		{
			renderMethod.invoke( renderObj, ( Float ) event.renderTickTime, player, System.currentTimeMillis(), harnessStack );
		}
		catch ( Exception exception )
		{
			exception.printStackTrace();
		}
	}
	
	private Object renderObj;
	private Method renderMethod;
}
