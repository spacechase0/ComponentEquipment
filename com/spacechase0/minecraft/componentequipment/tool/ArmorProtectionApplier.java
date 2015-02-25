package com.spacechase0.minecraft.componentequipment.tool;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class ArmorProtectionApplier
{
	@SubscribeEvent
	public void onLivingHurt( LivingHurtEvent event )
	{
		if ( event.source.isUnblockable() )
		{
			return;
		}
		
		/*
		event.entityLiving.damageArmor( event.ammount );
		
		int prot = Armor.getProtectionOf( event.entityLiving );
		int num = ( event.ammount * prot ) + event.entityLiving.carryoverDamage;
		*/
		//for(int i=0;i<5;++i)System.out.println("");
	}
}
