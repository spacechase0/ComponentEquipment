package com.spacechase0.minecraft.componentequipment.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCactus;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;

public class DiamondCactusBlock extends BlockCactus
{
	public DiamondCactusBlock()
	{
		super();
		
		setTickRandomly( false );
		setBlockName( "diamondCactus" );
		setCreativeTab( CreativeTabs.tabDecorations );
		setStepSound( Block.soundTypeCloth );
	}
	
	@Override
    public boolean canSustainPlant( IBlockAccess world, int x, int y, int z, ForgeDirection dir, IPlantable plant )
    {
        Block plantId = plant.getPlant( world, x, y + 1, z );
        if ( plantId == this )
        {
        	return true;
        }
        
        return super.canSustainPlant( world, x, y, z, dir, plant );
    }
	
	@Override
    public void onEntityCollidedWithBlock( World par1World, int par2, int par3, int par4, Entity entity )
    {
        entity.attackEntityFrom( DamageSource.cactus, 5 );
    }

    @Override
    public void registerBlockIcons( IIconRegister register )
    {
        blockIcon = register.registerIcon( "componentequipment:diamondCactusSide" );
        topIcon = register.registerIcon( "componentequipment:diamondCactusTop" );
        bottomIcon = register.registerIcon( "componentequipment:diamondCactusBottom" );
    }
    
    @Override
    public void updateTick( World par1World, int par2, int par3, int par4, Random par5Random )
    {
    }
    
    public IIcon getIcon(int par1, int par2)
    {
        return par1 == 1 ? topIcon : (par1 == 0 ? bottomIcon : blockIcon);
    }
    
    IIcon topIcon;
    IIcon bottomIcon;
}
