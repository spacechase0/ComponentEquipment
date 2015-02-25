package com.spacechase0.minecraft.componentequipment.tool.material;

import java.util.Random;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import com.spacechase0.minecraft.componentequipment.item.EquipmentItem;
import com.spacechase0.minecraft.componentequipment.tool.Equipment;
import com.spacechase0.minecraft.componentequipment.tool.Material;
import com.spacechase0.minecraft.componentequipment.tool.MaterialData;
import com.spacechase0.minecraft.componentequipment.tool.EquipmentData;
import com.spacechase0.minecraft.spacecore.block.BlockDestroyedNotifier;
import com.spacechase0.minecraft.spacecore.block.IBlockDestroyedMonitor;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class EnderDropCollector implements IBlockDestroyedMonitor
{
	public EnderDropCollector()
	{
		BlockDestroyedNotifier.addMonitor( this );
		MinecraftForge.EVENT_BUS.register( this );
	}
	
	@Override
	public void blockDestroyed( net.minecraft.server.management.ItemInWorldManager manager, int x, int y, int z )
	{
		if ( manager.theWorld.isRemote )
		{
			return;
		}
		
		stopCollecting();
	}
	
	public void startCollecting( EntityLivingBase breaker )
	{
		collector = breaker;
	}
	
	public void stopCollecting()
	{
		collector = null;
	}
	
	@SubscribeEvent
	public void entityHurt( LivingHurtEvent event )
	{
		if ( !( event.source.getEntity() instanceof EntityLiving ) )
		{
			return;
		}
		EntityLiving source = ( EntityLiving ) event.source.getEntity();
		
		ItemStack stack = source.getEquipmentInSlot( 0 );
		if ( stack == null || !( stack.getItem() instanceof EquipmentItem ) )
		{
			return;
		}
		EquipmentItem item = ( EquipmentItem ) stack.getItem();
		Equipment equipment = item.equipment;
		
		boolean foundEnderPearlMat = false;
		EquipmentData data = equipment.getBaseData( item.type );
		for ( int ip = 0; ip < data.getParts().length; ++ip )
		{
			String part = data.getParts()[ ip ];
			MaterialData mat = equipment.getMaterialOf( stack, part );
			if ( mat == Material.getData( "ender" ) )
			{
				startCollecting( source );
				return;
			}
		}
	}
	
	@SubscribeEvent
	public void entityJoinedWorld( EntityJoinWorldEvent event )
	{
		if ( event.world.isRemote )
		{
			return;
		}
		
		if ( !( event.entity instanceof EntityItem ) && !( event.entity instanceof EntityXPOrb ) )
		{
			return;
		}
		
		if ( collector == null || event.isCanceled() )
		{
			return;
		}
		
		if ( event.entity instanceof EntityXPOrb && collector instanceof EntityPlayer )
		{
			EntityPlayer player = ( EntityPlayer ) collector;
			EntityXPOrb xp = ( EntityXPOrb ) event.entity;
			
            player.addExperience( xp.getXpValue() );
            xp.playSound("random.orb", 0.1F, 0.5F * ((soundRand.nextFloat() - soundRand.nextFloat()) * 0.7F + 1.8F));
            
            event.setCanceled( true );
		}
		else if ( event.entity instanceof EntityItem )
		{
			EntityItem item = ( EntityItem ) event.entity;
			ItemStack stack = item.getEntityItem();
			
			if ( collector instanceof EntityPlayer )
			{
				EntityPlayer player = ( EntityPlayer ) collector;
				
				if ( player.inventory.addItemStackToInventory( stack ) )
				{
					item.playSound("random.pop", 0.2F, ((soundRand.nextFloat() - soundRand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
				}
			}
			
			if ( stack.stackSize == 0 )
			{
				event.setCanceled( true );
			}
			else
			{
				item.setEntityItemStack( stack );
			}
		}
	}
	
	private EntityLivingBase collector;
	private Random soundRand = new Random();
}
