package com.spacechase0.minecraft.componentequipment.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.MinecraftForge;

public class BonesBlock extends SolidBlock
{
	public BonesBlock()
	{
		super( Material.rock, "bonesBlock" );
		
		setHarvestLevel( "pickaxe", 1 );
		setHardness( 1.f );
		
		setStepSound( Block.soundTypeStone );
	}
}
