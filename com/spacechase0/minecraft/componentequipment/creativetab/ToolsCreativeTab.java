package com.spacechase0.minecraft.componentequipment.creativetab;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import com.spacechase0.minecraft.componentequipment.ComponentEquipment;

public class ToolsCreativeTab extends CreativeTabs {

	public ToolsCreativeTab()
	{
		super( "ceTools" );
	}

	@Override
	public ItemStack getIconItemStack()
	{
		ItemStack stack = new ItemStack( ComponentEquipment.items.pickaxe );
		
		NBTTagCompound tag = new NBTTagCompound();
		tag.setString( "handle", "diamond" );
		tag.setString( "head", "gold" );
		tag.setString( "binding", "iron" );
		
		stack.setTagCompound( tag );
		
		return stack;
	}

	@Override
	public Item getTabIconItem()
	{
		return null;
	}
}
