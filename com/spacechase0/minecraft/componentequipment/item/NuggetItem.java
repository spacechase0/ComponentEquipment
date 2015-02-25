package com.spacechase0.minecraft.componentequipment.item;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class NuggetItem extends SimpleItem
{
	public NuggetItem( String[] theTypes )
	{
		super( "nuggetItem" );
		types = theTypes;
	}

	@Override
    public void registerIcons( IIconRegister register )
    {
		icons = new IIcon[ types.length ];
		for ( int i = 0+1/*no stainless iron*/; i < types.length; ++i )
		{
			icons[ i ] = register.registerIcon( "componentequipment:" + types[ i ] + "Nugget" );
		}
    }
	
	@Override
    public void getSubItems( Item id, CreativeTabs tabs, List list )
    {
		for ( int i = 0+1/*no stainless iron*/; i < types.length; ++i )
		{
			list.add( new ItemStack( id, 1, i ) );
		}
    }
	
	@Override
    public IIcon getIconFromDamage( int damage )
    {
        return icons[ damage ];
    }
	
	@Override
    public String getUnlocalizedName( ItemStack stack )
    {
        return "item." + types[ stack.getItemDamage() ] + "Nugget";
    }
	
	public final String[] types;
	public IIcon[] icons;
}
