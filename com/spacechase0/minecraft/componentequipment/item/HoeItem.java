package com.spacechase0.minecraft.componentequipment.item;

import cpw.mods.fml.common.eventhandler.Event.Result;
import static net.minecraft.init.Blocks.*;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.UseHoeEvent;

public class HoeItem extends HarvesterToolItem
{
	public HoeItem()
	{
		super( "hoe" );
	}
    
	@Override
    public boolean onBlockDestroyed(ItemStack stack, World world, Block block, int x, int y, int z, EntityLivingBase breaker )
    {
        return true;
    }
	
	@Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int par7, float par8, float par9, float par10)
    {
		// TODO: Make sure this works right.. They can break from this.
        if ( !player.canPlayerEdit( x, y, z, par7, stack ) )
        {
            return false;
        }
        
        UseHoeEvent event = new UseHoeEvent( player, stack, world, x, y, z );
        if ( MinecraftForge.EVENT_BUS.post( event ) )
        {
            return false;
        }

        if ( event.getResult() == Result.ALLOW )
        {
            damageItemStack( player, stack, 1 );
            return true;
        }

        Block i1 = world.getBlock(x, y, z);
        Block j1 = world.getBlock(x, y + 1, z);

        if ((par7 == 0 || j1 != air || i1 != grass) && i1 != dirt)
        {
            return false;
        }
        else
        {
            Block block = farmland;
            world.playSoundEffect((double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), block.stepSound.soundName, (block.stepSound.getVolume() + 1.0F) / 2.0F, block.stepSound.getPitch() * 0.8F);

            if (world.isRemote)
            {
                return true;
            }
            else
            {
                world.setBlock(x, y, z, block);
                damageItemStack( player, stack, 1 );
                return true;
            }
        }
    }
}
