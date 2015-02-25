package com.spacechase0.minecraft.componentequipment.network;

import io.netty.buffer.ByteBuf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import net.minecraft.entity.player.EntityPlayerMP;

import com.spacechase0.minecraft.componentequipment.ComponentEquipment;
import com.spacechase0.minecraft.componentequipment.item.QuiverItem;
import com.spacechase0.minecraft.spacecore.network.Packet;

public class SelectedArrowPacket extends Packet
{
	public SelectedArrowPacket()
	{
	}
	
	public SelectedArrowPacket( int theSelected )
	{
		selected = theSelected;
	}
	
	@Override
	public byte getId()
	{
		return PacketCodec.ID_SELECTED_ARROW;
	}
	
	@Override
	public void processServer( EntityPlayerMP player )
	{
		QuiverItem.setSelected( player, selected );
		if ( !player.worldObj.isRemote )
		{
			ComponentEquipment.network.sendToPlayer(  ( EntityPlayerMP ) player, this );
		}
	}

	@Override
	public void write( ByteBuf buffer )
	{
		buffer.writeInt( selected );
	}
	
	@Override
	public void read( ByteBuf buffer )
	{
        selected = buffer.readInt();
	}

	public int selected = 0;
}
