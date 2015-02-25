package com.spacechase0.minecraft.componentequipment.item;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class IngotItem extends SimpleItem
{
	public IngotItem( String[] theTypes )
	{
		super( "ingotItem" );
		types = theTypes;
	}

	@Override
    public void registerIcons( IIconRegister register )
    {
		icons = new IIcon[ types.length ];
		for ( int i = 0; i < types.length; ++i )
		{
			icons[ i ] = register.registerIcon( "componentequipment:" + types[ i ] + "Ingot" );
		}
    }
	
	@Override
    public void getSubItems( Item id, CreativeTabs tabs, List list )
    {
		for ( int i = 0; i < types.length; ++i )
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
        return "item." + types[ stack.getItemDamage() ] + "Ingot";
    }
	
	public final String[] types;
	public IIcon[] icons;
	
	public static final int STAINLESS_IRON = 0;
	public static final int PERSISTIUM = 1;
	public static final String[] TYPES = new String[] { "stainlessIron", "persistium" };
}
