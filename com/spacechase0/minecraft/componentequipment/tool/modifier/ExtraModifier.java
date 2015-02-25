package com.spacechase0.minecraft.componentequipment.tool.modifier;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import com.spacechase0.minecraft.componentequipment.tool.Modifier;
import com.spacechase0.minecraft.spacecore.util.TranslateUtils;

public class ExtraModifier extends Modifier {

	public ExtraModifier( String theType, int theFrontCol, int theBackCol )
	{
		super( theType );
		frontCol = theFrontCol;
		backCol = theBackCol;
	}
	
	@Override
	public String getName( ItemStack stack )
	{
		return TranslateUtils.translate( "componentequipment:modifier." + type + ".name" );
	}
	
	@Override
	public int getIconColor( int pass )
	{
		return ( pass == 0 ) ? backCol : frontCol;
	}

	@Override
	public int getModifierCost()
	{
		return -1;
	}
	
	@Override
	public float getRepairModifier( ItemStack stack )
	{
		return 1.f;
	}

	private final int frontCol;
	private final int backCol;
}
