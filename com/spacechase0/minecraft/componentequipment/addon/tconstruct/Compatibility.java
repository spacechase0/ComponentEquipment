package com.spacechase0.minecraft.componentequipment.addon.tconstruct;

import net.minecraft.util.EnumChatFormatting;

import com.spacechase0.minecraft.componentequipment.CEAddon;
import com.spacechase0.minecraft.componentequipment.addon.tconstruct.material.StoneboundMaterial;
import com.spacechase0.minecraft.componentequipment.tool.Material;
import com.spacechase0.minecraft.componentequipment.tool.material.OreDictionaryMaterial;

public class Compatibility extends CEAddon
{
	@Override
	public void init()
	{
		Material.addType( new OreDictionaryMaterial( "cobalt",    "ingotCobalt",     800, 1.75f, 37, 11.f, 4, 3, 22, EnumChatFormatting.BLUE ) );
		Material.addType( new StoneboundMaterial   ( "ardite",    "ingotArdite",     600, 2.00f, 40,  8.f, 4, 3, 21, EnumChatFormatting.GOLD, 2 ) );
		Material.addType( new OreDictionaryMaterial( "manyullyn", "ingotManyullyn", 1200, 2.50f, 45,  9.f, 5, 4, 24, EnumChatFormatting.DARK_PURPLE ) );
		Material.addType( new OreDictionaryMaterial( "alumite",  " ingotAlumite",    550, 1.30f, 35,  8.f, 4, 3, 20, EnumChatFormatting.LIGHT_PURPLE ) );
		
		Material.addType( new StoneboundMaterial( "stone", "cobblestone", 200, 0.50f,  3,  4.0f, 1, 1, 5, EnumChatFormatting.DARK_GRAY, 1 ) );
	}
}
