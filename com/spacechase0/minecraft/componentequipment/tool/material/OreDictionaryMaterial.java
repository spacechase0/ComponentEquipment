package com.spacechase0.minecraft.componentequipment.tool.material;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import com.spacechase0.minecraft.componentequipment.tool.MaterialData;
import com.spacechase0.minecraft.spacecore.recipe.RecipeSimplifier;

public class OreDictionaryMaterial extends MaterialData
{
	public OreDictionaryMaterial( String theType, String theCrafting, int theBaseDur, float theMult, int theArmorDurMult, float theSpeed, int theLevel, int theDamage, int theTotalArmor, EnumChatFormatting theFormat )
	{
		super( theType, new ItemStack( Blocks.sponge ), theBaseDur, theMult, theArmorDurMult, theSpeed, theLevel, theDamage, theTotalArmor, theFormat.toString() );
		crafting = theCrafting;
	}

	@Override
	public ItemStack[] getCraftingMaterials()
	{
		return RecipeSimplifier.getAliases( crafting );
	}
	
	private final String crafting;
}
