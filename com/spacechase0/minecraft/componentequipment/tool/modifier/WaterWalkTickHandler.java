package com.spacechase0.minecraft.componentequipment.tool.modifier;

import java.util.EnumSet;

import com.spacechase0.minecraft.componentequipment.item.ArmorItem;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStaticLiquid;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.BlockFluidFinite;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

public class WaterWalkTickHandler
{
	@SubscribeEvent
	public void tick( TickEvent.PlayerTickEvent event )
	{
		if ( !event.phase.equals( TickEvent.Phase.END ) ) return;
		
		EntityPlayer player = event.player;
		if ( player.ridingEntity != null ) return;

		ItemStack boots = player.getEquipmentInSlot( 1 );
		if ( boots == null || !( boots.getItem() instanceof ArmorItem ) )
		{
			return;
		}
		ArmorItem armor = ( ArmorItem ) boots.getItem();
		
		if ( armor.armor.getModifierLevel( boots, "waterWalk" ) < 1 )
		{
			return;
		}

		// Terminal velocity of a player is 4 blocks a second?
		for ( int iy = ( int ) player.prevPosY; iy >= ( int ) player.posY; --iy)
		{
			int x = ( int ) player.posX;
			int y = ( int )( iy - player.yOffset + player.ySize );
			int z = ( int ) player.posZ;
			Block block = player.worldObj.getBlock( x, y    , z );
			Block above = player.worldObj.getBlock( x, y + 1, z );
			
			boolean still = ( block instanceof BlockStaticLiquid );
			if ( block instanceof BlockFluidBase )
			{
				still = ( ( ( BlockFluidBase ) block ).getFilledPercentage( player.worldObj, x, y, z ) >= 1.0 );
			}
			
			if ( !player.isSneaking() && still && ( !( above instanceof BlockStaticLiquid ) && !( above instanceof BlockFluidBase ) ) )
			{
				double yDiff = player.yOffset + player.ySize;
				player.setPosition( player.posX, ( y + 1 ) + yDiff /*- 0.01*/, player.posZ );
				player.onGround = true;
				player.setVelocity( player.motionX, 0, player.motionZ );
				player.fallDistance = 0;
				player.isAirBorne = false;
				break;
			}
		}
	}
}
