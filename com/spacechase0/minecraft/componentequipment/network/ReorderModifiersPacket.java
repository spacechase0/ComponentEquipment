package com.spacechase0.minecraft.componentequipment.network;

import io.netty.buffer.ByteBuf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

import com.spacechase0.minecraft.componentequipment.inventory.ReorderModifiersContainer;
import com.spacechase0.minecraft.componentequipment.item.EquipmentItem;
import com.spacechase0.minecraft.spacecore.network.Packet;

import cpw.mods.fml.common.Loader;

public class ReorderModifiersPacket extends Packet
{
	public ReorderModifiersPacket()
	{
	}
	
	public ReorderModifiersPacket( int a, int b )
	{
		swapA = a;
		swapB = b;
	}
	
	@Override
	public byte getId()
	{
		return PacketCodec.ID_REORDER_MODIFIERS;
	}
	
	@Override
	public void processServer( EntityPlayerMP player )
	{
		if ( !( player.openContainer instanceof ReorderModifiersContainer ) )
		{
			return;
		}
		
		ReorderModifiersContainer container = ( ReorderModifiersContainer ) player.openContainer;
		ItemStack stack = container.getStackInSlot( 1 );
		if ( stack == null )
		{
			return;
		}
		if ( stack.getItem() instanceof EquipmentItem )
		{
			EquipmentItem item = ( EquipmentItem ) stack.getItem();
			
			item.equipment.swapModifiers( stack, swapA, swapB );
		}
		else
		{
			// TODO: Test in 1.7
			try
			{
				if ( Loader.isModLoaded( "TConstruct" ) )
				{
					Class c = Class.forName( "tconstruct.library.tools.ToolCore" );
					boolean isTool = c.isAssignableFrom( stack.getItem().getClass() );
					
					
					if ( isTool )
					{
						NBTTagCompound tag = stack.getTagCompound().getCompoundTag( "InfiTool" );

						NBTBase tagA = tag.getTag( "Effect" + ( swapA + 1 ) );
						NBTBase tagB = tag.getTag( "Effect" + ( swapB + 1 ) );

						tag.setTag( "Effect" + ( swapA + 1 ), tagB );
						tag.setTag( "Effect" + ( swapB + 1 ), tagA );
					}
				}
			}
			catch ( Exception exception )
			{
				return;
			}
		}
		container.setInventorySlotContents( 1, stack );
	}

	@Override
	public void write( ByteBuf buffer )
	{
		buffer.writeInt( swapA );
		buffer.writeInt( swapB );
	}
	
	@Override
	public void read( ByteBuf buffer )
	{
    	swapA = buffer.readInt();
    	swapB = buffer.readInt();
	}
	
	public int swapA = 0;
	public int swapB = 0;
}
