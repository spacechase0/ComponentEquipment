package com.spacechase0.minecraft.componentequipment.tool;

import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;

import com.spacechase0.minecraft.componentequipment.item.HarvesterToolItem;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class AllowHarvestHandler
{
	// Metadata is now set in getDigSpeed, should carry over according to the EntityPlayer.getBreakSpeed
	/*
	@SubscribeEvent
	public void checkAllowHarvest( PlayerEvent.HarvestCheck event )
	{
		ItemStack held = event.entityPlayer.getCurrentEquippedItem();
		if ( held != null && held.getItem() instanceof HarvesterToolItem )
		{
			HarvesterToolItem tool = ( HarvesterToolItem ) held.getItem();
			HarvesterToolItem.metadata = 0; // TODO: The workaround
			event.success = ( event.success || tool.canHarvestBlock( event.block, held ) );
		}
		//System.out.println(event.success);
	}
	*/
}
