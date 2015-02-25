package com.spacechase0.minecraft.componentequipment.addon.forestry;

import java.util.Arrays;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import com.spacechase0.minecraft.componentequipment.CEAddon;
import com.spacechase0.minecraft.componentequipment.addon.forestry.modifier.BeeSuitModifier;
import com.spacechase0.minecraft.componentequipment.addon.forestry.modifier.NatureVisorModifier;
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
		Modifier.addType( new BeeSuitModifier() );
		Modifier.addType( new NatureVisorModifier() );

		// The stuff used for making the bee armor
		ItemStack beeMat = getStackFor( "forestry:craftingMaterial" );
		beeMat.stackSize = 6;
		ItemStack natureMat = getStackFor( "forestry:naturalistHat" );

		ModifierRecipes.add( "beeSuit", 1, ListUtils.asList( beeMat ) );
		ModifierRecipes.add( "beeSuit", 1, ListUtils.asList( natureMat ) );
	}
}
