package com.spacechase0.minecraft.componentequipment.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.spacechase0.minecraft.componentequipment.ComponentEquipment;
import com.spacechase0.minecraft.componentequipment.item.IngotItem;

public class PersistiumOreBlock extends SolidBlock
{
    public PersistiumOreBlock()
    {
    	super( Material.rock, "persistiumOre" );

		setHarvestLevel( "pickaxe", 3 );
		setHardness( 4.0F );
		setResistance( 6.0F );
		setStepSound( Block.soundTypeStone );
	}

    @Override
    public Item getItemDropped( int par1, Random rand, int par3 )
	{
		return ComponentEquipment.items.nugget;
	}

    @Override
	public int damageDropped( int par1 )
	{
		return IngotItem.PERSISTIUM;
	}

    @Override
	public int quantityDropped( Random rand )
	{
		return MathHelper.getRandomIntegerInRange( rand, 1, 3 );
	}

    @Override
	public int quantityDroppedWithBonus( int bonusBase, Random rand )
    {
    	// Modified from BlockOre
    	if ( bonusBase <= 0 )
    	{
    		return quantityDropped( rand );
    	}
    	
        int bonus = rand.nextInt( bonusBase + 2 ) - 1;
        return quantityDropped( rand ) * Math.max( bonus + 1, 1 );
    }
    
    @Override
    public int getExpDrop( IBlockAccess world, int par2, int par3 )
    {
    	return MathHelper.getRandomIntegerInRange( rand, 5, 9 );
    }
    
    private static final Random rand = new Random();
}
