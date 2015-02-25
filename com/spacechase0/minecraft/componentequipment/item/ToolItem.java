package com.spacechase0.minecraft.componentequipment.item;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;

import com.spacechase0.minecraft.componentequipment.ComponentEquipment;
import com.spacechase0.minecraft.componentequipment.tool.Tool;

public class ToolItem extends EquipmentItem
{
	public ToolItem( String theType )
	{
		super( theType, Tool.instance );
		tool = Tool.instance;
		
		setCreativeTab( ComponentEquipment.toolsTab );
	}
	
	@Override
	protected String getIconDirectory()
	{
		return "tools";
	}
	
	public final Tool tool;

}