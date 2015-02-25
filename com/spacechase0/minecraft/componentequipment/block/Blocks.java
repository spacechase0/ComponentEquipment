package com.spacechase0.minecraft.componentequipment.block;

import net.minecraftforge.common.config.Configuration;

import com.spacechase0.minecraft.componentequipment.item.IngotBlockItem;
import com.spacechase0.minecraft.componentequipment.item.IngotItem;
import com.spacechase0.minecraft.spacecore.BaseMod;
import com.spacechase0.minecraft.spacecore.block.BlockItem;
import com.spacechase0.minecraft.spacecore.util.ModObject;

public class Blocks extends com.spacechase0.minecraft.spacecore.block.Blocks
{
	@Override
	public void register( BaseMod mod, Configuration config )
	{
		super.register( mod, config );
	}
	
	@ModObject( name = "ingotBlock" )
	@BlockItem( item = IngotBlockItem.class ) // Change to ItemBlockWithMetadata?
	public IngotBlock ingot;
	public Object[] ingotParams = new Object[] { IngotItem.TYPES };
	
	@ModObject
	public PersistiumCrystalBlock persistiumCrystal;
	
	@ModObject
	public PersistiumOreBlock persistiumOre;
	
	@ModObject
	public PersistiumInfuserBlock persistiumInfuser;
	
	@ModObject
	public DiamondCactusBlock diamondCactus;
	
	@ModObject
	public ComponentStationBlock componentStation;
	
	@ModObject
	public ModificationStandBlock modificationStand;
	
	@ModObject
	public MysteriousOrbBlock mysteriousOrb;
	
	@ModObject( name = "boneBlock" )
	public BonesBlock bones;
}
