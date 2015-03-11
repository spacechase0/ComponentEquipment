package com.spacechase0.minecraft.componentequipment.item;

import ic2.api.item.IElectricItemManager;
import ic2.api.item.ISpecialElectricItem;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import cofh.api.energy.IEnergyContainerItem;

import com.google.common.collect.Multimap;
import com.spacechase0.minecraft.componentequipment.ComponentEquipment;
import com.spacechase0.minecraft.componentequipment.tool.Equipment;
import com.spacechase0.minecraft.componentequipment.tool.Material;
import com.spacechase0.minecraft.componentequipment.tool.MaterialData;
import com.spacechase0.minecraft.componentequipment.tool.Modifier;
import com.spacechase0.minecraft.spacecore.util.TranslateUtils;

import cpw.mods.fml.common.Optional;

@Optional.InterfaceList( {
                         	@Optional.Interface( iface = "ic2.api.item.ISpecialElectricItem", modid = "IC2" ),
                         	@Optional.Interface( iface = "cofh.api.energy.IEnergyContainerItem", modid = "CoFHCore" )
                         } )
public abstract class EquipmentItem extends SimpleItem implements ISpecialElectricItem, IEnergyContainerItem
{
	public EquipmentItem( String theType, Equipment theToolBase )
	{
		super( theType );
		type = theType;
		equipment = theToolBase;
		
		setMaxStackSize( 1 );
	}
	
	@Override
	public void registerIcons( IIconRegister register )
	{
		partIcons = new HashMap< String, Map< String, IIcon > >();
		String[] mats = Material.getTypes();
		String[] parts = equipment.getBaseData( type ).getParts();
		for ( int ip = 0; ip < parts.length; ++ip )
		{
			Map< String, IIcon > icons = new HashMap< String, IIcon >();
			
			for ( int im = 0; im < mats.length; ++im )
			{
				String path = getIconDirectory() + "/" + type + "/parts/" + parts[ ip ] + "/" + mats[ im ];
				icons.put( mats[ im ], register.registerIcon( "componentequipment:" + path ) );
			}
			
			partIcons.put( parts[ ip ], icons );
		}
	
		modIcons = new HashMap< String, Map< Integer, IIcon > >();
		String[] mods = Modifier.getTypes();
		for ( int im = 0; im < mods.length; ++im )
		{
			Map< Integer, IIcon > icons = new HashMap< Integer, IIcon >();
			
			Modifier mod = Modifier.getModifier( mods[ im ] );
			if ( mods[ im ].equals( "invisibleModifiers" ) || !mod.canApplyTo( this ) )
			{
				continue;
			}
			
			for ( int il = 1; il <= mod.getMaxLevel(); ++il )
			{
				String path = getIconDirectory() + "/" + type + "/modifiers/" + mods[ im ] + "/" + il;
				icons.put( il, register.registerIcon( "componentequipment:" + path ) );
			}
			
			
			modIcons.put( mods[ im ], icons );
		}
		
		blankIcon = register.registerIcon( "componentequipment:blank" );
		badIcon = register.registerIcon( "componentequipment:badTool" );
	}

	@Override
	public void getSubItems( Item id, CreativeTabs tabs, List list )
	{
		for ( int i = 0; i < InventoryPlayer.getHotbarSize(); ++i )
		{
			Random rand = new Random( ( type + i ).hashCode() );
			rand = new Random( rand.nextInt() );
			
			NBTTagCompound tag = new NBTTagCompound();
	
			String[] mats = Material.getTypes();
			String[] parts = equipment.getBaseData( type ).getParts();
			for ( int ip = 0; ip < parts.length; ++ip )
			{
				tag.setString( parts[ ip ], mats[ rand.nextInt( mats.length ) ] );
			}
	
			ItemStack stack = new ItemStack( this );
			stack.setTagCompound( tag );
			equipment.init( stack );
			list.add( stack );
		}
	}

	@Override
	public IIcon getIcon( ItemStack stack, int pass )
	{
		NBTTagCompound tag = stack.getTagCompound();
		if ( tag == null )
		{
			return badIcon;
		}
		
		try
		{
			String[] parts = equipment.getBaseData( type ).getParts().clone();
			String[] mods = equipment.getModifiers( stack ).toArray( new String[] {} );
			
			if ( equipment.getModifierLevel( stack, "invisibleModifiers" ) >= 1 )
			{
				int index = -1;
				for ( int i = 0; i < mods.length; ++i )
				{
					if ( mods[ i ].equals( "invisibleModifiers" ) )
					{
						index = i;
					}
				}
				
				if ( index >= 0 )
				{
					mods = Arrays.copyOfRange( mods, index + 1, mods.length );
				}
			}
			
			if ( pass < parts.length )
			{
				String mat = tag.getString( parts[ pass ] );
				if ( equipment.getCasing( stack, parts[ pass ] ) != null )
				{
					mat = equipment.getCasing( stack, parts[ pass ] );
				}
				return partIcons.get( parts[ pass ] ).get( mat );
			}
			else if ( ( pass - parts.length ) < mods.length )
			{
				pass -= parts.length;
				
				int level = equipment.getModifierLevel( stack, mods[ pass ] );
				return modIcons.get( mods[ pass ] ).get( level );
			}
			else if ( ( pass - parts.length ) == mods.length && stack.getTagCompound().hasKey( "PendingInfusedModifier" ) )
			{
				return modIcons.get( stack.getTagCompound().getString( "PendingInfusedModifier" ) ).get( 1 );
			}
		}
		catch ( Exception exception )
		{
			exception.printStackTrace();
			return badIcon;
		}
		
		return blankIcon;
	}

	@Override
	public void addInformation( ItemStack stack, EntityPlayer player, List list, boolean par4 )
	{
		if ( stack == null || stack.getTagCompound() == null )
		{
			return;
		}
		
		NBTTagCompound tag = stack.getTagCompound();
		
		String[] parts = equipment.getBaseData( type ).getParts();
		for ( int i = 0; i < parts.length; ++i )
		{
			String part = parts[ i ];
			if ( part.equals( "head" ) )
			{
				part = type + "Head";
			}
			else if ( part.endsWith( "Left" ) )
			{
				part = part.substring( 0, part.indexOf( "Left" ) );
			}
			else if ( part.endsWith( "Right" ) )
			{
				part = part.substring( 0, part.indexOf( "Right" ) );
			}
			String matRaw = tag.getString( parts[ i ] );
			MaterialData matObj = Material.getData( matRaw );
			
			String title = TranslateUtils.translate( "componentequipment:part." + part );
			String mat = TranslateUtils.translate( "componentequipment:material." + matRaw );
			
			list.add( TranslateUtils.translate( "item.equipment.tooltip.part", title, matObj.getFormat() + mat ) );
			if ( equipment.getCasing( stack, parts[ i ] ) != null )
			{
				matRaw = equipment.getCasing( stack, parts[ i ] );
				matObj = Material.getData( matRaw );
				mat = TranslateUtils.translate( "componentequipment:material." + matRaw );
				
				list.add( "   " + TranslateUtils.translate( "item.equipment.tooltip.partCasing", matObj.getFormat() + mat ) );
			}
		}
		
		list.add( "" );
		
		list.add( TranslateUtils.translate( "item.equipment.tooltip.modifiers" ) );
		
		List< String > mods = equipment.getModifiers( stack );
		Iterator< String > it = mods.iterator();
		while ( it.hasNext() )
		{
			String modStr = it.next();
			Modifier mod = Modifier.getModifier( modStr );
			
			String name = ( mod != null ) ? mod.getName( stack ) : ( "\"" + modStr + "\"?" );
			list.add( "   - " + mod.getFormat( stack ) + name );
			mod.addInformation( stack, list );
		}
		
		list.add( "   " + TranslateUtils.translate( "item.equipment.tooltip.remaining", equipment.getModifiersRemaining( stack ) ) );
	}

	@Override
	public int getDamage( ItemStack stack )
	{
		NBTTagCompound tag = stack.getTagCompound();
		
		if ( tag == null || !tag.hasKey( "Damage" ) )
		{
			return 0;
		}
		
		return tag.getInteger( "Damage" );
	}

	@Override
	public int getMaxDamage()
	{
		// Blah, evil tool overlay thingy
		return 1;
	}

	@Override
	public int getMaxDamage( ItemStack stack )
	{
		return equipment.getMaxDamage( this, stack );
	}

	@Override
	public int getDisplayDamage( ItemStack stack )
	{
		NBTTagCompound tag = stack.getTagCompound();
		
		if ( !tag.hasKey( "Damage" ) )
		{
			return 0;
		}
		
		return tag.getInteger( "Damage" );
	}

	@Override
	public void setDamage( ItemStack stack, int damage )
	{
		if ( damage < 0 )
		{
			damage = 0;
		}
		
		NBTTagCompound tag = stack.getTagCompound();
		tag.setInteger( "Damage", damage );
		
		if ( equipment.isBroken( stack ) )
		{
			tag.removeTag( "ench" );
			//tag.removeTag( "AttributeModifiers" );
		}
	}

	//@Override
	public void damageItemStack( EntityLivingBase damager, ItemStack stack, int damage )
	{
		boolean wasBroken = equipment.isBroken( stack );
		
		int curr = getDamage( stack );
		int amt = curr + damage;
		setDamage( stack, amt );
		
		if ( equipment.isBroken( stack ) )
		{
			NBTTagCompound tag = stack.getTagCompound();
			if ( tag.hasKey( "ench" ) ) tag.removeTag( "ench" );
			//if ( tag.hasKey( "AttributeModifiers" ) ) tag.removeTag( "AttributeModifiers" );
			
	    	if ( damager != null )
	    	{
	    		damager.renderBrokenItemStack( stack );
	    	}
		}
		else if ( wasBroken && !equipment.isBroken( stack ) )
		{
			equipment.initModifiers( stack );
		}
	}

	@Override
	public boolean isDamaged( ItemStack stack )
	{
	    return ( getDamage( stack ) > 0 );
	}

	@Override
	public boolean onEntityItemUpdate( EntityItem entity )
	{
		equipment.onEntityTick( entity );
	    return false;
	}

	@Override
	public void onUpdate( ItemStack stack, World world, Entity entity, int par4, boolean par5 )
	{
		if ( stack.getTagCompound().hasKey( "PendingInfusedModifier" ) )
		{
			equipment.setModifierLevel( stack, stack.getTagCompound().getString( "PendingInfusedModifier" ), 1 );
			stack.getTagCompound().removeTag( "PendingInfusedModifier" );
		}
		
		equipment.onItemTick( entity, stack );
	}
    
	@Override
	public Multimap getAttributeModifiers( ItemStack stack )
	{
		if ( equipment.isBroken( stack ) ) return super.getAttributeModifiers( stack );
		
		Multimap map = super.getAttributeModifiers( stack );
		equipment.addAttributeModifiers( stack, map );
		return map;
	}

	@Override
	public boolean hasEffect( ItemStack stack )
	{
		return false;
	}

	@Override
	public boolean isFull3D()
	{
	    return true;
	}

	@Override
	public boolean requiresMultipleRenderPasses()
	{
	    return true;
	}

	@Override
	public int getRenderPasses( int metadata )
	{
		// 4 components max
		// 3 default modifiers
		// Up to 4 modifiers from materials
		// 2*2 from "extra modifier" modifiers
		return 4 + ComponentEquipment.instance.config.get( "general", "defaultModifierCount", 3 ).getInt( 3 ) + 4 + ( 2 * 2 );
	}
	
	protected abstract String getIconDirectory();

	public final String type;
	public final Equipment equipment;
	private Map< String, Map< String, IIcon > > partIcons;
	private Map< String, Map< Integer, IIcon > > modIcons;
	protected IIcon blankIcon;
	private IIcon badIcon;


	// IC2
	@Optional.Method( modid = "IC2" )
	@Override
	public boolean canProvideEnergy( ItemStack stack )
	{
		return equipment.getModifierLevel( stack, "electric" ) > 0;
	}

	@Optional.Method( modid = "IC2" )
	@Override
	public Item getChargedItem( ItemStack stack )
	{
		return this;
	}

	@Optional.Method( modid = "IC2" )
	@Override
	public Item getEmptyItem( ItemStack stack )
	{
		return this;
	}

	@Optional.Method( modid = "IC2" )
	@Override
	public double getMaxCharge( ItemStack stack )
	{
		int level = equipment.getModifierLevel( stack, "electric" );
		switch ( level )
		{
			case 1: return 10000;
			case 2: return 100000;
			case 3: return 1000000;
			case 4: return 10000000;
		}
		
		return 0;
	}

	@Optional.Method( modid = "IC2" )
	@Override
	public int getTier( ItemStack stack )
	{
		return equipment.getModifierLevel( stack, "electric" );
	}

	@Optional.Method( modid = "IC2" )
	@Override
	public double getTransferLimit( ItemStack stack )
	{
		int level = equipment.getModifierLevel( stack, "electric" );
		switch ( level )
		{
			case 1: return 100;
			case 2: return 1000;
			case 3: return 2500;
			case 4: return 10000;
		}
		
		return 0;
	}

	@Optional.Method( modid = "IC2" )
	@Override
	public IElectricItemManager getManager( ItemStack stack )
	{
		return com.spacechase0.minecraft.componentequipment.addon.ic2.Compatibility.getElectricItemManager();
	}
	
	// TE
	@Optional.Method( modid = "CoFHCore" )
	@Override
	public int receiveEnergy( ItemStack stack, int maxReceive, boolean simulate )
	{
		if ( stack.getTagCompound() == null )
		{
			return 0;
		}
		
		int stored = getEnergyStored( stack );
		int max = getMaxEnergyStored( stack );
		int rate = getMaxEnergyRate( stack );
		
		int toReceive = Math.min( max - stored, Math.min( rate, maxReceive ) );
		if ( !simulate && toReceive > 0 )
		{
			setEnergyStored( stack, stored + toReceive );
		}
		
		return toReceive;
	}

	@Optional.Method( modid = "CoFHCore" )
	@Override
	public int extractEnergy( ItemStack stack, int maxExtract, boolean simulate )
	{
		if ( stack.getTagCompound() == null )
		{
			return 0;
		}
		
		int stored = getEnergyStored( stack );
		int rate = getMaxEnergyRate( stack );
		
		int toSend = Math.min( stored, Math.min( rate, maxExtract ) );
		if ( !simulate && toSend > 0 )
		{
			setEnergyStored( stack, stored - toSend );
		}
		
		return toSend;
	}

	@Optional.Method( modid = "CoFHCore" )
	@Override
	public int getEnergyStored( ItemStack stack )
	{
		return stack.getTagCompound().getInteger( CHARGE_RF );
	}

	@Optional.Method( modid = "CoFHCore" )
	//@Override
	public void setEnergyStored( ItemStack stack, int amount )
	{
		stack.getTagCompound().setInteger( CHARGE_RF, amount );
	}

	@Optional.Method( modid = "CoFHCore" )
	@Override
	public int getMaxEnergyStored( ItemStack stack )
	{
		int level = equipment.getModifierLevel( stack, "fluxEnergy" );
		switch ( level )
		{
			case 1: return 16000;
			case 2: return 80000;
			case 3: return 400000;
			case 4: return 2000000;
			case 5: return 10000000;
		}
		
		return 0;
	}

	// This is mine, just for easy of use
	@Optional.Method( modid = "CoFHCore" )
	//@Override
	public int getMaxEnergyRate( ItemStack stack )
	{
		if ( ignoreEnergyRate ) return Integer.MAX_VALUE;
		
		int level = equipment.getModifierLevel( stack, "fluxEnergy" );
		switch ( level )
		{
			case 1: return 20;
			case 2: return 20;
			case 3: return 100;
			case 4: return 500;
			case 5: return 2500;
		}
		
		return 0;
	}
	
	public static boolean ignoreEnergyRate = false;

	public static final String CHARGE_EU = "IC2_Energy";
	public static final String CHARGE_RF = "TE_Energy";
}