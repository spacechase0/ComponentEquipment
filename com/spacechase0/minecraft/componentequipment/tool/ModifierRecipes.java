package com.spacechase0.minecraft.componentequipment.tool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.minecraft.init.Blocks.*;
import static net.minecraft.init.Items.*;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import com.spacechase0.minecraft.componentequipment.ComponentEquipment;
import com.spacechase0.minecraft.componentequipment.tool.ModifierRecipes.Recipe.StackType;
import com.spacechase0.minecraft.spacecore.recipe.RecipeSimplifier;
import com.spacechase0.minecraft.spacecore.util.ListUtils;

public class ModifierRecipes
{
	public static class Recipe
	{
		public static class StackType
		{
			public StackType( ItemStack stack )
			{
				stacks.add( stack );
				amt = stack.stackSize;
			}
			
			public StackType( List< ItemStack > theStacks, int theAmt )
			{
				stacks = theStacks;
				amt = theAmt;
			}
			
			public boolean matches( ItemStack a )
			{
				for ( ItemStack b : stacks )
				{
					if ( RecipeSimplifier.matches( b, a ) )
					{
						return true;
					}
				}
				
				return false;
			}
			
			@Override
			public boolean equals( Object obj )
			{
				if ( !( obj instanceof StackType ) ) return false;
				
				List< ItemStack > match = ListUtils.clone( ( ( StackType ) obj ).stacks );
				int matchCount = 0;
				for ( ItemStack a : stacks )
				{
					int toRemove = -1;
					for ( int i = 0; i < match.size(); ++i )
					{
						ItemStack b = match.get( i );
						if ( a.getItem() != b.getItem() || a.getItemDamage() != b.getItemDamage() ||
						     ( a.getTagCompound() == null ) != ( b.getTagCompound() == null ) )
						{
							continue;
						}
						
						if ( a.getTagCompound() != null && !a.getTagCompound().equals( b.getTagCompound() ) )
						{
							continue;
						}
						
						toRemove = i;
						++matchCount;
						break;
					}
					
					match.remove( toRemove );
				}
				
				return ( matchCount == stacks.size() );
			}
			
			public StackType copy()
			{
				return new StackType( ListUtils.clone( stacks ), amt );
			}
			
			public List< ItemStack > stacks = new ArrayList< ItemStack >();
			public int amt;
		}
		
		public String mod;
		public int level;
		
		public List< StackType > ingredients = new ArrayList< StackType >();
	}
	
	public static void add( String mod, int level, List< ItemStack > ingredients )
	{
		Recipe recipe = new Recipe();
		recipe.mod = mod;
		recipe.level = level;
		for ( ItemStack ingredient : ingredients )
		{
			recipe.ingredients.add( new StackType( ingredient.copy() ) );
		}
		
		recipes.add( recipe );
	}
	
	public static void addWithVariants( String mod, int level, List< StackType > ingredients )
	{
		Recipe recipe = new Recipe();
		recipe.mod = mod;
		recipe.level = level;
		recipe.ingredients = ListUtils.clone( ingredients );
		
		recipes.add( recipe );
	}
	
	public static void addFixed( String mod, int levels, List< ItemStack > ingredients )
	{
		for ( int i = 1; i <= levels; ++i )
		{
			add( mod, i, ingredients );
		}
	}
	
	public static void addFixedWithVariants( String mod, int levels, List< StackType > ingredients )
	{
		for ( int i = 1; i <= levels; ++i )
		{
			addWithVariants( mod, i, ingredients );
		}
	}
	
	public static void addIncreasing( String mod, int levels, List< ItemStack > stacks, int initial, int incr )
	{
		for ( int i = 1; i <= levels; ++i )
		{
			List< ItemStack > newStacks = new ArrayList< ItemStack >();
			for ( ItemStack stack : stacks )
			{
				ItemStack newStack = stack.copy();
				newStack.stackSize = initial + ( incr * ( i - 1 ) );
				newStacks.add( newStack );
			}
			add( mod, i, newStacks );
		}
	}

	public static void addIncreasingWithVariants( String mod, int levels, List< StackType > stacks, int initial, int incr )
	{
		for ( int i = 1; i <= levels; ++i )
		{
			List< StackType > newStacks = new ArrayList< StackType >();
			for ( StackType stack : stacks )
			{
				StackType newStack = stack.copy();
				newStack.amt = initial + ( incr * ( i - 1 ) );
				newStacks.add( newStack );
			}
			addWithVariants( mod, i, newStacks );
		}
	}
	
	public static final List< Recipe > recipes = new ArrayList< Recipe >();
	
	public static void addDefaultRecipes()
	{
		// General
		addFixed( "unbreaking", 3, ListUtils.asList( new ItemStack( emerald_block ) ) );
		addIncreasing( "luck", 3, ListUtils.asList( new ItemStack( lapis_block ) ), 8, 6 ); // Sword + Pick
		
		// Tools
		addIncreasing( "efficiency", 5, ListUtils.asList( new ItemStack( redstone_block ) ), 4, 6 );
		add( "silkTouch", 1, ListUtils.asList( new ItemStack( string, 40 ) ) );

		// Swords
		addIncreasing( "damage", 5, ListUtils.asList( new ItemStack( iron_block ),
		                                              new ItemStack( quartz_block ) ), 1, 1 ); // Sword + Bow
		addIncreasing( "smite", 5, ListUtils.asList( new ItemStack( iron_block ),
		                                             new ItemStack( rotten_flesh ) ), 1, 1 );
		addIncreasing( "baneOfArthropods", 5, ListUtils.asList( new ItemStack( iron_block ),
		                                                        new ItemStack( spider_eye ) ), 1, 1 );
		addIncreasing( "knockback", 2, ListUtils.asList( new ItemStack( tnt ) ), 8, 12 ); // Sword + Bow
		addIncreasing( "fire", 2, ListUtils.asList( new ItemStack( fire_charge ) ), 16, 8 ); // Sword + Bow
		
		// Bow
		add( "infinity", 1, ListUtils.asList( new ItemStack( ComponentEquipment.blocks.mysteriousOrb, 4 ) ) );
		
		// Armor
		addIncreasing( "protection", 4, ListUtils.asList( new ItemStack( iron_block ),
		                                                  new ItemStack( quartz_block ) ), 1, 1 );
		addIncreasing( "blastProtection", 4, ListUtils.asList( new ItemStack( tnt ) ), 6, 4 );
		addIncreasing( "fireProtection", 4, ListUtils.asList( new ItemStack( nether_brick ),  new ItemStack( lava_bucket ) ), 4, 4 );
		addIncreasing( "projectileProtection", 4, ListUtils.asList( new ItemStack( ComponentEquipment.blocks.bones ) ), 4, 4 );
		addIncreasing( "featherFalling", 4, ListUtils.asList( new ItemStack( wool, 1, OreDictionary.WILDCARD_VALUE ) ), 16, 8 );
		addIncreasing( "thorns", 3, ListUtils.asList( new ItemStack( ComponentEquipment.blocks.diamondCactus ) ), 4, 8 );
		addIncreasing( "respiration", 3, ListUtils.asList( new ItemStack( pumpkin ),
		                                                   new ItemStack( carrot ) ), 8, 8 );
		add( "aquaAffinity", 1, ListUtils.asList( new ItemStack( fish, 32 ) ) );

		add( "extra1", 1, ListUtils.asList( new ItemStack( diamond_block ) ) );
		add( "extra2", 1, ListUtils.asList( new ItemStack( nether_star ) ) );
		
		addFixed( "cheapRepair", 3, ListUtils.asList( new ItemStack( gold_block ) ) );
		addFixed( "lifeSteal", 2, ListUtils.asList( new ItemStack( Items.skull, 1, 1 ) ) );
		addIncreasing( "selfRepair", 3, ListUtils.asList( new ItemStack( diamond_ore ) ), 6, 4 );

		addFixed( "maxHealth", 10, ListUtils.asList( new ItemStack( Items.cake, 16 ), new ItemStack( gold_ore, 16 ) ) );
		addFixed( "knockbackResist", 2, ListUtils.asList( new ItemStack( obsidian, 12 ), new ItemStack( brick_block, 12 ) ) );
		addFixed( "walkSpeed", 4, ListUtils.asList( new ItemStack( golden_rail, 24 ) ) );
		add( "walkSlope", 1, ListUtils.asList( new ItemStack( piston, 2 ), new ItemStack( slime_ball, 2 ) ) );
		add( "enderBackpack", 1, ListUtils.asList( new ItemStack( ender_chest, 1, OreDictionary.WILDCARD_VALUE ) ) );
		add( "chestBackpack", 1, ListUtils.asList( new ItemStack( chest, 1, OreDictionary.WILDCARD_VALUE ) ) );
		add( "waterWalk", 1, ListUtils.asList( new ItemStack( water_bucket ),
		                                       new ItemStack( ender_eye ),
		                                       new ItemStack( lapis_block ) ) );
		
		add( "portableJukebox", 1, ListUtils.asList( new ItemStack( jukebox, 1, OreDictionary.WILDCARD_VALUE ) ) );
		add( "invisibleModifiers", 1, ListUtils.asList( new ItemStack( potionitem, 1, 8206 ) ) );
	}
}
