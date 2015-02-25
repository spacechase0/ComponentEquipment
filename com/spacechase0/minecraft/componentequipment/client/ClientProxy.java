package com.spacechase0.minecraft.componentequipment.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundManager;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemRecord;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;

import org.lwjgl.input.Keyboard;

import paulscode.sound.SoundSystem;

import com.spacechase0.minecraft.componentequipment.CommonProxy;
import com.spacechase0.minecraft.componentequipment.ComponentEquipment;
import com.spacechase0.minecraft.componentequipment.client.model.ArmorModel;
import com.spacechase0.minecraft.componentequipment.client.model.ModificationStandModel;
import com.spacechase0.minecraft.componentequipment.client.model.MysteriousOrbModel;
import com.spacechase0.minecraft.componentequipment.client.render.entity.ArrowRenderer;
import com.spacechase0.minecraft.componentequipment.client.render.entity.PlayerArmorRenderer;
import com.spacechase0.minecraft.componentequipment.client.render.item.ModificationStandItemRenderer;
import com.spacechase0.minecraft.componentequipment.client.render.item.MysteriousOrbItemRenderer;
import com.spacechase0.minecraft.componentequipment.client.render.item.PersistiumCrystalItemRenderer;
import com.spacechase0.minecraft.componentequipment.client.render.tileentity.ModificationStandTileEntityRenderer;
import com.spacechase0.minecraft.componentequipment.client.render.tileentity.MysteriousOrbTileEntityRenderer;
import com.spacechase0.minecraft.componentequipment.client.render.tileentity.PersistiumCrystalTileEntityRenderer;
import com.spacechase0.minecraft.componentequipment.client.render.tileentity.PersistiumInfuserTileEntityRenderer;
import com.spacechase0.minecraft.componentequipment.client.render.tool.BackpackRenderer;
import com.spacechase0.minecraft.componentequipment.client.render.tool.JukeboxRenderer;
import com.spacechase0.minecraft.componentequipment.client.render.tool.WaterWalkRenderer;
import com.spacechase0.minecraft.componentequipment.client.tick.ArrowSlotRenderer;
import com.spacechase0.minecraft.componentequipment.client.tick.ArrowSlotSelector;
import com.spacechase0.minecraft.componentequipment.client.tick.BackpackOpener;
import com.spacechase0.minecraft.componentequipment.client.tick.JukeboxOpener;
import com.spacechase0.minecraft.componentequipment.client.tick.ReorderOpener;
import com.spacechase0.minecraft.componentequipment.entity.ArrowEntity;
import com.spacechase0.minecraft.componentequipment.inventory.JukeboxInventory;
import com.spacechase0.minecraft.componentequipment.tileentity.ModificationStandTileEntity;
import com.spacechase0.minecraft.componentequipment.tileentity.MysteriousOrbTileEntity;
import com.spacechase0.minecraft.componentequipment.tileentity.PersistiumCrystalTileEntity;
import com.spacechase0.minecraft.componentequipment.tileentity.PersistiumInfuserTileEntity;
import com.spacechase0.minecraft.componentequipment.tool.modifier.EquipmentEnchantmentTooltipModifier;
import com.spacechase0.minecraft.spacecore.client.render.item.GenericItemRenderer;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

public class ClientProxy extends CommonProxy
{
	@Override
	public void init()
	{
		ClientRegistry.bindTileEntitySpecialRenderer( PersistiumCrystalTileEntity.class, new PersistiumCrystalTileEntityRenderer() );
		ClientRegistry.bindTileEntitySpecialRenderer( PersistiumInfuserTileEntity.class, new PersistiumInfuserTileEntityRenderer() );
		ClientRegistry.bindTileEntitySpecialRenderer( ModificationStandTileEntity.class, new ModificationStandTileEntityRenderer() );
		ClientRegistry.bindTileEntitySpecialRenderer( MysteriousOrbTileEntity.class, new MysteriousOrbTileEntityRenderer() );
		
		MinecraftForgeClient.registerItemRenderer( Item.getItemFromBlock( ComponentEquipment.blocks.persistiumCrystal ), new PersistiumCrystalItemRenderer() );
		MinecraftForgeClient.registerItemRenderer( ComponentEquipment.items.part, new GenericItemRenderer() );
		MinecraftForgeClient.registerItemRenderer( ComponentEquipment.items.axe, new GenericItemRenderer() );
		MinecraftForgeClient.registerItemRenderer( ComponentEquipment.items.hoe, new GenericItemRenderer() );
		MinecraftForgeClient.registerItemRenderer( ComponentEquipment.items.pickaxe, new GenericItemRenderer() );
		MinecraftForgeClient.registerItemRenderer( ComponentEquipment.items.sword, new GenericItemRenderer() );
		MinecraftForgeClient.registerItemRenderer( ComponentEquipment.items.shovel, new GenericItemRenderer() );
		MinecraftForgeClient.registerItemRenderer( ComponentEquipment.items.bow, new GenericItemRenderer() );
		MinecraftForgeClient.registerItemRenderer( ComponentEquipment.items.helmet, new GenericItemRenderer() );
		MinecraftForgeClient.registerItemRenderer( ComponentEquipment.items.chestplate, new GenericItemRenderer() );
		MinecraftForgeClient.registerItemRenderer( ComponentEquipment.items.leggings, new GenericItemRenderer() );
		MinecraftForgeClient.registerItemRenderer( ComponentEquipment.items.boots, new GenericItemRenderer() );
		MinecraftForgeClient.registerItemRenderer( ComponentEquipment.items.partCasing, new GenericItemRenderer() );
		MinecraftForgeClient.registerItemRenderer( ComponentEquipment.items.paxel, new GenericItemRenderer() );
		MinecraftForgeClient.registerItemRenderer( Item.getItemFromBlock( ComponentEquipment.blocks.modificationStand ), new ModificationStandItemRenderer( new ModificationStandModel() ) );
		MinecraftForgeClient.registerItemRenderer( Item.getItemFromBlock( ComponentEquipment.blocks.mysteriousOrb ), new MysteriousOrbItemRenderer( new MysteriousOrbModel() ) );
		
		RenderingRegistry.registerEntityRenderingHandler( ArrowEntity.class, new ArrowRenderer() );

		KeyBinding[] quiverKeys = new KeyBinding[]
					              {
					            		new KeyBinding( "key.ce_quiverUp", Keyboard.KEY_R, "key.categories.gameplay" ),
					            		new KeyBinding( "key.ce_quiverDown", Keyboard.KEY_F, "key.categories.gameplay" ),
					              };
		
		FMLCommonHandler.instance().bus().register( ( arrowSlotSelector = new ArrowSlotSelector( quiverKeys ) ) );
		FMLCommonHandler.instance().bus().register( ( arrowSlotRenderer = new ArrowSlotRenderer() ) );
		
		MinecraftForge.EVENT_BUS.register( playerArmorRenderer = new PlayerArmorRenderer() );
		ArmorModel.registerModifierRenderer( "enderBackpack", new BackpackRenderer() );
		ArmorModel.registerModifierRenderer( "chestBackpack", new BackpackRenderer() );
		ArmorModel.registerModifierRenderer( "portableJukebox", new JukeboxRenderer() );
		ArmorModel.registerModifierRenderer( "waterWalk", new WaterWalkRenderer() );
		
		MinecraftForge.EVENT_BUS.register( equipmentEnchantmentToolModifier = new EquipmentEnchantmentTooltipModifier() );

		FMLCommonHandler.instance().bus().register( ( backpackOpener = new BackpackOpener( new KeyBinding( "key.ce_openBackpack", Keyboard.KEY_B, "key.categories.gameplay" ) ) ) );
		FMLCommonHandler.instance().bus().register( ( reorderOpener = new ReorderOpener( new KeyBinding( "key.ce_reorderMods", Keyboard.KEY_M, "key.categories.gameplay" ) ) ) );
		FMLCommonHandler.instance().bus().register( ( jukeboxOpener = new JukeboxOpener( new KeyBinding( "key.ce_portableJukebox", Keyboard.KEY_J, "key.categories.gameplay" ) ) ) );
	}

	@Override
	public void tickJukebox( Entity entity, ItemStack stack )
	{
		// TODO: Fix me!
		/*
		Minecraft mc = Minecraft.getMinecraft();
		World world = mc.theWorld;
		SoundManager sm = mc.sndManager;
		SoundSystem ss = sm.sndSystem;
		
		int mode = 0;//PortableJukeboxModifier.getModeOf( stack );
		JukeboxInventory inv = new JukeboxInventory( stack );
		
		List< ItemStack > records = new ArrayList< ItemStack >();
		for ( int i = 0; i < inv.getSizeInventory(); ++i )
		{
			ItemStack slot = inv.getStackInSlot( i );
			if ( slot == null )
			{
				continue;
			}
			
			ItemRecord record = ( ItemRecord ) slot.getItem();
			if ( record.recordName != prevPlaying )
			{
				records.add( slot );
			}
		}
		
		if ( records.size() == 0 )
		{
			if ( prevPlaying != null )
			{
				prevPlaying = null;
				//ss.stop( "streaming" );
			}
			else
			{
				// ???
				playingRecord = null;
			}
			return;
		}
		ItemStack recordStack = records.get( ( new Random() ).nextInt( records.size() ) );
		ItemRecord record = ( ItemRecord ) recordStack.getItem();
		
		if ( ss.playing( "BgMusic" ) ) ss.stop( "BgMusic" );
		if ( !ss.playing( "streaming" ) )
		{
			world.playRecord( record.recordName, 0, 0, 0 );
			ss.setAttenuation( "streaming", 0 );
			prevPlaying = record.recordName;
			playingRecord = recordStack.copy();
		}
		*/
	}
	
	public ArmorModel getArmorModel( EntityLivingBase living, int slot )
	{
		Map< Integer, ArmorModel > slots = armorModels.get( living );
		if ( slots == null )
		{
			return null;
		}
		
		return slots.get( slot );
	}
	
	public void addArmorModel( EntityLivingBase living, int slot, ArmorModel model )
	{
		Map< Integer, ArmorModel > slots = armorModels.get( living );
		if ( slots == null )
		{
			armorModels.put( living, new HashMap< Integer, ArmorModel >() );
			addArmorModel( living, slot, model );
			return;
		}
		
		slots.put( slot, model );
	}
	
	public ArrowSlotSelector arrowSlotSelector;
	public ArrowSlotRenderer arrowSlotRenderer;
	public PlayerArmorRenderer playerArmorRenderer;
	public EquipmentEnchantmentTooltipModifier equipmentEnchantmentToolModifier;
	public BackpackOpener backpackOpener;
	public ReorderOpener reorderOpener;
	public JukeboxOpener jukeboxOpener;
	private Map< EntityLivingBase, Map< Integer, ArmorModel > > armorModels = new HashMap< EntityLivingBase, Map< Integer, ArmorModel > >();
	
	private String prevPlaying = null;
	public ItemStack playingRecord = null;
}
