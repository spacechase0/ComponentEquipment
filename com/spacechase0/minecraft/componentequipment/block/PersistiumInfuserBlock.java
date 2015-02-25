package com.spacechase0.minecraft.componentequipment.block;

import java.util.Random;

import static net.minecraft.init.Blocks.*;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

import com.spacechase0.minecraft.componentequipment.ComponentEquipment;
import com.spacechase0.minecraft.componentequipment.item.IngotItem;
import com.spacechase0.minecraft.componentequipment.tileentity.PersistiumInfuserTileEntity;
import com.spacechase0.minecraft.spacecore.block.CustomSmelterBlock;
import com.spacechase0.minecraft.spacecore.util.TranslateUtils;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class PersistiumInfuserBlock extends CustomSmelterBlock
{
	public PersistiumInfuserBlock()
	{
		super( true );
		setBlockName( "persistiumInfuser" );

		setHarvestLevel( "pickaxe", 3 );
		setHardness( 7.5f );
		setResistance( 15.f );
		
		setStepSound( Block.soundTypeMetal );
		setLightLevel( 0.625f );
	}
	
	@Override
	public String getIconBase()
	{
		return "componentequipment";
	}

	@Override
	@SideOnly( Side.CLIENT )
    public void registerBlockIcons( IIconRegister register )
    {
		blockIcon = mainIcon = register.registerIcon( "componentequipment:persistiumBlock" );
    }
	
	@Override
	protected void openGui( World world, int x, int y, int z, EntityPlayer player )
	{
		int result = checkStructure( world, x, y, z, true );
		
		if ( result != OKAY )
		{
			String str = "";
			switch ( result )
			{
				case BAD_FLOOR: str = "badfloor"; break;
				case BAD_PILLAR: str = "badpillar"; break;
				case NO_BEACON: str = "nobeacon"; break;
			}
			
			TranslateUtils.chat( player, "componentequipment.persistiumInfuser." + str );
		}
		else
		{
	    	PersistiumInfuserTileEntity infuser = ( PersistiumInfuserTileEntity ) world.getTileEntity( x, y, z );
	    	player.openGui( ComponentEquipment.instance, ComponentEquipment.PERSISTIUM_INFUSER_GUI_ID, world, x, y, z );
		}
	}

	@Override
	protected Block getActiveBlock()
	{
		return this;
	}

	@Override
	protected Block getIdleBlock()
	{
		return this;
	}
	
	@Override
    public void setBlockBoundsBasedOnState( IBlockAccess access, int x, int y, int z )
	{
		setBlockBounds( 0.f, 0.f, 0.f, 1.f, 0.75f, 1.f );
	}
	
	@Override
	public int getRenderType()
	{
		return -1;
	}
	
	@Override
    public void randomDisplayTick( World world, int x, int y, int z, Random rand )
	{
		// ...
	}
	
	@Override
	public TileEntity createNewTileEntity( World world, int i )
	{
		return new PersistiumInfuserTileEntity();
	}
	
	public static int checkStructure( World world, int x, int y, int z, boolean made )
	{
		for ( int iy = y - 1; iy <= y + 2; ++iy )
		{
			for ( int ix = x - 3; ix <= x + 3; ++ix )
			{
				for ( int iz = z - 3; iz <= z + 3; ++iz )
				{
					Block id = world.getBlock( ix, iy, iz );
					int meta = world.getBlockMetadata( ix, iy, iz );
					
					int outerCount = 0;
					outerCount += ( Math.abs( x - ix ) == 3 ) ? 1 : 0;
					outerCount += ( Math.abs( z - iz ) == 3 ) ? 1 : 0;
					
					if ( iy == y - 1 && id != obsidian )
					{
						return BAD_FLOOR;
					}
					
					if ( outerCount == 2 && iy >= y && iy <= y + 1 && ( id != quartz_block || meta != 2 ) )
					{
						return BAD_PILLAR;
					}
					
					if ( outerCount == 2 && iy == y + 2 && id != ComponentEquipment.blocks.persistiumCrystal )
					{
						return BAD_PILLAR;
					}
					
					if ( ix == x && iz == z )
					{
						if ( !made )
						{
							if ( iy == y && ( id != ComponentEquipment.blocks.ingot && meta != IngotItem.PERSISTIUM ) )
							{
								return NO_BLOCK;
							}
							if ( iy == y + 1 && id != beacon )
							{
								return NO_BEACON;
							}
						}
						else if ( iy == y && id != ComponentEquipment.blocks.persistiumInfuser )
						{
							return NO_BLOCK;
						}
					}
				}
			}
		}
		
		return OKAY;
	}
	
	public static void createStructure( World world, int x, int y, int z )
	{
		world.setBlock( x, y, z, ComponentEquipment.blocks.persistiumInfuser );
		world.setBlock( x, y + 1, z, air );
	}
	
	public static final int OKAY = 0;
	public static final int BAD_FLOOR = 1;
	public static final int BAD_PILLAR = 2;
	public static final int NO_BEACON = 3;
	public static final int NO_BLOCK = 4;
}
