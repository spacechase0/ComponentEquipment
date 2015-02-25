package com.spacechase0.minecraft.componentequipment.addon.thaumcraft.recipe;

import java.util.ArrayList;

import com.spacechase0.minecraft.componentequipment.item.EquipmentItem;
import com.spacechase0.minecraft.componentequipment.tool.Modifier;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.InfusionRecipe;

public class ModifierInfusionRecipe extends InfusionRecipe
{
	public ModifierInfusionRecipe( String theMod, Object output, int inst, AspectList aspects2, ItemStack input, ItemStack[] recipe )
	{
		super( "CE_MOD_" + theMod.toUpperCase(), output, inst, aspects2, input, recipe );
		mod = theMod;
	}
	
	@Override
	public boolean matches(ArrayList<ItemStack> input, ItemStack central, World world, EntityPlayer player) {
		if (research.length()>0 && !ThaumcraftApiHelper.isResearchComplete(player.getCommandSenderName(), research)) {
    		return false;
    	}

		ItemStack i2 = central.copy();
		/*
		if (recipeInput.getItemDamage()==OreDictionary.WILDCARD_VALUE) {
			i2.setItemDamage(OreDictionary.WILDCARD_VALUE);
		}
		
		if (!areItemStacksEqual(i2, recipeInput, false)) return false;
		*/
		if ( getRecipeInput().getItem() != i2.getItem()) return false;
		
		ArrayList<ItemStack> ii = new ArrayList<ItemStack>();
		for (ItemStack is:input) {
			ii.add(is.copy());
		}
		
		for (ItemStack comp:getComponents()) {
			boolean b=false;
			for (int a=0;a<ii.size();a++) {
				 i2 = ii.get(a).copy();
				if (comp.getItemDamage()==OreDictionary.WILDCARD_VALUE) {
					i2.setItemDamage(OreDictionary.WILDCARD_VALUE);
				}
				if (areItemStacksEqual(i2, comp,true)) {
					ii.remove(a);
					b=true;
					break;
				}
			}
			if (!b) return false;
		}
//		System.out.println(ii.size());
		// My changes start here -- line below was originally last return
		boolean cont = ii.size()==0?true:false;
		if ( !cont )
		{
			return false;
		}
		
		EquipmentItem item = ( EquipmentItem ) central.getItem();
		
		
		return ( item.equipment.getModifiersRemaining( central ) >= Modifier.getModifier( mod ).getModifierCost() );
    }
	
	public final String mod;
}
