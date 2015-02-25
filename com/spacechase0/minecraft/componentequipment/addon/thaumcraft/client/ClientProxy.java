package com.spacechase0.minecraft.componentequipment.addon.thaumcraft.client;

import com.spacechase0.minecraft.componentequipment.addon.thaumcraft.CommonProxy;
import com.spacechase0.minecraft.componentequipment.client.model.ArmorModel;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

public class ClientProxy extends CommonProxy
{
	@Override
	public void init()
	{
		ArmorModel.registerModifierRenderer( "hoverHarness", new HoverHarnessRenderer() );
		
		FMLCommonHandler.instance().bus().register( harnessFuel = new HarnessFuelRenderer() );
	}
	
	private HarnessFuelRenderer harnessFuel;
}
