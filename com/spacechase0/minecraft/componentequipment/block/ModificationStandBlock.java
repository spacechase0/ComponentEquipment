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
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

import com.spacechase0.minecraft.componentequipment.ComponentEquipment;
import com.spacechase0.minecraft.componentequipment.tileentity.ModificationStandTileEntity;

public class ModificationStandBlock extends BlockContainer
{
	public ModificationStandBlock()
	{
		super( Material.rock );
		setBlockName( "modificationStand" );
		setCreativeTab( CreativeTabs.tabDecorations );
		
		setHarvestLevel( "pickaxe", 0 );
		setHardness( 0.25f );
		
		setStepSound( Block.soundTypeStone );
	}

	@Override
    public void registerBlockIcons( IIconRegister register )
    {
    }
	
	@Override
    public boolean onBlockActivated( World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9 )
    {
        if ( world.isRemote )
        {
            return true;
        }
        else
        {
    		//player.openGui( ComponentEquipment.instance, ( ( player.getHeldItem() == null ) ? ComponentEquipment.REORDER_MODIFIERS_GUI_ID : ComponentEquipment.MODIFICATION_STAND_GUI_ID ), world, x, y, z );
    		player.openGui( ComponentEquipment.instance, ComponentEquipment.MODIFICATION_STAND_GUI_ID, world, x, y, z );
            return true;
        }
    }

	@Override
	public TileEntity createNewTileEntity( World world, int i )
	{
		return new ModificationStandTileEntity();
	}
	
	@Override
    public void breakBlock(World world, int x, int y, int z, Block par5, int par6)
    {
		ModificationStandTileEntity stand = ( ModificationStandTileEntity ) world.getTileEntity(x, y, z);
        if ( stand == null )
        {
        	return;
        }
        
    	for ( int i = 0; i < stand.getSizeInventory(); ++i )
    	{
        	ItemStack stack = stand.getStackInSlot( i );
        	if ( stack != null )
        	{
        		Random rand = new Random();
        		
                float fx = rand.nextFloat() * 0.8F + 0.1F;
                float fy = rand.nextFloat() * 0.8F + 0.1F;
                float fz = rand.nextFloat() * 0.8F + 0.1F;
                
        		EntityItem entity = new EntityItem( world, (double)((float)x + fx), (double)((float)y + fy), (double)((float)z + fz), stack.copy());
        		
                float f3 = 0.05F;
                entity.motionX = (double)((float)rand.nextGaussian() * f3);
                entity.motionY = (double)((float)rand.nextGaussian() * f3 + 0.2F);
                entity.motionZ = (double)((float)rand.nextGaussian() * f3);
                
                world.spawnEntityInWorld(entity);
                
            	stand.decrStackSize( i, stack.stackSize );
        	}
    	}
    	
        super.breakBlock( world, x, y, z, par5, par6 );
    }
	
	@Override
    public void setBlockBoundsBasedOnState( IBlockAccess access, int x, int y, int z )
    {
		ModificationStandTileEntity stand = ( ModificationStandTileEntity ) access.getTileEntity( x, y, z );
		if ( stand.getStackInSlot( 0 ) != null )
		{
			setBlockBounds( 0.25f, 0.f, 0.25f, 0.75f, 0.0625f + 0.7f, 0.75f );
		}
		else
		{
			setBlockBounds( 0.25f, 0.f, 0.25f, 0.75f, 0.0625f * 2.5f, 0.75f );
		}
    }
	
	@Override
    public boolean isOpaqueCube()
    {
        return false;
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
}
