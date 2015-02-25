package com.spacechase0.minecraft.componentequipment.addon.thaumcraft;

import java.lang.reflect.Method;
import java.util.EnumSet;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import thaumcraft.api.ItemApi;

import com.spacechase0.minecraft.componentequipment.ComponentEquipment;
import com.spacechase0.minecraft.componentequipment.item.ArmorItem;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class ArcaneBoreSlotChanger
{
	public void tickEnd( PlayerTickEvent event )
	{
		if ( !event.phase.equals( TickEvent.Phase.END ) )
		{
			return;
		}
		EntityPlayer player = ( EntityPlayer ) event.player;
		
		if ( !player.openContainer.getClass().getName().equals( "thaumcraft.common.container.ContainerArcaneBore" ) )
		{
			return;
		}
		
		Slot oldSlot = player.openContainer.getSlot( 1 );
		if ( oldSlot instanceof PickaxeSlot )
		{
			return;
		}
		
		PickaxeSlot newSlot = new PickaxeSlot( oldSlot.inventory, 1, oldSlot.xDisplayPosition, oldSlot.yDisplayPosition );
		newSlot.slotNumber = oldSlot.slotNumber;
		player.openContainer.inventorySlots.set( 1, newSlot );
	}
}
