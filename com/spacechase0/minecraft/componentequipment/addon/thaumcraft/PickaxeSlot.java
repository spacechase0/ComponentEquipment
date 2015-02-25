package com.spacechase0.minecraft.componentequipment.addon.thaumcraft;

import com.spacechase0.minecraft.componentequipment.ComponentEquipment;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;

public class PickaxeSlot extends Slot
{
	public PickaxeSlot( IInventory inv, int slot, int x, int y )
	{
		super( inv, slot, x, y );
	}
	
	@Override
	public boolean isItemValid( ItemStack stack )
	{
		if ( stack == null )
		{
			return true;
		}
		
		
		Item item = stack.getItem();
		if ( item instanceof ItemPickaxe || item == ComponentEquipment.items.pickaxe )
		{
			return true;
		}
		
		return false;
	}
}
