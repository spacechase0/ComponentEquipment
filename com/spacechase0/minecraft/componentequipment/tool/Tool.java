package com.spacechase0.minecraft.componentequipment.tool;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import com.google.common.collect.Multimap;
import com.spacechase0.minecraft.componentequipment.ComponentEquipment;
import com.spacechase0.minecraft.componentequipment.item.EquipmentItem;
import com.spacechase0.minecraft.componentequipment.item.ToolItem;

public class Tool extends Equipment
{
	public void addType( String type, int theType, int damage, String[] parts )
	{
		ToolData data = new ToolData( theType, damage, parts );
		types.put( type, data );
		baseTypes.put( type, data );
	}
	
	public String[] getTypes()
	{
		return types.keySet().toArray( new String[] {} );
	}
	
	public ToolData getData( String type )
	{
		return types.get( type );
	}
	
	@Override
	public void init( ItemStack stack )
	{
		NBTTagCompound tag = stack.getTagCompound();
		if ( tag == null )
		{
			tag = new NBTTagCompound();
		}
		
		stack.setTagCompound( tag );
	}
	
	@Override
	public int getMaxDamage( EquipmentItem item, ItemStack stack )
	{
		int base = getMaterialOf( stack, "head" ).getBaseDurability();
		float mult = getMaterialOf( stack, "handle" ).getToolMultiplier();
		int extra = 0;//getMaterialOf( stack, "binding" ).getBaseDurability();
		
		return ( int )( base * mult ) + extra;
	}
	
	@Override
	public void addAttributeModifiers( ItemStack stack, Multimap modifiers )
	{
		super.addAttributeModifiers( stack, modifiers );
		
		int damage = getData( ( ( ToolItem ) stack.getItem() ).type ).getAttackDamage();
		damage += getMaterialOf( stack, "head" ).getAttackDamage();
		modifiers.put( SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier( weaponDamage, "Weapon modifier", damage, 0 ) );
		
		/*
		NBTTagList attrs = tag.getTagList( "AttributeModifiers", 10 );
		{
			for ( int i = 0; i < attrs.tagCount(); ++i )
			{
				NBTTagCompound mod = ( NBTTagCompound ) attrs.getCompoundTagAt( i );
				if ( SharedMonsterAttributes.readAttributeModifierFromNBT( mod ).getID().equals( weaponDamage ) )
				{
					attrs.removeTag( i );
					break;
				}
			}
			
			int damage = getData( ( ( ToolItem ) stack.getItem() ).type ).getAttackDamage();
			damage += Material.getData( tag.getString( "head" ) ).getAttackDamage();
			
			NBTTagCompound data = Equipment.getAttributeData( new AttributeModifier( weaponDamage, "Weapon modifier", damage, 0 ) );
			data.setString( "AttributeName", SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName() );
			attrs.appendTag( data );
		}
		tag.setTag( "AttributeModifiers", attrs );
		*/
	}
	
	public float getMiningSpeed( ToolItem item, ItemStack stack )
	{
		if ( isBroken( stack ) )
		{
			return 1.f;
		}
		
		float speed = getMaterialOf( stack, "head" ).getMiningSpeed();
		for ( String part : getData( item.type ).getParts() )
		{
			MaterialData mat = Material.getData( getMaterialOf( stack, part ).getType() );
			speed += mat.getMiningSpeedBonus( stack );
		}
		
        return speed;
	}
	
	public int getAttackDamage( ToolItem item, ItemStack stack )
	{
		if ( isBroken( stack ) )
		{
			return 1;
		}
		
		int damage = getData( item.type ).getAttackDamage();
		for ( String part : getData( item.type ).getParts() )
		{
			MaterialData mat = Material.getData( getMaterialOf( stack, part ).getType() );
			damage += mat.getAttackDamageBonus( stack );
		}
		
		return damage;
	}

	public int getBowChargeTime( ItemStack stack )
	{
		float base = getMiningSpeed( ComponentEquipment.items.bow, stack );
		float mult = 6.f / base;
		
		int result = ( int )( 18 * mult );
		
		return Math.max( result, 8 );
	}
	
	public boolean actsLike( ItemStack stack, String type )
	{
		if ( stack == null || stack.getItem() == null || !( stack.getItem() instanceof ToolItem ) )
		{
			return false;
		}
		
		return actsLike( ( ToolItem ) stack.getItem(), type );
	}
	
	public boolean actsLike( ToolItem tool, String type )
	{
		if ( tool.type.equals( type ) )
		{
			return true;
		}
		
		if ( tool.type.equals( "paxel" ) && ( type.equals( "pickaxe" ) || type.equals( "shovel" ) || type.equals( "axe" ) ) )
		{
			return true;
		}
		
		return false;
	}
	
	private static Map< String, ToolData > types = new HashMap< String, ToolData >();
	public static final Tool instance = new Tool();
	
	public static final int HARVEST = 1 << 0;
	public static final int WEAPON = 1 << 1;
	public static final int BOW = 1 << 2;

	private Tool() // Pretend this is static { ... } :P
	{
		addType( "axe", HARVEST, 3, new String[] { "handle", "head", "binding" } );
		addType( "hoe", 0, 0, new String[] { "handle", "head", "binding" } );
		addType( "pickaxe", HARVEST, 2, new String[] { "handle", "head", "binding" } );
		addType( "sword", WEAPON, 4, new String[] { "handle", "head", "binding" } );
		addType( "shovel", HARVEST, 1, new String[] { "handle", "head", "binding" } );
		addType( "bow", BOW, 0, new String[] { "head", "handle", "binding" } );
		
		addType( "paxel", HARVEST, 2, new String[] { "handle", "head", "binding" } );
	}
	
	private static final UUID weaponDamage = UUID.fromString( "CB3F55D3-645C-4F38-A497-9C13A33DB5CF" );
}
