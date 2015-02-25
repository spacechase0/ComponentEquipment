package com.spacechase0.minecraft.componentequipment.addon.thaumcraft;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.MinecraftForge;
import thaumcraft.api.ItemApi;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.InfusionRecipe;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.api.research.ResearchPage;

import com.spacechase0.minecraft.componentequipment.CEAddon;
import com.spacechase0.minecraft.componentequipment.CELog;
import com.spacechase0.minecraft.componentequipment.ComponentEquipment;
import com.spacechase0.minecraft.componentequipment.addon.thaumcraft.modifier.HoverHarnessModifier;
import com.spacechase0.minecraft.componentequipment.addon.thaumcraft.modifier.RevealingGogglesModifier;
import com.spacechase0.minecraft.componentequipment.addon.thaumcraft.recipe.ModifierInfusionRecipe;
import com.spacechase0.minecraft.componentequipment.tool.Material;
import com.spacechase0.minecraft.componentequipment.tool.Modifier;
import com.spacechase0.minecraft.componentequipment.tool.ModifierRecipes;
import com.spacechase0.minecraft.componentequipment.tool.material.ExtraModMaterial;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.relauncher.Side;

public class Compatibility extends CEAddon
{
	/*
	@SidedProxy( clientSide = "com.spacechase0.minecraft.componentequipment.addon.thaumcraft.client.ClientProxy",
	             serverSide = "com.spacechase0.minecraft.componentequipment.addon.thaumcraft.CommonProxy" )
	*/
	public static CommonProxy proxy;
	
	@Override
	public void init()
	{
		Material.addType( new ExtraModMaterial( "thaumium", ItemApi.getItem( "itemResource", 2 ), 250, 1.1f, 20, 7.f, 3, 2, 15, EnumChatFormatting.DARK_PURPLE ) );
		
		boreSlotChanger = new ArcaneBoreSlotChanger();
		FMLCommonHandler.instance().bus().register( boreSlotChanger );
	}
	
	@Override
	public void postInit() //throws Exception
	{
		// Crashes outside of MCP otherwise.
		try
		{
			String c = "com.spacechase0.minecraft.componentequipment.addon.thaumcraft.CommonProxy";
			if ( FMLCommonHandler.instance().getSide() == Side.CLIENT )
			{
				c = "com.spacechase0.minecraft.componentequipment.addon.thaumcraft.client.ClientProxy";
			}
			proxy = ( CommonProxy ) Class.forName( c ).getConstructor().newInstance();
		}
		catch ( Exception exception )
		{
			CELog.severe( "Problem making Thaumcraft addon proxy. Things might break now." );
			exception.printStackTrace();
		}
		
		Modifier.addType( new RevealingGogglesModifier() );
		Modifier.addType( new HoverHarnessModifier() );
		
		//ModifierRecipes.add( "seeAuraNodes", new ItemStack[][] { new ItemStack[] { ItemApi.getItem( "itemGoggles", 0 ) } } );
		//ModifierRecipes.add( "hoverHarness", new ItemStack[][] { new ItemStack[] { ItemApi.getItem( "itemHoverHarness", 0 ) } } );
		
		ItemStack baseHelmet = new ItemStack( ComponentEquipment.items.helmet );
		{
			NBTTagCompound tag = new NBTTagCompound();
			tag.setString( "helmLeft", "iron" );
			tag.setString( "helmRight", "thaumium" );
			baseHelmet.setTagCompound( tag );
		}
		ItemStack gogglesArmor = baseHelmet.copy();
		{
			NBTTagList mods = new NBTTagList();
			NBTTagCompound mod = new NBTTagCompound();
			mod.setString( "Type", "seeAuraNodes" );
			mod.setInteger( "Level", 1 );
			mods.appendTag( mod );
			gogglesArmor.getTagCompound().setTag( "Modifiers", mods );
		}
		NBTTagCompound onGoggles = new NBTTagCompound();
		onGoggles.setString( "PendingInfusedModifier", "seeAuraNodes" );
		
		ItemStack baseChestplate = new ItemStack( ComponentEquipment.items.chestplate );
		{
			NBTTagCompound tag = new NBTTagCompound();
			tag.setString( "armLeft", "iron" );
			tag.setString( "armRight", "thaumium" );
			tag.setString( "chestLeft", "iron" );
			tag.setString( "chestRight", "thaumium" );
			baseChestplate.setTagCompound( tag );
		}
		ItemStack hoverArmor = baseChestplate.copy();
		{
			NBTTagList mods = new NBTTagList();
			NBTTagCompound mod = new NBTTagCompound();
			mod.setString( "Type", "hoverHarness" );
			mod.setInteger( "Level", 1 );
			mods.appendTag( mod );
			hoverArmor.getTagCompound().setTag( "Modifiers", mods );
		}
		NBTTagCompound onHover = new NBTTagCompound();
		onHover.setString( "PendingInfusedModifier", "hoverHarness" );
		
		InfusionRecipe gogglesRecipe = new ModifierInfusionRecipe( "seeAuraNodes",
				                                                   onGoggles,
				                                                   7, 
				                                                   ( new AspectList() )
				                                                   	.add( Aspect.SENSES, 25 )
				                                                   	.add( Aspect.ARMOR, 30 )
				                                                   	.add( Aspect.AURA, 15 )
				                                                   	.add( Aspect.CRAFT, 20 ),
				                                                   baseHelmet,
				                                                   new ItemStack[]
				                                                   {
		                                                           	ItemApi.getItem( "itemGoggles", 0 ),
		                                                           	// TODO: ???
				                                                   } );
		InfusionRecipe hoverHarnessRecipe = new ModifierInfusionRecipe( "hoverHarness",
				                                                        onHover,
				                                                        13, 
				                                                        ( new AspectList() )
				                                                        	.add( Aspect.FLIGHT, 50 )
				                                                        	.add( Aspect.ARMOR, 60 )
				                                                        	.add( Aspect.ENERGY, 30 )
				                                                        	.add( Aspect.CRAFT, 40 ),
				                                                        baseChestplate,
				                                                        new ItemStack[]
				                                                        {
		                                                                	ItemApi.getItem( "itemHoverHarness", 0 ),
		                                                                	// TODO: ???
		                                                                } );
		
		ThaumcraftApi.getCraftingRecipes().add( gogglesRecipe );
		ThaumcraftApi.getCraftingRecipes().add( hoverHarnessRecipe );
		
		( new ResearchItem( "CE_MOD_SEEAURANODES",
				            "ARTIFICE",
				            ( new AspectList() )
				            	.add( Aspect.SENSES, 2 )
				            	.add( Aspect.ARMOR, 2 )
				            	.add( Aspect.AURA, 2 ),
				            2,
				            -5,
				            2,
				            gogglesArmor ) )
		.setPages( new ResearchPage[]
				   {
				   	new ResearchPage( "componentequipment:research.modSeeAuraNodes.1" ),
				   	new ResearchPage( gogglesRecipe ),
                   } )
        .setParents( new String[]
        		     {
        		     	"GOGGLES"
                     } )
        .setConcealed()
        .registerResearchItem();
		
		( new ResearchItem( "CE_MOD_HOVERHARNESS",
				            "ARTIFICE",
				            ( new AspectList() )
				            	.add( Aspect.FLIGHT, 2 )
				            	.add( Aspect.ARMOR, 2 )
				            	.add( Aspect.ENERGY, 2 ),
				            2,
				            4,
				            4,
				            hoverArmor ) )
		.setPages( new ResearchPage[]
				   {
				   	new ResearchPage( "componentequipment:research.modHoverHarness.1" ),
				   	new ResearchPage( hoverHarnessRecipe ),
                   } )
        .setParents( new String[]
        		     {
        		     	"HOVERHARNESS"
                     } )
        .setConcealed()
        .registerResearchItem();
		
		MinecraftForge.EVENT_BUS.register( hoverTicker = new HoverEventHandler() );
		
		proxy.init();
	}
	
	private static ArcaneBoreSlotChanger boreSlotChanger;
	private static HoverEventHandler hoverTicker;
}
