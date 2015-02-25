package com.spacechase0.minecraft.componentequipment.tool.modifier;

import java.util.UUID;

import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import com.google.common.collect.Multimap;
import com.spacechase0.minecraft.componentequipment.item.EquipmentItem;
import com.spacechase0.minecraft.componentequipment.tool.Equipment;
import com.spacechase0.minecraft.componentequipment.tool.Modifier;
import com.spacechase0.minecraft.spacecore.util.TranslateUtils;

public class ItemAttributeModifier extends Modifier
{
	public ItemAttributeModifier( String name, EnumEnchantmentType theSlot, EnumChatFormatting theFormat, int theFrontCol, int theBackCol, int theMax, IAttribute theAttr, int theOper, double theValue, String theId )
	{
		super( name );
		slot = theSlot;
		format = theFormat;
		frontCol = theFrontCol;
		backCol = theBackCol;
		maxLevel = theMax;
		attr = theAttr;
		oper = theOper;
		perLevel = theValue;
		attrModId = UUID.fromString( theId );
	}
	
	@Override
	public String getName( ItemStack stack )
	{
		EquipmentItem item = ( EquipmentItem ) stack.getItem();
		return TranslateUtils.translate( "componentequipment:modifier." + type + ".name" ) + " " + TranslateUtils.translate( "enchantment.level." + item.equipment.getModifierLevel( stack, type ) );
	}
	
	@Override
	public String getFormat( ItemStack stack )
	{
		return EnumChatFormatting.GRAY.toString();
	}
	
	@Override
	public int getIconColor( int pass )
	{
		return ( pass == 0 ) ? backCol : frontCol;
	}
	
	public boolean canApplyTo( EquipmentItem item )
	{
		return EnchantmentModifier.doesMatch( slot, item );
	}
	
	@Override
	public int getMaxLevel()
	{
		return maxLevel;
	}

	@Override
	public void addAttributeModifiers( ItemStack stack, Multimap modifiers )
	{
		EquipmentItem item = ( EquipmentItem ) stack.getItem();
		int level = item.equipment.getModifierLevel( stack, type );
		
		modifiers.put( attr.getAttributeUnlocalizedName(), new AttributeModifier( attrModId, "Armor mod: " + type + " " + level, perLevel * level, oper ) );
	}

	private final EnumEnchantmentType slot;
    private final EnumChatFormatting format;
    private final int frontCol;
    private final int backCol;
    private final int maxLevel;
    private final IAttribute attr;
    private final int oper;
    private final double perLevel;
    private final UUID attrModId;
}
