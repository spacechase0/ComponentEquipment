package com.spacechase0.minecraft.componentequipment.item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.OreDictionary;

import com.spacechase0.minecraft.componentequipment.tool.Material;

import cpw.mods.fml.common.Optional;

public class HarvesterToolItem extends ToolItem
{
	public HarvesterToolItem( String theType )
	{
		super( theType );
	}
	
	// From TConstruct - the code is public domain
	// I modified it to do the slot above though
	@Override
    public boolean onItemUse (ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float clickX, float clickY, float clickZ)
    {
        /*if (world.isRemote)
            return true;*/

        int posX = x;
        int posY = y;
        int posZ = z;
        int playerPosX = (int) Math.floor(player.posX);
        int playerPosY = (int) Math.floor(player.posY);
        int playerPosZ = (int) Math.floor(player.posZ);
        if (side == 0)
        {
            --posY;
        }

        if (side == 1)
        {
            ++posY;
        }

        if (side == 2)
        {
            --posZ;
        }

        if (side == 3)
        {
            ++posZ;
        }

        if (side == 4)
        {
            --posX;
        }

        if (side == 5)
        {
            ++posX;
        }
        if (posX == playerPosX && (posY == playerPosY || posY == playerPosY + 1 || posY == playerPosY - 1) && posZ == playerPosZ)
        {
            return false;
        }

        boolean used = false;
        int hotbarSlot = player.inventory.currentItem;
        int itemSlot = hotbarSlot + ( InventoryPlayer.getHotbarSize() * 3 );//hotbarSlot == 0 ? 8 : hotbarSlot + 1;
        ItemStack nearbyStack = null;

        //if (hotbarSlot < 8)
        {
            nearbyStack = player.inventory.getStackInSlot(itemSlot);
            if (nearbyStack != null && nearbyStack.getItem() instanceof ItemBlock)
            {
                used = nearbyStack.getItem().onItemUse(nearbyStack, player, world, x, y, z, side, clickX, clickY, clickZ);
                if (nearbyStack.stackSize < 1)
                {
                    nearbyStack = null;
                    player.inventory.setInventorySlotContents(itemSlot, null);
                }
            }
        }

        /*if (used) //Update client
        {
            Packet103SetSlot packet = new Packet103SetSlot(player.openContainer.windowId, itemSlot, nearbyStack);
            ((EntityPlayerMP)player).playerNetServerHandler.sendPacketToPlayer(packet);
        }*/
        return used;
    }

	@Override
	public boolean hitEntity( ItemStack stack, EntityLivingBase living1, EntityLivingBase living2 )
    {
		if ( getDamage( stack ) > getMaxDamage( stack ) )
		{
			return false;
		}
		
    	if ( tool.getData( type ).isWeapon() )
    	{
    		damageItemStack( living2, stack, 1 );
    	}
    	else
    	{
    		damageItemStack( living2, stack, 2 );
    	}
    	
    	tool.onHit( living2, living1, stack );
    	
        return true;
    }

	@Override
    public boolean onBlockDestroyed( ItemStack stack, World world, Block block, int x, int y, int z, EntityLivingBase breaker )
    {
		if ( getDamage( stack ) > getMaxDamage( stack ) )
		{
			return false;
		}

		boolean ignore = false;
		if ( tool.actsLike( stack, "axe" ) )
		{
			for ( ItemStack ore : OreDictionary.getOres( "treeLeaves" ) )
			{
				if ( ore.getItem() == Item.getItemFromBlock( block ) )
				{
					if ( ore.getItemDamage() == OreDictionary.WILDCARD_VALUE || world.getBlockMetadata( x, y, z ) == ore.getItemDamage() )
					{
						ignore = true;
						break;
					}
				}
			}
		}
		
        if ( !ignore && ( double ) block.getBlockHardness(world, x, y, z) != 0.0D)
        {
        	if ( !canHarvestBlock( block, stack ) )
        	{
        		damageItemStack( breaker, stack, 2 );
        	}
        	else
        	{
        		damageItemStack( breaker, stack, 1 );
        	}
        }
        
        equipment.onBlockDestroyed( breaker, stack, x, y, z );

        return true;
    }
	
	@Override
	public Set< String > getToolClasses( ItemStack stack )
	{
		Set< String > ret = new HashSet< String >();

		if ( tool.actsLike( stack, "pickaxe" ) ) ret.add( "pickaxe" );
		if ( tool.actsLike( stack, "shovel" ) ) ret.add( "shovel" );
		if ( tool.actsLike( stack, "axe" ) ) ret.add( "axe" );
		
		return ret;
	}
	
	@Override
	public int getHarvestLevel( ItemStack stack, String toolType )
	{
		if ( getDamage( stack ) > getMaxDamage( stack ) )
		{
			return -1;
		}
		
		NBTTagCompound tag = stack.getTagCompound();
		String head = tag.getString( "head" );
		if ( head == null || head == "" )
		{
			return -1;
		}
		
		if ( tool.actsLike( stack, toolType ) )
		{
			return Material.getData( head ).getMiningLevel();
		}
		
		return -1;
	}

	@Override
    public boolean canHarvestBlock( Block block, ItemStack stack )
    {
		if ( getDamage( stack ) > getMaxDamage( stack ) )
		{
			return false;
		}
		
		NBTTagCompound tag = stack.getTagCompound();
		String head = tag.getString( "head" );
		if ( head == null || head == "" )
		{
			return false;
		}
		
		boolean goodLevel = ForgeHooks.canToolHarvestBlock( block, metadata, stack );
		
		//System.out.println(needed+" "+level+" "+goodLevel);
		//System.out.println(MinecraftForge.getBlockHarvestLevel( block, 0, "pickaxe" ));

		if ( tool.actsLike( stack, "axe" ) )
		{
			if ( block.getMaterial().equals( net.minecraft.block.material.Material.wood ) || block.getMaterial().equals( net.minecraft.block.material.Material.plants ) || block.getMaterial().equals( net.minecraft.block.material.Material.vine ) )
			{
				return goodLevel;
			}
		}
		if ( tool.actsLike( stack, "pickaxe" ) )
		{
			if ( block.getMaterial().equals( net.minecraft.block.material.Material.iron ) || block.getMaterial().equals( net.minecraft.block.material.Material.anvil ) || block.getMaterial().equals( net.minecraft.block.material.Material.rock ) )
			{
				return goodLevel;
			}
		}
		if ( tool.actsLike( stack, "shovel" ) )
		{
			if ( block.getMaterial().equals( net.minecraft.block.material.Material.snow ) )
			{
				return goodLevel;
			}
		}
		if ( tool.actsLike( stack, "sword" ) )
		{
			if ( block == net.minecraft.init.Blocks.web )
			{
				return goodLevel;
			}
		}
		
        return false;
    }
	
	@Override
    public float getDigSpeed( ItemStack stack, Block block, int meta )
    {
		metadata = meta;
		
		if ( getDamage( stack ) > getMaxDamage( stack ) )
		{
			return 1.f;
		}
		
		if ( tool.actsLike( stack, "axe" ) )
		{
			for ( ItemStack ore : OreDictionary.getOres( "treeLeaves" ) )
			{
				if ( ore.getItem() == Item.getItemFromBlock( block ) )
				{
					if ( ore.getItemDamage() == OreDictionary.WILDCARD_VALUE || metadata == ore.getItemDamage() )
					{
						return tool.getMiningSpeed( this, stack ) * 3;
					}
				}
			}
		}
		
		// TODO: Swords = shears?
		
        if ( ForgeHooks.isToolEffective( stack, block, metadata ) )
        {
            return tool.getMiningSpeed( this, stack );
        }
        
        /*
        List< Block > effList = new ArrayList< Block >();
    	if ( tool.actsLike( stack, "pickaxe" ) ) effList.addAll( Arrays.asList( ItemPickaxe.field_150915_c ) );
    	if ( tool.actsLike( stack, "shovel" ) ) effList.addAll( Arrays.asList( ItemSpade.field_150916_c ) );
    	if ( tool.actsLike( stack, "axe" ) ) effList.addAll( Arrays.asList( ItemAxe.field_150917_c ) );
    	
        for ( Block b : effList )
        {
            if ( b == block )
            {
                return tool.getMiningSpeed( this, stack );
            }
        }
        */

		if ( tool.actsLike( stack, "axe" ) )
		{
			if ( block.getMaterial().equals( net.minecraft.block.material.Material.wood ) || block.getMaterial().equals( net.minecraft.block.material.Material.plants ) || block.getMaterial().equals( net.minecraft.block.material.Material.vine ) )
			{
				return tool.getMiningSpeed( this, stack );
			}
		}
		if ( tool.actsLike( stack, "pickaxe" ) )
		{
			if ( block.getMaterial().equals( net.minecraft.block.material.Material.iron ) || block.getMaterial().equals( net.minecraft.block.material.Material.anvil ) || block.getMaterial().equals( net.minecraft.block.material.Material.rock ) )
			{
				return tool.getMiningSpeed( this, stack );
			}
		}
		if ( tool.actsLike( stack, "shovel" ) )
		{
			if ( block.getMaterial().equals( net.minecraft.block.material.Material.snow ) )
			{
				return tool.getMiningSpeed( this, stack );
			}
		}
		if ( tool.actsLike( stack, "sword" ) )
		{
			if ( block == net.minecraft.init.Blocks.web )
			{
				return tool.getMiningSpeed( this, stack );
			}
		}

        return 1.0F;
    }
	
	@Optional.Method( modid = "Thaumcraft" )
	public static boolean isPickaxe( Item item )
	{
		if ( !( item instanceof HarvesterToolItem ) )
		{
			return false;
		}
		HarvesterToolItem tool = ( HarvesterToolItem ) item;
		
		return tool.tool.actsLike( tool, "pickaxe" );
	}
	
	// NOTE: This is a workaround for canHarvestBlock not supplying metadata.
	//@Deprecated
	public static int metadata;
}
