package com.spacechase0.minecraft.componentequipment.tool.modifier;

import java.util.EnumSet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import com.spacechase0.minecraft.componentequipment.ComponentEquipment;
import com.spacechase0.minecraft.componentequipment.tool.Armor;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;

public class WalkSlopeTickHandler
{
	@SubscribeEvent
	public void tick( TickEvent.PlayerTickEvent event )
	{
		if ( !event.phase.equals( TickEvent.Phase.END ) ) return;
		
		EntityPlayer player = event.player;
		
		boolean hasWalk = false;
		ItemStack stack = player.getCurrentArmor( 0 );
		if ( stack != null && stack.getItem() == ComponentEquipment.items.boots )
		{
			int level = Armor.instance.getModifierLevel( stack, "walkSlope" );
			if ( level >= 1 )
			{
				hasWalk = true;
			}
		}
		// Doing it this way to allow other mods to do similar things without me breaking them
		
		NBTTagCompound data = player.getEntityData();
		if ( hasWalk )
		{
			data.setBoolean( "CE_WalkSlope", true );
			player.stepHeight = 1.f;
		}
		else if ( player.stepHeight > 0.5f && data.hasKey( "CE_WalkSlope" ) && data.getBoolean( "CE_WalkSlope" ) == true )
		{
			data.removeTag( "CE_WalkSlope" );
			player.stepHeight = 0.5f;
		}
	}
}
