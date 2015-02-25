package com.spacechase0.minecraft.componentequipment.creativetab;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import com.spacechase0.minecraft.componentequipment.ComponentEquipment;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class PartsCreativeTab extends CreativeTabs {

	public PartsCreativeTab()
	{
		super( "ceParts" );
	}

	@Override
	public ItemStack getIconItemStack()
	{
		ItemStack stack = new ItemStack( ComponentEquipment.items.part );
		
		NBTTagCompound tag = new NBTTagCompound();
		tag.setString( "Part", "pickaxeHead" );
		tag.setString( "Material", "iron" );
		
		stack.setTagCompound( tag );
		
		return stack;
	}

	@Override
	public Item getTabIconItem()
	{
		return null;
	}
}
