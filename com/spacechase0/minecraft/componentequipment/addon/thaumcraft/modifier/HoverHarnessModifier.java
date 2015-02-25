package com.spacechase0.minecraft.componentequipment.addon.thaumcraft.modifier;

import java.lang.reflect.Method;
import java.util.List;

import thaumcraft.api.aspects.Aspect;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import com.spacechase0.minecraft.componentequipment.ComponentEquipment;
import com.spacechase0.minecraft.componentequipment.item.EquipmentItem;
import com.spacechase0.minecraft.componentequipment.tool.Modifier;

public class HoverHarnessModifier extends Modifier
{
	public HoverHarnessModifier()
	{
		super( "hoverHarness" );
	}
	
	@Override
	public String getName( ItemStack stack )
	{
		EquipmentItem item = ( EquipmentItem ) stack.getItem();
		return StatCollector.translateToLocal( "componentequipment:modifier.hoverHarness.name" );
	}
	
	@Override
	public String getFormat( ItemStack stack )
	{
		return EnumChatFormatting.DARK_PURPLE.toString();
	}
	
	@Override
	public int getIconColor( int pass )
	{
		return ( pass == 0 ) ? 0x007E553D : 0x00FFED64;
	}
	
	@Override
	public boolean canApplyTo( EquipmentItem item )
	{
		return ( item == ComponentEquipment.items.chestplate );
	}
	
	@Override
	public boolean handleArmorRightClick( EntityPlayer player, ItemStack stack )
	{
		if ( player.worldObj.isRemote )
		{
			return false;
		}
		
		try
		{
			Object tc = Class.forName( "thaumcraft.common.Thaumcraft" ).getField( "instance" ).get( null );
			Object tcp = Class.forName( "thaumcraft.common.Thaumcraft" ).getField( "proxy" ).get( null );
			//System.out.println(tc);
	        
			// cheaty stuff
			/*
			{
				Class c = Class.forName( "thaumcraft.common.lib.research.ResearchManager" );
				Method m = c.getMethod( "completeResearch", EntityPlayer.class, String.class );
				m.invoke( tcp.getClass().getMethod( "getResearchManager" ).invoke( tcp ), player, "NITOR" );
				m.invoke( tcp.getClass().getMethod( "getResearchManager" ).invoke( tcp ), player, "ALUMENTUM" );
				m.invoke( tcp.getClass().getMethod( "getResearchManager" ).invoke( tcp ), player, "DISTILESSENTIA" );
				m.invoke( tcp.getClass().getMethod( "getResearchManager" ).invoke( tcp ), player, "INFUSION" );
				m.invoke( tcp.getClass().getMethod( "getResearchManager" ).invoke( tcp ), player, "ENCHFABRIC" );
				m.invoke( tcp.getClass().getMethod( "getResearchManager" ).invoke( tcp ), player, "BOOTSTRAVELLER" );
				m.invoke( tcp.getClass().getMethod( "getResearchManager" ).invoke( tcp ), player, "HOVERHARNESS" );
				m.invoke( tcp.getClass().getMethod( "getResearchManager" ).invoke( tcp ), player, "GOGGLES" );
				m.invoke( tcp.getClass().getMethod( "getResearchManager" ).invoke( tcp ), player, "CE_MOD_SEEAURANODES" );
				m.invoke( tcp.getClass().getMethod( "getResearchManager" ).invoke( tcp ), player, "CE_MOD_HOVERHARNESS" );
				m.invoke( tcp.getClass().getMethod( "getResearchManager" ).invoke( tcp ), player, "CE_MOD_HOVERHARNESS" );
			}
			{
				Class c = Class.forName( "thaumcraft.common.lib.research.PlayerKnowledge" );
				Method m = c.getMethod( "addDiscoveredAspect", String.class, Aspect.class );
				m.invoke( tcp.getClass().getField( "playerKnowledge" ).get( tcp ), player.username, Aspect.AURA );
				m.invoke( tcp.getClass().getField( "playerKnowledge" ).get( tcp ), player.username, Aspect.ARMOR );
				m.invoke( tcp.getClass().getField( "playerKnowledge" ).get( tcp ), player.username, Aspect.CRAFT );
				m.invoke( tcp.getClass().getField( "playerKnowledge" ).get( tcp ), player.username, Aspect.SENSES );
				m.invoke( tcp.getClass().getField( "playerKnowledge" ).get( tcp ), player.username, Aspect.FLIGHT );
				m.invoke( tcp.getClass().getField( "playerKnowledge" ).get( tcp ), player.username, Aspect.ENERGY );
			}
			*/
			
			player.openGui( tc, 17, player.worldObj, MathHelper.floor_double( player.posX ), MathHelper.floor_double( player.posY ), MathHelper.floor_double( player.posZ ) );
	        
	        return true;
		}
		catch ( Exception exception )
		{
			exception.printStackTrace();
		}
		
		return false;
	}
	
	@Override
	public void addDisassemblyResults( ItemStack stack, List< ItemStack > results )
	{
		ItemStack result = getDisassembledSelf( stack );
		{
			NBTTagCompound tag = new NBTTagCompound();
			tag.setTag( "jar", stack.getTagCompound().getTag( "jar" ) );
			result.getTagCompound().setTag( "Data", tag );
		}
		
		results.add( result );
	}
	
	@Override
	public int getModifierCost()
	{
		return 3;
	}
}
