package com.spacechase0.minecraft.componentequipment.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

import com.spacechase0.minecraft.componentequipment.item.IngotItem;
import com.spacechase0.minecraft.spacecore.util.TranslateUtils;

public class IngotBlock extends SolidBlock
{
	public IngotBlock( String[] theTypes )
	{
		super( Material.iron, "ingotBlock" );
		types = theTypes;
		
		setHarvestLevel( "pickaxe", 2 );
		setHardness( 4.f );
		setResistance( 10.f );
		
		setStepSound( Block.soundTypeMetal );
	}
	
	@Override
    public void registerBlockIcons( IIconRegister register )
    {
		icons = new IIcon[ types.length ];
		for ( int i = 0; i < types.length; ++i )
		{
			icons[ i ] = register.registerIcon( "componentequipment:" + types[ i ] + "Block" );
		}
    }
	
	@Override
    public boolean onBlockActivated( World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9 )
	{
		if ( world.getBlockMetadata( x, y, z ) != IngotItem.PERSISTIUM )
		{
			return false;
		}
		
		if ( !player.isSneaking() )
		{
			return false;
		}
		
		int result = PersistiumInfuserBlock.checkStructure( world, x, y, z, false );
		if ( result == PersistiumInfuserBlock.OKAY )
		{
			// Sound for server but particles for client? Weird :P
			if ( world.isRemote )
			{
				for ( int i = 0; i < 3; ++i )
				{
					world.spawnParticle( "hugeexplosion", x + 0.5, y + 1.5, z + 0.5, 0, 0, 0 );
				}
			}
			else
			{
	            world.playSoundEffect( x, y, z, "ambient.weather.thunder", 20000.0F, 0.8f + ( world.rand.nextFloat() * 0.2f ) );
	            world.playSoundEffect( x, y, z, "random.explode", 3.5f, 0.5f + ( world.rand.nextFloat() * 0.2f ) );
				PersistiumInfuserBlock.createStructure( world, x, y, z );
			}
		}
		else if ( !world.isRemote )
		{
			String str = "";
			switch ( result )
			{
				case PersistiumInfuserBlock.BAD_FLOOR: str = "badfloor"; break;
				case PersistiumInfuserBlock.BAD_PILLAR: str = "badpillar"; break;
				case PersistiumInfuserBlock.NO_BEACON: str = "nobeacon"; break;
			}
			
			TranslateUtils.chat( player, "componentequipment.persistiumInfuser." + str );
		}
		
		return true;
	}
	
	@Override
    public void getSubBlocks( Item id, CreativeTabs tabs, List list )
    {
		for ( int i = 0; i < types.length; ++i )
		{
			list.add( new ItemStack( id, 1, i ) );
		}
    }
	
	@Override
    public IIcon getIcon( int side, int meta )
    {
        return icons[ meta ];
    }
	
	@Override
    public int damageDropped( int data )
    {
        return data;
    }
	
	//@Override
    public String getUnlocalizedName( ItemStack stack )
    {
        return "tile." + types[ stack.getItemDamage() ] + "Block";
    }
	
	public final String[] types;
	public IIcon[] icons;
}
