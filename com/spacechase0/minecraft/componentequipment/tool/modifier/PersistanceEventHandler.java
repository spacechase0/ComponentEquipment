package com.spacechase0.minecraft.componentequipment.tool.modifier;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.item.ItemExpireEvent;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;

import com.spacechase0.minecraft.componentequipment.item.EquipmentItem;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class PersistanceEventHandler
{
	@SubscribeEvent
	public void tickEnd( EntityJoinWorldEvent event )
	{
		if ( !( event.entity instanceof EntityPlayer ) )
		{
			return;
		}
		EntityPlayer player = ( EntityPlayer ) event.entity;

		NBTTagCompound data = player.getEntityData().getCompoundTag( EntityPlayer.PERSISTED_NBT_TAG );
		if ( data != null )
		{
			NBTTagList list = ( NBTTagList ) data.getTag( PERSIST_TAG );
			if ( list != null )
			{
				System.out.println("meow!");
				for ( int i = 0; i < list.tagCount(); ++i )
				{
					NBTTagCompound tag = ( NBTTagCompound ) list.getCompoundTagAt( i );
					ItemStack stack = ItemStack.loadItemStackFromNBT( tag );
					if ( stack != null )
					{
						player.inventory.addItemStackToInventory( stack );
					}
				}
				data.removeTag( PERSIST_TAG );
			}
		}
	}
	
	@SubscribeEvent
	public void onItemExpired( ItemExpireEvent event )
	{
		ItemStack stack = event.entityItem.getEntityItem();
		if ( !( stack.getItem() instanceof EquipmentItem ) )
		{
			return;
		}

		EquipmentItem item = ( EquipmentItem ) stack.getItem();
		int level = item.equipment.getModifierLevel( stack, "persistance" );
		if ( ( level & PersistanceModifier.TIME ) != 0 )
		{
			event.setCanceled( true );
			event.extraLife = 20 * 60 * 5; // 5 minutes
		}
	}
	
	@SubscribeEvent
	public void onPlayerDeath( PlayerDropsEvent event )
	{
		NBTTagList persisted = new NBTTagList();
		
		List< EntityItem > items = event.drops;
		for ( int i = 0; i < items.size(); ++i )
		{
			EntityItem entity = items.get( i );
			ItemStack stack = entity.getEntityItem();
			if ( stack == null || !( stack.getItem() instanceof EquipmentItem ) )
			{
				continue;
			}

			EquipmentItem itemBase = ( EquipmentItem ) stack.getItem();
			int level = itemBase.equipment.getModifierLevel( stack, "persistance" );
			if ( ( level & PersistanceModifier.INVENTORY ) != 0 )
			{
				NBTTagCompound item = new NBTTagCompound();
				stack.writeToNBT( item );
				persisted.appendTag( item );
				
				items.remove( i ); --i;
				continue;
			}
		}
		
		if ( persisted.tagCount() > 0 )
		{
			NBTTagCompound tag = event.entityPlayer.getEntityData();
			NBTTagCompound persistant = tag.getCompoundTag( EntityPlayer.PERSISTED_NBT_TAG );
			
			persistant.setTag( PERSIST_TAG, persisted );
			
			tag.setTag( EntityPlayer.PERSISTED_NBT_TAG, persistant );
		}
	}
	
	public static void setInvulnerable( Entity entity )
	{
		NBTTagCompound tag = new NBTTagCompound();
		entity.writeToNBT( tag );
		tag.setBoolean( "Invulnerable", true );
		entity.readFromNBT( tag );
	}
	
	private static final String PERSIST_TAG = "DecoPersist";
}
