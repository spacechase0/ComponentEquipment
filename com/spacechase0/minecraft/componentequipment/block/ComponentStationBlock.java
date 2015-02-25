package com.spacechase0.minecraft.componentequipment.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

import com.spacechase0.minecraft.componentequipment.ComponentEquipment;
import com.spacechase0.minecraft.componentequipment.tileentity.ComponentStationTileEntity;

public class ComponentStationBlock extends BlockContainer
{
	public ComponentStationBlock()
	{
		super( Material.wood );
		setBlockName( "componentStation" );
		setCreativeTab( CreativeTabs.tabDecorations );
		
		setHarvestLevel( "axe", 0 );
		setHardness( 0.5f );
		
		setStepSound( Block.soundTypeWood );
	}
	
	// Copied from workbench code
	@Override
    public IIcon getIcon(int par1, int par2)
    {
        return par1 == 1 ? top : (par1 == 0 ? net.minecraft.init.Blocks.planks.getBlockTextureFromSide(par1) : (par1 != 2 && par1 != 4 ? blockIcon : front));
    }

	@Override
    public void registerBlockIcons( IIconRegister register)
    {
        blockIcon = register.registerIcon( "componentequipment:componentStationSide" );
        top = register.registerIcon( "componentequipment:componentStationTop" );
        front = register.registerIcon( "componentequipment:componentStationFront" );
    }
	
	@Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
    {
        if ( world.isRemote )
        {
            return true;
        }
        else
        {
    		player.openGui( ComponentEquipment.instance, ComponentEquipment.COMPONENT_STATION_GUI_ID, world, x, y, z );
            return true;
        }
    }

	@Override
	public TileEntity createNewTileEntity( World world, int i )
	{
		return new ComponentStationTileEntity();
	}
	
	@Override
    public void breakBlock( World world, int x, int y, int z, Block par5, int par6 )
    {
		ComponentStationTileEntity station = ( ComponentStationTileEntity ) world.getTileEntity(x, y, z);
        if ( station == null )
        {
        	return;
        }
        
    	for ( int i = 0; i < station.getSizeInventory(); ++i )
    	{
        	ItemStack stack = station.getStackInSlot( i );
        	if ( stack != null )
        	{
        		Random rand = new Random();
        		
                float fx = rand.nextFloat() * 0.8F + 0.1F;
                float fy = rand.nextFloat() * 0.8F + 0.1F;
                float fz = rand.nextFloat() * 0.8F + 0.1F;
                
        		EntityItem entity = new EntityItem( world, (double)((float)x + fx), (double)((float)y + fy), (double)((float)z + fz), stack);
        		
                float f3 = 0.05F;
                entity.motionX = (double)((float)rand.nextGaussian() * f3);
                entity.motionY = (double)((float)rand.nextGaussian() * f3 + 0.2F);
                entity.motionZ = (double)((float)rand.nextGaussian() * f3);
                
                world.spawnEntityInWorld(entity);
                
            	station.decrStackSize( 0, stack.stackSize );
        	}
    	}
    	
        super.breakBlock( world, x, y, z, par5, par6 );
    }
	
	private IIcon top;
	private IIcon front;
}
