package com.spacechase0.minecraft.componentequipment.network;

import io.netty.buffer.ByteBuf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

import com.spacechase0.minecraft.componentequipment.tileentity.PersistiumInfuserTileEntity;
import com.spacechase0.minecraft.spacecore.network.Packet;

public class ActivateInfuserPacket extends Packet
{
	public ActivateInfuserPacket()
	{
	}
	
	public ActivateInfuserPacket( int theDim, int theX, int theY, int theZ )
	{
		dim = theDim;
		x = theX;
		y = theY;
		z = theZ;
	}
	
	@Override
	public byte getId()
	{
		return PacketCodec.ID_ACTIVATE_INFUSER;
	}
	
	@Override
	public void processServer( EntityPlayerMP player )
	{
    	World world = DimensionManager.getWorld( dim );
    	TileEntity tileEntity = world.getTileEntity( x, y, z );
    	if ( tileEntity != null && tileEntity instanceof PersistiumInfuserTileEntity )
    	{
    		PersistiumInfuserTileEntity infuser = ( PersistiumInfuserTileEntity ) tileEntity;
    		infuser.active = level;
    		player.closeScreen();
    	}
	}

	@Override
	public void write( ByteBuf buffer )
	{
		buffer.writeInt( dim );
		buffer.writeInt( x );
		buffer.writeInt( y );
		buffer.writeInt( z );
		buffer.writeInt( level );
	}
	
	@Override
	public void read( ByteBuf buffer )
	{
    	dim = buffer.readInt();
    	x = buffer.readInt();
    	y = buffer.readInt();
    	z = buffer.readInt();
    	level = buffer.readInt();
	}
	
	public int dim = 0;
	public int x = 0;
	public int y = 0;
	public int z = 0;
	public int level = -1;
}
