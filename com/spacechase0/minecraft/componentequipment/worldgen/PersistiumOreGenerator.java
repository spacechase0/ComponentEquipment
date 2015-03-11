package com.spacechase0.minecraft.componentequipment.worldgen;

import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;

import com.spacechase0.minecraft.componentequipment.ComponentEquipment;

import cpw.mods.fml.common.IWorldGenerator;

public class PersistiumOreGenerator implements IWorldGenerator
{
	@Override
	public void generate( Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider )
	{
		currentWorld = world;
		randomGenerator = random;
		chunk_X = chunkX;
		chunk_Z = chunkZ;
		
		if ( random.nextInt( 4 ) == 0 )
		{
			genStandardOre1( 1, oreGenSmall, 8, 16 );
		}
		else
		{
			genStandardOre1( 1, oreGenBig, 16, 24 );
		}
	}
	
    protected void genStandardOre1(int par1, WorldGenerator par2WorldGenerator, int par3, int par4)
    {
        for (int var5 = 0; var5 < par1; ++var5)
        {
            int var6 = this.chunk_X + this.randomGenerator.nextInt(16);
            int var7 = this.randomGenerator.nextInt(par4 - par3) + par3;
            int var8 = this.chunk_Z + this.randomGenerator.nextInt(16);
            par2WorldGenerator.generate(this.currentWorld, this.randomGenerator, var6, var7, var8);
        }
    }
	
	private WorldGenMinable oreGenBig = new WorldGenMinable( ComponentEquipment.blocks.persistiumOre, 4 );
	private WorldGenMinable oreGenSmall = new WorldGenMinable( ComponentEquipment.blocks.persistiumOre, 2 );
	
	private World currentWorld;
	private Random randomGenerator;
	private int chunk_X, chunk_Z;
}
