package com.spacechase0.minecraft.componentequipment.tool.modifier;

import java.util.List;
import java.util.UUID;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import com.google.common.collect.Multimap;
import com.spacechase0.minecraft.componentequipment.ComponentEquipment;
import com.spacechase0.minecraft.componentequipment.item.EquipmentItem;
import com.spacechase0.minecraft.componentequipment.tool.Equipment;
import com.spacechase0.minecraft.componentequipment.tool.Modifier;
import com.spacechase0.minecraft.spacecore.util.TranslateUtils;

import cpw.mods.fml.common.Loader;

public class BackpackModifier extends Modifier
{
	public BackpackModifier( boolean theEnder )
	{
		super( theEnder ? "enderBackpack" : "chestBackpack" );
		ender = theEnder;
	}
	
	@Override
	public String getName( ItemStack stack )
	{
		EquipmentItem item = ( EquipmentItem ) stack.getItem();
		if ( ender )
		{
			return TranslateUtils.translate( "componentequipment:modifier.enderBackpack.name" );
		}
		else
		{
			return TranslateUtils.translate( "componentequipment:modifier.chestBackpack.name" ) + " " + TranslateUtils.translate( "enchantment.level." + item.equipment.getModifierLevel( stack, type ) );
		}
	}
	
	@Override
	public String getFormat( ItemStack stack )
	{
		if ( ender )
		{
			return EnumChatFormatting.DARK_BLUE.toString();
		}
		else
		{
			return EnumChatFormatting.GOLD.toString();
		}
	}
	
	@Override
	public int getIconColor( int pass )
	{
		if ( pass == 0 ) return 0x00884400;
		
		if ( ender )
		{
			return 0x002288CC;
		}
		else
		{
			return 0x00CC8822;
		}
	}

	@Override
	public void addAttributeModifiers( ItemStack stack, Multimap modifiers )
	{
		EquipmentItem item = ( EquipmentItem ) stack.getItem();
		int level = item.equipment.getModifierLevel( stack, type );
		
		modifiers.put( SharedMonsterAttributes.movementSpeed.getAttributeUnlocalizedName(), new AttributeModifier( attrModId, "Armor mod: " + type, -0.075 * item.equipment.getModifierLevel( stack, type ), 2 ) );
	}
	/*
	@Override
	public void onAdded( ItemStack stack )
	{
		EquipmentItem item = ( EquipmentItem ) stack.getItem();
		
		NBTTagCompound tag = stack.getTagCompound();
		if ( tag == null )
		{
			tag = new NBTTagCompound();
			stack.setTagCompound( tag );
		}
		
		if ( type.equals( "chestBackpack" ) && !tag.hasKey( "BackpackInventory" ) )
		{
			// ...
		}
		
		NBTTagList attrs = tag.getTagList( "AttributeModifiers", 10 );
		{
			for ( int i = 0; i < attrs.tagCount(); ++i )
			{
				NBTTagCompound mod = ( NBTTagCompound ) attrs.getCompoundTagAt( i );
				if ( SharedMonsterAttributes.readAttributeModifierFromNBT( mod ).getID().equals( attrModId ) )
				{
					attrs.removeTag( i );
					break;
				}
			}
			
			NBTTagCompound data = Equipment.getAttributeData( new AttributeModifier( attrModId, "Armor mod: " + type, -0.075 * item.equipment.getModifierLevel( stack, type ), 2 ) );
			data.setString( "AttributeName", SharedMonsterAttributes.movementSpeed.getAttributeUnlocalizedName() );
			attrs.appendTag( data );
		}
		tag.setTag( "AttributeModifiers", attrs );
	}
	*/
	
	public boolean canAdd( ItemStack stack )
	{
		EquipmentItem item = ( EquipmentItem ) stack.getItem();
		if ( ender && item.equipment.getModifierLevel( stack, "chestBackpack" ) > 0 )
		{
			return false;
		}
		else if ( !ender && item.equipment.getModifierLevel( stack, "enderBackpack" ) > 0 )
		{
			return false;
		}
		
		return super.canAdd( stack );
	}
	
	@Override
	public boolean canApplyTo( EquipmentItem item )
	{
		return ( item == ComponentEquipment.items.chestplate );
	}
	
	@Override
	public void addDisassemblyResults( ItemStack stack, List< ItemStack > results )
	{
		ItemStack result = getDisassembledSelf( stack );
		if ( !ender )
		{
			NBTTagCompound tag = new NBTTagCompound();
			tag.setTag( "Inventory", stack.getTagCompound().getTag( "Inventory" ) );
			result.getTagCompound().setTag( "Data", tag );
		}
		
		results.add( result );
	}
	
	@Override
	public int getModifierCost()
	{
		return 2;
	}
	
	public int getSlotCount( int level )
	{
		return 9 * 3;
	}
	
	public String getGuiTexture( int level )
	{
		return "componentequipment:textures/gui/backpack.png";
	}
	
	public String getModelTexture( int level )
	{
		return ( ender ? "textures/entity/chest/ender.png" : "textures/entity/chest/normal.png" );
	}
	
	private final boolean ender;
    private final UUID attrModId = UUID.fromString( "CB5F55D3-655D-7F38-A597-9C13F334B5CF" );
}
