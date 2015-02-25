package com.spacechase0.minecraft.componentequipment.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.server.MinecraftServer;

import com.spacechase0.minecraft.componentequipment.item.QuiverItem;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.FMLNetworkEvent.ServerConnectionFromClientEvent;

public class ConnectionHandler
{
	@SubscribeEvent
	public void playerLoggedIn( ServerConnectionFromClientEvent event )
	{
		EntityPlayer entity = ( ( NetHandlerPlayServer ) event.handler ).playerEntity;
		QuiverItem.setSelected( entity, 0 );
	}
}
