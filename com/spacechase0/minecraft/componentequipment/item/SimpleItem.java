package com.spacechase0.minecraft.componentequipment.item;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SimpleItem extends Item
{
	public SimpleItem( String name )
	{
		tex = name;
		setUnlocalizedName( name );
		setCreativeTab( CreativeTabs.tabMisc );
	}

	@Override
    @SideOnly(Side.CLIENT)
    public void registerIcons( IIconRegister register )
    {
        itemIcon = register.registerIcon( "componentequipment:" + tex );
    }
	
	private final String tex;
}
