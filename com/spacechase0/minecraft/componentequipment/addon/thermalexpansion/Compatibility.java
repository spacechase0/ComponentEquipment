package com.spacechase0.minecraft.componentequipment.addon.thermalexpansion;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import com.spacechase0.minecraft.componentequipment.CEAddon;
import com.spacechase0.minecraft.componentequipment.addon.thermalexpansion.modifier.FluxEnergyModifier;
import com.spacechase0.minecraft.componentequipment.tool.Material;
import com.spacechase0.minecraft.componentequipment.tool.Modifier;
import com.spacechase0.minecraft.componentequipment.tool.ModifierRecipes;
import com.spacechase0.minecraft.componentequipment.tool.material.OreDictionaryMaterial;
import com.spacechase0.minecraft.spacecore.util.ListUtils;

import cpw.mods.fml.common.registry.GameRegistry;

/*
THERMAL EXPANSION:

    IRON(2, 250, 6.0F, 2.0F, 14),
    IRON(15, new int[]{2, 6, 5, 2}, 9),
    
    harvest, dur, eff, damage, ench
    durFactor, damageRed, enchant
    
    public static final EnumToolMaterial TOOL_INVAR = EnumHelper.addToolMaterial("TE_INVAR", 2, 450, 6F, 2.0F, 16);
    public static final EnumArmorMaterial ARMOR_INVAR = EnumHelper.addArmorMaterial("TE_INVAR", 25, new int[] {
        2, 7, 5, 2
    }, 11);
    "ingotInvar"
    
    Also, RF battery modifier? :P
    TConstruct did it
*/

public class Compatibility extends CEAddon
{
	@Override
	public void init()
	{
		                             //Material.addType( "iron",    Item.ingotIron,    300, 1.00f, 15,  6.0f, 2, 2, 2.00f, 1.50f, EnumChatFormatting.WHITE );
		Material.addType( new OreDictionaryMaterial( "invar", "ingotInvar",     400, 1.10f, 25, 6.f, 2, 2, 16, EnumChatFormatting.GRAY ) );
		
		Modifier.addType( new FluxEnergyModifier() );
		ModifierRecipes.add( "fluxEnergy", 1, ListUtils.asList( GameRegistry.findItemStack( "ThermalExpansion", "capacitorPotato",     1 ) ) );
		ModifierRecipes.add( "fluxEnergy", 2, ListUtils.asList( GameRegistry.findItemStack( "ThermalExpansion", "capacitorBasic",      1 ) ) );
		ModifierRecipes.add( "fluxEnergy", 3, ListUtils.asList( GameRegistry.findItemStack( "ThermalExpansion", "capacitorHardened",   1 ) ) );
		ModifierRecipes.add( "fluxEnergy", 4, ListUtils.asList( GameRegistry.findItemStack( "ThermalExpansion", "capacitorReinforced", 1 ) ) );
		ModifierRecipes.add( "fluxEnergy", 5, ListUtils.asList( GameRegistry.findItemStack( "ThermalExpansion", "capacitorResonant",   1 ) ) );
	}
}
