package com.spacechase0.minecraft.componentequipment.item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;

import com.spacechase0.minecraft.componentequipment.ComponentEquipment;
import com.spacechase0.minecraft.componentequipment.tool.Material;
import com.spacechase0.minecraft.componentequipment.tool.Part;
import com.spacechase0.minecraft.spacecore.util.TranslateUtils;

public class PartCasingItem extends SimpleItem
{
	public PartCasingItem()
	{
		super( "partCasingItem" );
		setCreativeTab( ComponentEquipment.partsTab );
		
		//setMaxStackSize( 8 );
	}

	@Override
    public void registerIcons( IIconRegister register )
    {
    }
	
	@Override
    public void getSubItems( Item id, CreativeTabs tabs, List list )
    {
		String[] mats = Material.getTypes();
		String[] parts = Part.getTypes();
		for ( int ip = 0; ip < parts.length; ++ip )
		{
			String part = parts[ ip ];
			if ( part.equals( "paxelHead" ) || part.equals( "helm" ) || part.equals( "chest" ) || part.equals( "leggings" ) || part.equals( "boots" ) )
			{
				continue;
			}
			
			for ( int im = 0; im < mats.length; ++im )
			{
				NBTTagCompound tag = new NBTTagCompound();
				tag.setString( "Part", part );
				tag.setString( "Material", mats[ im ] );

				ItemStack stack = new ItemStack( this );
				stack.setTagCompound( tag );
				list.add( stack );
			}
		}
    }
	
	@Override
    public IIcon getIcon( ItemStack stack, int pass )
    {
		return ComponentEquipment.items.part.getIcon( stack, pass );
    }
	
	@Override
	public void addInformation( ItemStack stack, EntityPlayer player, List list, boolean par4 )
	{
		if ( stack == null || stack.getTagCompound() == null )
		{
			return;
		}
		NBTTagCompound tag = stack.getTagCompound();

		String part = tag.getString( "Part" );
		
		String mat = tag.getString( "Material" );
		
		list.add( TranslateUtils.translate( "item.partCasing.tooltip.part", TranslateUtils.translate( "componentequipment:part." + part ) ) );
		list.add( TranslateUtils.translate( "item.partCasing.tooltip.material", Material.getData( mat ).getFormat() + TranslateUtils.translate( "componentequipment:material." + mat ) ) );
	}
	
	@Override
    public String getUnlocalizedName( ItemStack stack )
    {
		NBTTagCompound tag = stack.getTagCompound();
		String part = tag.getString( "Part" );
		
        return "item.partCasing";//part + "Part";
    }
	
	private Map< String, Map< String, IIcon > > icons;
}
