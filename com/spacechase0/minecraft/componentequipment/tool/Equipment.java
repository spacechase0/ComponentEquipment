package com.spacechase0.minecraft.componentequipment.tool;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import com.google.common.collect.Multimap;
import com.spacechase0.minecraft.componentequipment.ComponentEquipment;
import com.spacechase0.minecraft.componentequipment.item.EquipmentItem;

public abstract class Equipment
{
	public MaterialData getMaterialOf( ItemStack stack, String partType )
	{
		NBTTagCompound tag = stack.getTagCompound();
		if ( tag == null )
		{
			return Material.getData( "paper" );
		}
		
		String part = tag.getString( partType );
		if ( part.equals( "" ) )
		{
			return Material.getData( "paper" );
		}
		
		return Material.getData( part );
	}
	
	public String getCasing( ItemStack stack, String partType )
	{
		NBTTagCompound tag = stack.getTagCompound();
		if ( tag == null )
		{
			return null;
		}
		
		if ( tag.getString( partType + "Casing" ).equals( "" ) )
		{
			tag.removeTag( partType + "Casing" );
			return null;
		}
		
		return tag.getString( partType + "Casing" );
	}
	
	public void setCasing( ItemStack stack, String partType, String caseMat )
	{
		stack.getTagCompound().setString( partType + "Casing", caseMat );
	}
	
	// Bah, I want static abstract methods

	public EquipmentData getBaseData( String type )
	{
		return baseTypes.get( type );
	}
	
	public boolean isBroken( ItemStack stack )
	{
		return ( stack.getItemDamage() > stack.getMaxDamage() );
	}
	
	public void init( ItemStack stack )
	{
	}
	
	public abstract int getMaxDamage( EquipmentItem item, ItemStack stack );
	
	public int getXpCost( ItemStack tool )
	{
		float sum = 0;
		for ( String modName : getModifiers( tool ) )
		{
			Modifier mod = Modifier.getModifier( modName );
			
			int level = getModifierLevel( tool, modName );
			float repCost = mod.getRepairModifier( tool );
			
			if ( repCost >= 1.f ) continue;
			sum += repCost * level * 0.85;
		}
		
		return ( int )( 1 + ( sum * 1.85 ) );
	}
	
	public float getRepairMultiplier( ItemStack tool )
	{
		String[] mods = getModifiers( tool ).toArray( new String[] {} );
		float repairMult = 1.f;
		for ( int im = 0; im < mods.length; ++im )
		{
			Modifier mod = Modifier.getModifier( mods[ im ] );
			if ( mod != null )
			{
				repairMult *= mod.getRepairModifier( tool );
			}
		}
		
		return repairMult;
	}

	public int getModifiersRemaining( ItemStack stack )
	{
		NBTTagCompound tag = stack.getTagCompound();
		
		int count = ComponentEquipment.instance.config.get( "general", "defaultModifierCount", 3 ).getInt( 3 );
	
		EquipmentItem item = ( EquipmentItem ) stack.getItem();
		EquipmentData data = baseTypes.get( item.type );
		
		if ( item.type.equals( "paxel" ) )
		{
			return Integer.MIN_VALUE;
		}
		
		for ( int i = 0; i < data.getParts().length; ++i )
		{
			String matStr = tag.getString( data.getParts()[ i ] );
			MaterialData mat = Material.getData( matStr );
			if ( mat != null )
			{
				count += mat.getExtraModifiers();
			}
		}
	
		NBTTagList list = tag.getTagList( "Modifiers", 10 );
		for ( int i = 0; i < list.tagCount(); ++i )
		{
			NBTTagCompound modTag = ( NBTTagCompound ) list.getCompoundTagAt( i );
			Modifier mod = Modifier.getModifier( modTag.getString( "Type" ) );
			if ( mod != null )
			{
				count -= mod.getModifierCost();
			}
		}
		
		return count;
	}

	public List< String > getModifiers( ItemStack stack )
	{
		NBTTagCompound tag = stack.getTagCompound();
		if ( !tag.hasKey( "Modifiers" ) )
		{
			return new ArrayList< String >();
		}
		NBTTagList list = tag.getTagList( "Modifiers", 10 );
		
		List< String > outList = new ArrayList< String >();
		for ( int i = 0; i < list.tagCount(); ++i )
		{
			NBTTagCompound modTag = ( NBTTagCompound ) list.getCompoundTagAt( i );
			outList.add( modTag.getString( "Type" ) );
		}
		
		return outList;
	}

	public int getModifierLevel( ItemStack stack, String type )
	{
		NBTTagCompound tag = stack.getTagCompound();
		if ( !tag.hasKey( "Modifiers" ) )
		{
			return 0;
		}
		NBTTagList list = tag.getTagList( "Modifiers", 10 );
		
		for ( int i = 0; i < list.tagCount(); ++i )
		{
			NBTTagCompound modTag = ( NBTTagCompound ) list.getCompoundTagAt( i );
			if ( modTag.getString( "Type" ).equals( type ) )
			{
				return modTag.getInteger( "Level" );
			}
		}
		
		return 0;
	}

	public void setModifierLevel( ItemStack stack, String type, int num )
	{
		NBTTagCompound tag = stack.getTagCompound();
		if ( !tag.hasKey( "Modifiers" ) )
		{
			tag.setTag( "Modifiers", new NBTTagList() );
		}
		NBTTagList list = tag.getTagList( "Modifiers", 10 );
		
		boolean found = false;
		for ( int i = 0; i < list.tagCount(); ++i )
		{
			NBTTagCompound modTag = ( NBTTagCompound ) list.getCompoundTagAt( i );
			if ( modTag.getString( "Type" ).equals( type ) && !found )
			{
				modTag.setInteger( "Level", num );
				found = true;
				break;
			}
		}
		
		if ( !found )
		{
			NBTTagCompound modTag = new NBTTagCompound();
			modTag.setString( "Type", type );
			modTag.setInteger( "Level", num );
			
			list.appendTag( modTag );
		}
		
		initModifiers( stack );
	}
	
	public void swapModifiers( ItemStack stack, int swapA, int swapB )
	{
		NBTTagCompound tag = stack.getTagCompound();
		if ( !tag.hasKey( "Modifiers" ) )
		{
			tag.setTag( "Modifiers", new NBTTagList() );
		}
		NBTTagList list = tag.getTagList( "Modifiers", 10 );
		
		if ( swapA >= list.tagCount() || swapB >= list.tagCount() )
		{
			return;
		}

		NBTBase tagA = list.getCompoundTagAt( swapA );
		NBTBase tagB = list.getCompoundTagAt( swapB );
		setTagAt( list, swapA, tagB );
		setTagAt( list, swapB, tagA );
	}
	
	public void initModifiers( ItemStack stack )
	{
		NBTTagCompound tag = stack.getTagCompound();
		NBTTagList list = tag.getTagList( "Modifiers", 10 );
		
		// Otherwise you get all of the enchantments/attributes, like Eff. I, Eff. 2, Eff. 3, etc. on the same pick
		tag.removeTag( "ench" );
		//tag.removeTag( "AttributeModifiers" );
	
		for ( int i = 0; i < list.tagCount(); ++i )
		{
			NBTTagCompound modTag = ( NBTTagCompound ) list.getCompoundTagAt( i );
			Modifier mod = Modifier.getModifier( modTag.getString( "Type" ) );
			if ( mod != null )
			{
				mod.onAdded( stack );
			}
		}
		
		init( stack );
	}
	
	public void addAttributeModifiers( ItemStack stack, Multimap modifiers )
	{
		String[] mods = getModifiers( stack ).toArray( new String[] {} );
		for ( int im = 0; im < mods.length; ++im )
		{
			String mod = mods[ im ];
			Modifier.getModifier( mod ).addAttributeModifiers( stack, modifiers );
		}
	}

	public void onItemTick( Entity entity, ItemStack stack )
	{
		EquipmentItem item = ( EquipmentItem ) stack.getItem();
		EquipmentData data = baseTypes.get( item.type );
		for ( int ip = 0; ip < data.getParts().length; ++ip )
		{
			String part = data.getParts()[ ip ];
			MaterialData mat = getMaterialOf( stack, part );
			if ( mat != null )
			{
				mat.itemTick( entity, stack );
			}
		}
		
		String[] mods = getModifiers( stack ).toArray( new String[] {} );
		for ( int im = 0; im < mods.length; ++im )
		{
			String mod = mods[ im ];
			Modifier.getModifier( mod ).itemTick( entity, stack );
		}
	}

	public void onEntityTick( EntityItem entity )
	{
		ItemStack stack = entity.getEntityItem();
		
		EquipmentItem item = ( EquipmentItem ) stack.getItem();
		EquipmentData data = baseTypes.get( item.type );
		for ( int ip = 0; ip < data.getParts().length; ++ip )
		{
			String part = data.getParts()[ ip ];
			MaterialData mat = getMaterialOf( stack, part );
			if ( mat != null )
			{
				mat.entityTick( entity );
			}
		}
		
		String[] mods = getModifiers( stack ).toArray( new String[] {} );
		for ( int im = 0; im < mods.length; ++im )
		{
			String mod = mods[ im ];
			Modifier.getModifier( mod ).entityTick( entity );
		}
	}

	public void onBlockDestroyed( EntityLivingBase breaker, ItemStack stack, int x, int y, int z )
	{
		if ( isBroken( stack ) )
		{
			return;
		}
		
		EquipmentItem item = ( EquipmentItem ) stack.getItem();
		EquipmentData data = baseTypes.get( item.type );
		for ( int ip = 0; ip < data.getParts().length; ++ip )
		{
			String part = data.getParts()[ ip ];
			MaterialData mat = getMaterialOf( stack, part );
			if ( mat != null )
			{
				mat.breakBlock( breaker, stack, x, y, z );
			}
		}
		
		String[] mods = getModifiers( stack ).toArray( new String[] {} );
		for ( int im = 0; im < mods.length; ++im )
		{
			String mod = mods[ im ];
			Modifier.getModifier( mod ).breakBlock( breaker, stack, x, y, z );
		}
	}

	public void onHit( EntityLivingBase living2, EntityLivingBase living1, ItemStack stack )
	{
		if ( isBroken( stack ) )
		{
			return;
		}
		
		EquipmentItem item = ( EquipmentItem ) stack.getItem();
		EquipmentData data = baseTypes.get( item.type );
		for ( int ip = 0; ip < data.getParts().length; ++ip )
		{
			String part = data.getParts()[ ip ];
			MaterialData mat = getMaterialOf( stack, part );
			if ( mat != null )
			{
				mat.hitEntity( living2, living1, stack );
			}
		}
		
		String[] mods = getModifiers( stack ).toArray( new String[] {} );
		for ( int im = 0; im < mods.length; ++im )
		{
			String mod = mods[ im ];
			Modifier.getModifier( mod ).hitEntity( living2, living1, stack );
		}
	}
	
	private static void setTagAt( NBTTagList list, int index, NBTBase tag )
	{
		try
		{
			Class c = NBTTagList.class;
			Field field = c.getDeclaredFields()[ 0 ];
			field.setAccessible( true );
			
			List l = ( List ) field.get( list );
			l.set( index, tag );
		}
		catch ( Exception exception )
		{
			exception.printStackTrace();
		}
	}
	
	protected Map< String, EquipmentData > baseTypes = new HashMap< String, EquipmentData >();
}