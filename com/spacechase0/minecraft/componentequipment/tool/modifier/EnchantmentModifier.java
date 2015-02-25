package com.spacechase0.minecraft.componentequipment.tool.modifier;

import java.util.Iterator;
import java.util.Map;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import com.spacechase0.minecraft.componentequipment.item.ArmorItem;
import com.spacechase0.minecraft.componentequipment.item.EquipmentItem;
import com.spacechase0.minecraft.componentequipment.item.ToolItem;
import com.spacechase0.minecraft.componentequipment.tool.Modifier;
import com.spacechase0.minecraft.componentequipment.tool.Tool;
import com.spacechase0.minecraft.componentequipment.tool.EquipmentData;

public class EnchantmentModifier extends Modifier
{
	public EnchantmentModifier( String theType, EnumChatFormatting theFormat, int theFrontCol, int theBackCol, Enchantment theEnch )
	{
		super( theType );
		ench = theEnch;
		format = theFormat.toString();
		frontCol = theFrontCol;
		backCol = theBackCol;
	}
	
	@Override
	public String getName( ItemStack stack )
	{
		EquipmentItem item = ( EquipmentItem ) stack.getItem();
		return ench.getTranslatedName( item.equipment.getModifierLevel( stack, type ) );
	}
	
	@Override
	public String getFormat( ItemStack stack )
	{
		return format;
	}
	
	@Override
	public int getIconColor( int pass )
	{
		return ( pass == 0 ) ? backCol : frontCol;
	}
	
	public boolean canApplyTo( EquipmentItem item )
	{
		return doesMatch( ench.type, item ) || ench == Enchantment.unbreaking;
	}
	
	@Override
	public boolean canAdd( ItemStack stack )
	{
		Map enchs = EnchantmentHelper.getEnchantments( stack );
		Iterator it = enchs.entrySet().iterator();
		while ( it.hasNext() )
		{
			Map.Entry entry = ( Map.Entry ) it.next();
			int id = ( Integer ) entry.getKey();
			int level = ( Integer ) entry.getValue();
			
			Enchantment ench2 = Enchantment.enchantmentsList[ id ];
			if ( ench != ench2 && !ench.canApplyTogether( ench2 ) )
			{
				return false;
			}
		}

		EquipmentItem item = ( EquipmentItem ) stack.getItem();
		return ( ( doesMatch( ench.type, item ) || ench == Enchantment.unbreaking ) && item.equipment.getModifierLevel( stack, type ) < ench.getMaxLevel() && super.canAdd( stack ) );
	}
	
	@Override
	public int getMaxLevel( /*ItemStack stack*/ )
	{
		return ench.getMaxLevel();
	}
	
	@Override
	public void onAdded( ItemStack stack )
	{
		EquipmentItem item = ( EquipmentItem ) stack.getItem();
		stack.addEnchantment( ench, item.equipment.getModifierLevel( stack, type ) );
	}
	
	protected static boolean isTool( EquipmentItem itemBase, int type )
	{
		try
		{
			ToolItem item = ( ToolItem ) itemBase;
			if ( ( Tool.instance.getData( item.type ).getType() & type ) != 0 )
			{
				return true;
			}
			
			return false;
		}
		catch ( ClassCastException exception )
		{
			return false;
		}
	}
	
	protected static boolean doesMatch( EnumEnchantmentType type, EquipmentItem itemBase )
	{
		EquipmentData dataBase = itemBase.equipment.getBaseData( itemBase.type );
		
		if ( type == EnumEnchantmentType.all )
		{
			return true;
		}
		if ( type == EnumEnchantmentType.digger && isTool( itemBase, Tool.HARVEST ) )
		{
			return true;
		}
		else if ( type == EnumEnchantmentType.weapon && ( isTool( itemBase, Tool.WEAPON ) || itemBase.type.equals( "axe" ) ) )
		{
			return true;
		}
		else if ( type == EnumEnchantmentType.bow && isTool( itemBase, Tool.BOW ) )
		{
			return true;
		}
		else if ( type == EnumEnchantmentType.armor && itemBase instanceof ArmorItem )
		{
			return true;
		}
		else if ( itemBase instanceof ArmorItem )
		{
			ArmorItem item = ( ArmorItem ) itemBase;
			if ( type == EnumEnchantmentType.armor_head && item.type.equals( "helmet" ) )
			{
				return true;
			}
			else if ( type == EnumEnchantmentType.armor_torso && item.type.equals( "chestplate" ) )
			{
				return true;
			}
			else if ( type == EnumEnchantmentType.armor_legs && item.type.equals( "leggings" ) )
			{
				return true;
			}
			else if ( type == EnumEnchantmentType.armor_feet && item.type.equals( "boots" ) )
			{
				return true;
			}
		}
		
		return false;
	}
	
	protected Enchantment ench;
	private final String format;
	private final int frontCol;
	private final int backCol;
}
