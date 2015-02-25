package com.spacechase0.minecraft.componentequipment.addon.tconstruct.material;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import com.spacechase0.minecraft.componentequipment.tool.MaterialData;
import com.spacechase0.minecraft.spacecore.recipe.RecipeSimplifier;

public class StoneboundMaterial extends MaterialData
{
	public StoneboundMaterial( String theType, String theCrafting, int theBaseDur, float theMult, int theArmorDurMult, float theSpeed, int theLevel, int theDamage, int theTotalArmor, EnumChatFormatting theFormat, int theStonebound )
	{
		super( theType, new ItemStack( net.minecraft.init.Blocks.sponge ), theBaseDur, theMult, theArmorDurMult, theSpeed, theLevel, theDamage, theTotalArmor, theFormat.toString() );
		crafting = theCrafting;
		stonebound = theStonebound;
	}

	@Override
	public ItemStack[] getCraftingMaterials()
	{
		return RecipeSimplifier.getAliases( crafting );
	}
	
	@Override
	public float getMiningSpeedBonus( ItemStack stack )
	{
		int durability = stack.getItemDamage();
		
		float bonus = ( ( float ) Math.log( durability / 72f + 1) * 2 * stonebound ) / 2.5f; // Copied from TiC
		
		//System.out.println( "stonebound bonus="+bonus);
		return bonus;
	}
	
	@Override
	public String getSpecialAbility()
	{
		return "stonebound";
	}
	
	public final String crafting;
	public final int stonebound;
}
