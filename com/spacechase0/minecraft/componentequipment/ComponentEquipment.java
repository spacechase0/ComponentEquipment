package com.spacechase0.minecraft.componentequipment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.creativetab.CreativeTabs;
import static net.minecraft.init.Blocks.*;
import static net.minecraft.init.Items.*;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import com.spacechase0.minecraft.componentequipment.block.Blocks;
import com.spacechase0.minecraft.componentequipment.creativetab.PartsCreativeTab;
import com.spacechase0.minecraft.componentequipment.creativetab.ToolsCreativeTab;
import com.spacechase0.minecraft.componentequipment.entity.ArrowEntity;
import com.spacechase0.minecraft.componentequipment.item.IngotItem;
import com.spacechase0.minecraft.componentequipment.item.Items;
import com.spacechase0.minecraft.componentequipment.network.ConnectionHandler;
import com.spacechase0.minecraft.componentequipment.network.PacketCodec;
import com.spacechase0.minecraft.componentequipment.recipe.ArmorRecipes;
import com.spacechase0.minecraft.componentequipment.recipe.ArrowRecipes;
import com.spacechase0.minecraft.componentequipment.recipe.EquipmentRepairRecipes;
import com.spacechase0.minecraft.componentequipment.recipe.CaseToolRecipes;
import com.spacechase0.minecraft.componentequipment.recipe.ModifierRecipes;
import com.spacechase0.minecraft.componentequipment.recipe.PartCasingRecipes;
import com.spacechase0.minecraft.componentequipment.recipe.PaxelRecipes;
import com.spacechase0.minecraft.componentequipment.recipe.ToolRecipes;
import com.spacechase0.minecraft.componentequipment.recipe.ToolRepairRecipes;
import com.spacechase0.minecraft.componentequipment.tileentity.ComponentStationTileEntity;
import com.spacechase0.minecraft.componentequipment.tileentity.ModificationStandTileEntity;
import com.spacechase0.minecraft.componentequipment.tileentity.MysteriousOrbTileEntity;
import com.spacechase0.minecraft.componentequipment.tileentity.PersistiumCrystalTileEntity;
import com.spacechase0.minecraft.componentequipment.tileentity.PersistiumInfuserTileEntity;
import com.spacechase0.minecraft.componentequipment.tool.AllowHarvestHandler;
import com.spacechase0.minecraft.componentequipment.tool.ArmorMaterialIndex;
import com.spacechase0.minecraft.componentequipment.tool.ArmorProtectionApplier;
import com.spacechase0.minecraft.componentequipment.tool.Material;
import com.spacechase0.minecraft.componentequipment.tool.Modifier;
import com.spacechase0.minecraft.componentequipment.tool.Part;
import com.spacechase0.minecraft.componentequipment.tool.ModifierRecipes.Recipe;
import com.spacechase0.minecraft.componentequipment.tool.modifier.KnockbackEventHandler;
import com.spacechase0.minecraft.componentequipment.tool.modifier.PersistanceEventHandler;
import com.spacechase0.minecraft.componentequipment.tool.modifier.WalkSlopeTickHandler;
import com.spacechase0.minecraft.componentequipment.tool.modifier.WaterWalkTickHandler;
import com.spacechase0.minecraft.componentequipment.worldgen.PersistiumOreGenerator;
import com.spacechase0.minecraft.spacecore.BaseMod;
import com.spacechase0.minecraft.spacecore.StarterItemEventHandler;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;

/* 1.7 testing:
jukebox playing

to test:
	paxel?
 */

/*
Change knockback resistance to a custom thing that reduces it by % instead of chance to ignore like vanilla
(Probably requires coremod)
Maybe allow 3/4 levels, going up to 75%/80% instead
Or even 100%, hmm

Render right click place item thing above selected tool
^, pull from other stacks first

functional(?):
	Map + compass + clock = GPS?
		Add ability to disable
	Maybe electric jetpack modifier as well?
	RF jetpack thing support
	Mekanism jetpack support
	Item magnet?
	Spider legs
	Higher jumping
	Lamp for light following you around.
	Double jump?
	Auto-smelt, TConstruct
	ender storage support

silly:
	note-block walking
		Fairly easy maybe? Hook into footstep sounds
	fireworks - various effects, depending on tool, 2 levels
		Fireworks on death -> take damage
		Fireworks on kill -> attack
		Fireworks on mine block? 50% chance -> 100%
	Dying armor! Casing filled some of the need
 */

// Fix crystal animations depending on framerate
// Fix still-broken mining levels (see AllowHarvestHandler) -- might need coremod, and Forge PR for the future.
// Fix portable jukebox not working.

// 0.17.3 - Another crash fix, this time for the component station. Attempted to fix EU tooltip thing when electric modifier is not added.
// 0.17.2 - Model loading crash fix.
// 0.17.1 - Fixed startup crash.
// 0.17 - Updated for Minecraft 1.7. Changed armor system to be less non-sensical. Reworked modifier recipe system, allows more flexibility and convenience. Changed/added some modifier recipes. Hopefully fixed modifier stand consuming items when it shouldn't. Fixed persistium ore generation.
// 0.16 - Added (temporary?) recipe for water walking modifier. Slightly lessened speed cost of backpack modifier. Updated for SpaceCore 0.6.0.
// 0.15.1 - Fixed exploit involving fully repairing a tool through disassembly. Fixed inventory persistance not working. Hopefully fixed issues with some Ore Dictionary things (like wood) not working in the component station. Hopefully fixed rendering oddity with looking slightly away from the persistium infuser.
// 0.15 - Added water walking modifier. Fixed chest backpack not showing level. Fixed missing texture spam for invisible modifiers modifier. Fixed creative tabs for SpaceCore 0.5.
// 0.14 - Added two Forestry modifiers (possibly untested). Added backpack upgrades if Iron Chests is installed. Made walk speed and application type also work on leggings. Fixed crash when using documentation book.
// 0.13 - Added paxels. Made pickaxes+paxels able to go in Thaumcraft Arcane Bore. Made persistium generation configurable. Fixed crash bug in documentation book when clicking arrows without selecting a category. Fixed life steal recipe. Fixed bug where enchantments may not apply when using modifier essences. Fixed persistium block dropping stainless iron. Fixed weirdness in bow base graphics. Cleaned up ore dictionary abuse.
// 0.12.8 - Fixed bug with ender weapons taking drops longer than it should. (Forgot to change EntityLiving to EntityLivingBase in one class but not the parent, and the @Override was missing. :P)
// 0.12.7 - Fixed infinite torch exploit. Fixed bonemeal arrow recipe. Needs SpaceCore 0.3.3 for some recipes (like arrows) to show correctly (+ not crash) in the guide book.
// 0.12.6 - Made selected arrow not change if a bow is not selected. Made modification stand preserve item frames. Raised XP cost slightly (?). Fixed modification stand not syncing after activation. Fixed duplicate enchantment tooltip bug.
// 0.12.5 - Fixed non-OP armor crashing.
// 0.12.4 - Fixed armor being extremely OP. Reduced XP cost for modifiers. Made armor easier to repair. Fixed weapon damage being removed on applying modifiers through modification stand.
// 0.12.3 - Fixed crash bug when quiver is not full in bottom 3 slots. Tweaked thaumium armor values.
// 0.12.2 - Made backpacks reduce speed less. Fixed loss of attributes on crafting with modifier essence. Removed some debug output.
// 0.12.1 - Hopefully fixed crash for "missing" class definition.
// 0.12 - Added modifier for Thermal Expansion (flux energy). Added stonebound like TiC to some materials, EXCEPT it only applies when TiC is installed. Added part casing (graphical, see guide book). Added invisible modifiers modifier (see guide book). Added config options for if certain modifiers are craftable through the modification stand (untested). Changed (read: nerfed) how repairing works, might need tweaking. Added small client-side rendering to portable jukebox. Fixed non-axes breaking leaves quickly, and nerfed the speed of it. Fixed bug allowing any modifier on anything, therefore crashing the game because it had no icon. Fixed crash on thaumium page. Fixed modifier essence tooltip bug. Slight code cleanup, requires SpaceCore 0.3.2.
// 0.11.1 - Made Thaumcraft goggles modifier show more information for blocks other than nodes (Requires Thaumcraft 4.0.5). Fixed crash with modifier essences in chests. Fixed crash with recent Forge builds.
// 0.11 - Added portable jukebox modifier. Made modifiers cost XP to apply. Made modifier essences rarely appear as loot in chests. Fixed more compatibility with Larger Inventory/Hotbar.
// 0.10 - Added basic Thermal Expansion compatibility (invar material). Added disassembling and modifier essences (read guide). Fixed quivers being unable to change arrows. Fixed thaumium crashing in the guide book. Fixed more localization. Fixed compatibility with Larger Inventory/Hotbar.
// 0.9.3 - Fixed quiver crash. Removed dummy Thaumcraft research item. Fixed format errors in documentation.
// 0.9.2 - Maybe fixed some crashing errors without TC or IC2?
// 0.9.1 - Fixed Thaumcraft cheatiness.
// 0.9 - Added Thaumcraft thaumostatic harness modifier. Made the Thaumcraft modifiers require research and infusion crafting. Nerfed Electric (+cheaper recipe) and Haste modifiers. Changed modification stand to always open the stand, with or without a selected item. To open the reorder modifiers screen, press M (default). Fixed more localization. Fixed most graphical glitches with Persistium Infuser. Fixed graphical glitch for modification stand in item form.
// 0.8 - Added basic IC2 compatibility (electric modifier). Added chest backpack. REALLY added armor repairing. Added graphics for backpacks (check third person). Made backpacks slow you down. Made it where you can't open backpacks when armor is broken. Fixed items keeping attributes and enchants after breaking (repairing brings them back). Fixed ability to mine above a tool's harvest level. Requires SpaceCore 0.2.9.
// 0.7.1 - Fixed crashing bug with making some material components.
// 0.7 - Basic Thaumcraft (4.0.4a+) compatibility (thaumium material, goggles modifier). Added two other modifiers as well. Fix some crashing bugs that may or may not have existed. Requires SpaceCore 0.2.8.
// 0.6 - Added some TConstruct materials. Axes break leaves fast. Right-click with tool places block above the tool (similar to TConstruct).
// 0.5.5 - Possibly fixed crashing modification stand rendering.
// 0.5.4 - Fixed swords (and tools in general) not doing any extra damage. Fixed ore dictionary compatibility.
// 0.5.3 - Slightly improved documentation of tools and modifiers. 
// 0.5.2 - Compatibility with TConstruct for reordering modifiers.
// 0.5.1 - Fix modifier localization bug.
// 0.5 - DOCUMENTATION! Added some modifiers, textures, armor repairing, and gold arrows. Added much more OreDictionary compatibility. Changed some modifier recipes to include items. Fixed bug where tools could not harvest things requiring a tool. Fixed crashing issue with rendering quivers when holding a bow. Fixed some localization.

@Mod( modid = "SC0_ComponentEquipment", useMetadata = true, dependencies = "after:Thaumcraft;after:IC2;after:ThermalExpansion;after:Forestry;required-after:SC0_SpaceCore" )
public class ComponentEquipment extends BaseMod
{
	public ComponentEquipment()
	{
		super( "componentequipment" );
	}
	
	@Instance( "SC0_ComponentEquipment" )
	public static ComponentEquipment instance;

	@SidedProxy( clientSide = "com.spacechase0.minecraft.componentequipment.client.ClientProxy",
			     serverSide = "com.spacechase0.minecraft.componentequipment.CommonProxy" )
	public static CommonProxy proxy;
	
	@EventHandler
	public void preInit( FMLPreInitializationEvent event )
	{
		registerCreativeTabs();
		
		super.preInit( event );
		
		Map< String, String > addonClasses = new HashMap< String, String >();
		addonClasses.put( "TConstruct", "com.spacechase0.minecraft.componentequipment.addon.tconstruct.Compatibility" );
		addonClasses.put( "IronChest", "com.spacechase0.minecraft.componentequipment.addon.ironchests.Compatibility" );
		addonClasses.put( "Forestry", "com.spacechase0.minecraft.componentequipment.addon.forestry.Compatibility" );
		addonClasses.put( "Thaumcraft", "com.spacechase0.minecraft.componentequipment.addon.thaumcraft.Compatibility" );
		addonClasses.put( "ThermalExpansion", "com.spacechase0.minecraft.componentequipment.addon.thermalexpansion.Compatibility" );
		addonClasses.put( "IC2", "com.spacechase0.minecraft.componentequipment.addon.ic2.Compatibility" );
		
		for ( Map.Entry< String, String > path : addonClasses.entrySet() )
		{
			if ( Loader.isModLoaded( path.getKey() ) && isCompatEnabled( path.getKey() ) )
			{
				try
				{
					Class c = Class.forName( path.getValue() );
					addons.add( ( CEAddon ) c.getConstructor().newInstance() );
				}
				catch ( Exception exception )
				{
					CELog.severe( "Exception while initializing compatibility with " + path.getKey() + ": " );
					exception.printStackTrace();
				}
			}
		}
	}
	
	@EventHandler
	public void init( FMLInitializationEvent event )
	{
		super.init( event );
		
		registerNetwork();
		registerEventHandlers();
		registerWorldGen();
		registerRecipes();
		registerEntities();
		registerTileEntities();
		registerGui();

		if ( config.get( "general", "startWithGuide", true ).getBoolean( true ) )
		{
			StarterItemEventHandler.addStarterItem( "CE_GuideBook", new ItemStack( items.docBook ) );
		}
		
		// To make sure it is in the config file
		config.get( "general", "defaultModifierCount", 3 ).getInt( 3 );
		config.get( "general", "allowDisassembly", true ).getBoolean( true );
		
		proxy.init();
	}
	
	@EventHandler
	public void postInit( FMLPostInitializationEvent event )
	{
		super.postInit( event );
		
		ArmorMaterialIndex.initialize();
		com.spacechase0.minecraft.componentequipment.tool.ModifierRecipes.addDefaultRecipes();
		
		List< Recipe > toRemove = new ArrayList< Recipe >();
		for ( Recipe recipe : com.spacechase0.minecraft.componentequipment.tool.ModifierRecipes.recipes )
		{
			if ( !config.get( "canCraftModifier", recipe.mod, true ).getBoolean( true ) )
			{
				toRemove.add( recipe );
			}
		}
		for ( Recipe recipe : toRemove )
		{
			com.spacechase0.minecraft.componentequipment.tool.ModifierRecipes.recipes.remove( recipe );
		}
		
		config.save();
	}
	
	public static boolean isCompatEnabled( String mod )
	{
		return instance.config.get( "compatibility", mod, true ).getBoolean( true );
	}
	
	private void registerNetwork()
	{
		network = new PacketCodec();
		FMLCommonHandler.instance().bus().register( connectionHandler = new ConnectionHandler() );
	}
	
	private void registerEventHandlers()
	{
		MinecraftForge.EVENT_BUS.register( persistanceHandler = new PersistanceEventHandler() );
		MinecraftForge.EVENT_BUS.register( knockbackHandler = new KnockbackEventHandler() );
		MinecraftForge.EVENT_BUS.register( armorProtectionApplier = new ArmorProtectionApplier() );
		MinecraftForge.EVENT_BUS.register( allowHarvest = new AllowHarvestHandler() );
		FMLCommonHandler.instance().bus().register( walkSlope = new WalkSlopeTickHandler() );
		FMLCommonHandler.instance().bus().register( waterWalk = new WaterWalkTickHandler() );
	}
	
	private void registerCreativeTabs()
	{
		partsTab = new PartsCreativeTab();
		toolsTab = new ToolsCreativeTab();
	}
	
	
	private void registerWorldGen()
	{
		if ( config.get( "world", "genPersistium", true ).getBoolean( true ) )
		{
			GameRegistry.registerWorldGenerator( persistiumOreGen = new PersistiumOreGenerator(), 10 );
		}
		
		if ( config.get( "world", "modifierEssenceLoot", true ).getBoolean( true ) )
		{
			ItemStack modStack = new ItemStack( items.modifierEssence );
			WeightedRandomChestContent wrcc = new WeightedRandomChestContent( modStack, 1, 1, 1 );
	
			ChestGenHooks.addItem( ChestGenHooks.MINESHAFT_CORRIDOR, wrcc );
			ChestGenHooks.addItem( ChestGenHooks.VILLAGE_BLACKSMITH, wrcc );
			ChestGenHooks.addItem( ChestGenHooks.PYRAMID_DESERT_CHEST, wrcc );
			ChestGenHooks.addItem( ChestGenHooks.PYRAMID_JUNGLE_CHEST, wrcc );
			ChestGenHooks.addItem( ChestGenHooks.STRONGHOLD_CORRIDOR, wrcc );
			ChestGenHooks.addItem( ChestGenHooks.STRONGHOLD_LIBRARY, wrcc );
			ChestGenHooks.addItem( ChestGenHooks.STRONGHOLD_CROSSING, wrcc );
			ChestGenHooks.addItem( ChestGenHooks.DUNGEON_CHEST, wrcc );
		}
	}
	
	private void registerRecipes()
	{
		GameRegistry.addShapedRecipe( new ItemStack( blocks.ingot, 21, IngotItem.STAINLESS_IRON ),
				                         new Object[]
				                         {
										    "***",
										    "*#*",
										    "***",
											'#', iron_block,
											'*', gold_nugget,
				                         } );
		
		GameRegistry.addShapedRecipe( new ItemStack( blocks.ingot, 1, IngotItem.STAINLESS_IRON ),
				                      new Object[]
				                      {
										"II",
										"II",
										'I', new ItemStack( blocks.ingot, 1, IngotItem.STAINLESS_IRON ),
				                      } );
		
		GameRegistry.addShapelessRecipe( new ItemStack( items.ingot, 4, IngotItem.STAINLESS_IRON ),
				                         new Object[]
				                         {
										    new ItemStack( blocks.ingot, 1, IngotItem.STAINLESS_IRON ),
				                         } );
		/*
		GameRegistry.addShapedRecipe( new ItemStack( idleStainer ),
				                      new Object[]
				                      {
										"III",
										"I#I",
										"I|I",
										'I', new ItemStack( ingotItem, 1, IngotItem.STAINLESS_IRON ),
										'#', Block.furnaceIdle,
										'|', Item.glassBottle,
				                      } );*/
		
		GameRegistry.addShapedRecipe( new ItemStack( blocks.ingot, 1, IngotItem.PERSISTIUM ),
				                      new Object[]
				                      {
										"III",
										"III",
										"III",
										'I', new ItemStack( items.ingot, 1, IngotItem.PERSISTIUM ),
				                      } );
		
		GameRegistry.addShapedRecipe( new ItemStack( items.ingot, 1, IngotItem.PERSISTIUM ),
				                      new Object[]
				                      {
										"***",
										"***",
										"***",
										'*', new ItemStack( items.nugget, 1, IngotItem.PERSISTIUM ),
				                      } );
		
		GameRegistry.addShapelessRecipe( new ItemStack( items.ingot, 9, IngotItem.PERSISTIUM ),
				                         new Object[]
				                         {
										    new ItemStack( blocks.ingot, 1, IngotItem.PERSISTIUM ),
				                         } );
		
		GameRegistry.addShapelessRecipe( new ItemStack( items.nugget, 9, IngotItem.PERSISTIUM ),
				                         new Object[]
				                         {
										    new ItemStack( items.ingot, 1, IngotItem.PERSISTIUM ),
				                         } );
		
		GameRegistry.addShapedRecipe( new ItemStack( blocks.persistiumCrystal ),
				                      new Object[]
				                      {
										" * ",
										"*#*",
										" * ",
										'#', glowstone,
										'*', new ItemStack( items.nugget, 1, IngotItem.PERSISTIUM ),
				                      } );
		
		GameRegistry.addShapedRecipe( new ItemStack( blocks.diamondCactus, 8 ),
				                      new Object[]
				                      {
										"###",
										"#*#",
										"###",
										'*', diamond,
										'#', cactus,
				                      } );
		
		GameRegistry.addRecipe( new ShapedOreRecipe( new ItemStack( blocks.componentStation ),
				                                     new Object[]
				                                     {
		                                             	"O#",
		                                             	"#O",
		                                             	'#', "plankWood",
		                                             	'O', "logWood",
		                                             } ) );
		
		GameRegistry.addShapedRecipe( new ItemStack( blocks.modificationStand ),
				                      new Object[]
				                      {
										"_",
										"#",
										'_', new ItemStack( stone_slab, 3 ),
										'#', cobblestone_wall,
				                      } );
		
		GameRegistry.addShapedRecipe( new ItemStack( blocks.mysteriousOrb, 1 ),
				                      new Object[]
				                      {
										"#*#",
										"gdg",
										"#*#",
										'#', iron_block,
										'g', gold_block,
										'd', diamond_block,
										'*', ender_eye,
				                      } );
		
		GameRegistry.addShapedRecipe( new ItemStack( items.quiver ),
				                      new Object[]
				                      {
										" % ",
										"%#%",
										"%%%",
										'#', chest,
										'%', leather,
				                      } );
		
		GameRegistry.addShapedRecipe( new ItemStack( blocks.bones ),
				                      new Object[]
				                      {
										"///",
										"///",
										"///",
										'/', bone,
				                      } );
		
		GameRegistry.addShapedRecipe( new ItemStack( bone, 9 ),
				                      new Object[]
				                      {
										"#",
										'#', ComponentEquipment.blocks.bones
				                      } );
		
		GameRegistry.addShapelessRecipe( new ItemStack( items.docBook ),
				                         book,
				                         blocks.componentStation );

		GameRegistry.addRecipe( new ToolRecipes( items.axe ) );
		GameRegistry.addRecipe( new ToolRecipes( items.hoe ) );
		GameRegistry.addRecipe( new ToolRecipes( items.pickaxe ) );
		GameRegistry.addRecipe( new ToolRecipes( items.sword ) );
		GameRegistry.addRecipe( new ToolRecipes( items.shovel ) );
		GameRegistry.addRecipe( new ToolRecipes( items.bow ) );
		GameRegistry.addRecipe( new ArmorRecipes( items.helmet ) );
		GameRegistry.addRecipe( new ArmorRecipes( items.chestplate ) );
		GameRegistry.addRecipe( new ArmorRecipes( items.leggings ) );
		GameRegistry.addRecipe( new ArmorRecipes( items.boots ) );
		GameRegistry.addRecipe( new EquipmentRepairRecipes( items.axe, new String[] { "head" } ) );
		GameRegistry.addRecipe( new EquipmentRepairRecipes( items.hoe, new String[] { "head" } ) );
		GameRegistry.addRecipe( new EquipmentRepairRecipes( items.pickaxe, new String[] { "head" } ) );
		GameRegistry.addRecipe( new EquipmentRepairRecipes( items.sword, new String[] { "head" } ) );
		GameRegistry.addRecipe( new EquipmentRepairRecipes( items.shovel, new String[] { "head" } ) );
		GameRegistry.addRecipe( new EquipmentRepairRecipes( items.bow, new String[] { "head" } ) );
		GameRegistry.addRecipe( new EquipmentRepairRecipes( items.helmet, new String[] { "helmLeft", "helmRight" } ) );
		GameRegistry.addRecipe( new EquipmentRepairRecipes( items.chestplate, new String[] { "armLeft", "armRight" } ) );
		GameRegistry.addRecipe( new EquipmentRepairRecipes( items.leggings, new String[] { "belt" } ) );
		GameRegistry.addRecipe( new EquipmentRepairRecipes( items.boots, new String[] { "bootLeft", "bootRight" } ) );
		GameRegistry.addRecipe( new ModifierRecipes() );
		GameRegistry.addRecipe( new PartCasingRecipes() );
		GameRegistry.addRecipe( new CaseToolRecipes() );
		if ( config.get( "general", "canCraftPaxel", true ).getBoolean( true ) )
		{
			GameRegistry.addRecipe( new PaxelRecipes() );
			//GameRegistry.addRecipe( new PaxelCasingRecipes() );
		}
		ArrowRecipes.addRecipes();
	}
	
	private void registerEntities()
	{
		arrowEntityId = EntityRegistry.findGlobalUniqueEntityId();
		EntityRegistry.registerGlobalEntityID( ArrowEntity.class, "decoArrow", arrowEntityId );
		EntityRegistry.registerModEntity( ArrowEntity.class, "decoArrow", arrowEntityId, this, 64, 20, false );
		//EntityRegistry.instance().lookupModSpawn( ArrowEntity.class, false ).setCustomSpawning( null,  true );
		LanguageRegistry.instance().addStringLocalization( "entity.deco_arrow.name", "Arrow" );
	}
	
	private void registerTileEntities()
	{
		GameRegistry.registerTileEntity( PersistiumCrystalTileEntity.class, "PersistiumCrystal" );
		GameRegistry.registerTileEntity( PersistiumInfuserTileEntity.class, "PersistiumInfuser" );
		GameRegistry.registerTileEntity( ComponentStationTileEntity.class, "ComponentStation" );
		GameRegistry.registerTileEntity( ModificationStandTileEntity.class, "ModificationStand" );
		GameRegistry.registerTileEntity( MysteriousOrbTileEntity.class, "MysteriousOrb" );
	}
	
	private void registerGui()
	{
		NetworkRegistry.INSTANCE.registerGuiHandler( this, new GuiHandler() );
	}
	
	public static PacketCodec network;
	private ConnectionHandler connectionHandler;
	
	private PersistanceEventHandler persistanceHandler;
	private KnockbackEventHandler knockbackHandler;
	private ArmorProtectionApplier armorProtectionApplier;
	private AllowHarvestHandler allowHarvest;
	private WalkSlopeTickHandler walkSlope;
	private WaterWalkTickHandler waterWalk;
	
	private List< CEAddon > addons = new ArrayList< CEAddon >();

	public static CreativeTabs partsTab;
	public static CreativeTabs toolsTab;

	public static Configuration config;
	public static Blocks blocks;
	public static Items items;
	
	public static PersistiumOreGenerator persistiumOreGen;
	
	public static int arrowEntityId;
	
	public static final int STAINER_GUI_ID = 0;
	public static final int PERSISTIUM_INFUSER_GUI_ID = 1;
	public static final int COMPONENT_STATION_GUI_ID = 2;
	public static final int MODIFICATION_STAND_GUI_ID = 3;
	public static final int QUIVER_GUI_ID = 4;
	public static final int REORDER_MODIFIERS_GUI_ID = 5;
	public static final int DOCUMENTATION_GUI_ID = 6;
	public static final int BACKPACK_GUI_ID = 7;
	public static final int JUKEBOX_GUI_ID = 8;
}
