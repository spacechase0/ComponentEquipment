package com.spacechase0.minecraft.componentequipment.item;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class SwordItem extends HarvesterToolItem
{
	public SwordItem()
	{
		super( "sword" );
	}
	
	@Override
    public ItemStack onItemRightClick( ItemStack stack, World world, EntityPlayer player )
    {
        player.setItemInUse( stack, getMaxItemUseDuration( stack ) );
        return stack;
    }
	
	@Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float clickX, float clickY, float clickZ)
	{
		return false;
	}
	
	@Override
    public EnumAction getItemUseAction( ItemStack stack )
    {
        return EnumAction.block;
    }
	
	@Override
    public int getMaxItemUseDuration( ItemStack stack )
    {
        return 72000;
    }
	
	@Override
    public boolean canHarvestBlock( Block block, ItemStack stack )
    {
        return block == net.minecraft.init.Blocks.web;
    }
	
	@Override
    public float getDigSpeed( ItemStack stack, Block block, int metadata )
    {
		if ( getDamage( stack ) > getMaxDamage( stack ) )
		{
			return 1.f;
		}
		
		if ( canHarvestBlock( block, stack ) )
		{
			return 15.f;
		}
		
		return 1.5f;
    }
}
