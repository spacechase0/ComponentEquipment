package com.spacechase0.minecraft.componentequipment.addon.ironchests;

import java.util.Arrays;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import com.spacechase0.minecraft.componentequipment.CEAddon;
import com.spacechase0.minecraft.componentequipment.CELog;
import com.spacechase0.minecraft.componentequipment.addon.ironchests.modifier.ExpandedBackpackModifier;
import com.spacechase0.minecraft.componentequipment.tool.Material;
import com.spacechase0.minecraft.componentequipment.tool.Modifier;
import com.spacechase0.minecraft.componentequipment.tool.ModifierRecipes;
import com.spacechase0.minecraft.componentequipment.tool.material.OreDictionaryMaterial;
import com.spacechase0.minecraft.spacecore.util.ListUtils;

import cpw.mods.fml.common.registry.GameRegistry;

public class Compatibility extends CEAddon
{
	@Override
	public void init()
	{
		Modifier.addType( new ExpandedBackpackModifier() );

		ModifierRecipes.add( "chestBackpack", 2, ListUtils.asList( getStackFor( "ironchest:woodIronUpgrade" ) ) );
		ModifierRecipes.add( "chestBackpack", 3, ListUtils.asList( getStackFor( "ironchest:ironGoldUpgrade" ) ) );
		ModifierRecipes.add( "chestBackpack", 4, ListUtils.asList( getStackFor( "ironchest:goldDiamondUpgrade" ) ) );
	}
}
