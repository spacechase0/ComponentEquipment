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

import com.spacechase0.minecraft.componentequipment.tileentity.MysteriousOrbTileEntity;

public class MysteriousOrbBlock extends BlockContainer
{
	public MysteriousOrbBlock()
	{
		super( Material.glass );
		setBlockName( "mysteriousOrb" );
		setCreativeTab( CreativeTabs.tabDecorations );
		
		setHarvestLevel( "pickaxe", 2 );
		setHardness( 2.5f );
		
		setStepSound( Block.soundTypeGlass );
		setLightLevel( 0.5f );
	}
	
	@Override
    public void setBlockBoundsBasedOnState( IBlockAccess access, int x, int y, int z )
	{
		setBlockBounds( 0.25f, 0.25f, 0.25f, 0.75f, 0.75f, 0.75f );
	}

	@Override
	public TileEntity createNewTileEntity( World world, int i )
	{
		return new MysteriousOrbTileEntity();
	}

	@Override
    public void registerBlockIcons( IIconRegister register )
    {
		//blockIcon = register.registerIcon( "componentequipment:mysteriousOrb" );
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
