package com.spacechase0.minecraft.componentequipment.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;

public class SolidBlock extends Block
{
	public SolidBlock( Material mat, String theName )
	{
		super( mat );
		
		name = theName;
		setBlockName( name );
		setCreativeTab( CreativeTabs.tabBlock );
	}

	@Override
    public void registerBlockIcons( IIconRegister register )
    {
		blockIcon = register.registerIcon( "componentequipment:" + name );
    }
	
	private final String name;
}
