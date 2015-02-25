package com.spacechase0.minecraft.componentequipment.inventory;

import net.minecraft.item.ItemStack;

import com.spacechase0.minecraft.componentequipment.item.EquipmentItem;
import com.spacechase0.minecraft.componentequipment.tool.Modifier;
import com.spacechase0.minecraft.componentequipment.tool.modifier.BackpackModifier;
import com.spacechase0.minecraft.spacecore.inventory.ItemStackInventory;

public class BackpackInventory extends ItemStackInventory
{
	public BackpackInventory( ItemStack theInvStack )
	{
		super( theInvStack, getSlotCount( theInvStack ) );
	}

	@Override
	public String getInventoryName()
	{
		return "gui.componentequipment:backpack.name";
	}

	@Override
	public boolean hasCustomInventoryName()
	{
		return false;
	}
	
	private static int getSlotCount( ItemStack stack )
	{
		EquipmentItem equipment = ( EquipmentItem ) stack.getItem();
		BackpackModifier backpack = ( BackpackModifier ) Modifier.getModifier( "chestBackpack" );
		
		return backpack.getSlotCount( equipment.equipment.getModifierLevel( stack, "chestBackpack" ) );
	}
}
