package com.spacechase0.minecraft.componentequipment.tool.modifier;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import com.spacechase0.minecraft.componentequipment.item.ArmorItem;
import com.spacechase0.minecraft.componentequipment.item.EquipmentItem;
import com.spacechase0.minecraft.componentequipment.tool.Tool;

public class MultiEnchantmentModifier extends EnchantmentModifier
{
	public MultiEnchantmentModifier( String theType, EnumChatFormatting theFormat, int theFrontCol, int theBackCol, Enchantment... theEnchs )
	{
		super( theType, theFormat, theFrontCol, theBackCol, theEnchs[ 0 ] );
		enchs = theEnchs;
	}
	
	@Override
	public String getName( ItemStack stack )
	{
		ench = getMatchingEnchantment( ( EquipmentItem ) stack.getItem() );
		if ( ench == null )
		{
			ench = enchs[ 0 ];
		}
		return super.getName( stack );
	}
	
	@Override
	public boolean canApplyTo( EquipmentItem item )
	{
		ench = getMatchingEnchantment( item );
		return ( ench != null && super.canApplyTo( item ) );
	}
	
	@Override
	public boolean canAdd( ItemStack stack )
	{
		ench = getMatchingEnchantment( ( EquipmentItem ) stack.getItem() );
		return ( ench != null && super.canAdd( stack ) );
	}
	
	@Override
	public int getMaxLevel()
	{
		int max = 0;
		for ( Enchantment theEnch : enchs )
		{
			ench = theEnch;
			max = Math.max( max, super.getMaxLevel() );
		}
		
		return max;
	}
	
	@Override
	public void onAdded( ItemStack stack )
	{
		ench = getMatchingEnchantment( ( EquipmentItem ) stack.getItem() );
		super.onAdded( stack );
	}
	
	private Enchantment getMatchingEnchantment( EquipmentItem item )
	{
		for ( int i = 0; i < enchs.length; ++i )
		{
			Enchantment ench = enchs[ i ];
			if ( ench.type == EnumEnchantmentType.all )
			{
				return ench;
			}
			else if ( ench.type == EnumEnchantmentType.digger && isTool( item, Tool.HARVEST ) )
			{
				return ench;
			}
			else if ( ench.type == EnumEnchantmentType.weapon && isTool( item, Tool.WEAPON ) )
			{
				return ench;
			}
			else if ( ench.type == EnumEnchantmentType.bow && isTool( item, Tool.BOW ) )
			{
				return ench;
			}
			else if ( ench.type == EnumEnchantmentType.armor && item instanceof ArmorItem )
			{
				return ench;
			}
			else if ( item instanceof ArmorItem )
			{
				ArmorItem armorItem = ( ArmorItem ) item;
				if ( ench.type == EnumEnchantmentType.armor_head && armorItem.type.equals( "helmet" ) )
				{
					return ench;
				}
				else if ( ench.type == EnumEnchantmentType.armor_torso && armorItem.type.equals( "chestplate" ) )
				{
					return ench;
				}
				else if ( ench.type == EnumEnchantmentType.armor_legs && armorItem.type.equals( "leggings" ) )
				{
					return ench;
				}
				else if ( ench.type == EnumEnchantmentType.armor_feet && armorItem.type.equals( "boots" ) )
				{
					return ench;
				}
			}
		}
		
		return null;
	}
	
	private Enchantment[] enchs;
}
