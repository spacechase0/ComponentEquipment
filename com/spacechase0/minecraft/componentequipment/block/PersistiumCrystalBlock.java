package com.spacechase0.minecraft.componentequipment.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

import com.spacechase0.minecraft.componentequipment.tileentity.PersistiumCrystalTileEntity;

public class PersistiumCrystalBlock extends BlockContainer
{
	public PersistiumCrystalBlock()
	{
		super( Material.glass );
		setBlockName( "persistiumCrystal" );
		setCreativeTab( CreativeTabs.tabDecorations );
		
		setHarvestLevel( "pickaxe", 0 );
		setHardness( 0.5f );
		
		setStepSound( Block.soundTypeGlass );
		setLightLevel( 0.375f );
	}
	
	@Override
    public void setBlockBoundsBasedOnState( IBlockAccess access, int x, int y, int z )
	{
		setBlockBounds( 0.375f, 0.45f, 0.375f, 0.625f, 0.95f, 0.625f );
	}

	@Override
	public TileEntity createNewTileEntity( World world, int i )
	{
		return new PersistiumCrystalTileEntity();
	}

	@Override
    public void registerBlockIcons( IIconRegister register )
    {
		blockIcon = register.registerIcon( "componentequipment:persistiumCrystal" );
    }
	
	@Override
	public boolean renderAsNormalBlock()
	{
		return false;
	}

	@Override
	public int getRenderType()
	{
		return -1;
	}

	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}
}
