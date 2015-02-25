package com.spacechase0.minecraft.componentequipment.tool.modifier;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

import com.spacechase0.minecraft.componentequipment.item.EquipmentItem;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

// Enchantments are in the modifiers list (and colored!), so the vanilla enchantment list is redundant.
public class EquipmentEnchantmentTooltipModifier
{
	public EquipmentEnchantmentTooltipModifier()
	{
	}
	
	@SubscribeEvent
	public void onTooltip( ItemTooltipEvent event )
	{
		modify( event.itemStack, event.toolTip );
	}
	
	// Old stuff for ITooltipModifier
	public void modify( ItemStack stack, List list )
	{
		if ( stack == null || !( stack.getItem() instanceof EquipmentItem ) )
		{
			return;
		}
		
		List< String > toRemove = new ArrayList< String >();
		
		Iterator it = list.iterator();
		while ( it.hasNext() )
		{
			String str = ( String ) it.next();
			if ( str.contains( "0/0 EU" ) )
			{
				toRemove.add( str );
				continue;
			}
			
            for ( int ie = 0; ie < Enchantment.enchantmentsList.length; ++ie )
            {
            	Enchantment ench = Enchantment.enchantmentsList[ ie ];
            	if ( ench == null )
            	{
            		continue;
            	}
            	
            	for ( int il = 1; il <= ench.getMaxLevel(); ++il )
            	{
            		String translated = ench.getTranslatedName( il );
            		if ( str.equals( translated ) )
            		{
            			ie = Enchantment.enchantmentsList.length;
            			toRemove.add( str );
            			break;
            		}
            	}
            }
		}
		
		it = toRemove.iterator();
		while ( it.hasNext() )
		{
			String str = ( String ) it.next();
			list.remove( str );
		}
	}
}
