package com.spacechase0.minecraft.componentequipment.tool.modifier;

import java.util.List;
import java.util.UUID;

import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import com.spacechase0.minecraft.componentequipment.ComponentEquipment;
import com.spacechase0.minecraft.componentequipment.item.EquipmentItem;
import com.spacechase0.minecraft.componentequipment.tool.Modifier;
import com.spacechase0.minecraft.spacecore.util.TranslateUtils;

public class PortableJukeboxModifier extends Modifier
{
	public PortableJukeboxModifier()
	{
		super( "portableJukebox" );
	}
	
	@Override
	public String getName( ItemStack stack )
	{
		return TranslateUtils.translate( "componentequipment:modifier.portableJukebox.name" );
	}
	
	@Override
	public String getFormat( ItemStack stack )
	{
		return EnumChatFormatting.WHITE.toString();
	}
	
	@Override
	public int getIconColor( int pass )
	{
		return ( pass == 0 ) ? 0x00886644 : 0x00332222;
	}
	
	@Override
	public boolean canApplyTo( EquipmentItem item )
	{
		return ( item == ComponentEquipment.items.helmet );
	}
	
	@Override
	public void itemTick( Entity entity, ItemStack stack )
	{
		if ( !entity.worldObj.isRemote || !( entity instanceof EntityPlayer ) )
		{
			return;
		}
		EntityPlayer player = ( EntityPlayer ) entity;
		
		if ( player.getCurrentArmor( 3 ) != stack )
		{
			return;
		}
		
		ComponentEquipment.proxy.tickJukebox( entity, stack );
	}
	
	@Override
	public void addDisassemblyResults( ItemStack stack, List< ItemStack > results )
	{
		ItemStack result = getDisassembledSelf( stack );
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
		return 0;
	}
}
