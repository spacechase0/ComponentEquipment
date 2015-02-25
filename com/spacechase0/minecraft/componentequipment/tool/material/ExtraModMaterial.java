package com.spacechase0.minecraft.componentequipment.tool.material;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import com.spacechase0.minecraft.componentequipment.tool.MaterialData;

public class ExtraModMaterial extends MaterialData
{
	public ExtraModMaterial( String theType, ItemStack mat, int theBaseDur, float theMult, int theArmorDurMult, float theSpeed, int theLevel, int theDamage, int theTotalArmor, EnumChatFormatting theFormat )
	{
		super( theType, mat, theBaseDur, theMult, theArmorDurMult, theSpeed, theLevel, theDamage, theTotalArmor, theFormat.toString() );
	}
	
	@Override
	public int getExtraModifiers()
	{
		return 1;
	}
	
	@Override
	public String getSpecialAbility()
	{
		return "extraMod";
	}
}
