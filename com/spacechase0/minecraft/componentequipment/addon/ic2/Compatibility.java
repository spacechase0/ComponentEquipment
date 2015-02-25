package com.spacechase0.minecraft.componentequipment.addon.ic2;

import ic2.api.item.IC2Items;
import ic2.api.item.IElectricItemManager;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import com.spacechase0.minecraft.componentequipment.CEAddon;
import com.spacechase0.minecraft.componentequipment.addon.ic2.modifier.ElectricModifier;
import com.spacechase0.minecraft.componentequipment.tool.Modifier;
import com.spacechase0.minecraft.componentequipment.tool.ModifierRecipes;
import com.spacechase0.minecraft.spacecore.util.ListUtils;

public class Compatibility extends CEAddon
{
	@Override
	public void init()
	{
		Modifier.addType( new ElectricModifier() );
		ModifierRecipes.add( "electric", 1, ListUtils.asList( IC2Items.getItem( "reBattery" ) ) );
		ModifierRecipes.add( "electric", 1, ListUtils.asList( IC2Items.getItem( "advBattery" ) ) );
		ModifierRecipes.add( "electric", 1, ListUtils.asList( IC2Items.getItem( "energyCrystal" ) ) );
		ModifierRecipes.add( "electric", 1, ListUtils.asList( IC2Items.getItem( "lapotronCrystal" ) ) );
	}
	
	public static IElectricItemManager getElectricItemManager()
	{
		return electricManager;
	}
	
	private static IElectricItemManager electricManager = new ElectricItemManager();
}
