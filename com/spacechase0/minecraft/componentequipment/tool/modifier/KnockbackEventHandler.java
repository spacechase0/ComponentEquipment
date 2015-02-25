package com.spacechase0.minecraft.componentequipment.tool.modifier;

import java.util.Map.Entry;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class KnockbackEventHandler
{
	@SubscribeEvent
	public void onEntityHurt( LivingHurtEvent event )
	{
		EntityLivingBase entity = event.entityLiving;
		double resist = entity.getEntityAttribute( SharedMonsterAttributes.knockbackResistance ).getAttributeValue();
		/*
		for ( int i = 0; i < 5; ++i )
		{
			ItemStack stack = entity.getCurrentItemOrArmor( i );
			if ( stack == null )
			{
				continue;
			}
			
			stack.getAttributeModifiers();
		}
		*/
		
		for ( Object o : entity.getAttributeMap().getAllAttributes() )
		{
			IAttributeInstance attr = (IAttributeInstance)o;
			// TODO: Reduce momentum instead of random avoidance
		}
		//entity.setVelocity( entity.motionX * resist, entity.motionY, entity.motionZ * resist );
	}
}
