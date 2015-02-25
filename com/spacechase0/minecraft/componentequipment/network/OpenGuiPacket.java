package com.spacechase0.minecraft.componentequipment.network;

import io.netty.buffer.ByteBuf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.InventoryEnderChest;
import net.minecraft.item.ItemStack;

import com.spacechase0.minecraft.componentequipment.ComponentEquipment;
import com.spacechase0.minecraft.componentequipment.item.ArmorItem;
import com.spacechase0.minecraft.componentequipment.tool.Armor;
import com.spacechase0.minecraft.spacecore.network.Packet;
import com.spacechase0.minecraft.spacecore.util.TranslateUtils;

public class OpenGuiPacket extends Packet
{
	public OpenGuiPacket()
	{
	}
	
	public OpenGuiPacket( int theId )
	{
		id = theId;
	}
	
	@Override
	public byte getId()
	{
		return PacketCodec.ID_OPEN_GUI;
	}
	
	@Override
	public void processServer( EntityPlayerMP player )
	{
		if ( id == ComponentEquipment.REORDER_MODIFIERS_GUI_ID )
		{
			player.openGui( ComponentEquipment.instance, ComponentEquipment.REORDER_MODIFIERS_GUI_ID, player.worldObj, ( int ) player.posX, ( int ) player.posY, ( int ) player.posZ );
		}
		else if ( id == ComponentEquipment.BACKPACK_GUI_ID )
		{
			ItemStack stack = player.getCurrentArmor( 2 );
			if ( stack == null || stack.getItem() != ComponentEquipment.items.chestplate )
			{
				return;
			}
			else if ( Armor.instance.isBroken( stack ) )
			{
				TranslateUtils.chat( player, "chat.componentequipment:backpack.broken" );
				return;
			}
			
			ArmorItem item = ( ArmorItem ) stack.getItem();
			if ( item.armor.getModifierLevel( stack, "enderBackpack" ) > 0 )
			{
				InventoryEnderChest inv = player.getInventoryEnderChest();
				player.displayGUIChest( inv );
			}
			else if ( item.armor.getModifierLevel( stack, "chestBackpack" ) > 0 )
			{
				player.openGui( ComponentEquipment.instance, ComponentEquipment.BACKPACK_GUI_ID, player.worldObj, ( int ) player.posX, ( int ) player.posY, ( int ) player.posZ );
			}
		}
		else if ( id == ComponentEquipment.JUKEBOX_GUI_ID )
		{
			ItemStack stack = player.getCurrentArmor( 3 );
			if ( stack == null || stack.getItem() != ComponentEquipment.items.helmet )
			{
				return;
			}
			else if ( Armor.instance.isBroken( stack ) )
			{
				TranslateUtils.chat( player, "chat.componentequipment:jukebox.broken" );
				return;
			}
			
			ArmorItem item = ( ArmorItem ) stack.getItem();
			if ( item.armor.getModifierLevel( stack, "portableJukebox" ) > 0 )
			{
				player.openGui( ComponentEquipment.instance, ComponentEquipment.JUKEBOX_GUI_ID, player.worldObj, ( int ) player.posX, ( int ) player.posY, ( int ) player.posZ );
			}
		}
	}

	@Override
	public void write( ByteBuf buffer )
	{
		buffer.writeInt( id );
	}
	
	@Override
	public void read( ByteBuf buffer )
	{
        id = buffer.readInt();
	}
	
	public int id = 0;
}
