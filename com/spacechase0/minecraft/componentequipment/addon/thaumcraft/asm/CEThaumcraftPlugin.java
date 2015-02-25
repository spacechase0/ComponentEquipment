package com.spacechase0.minecraft.componentequipment.addon.thaumcraft.asm;

import java.util.Map;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;

@TransformerExclusions( value = { "com.spacechase0.minecraft.componentequipment.addon.thaumcraft.asm" } )
public class CEThaumcraftPlugin implements IFMLLoadingPlugin
{
	public CEThaumcraftPlugin()
	{
	}
	
	@Override
	public String getAccessTransformerClass()
	{
		return null;
	}

	@Override
	public String[] getASMTransformerClass()
	{
		return new String[]
			   {
				"com.spacechase0.minecraft.componentequipment.addon.thaumcraft.asm.BorePickaxeCheckTransformer",
			   };
	}

	@Override
	public String getModContainerClass()
	{
		return null;
	}

	@Override
	public String getSetupClass()
	{
		return null;
	}

	@Override
	public void injectData( Map< String, Object > data )
	{
	}
}
