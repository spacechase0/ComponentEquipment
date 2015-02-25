package com.spacechase0.minecraft.componentequipment.network;

import io.netty.buffer.ByteBuf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import static net.minecraft.init.Blocks.*;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

import com.spacechase0.minecraft.componentequipment.ComponentEquipment;
import com.spacechase0.minecraft.componentequipment.item.EquipmentItem;
import com.spacechase0.minecraft.componentequipment.tileentity.ModificationStandTileEntity;
import com.spacechase0.minecraft.componentequipment.tool.Equipment;
import com.spacechase0.minecraft.spacecore.network.Packet;
import com.spacechase0.minecraft.spacecore.util.TranslateUtils;

public class ActivateModificationPacket extends Packet
{
	public ActivateModificationPacket()
	{
	}
	
	public ActivateModificationPacket( int theDim, int theX, int theY, int theZ )
	{
		dim = theDim;
		x = theX;
		y = theY;
		z = theZ;
	}
	
	@Override
	public byte getId()
	{
		return PacketCodec.ID_ACTIVATE_MODIFICATION;
	}
	
	@Override
	public void processServer( EntityPlayerMP player )
	{
    	World world = DimensionManager.getWorld( dim );
    	TileEntity tileEntity = world.getTileEntity( x, y, z );
    	if ( tileEntity != null && tileEntity instanceof ModificationStandTileEntity )
    	{
    		ModificationStandTileEntity stand = ( ModificationStandTileEntity ) tileEntity;
    		if ( stand.getStackInSlot( 0 ) == null || !( stand.getStackInSlot( 0 ).getItem() instanceof EquipmentItem ) )
    		{
    			TranslateUtils.chat( player, "componentequipment.modstand.badtool" );
    		}
    		else
    		{
    			Block below = world.getBlock( x, y - 1, z );
    			if ( below == cobblestone_wall )
    			{
	    			String result = stand.checkRecipe().error;
	    			if ( result != null )
	    			{
		    			if ( result.equals( "needxp" ) )
		    			{
		    				ItemStack tool = stand.getStackInSlot( 0 );
		    				Equipment equip = ( ( EquipmentItem ) tool.getItem() ).equipment;
		    				TranslateUtils.chat( player, "componentequipment.modstand.needxp", equip.getXpCost( tool ) );
		    			}
		    			else
		    			{
		    				TranslateUtils.chat( player, "componentequipment.modstand." + result );
		    			}
	    			}
	    			else
	    			{
	    				stand.activate();
	    			}
    			}
    			else if ( below == gold_block )
    			{
    				if ( ComponentEquipment.instance.config.get( "general", "allowDisassembly", true ).getBoolean( true ) )
    				{
	    				ItemStack tool = stand.getStackInSlot( 0 );
	    				EquipmentItem item = ( EquipmentItem ) tool.getItem();
	    				if ( tool.getItemDamageForDisplay() > 0 )
	    				{
	    					TranslateUtils.chat( player, "componentequipment.modstand.needsRepaired" );
	    				}
	    				else
	    				{
	    					stand.disassemble();
	    				}
    				}
    				else
    				{
    					TranslateUtils.chat( player, "componentequipment.modstand.disassemblyDisabled" );
    				}
    			}
    		}
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
	}
	
	@Override
	public void read( ByteBuf buffer )
	{
    	dim = buffer.readInt();
    	x = buffer.readInt();
    	y = buffer.readInt();
    	z = buffer.readInt();
	}
	
	public int dim = 0;
	public int x = 0;
	public int y = 0;
	public int z = 0;
}
