package com.spacechase0.minecraft.componentequipment.tool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;

import com.google.common.collect.Multimap;
import com.spacechase0.minecraft.componentequipment.ComponentEquipment;
import com.spacechase0.minecraft.componentequipment.item.EquipmentItem;
import com.spacechase0.minecraft.componentequipment.item.ModifierItem;
import com.spacechase0.minecraft.componentequipment.tool.modifier.BackpackModifier;
import com.spacechase0.minecraft.componentequipment.tool.modifier.CheaperRepairModifier;
import com.spacechase0.minecraft.componentequipment.tool.modifier.EnchantmentModifier;
import com.spacechase0.minecraft.componentequipment.tool.modifier.ExtraModifier;
import com.spacechase0.minecraft.componentequipment.tool.modifier.InvisibleModifiersModifier;
import com.spacechase0.minecraft.componentequipment.tool.modifier.ItemAttributeModifier;
import com.spacechase0.minecraft.componentequipment.tool.modifier.LifeStealModifier;
import com.spacechase0.minecraft.componentequipment.tool.modifier.MultiEnchantmentModifier;
import com.spacechase0.minecraft.componentequipment.tool.modifier.PersistanceModifier;
import com.spacechase0.minecraft.componentequipment.tool.modifier.PortableJukeboxModifier;
import com.spacechase0.minecraft.componentequipment.tool.modifier.SelfRepairModifier;
import com.spacechase0.minecraft.componentequipment.tool.modifier.WalkSlopeModifier;
import com.spacechase0.minecraft.componentequipment.tool.modifier.WalkSpeedModifier;
import com.spacechase0.minecraft.componentequipment.tool.modifier.WaterWalkModifier;

public class Modifier
{
	public Modifier( String theType )
	{
		type = theType;
	}
	
	public String getName( ItemStack stack ) { return "???"; }
	public String getFormat( ItemStack stack ) { return ""; }
	public void addInformation( ItemStack stack, List list ) {}
	public int getIconColor( int pass ) { return 0; }
	public int getIconColor( int level, int pass )
	{
		int col = getIconColor( pass );
		if ( pass == 1 )
		{
			float factor = level / ( float ) getMaxLevel();
			
			int a = ( col >>  0 ) & 0xFF;
			int r = ( col >>  8 ) & 0xFF;
			int g = ( col >> 16 ) & 0xFF;
			int b = ( col >> 24 ) & 0xFF;

			a *= factor;
			r *= factor;
			g *= factor;
			b *= factor;
			
			col  = 0;
			col |= ( a <<  0 );
			col |= ( r <<  8 );
			col |= ( g << 16 );
			col |= ( b << 24 );
		}
		
		return col;
	}
	
	public boolean canApplyTo( EquipmentItem item ) { return true; }
	public boolean canAdd( ItemStack stack )
	{
		EquipmentItem item = ( EquipmentItem ) stack.getItem();
		return item.equipment.getModifierLevel( stack, type ) < getMaxLevel( /*stack*/ ) && canApplyTo( item );
	}
	public int getMaxLevel( /*ItemStack stack*/ ) { return 1; }
	
	public void onAdded( ItemStack stack ) {}
	public void itemTick( Entity entity, ItemStack stack ) {}
	public void entityTick( EntityItem entity ) {}
	public void breakBlock( EntityLivingBase breaker, ItemStack stack, int x, int y, int z ) {}
	public void hitEntity( EntityLivingBase living2, EntityLivingBase living1, ItemStack stack ) {}
	public void addAttributeModifiers( ItemStack stack, Multimap modifiers ) {}
	public boolean handleArmorRightClick( EntityPlayer clicker, ItemStack stack ) { return false; }
	public void addDisassemblyResults( ItemStack stack, List< ItemStack > results )
	{
		results.add( getDisassembledSelf( stack ) );
	}
	
	//public int getExtraDurability( ItemStack stack ) { return 0; }
	//public float getExtraMiningSpeed( ItemStack stack ) { return 0.f; }
	//public int getExtraDamage( ItemStack stack ) { return 0; }
	public float getRepairModifier( ItemStack stack ) { return 0.85f; }
	public int getModifierCost() { return 1; }
	
	public ItemStack getEssenceItem( ItemStack stack )
	{
		EquipmentItem equipment = ( EquipmentItem ) stack.getItem();
		
		NBTTagCompound tag = new NBTTagCompound();
		tag.setString( "Modifier", type );
		tag.setInteger( "Level", equipment.equipment.getModifierLevel( stack, type ) );
		
		ItemStack ret = new ItemStack( ComponentEquipment.items.modifierEssence );
		ret.setTagCompound( tag );
		
		return ret;
	}
	
	protected ItemStack getDisassembledSelf( ItemStack stack )
	{
		EquipmentItem item = ( EquipmentItem ) stack.getItem();
		int level = item.equipment.getModifierLevel( stack,  type );
		
		return ModifierItem.getStackFor( type, level );
	}
	
	public final String type;
	
	// MORE LATER
	
	public static void addType( Modifier mod )
	{
		mods.put( mod.type, mod );
	}
	
	public static String[] getTypes()
	{
		return mods.keySet().toArray( new String[] {} );
	}
	
	public static Modifier getModifier( String type )
	{
		if ( !mods.containsKey( type ) )
		{
			throw new IllegalArgumentException( "No such modifier '" + type + "'!" );
		}
		return mods.get( type );
	}
	
	private static Map< String, Modifier > mods = new HashMap< String, Modifier >();
	
	static
	{
		addType( new EnchantmentModifier( "unbreaking", EnumChatFormatting.GREEN, 0x0000FF00, 0x00004488, Enchantment.unbreaking ) );
		addType( new EnchantmentModifier( "silkTouch", EnumChatFormatting.WHITE, 0x00FFFFFF, 0x00888888, Enchantment.silkTouch ) );
		addType( new MultiEnchantmentModifier( "luck", EnumChatFormatting.BLUE, 0x000000FF, 0x0000AAAA, Enchantment.looting, Enchantment.fortune ) );
		
		addType( new EnchantmentModifier( "efficiency", EnumChatFormatting.RED, 0x00FF0000, 0x00884444, Enchantment.efficiency ) );

		addType( new MultiEnchantmentModifier( "damage", EnumChatFormatting.RED, 0x00FFFFFFF, 0x00888888, Enchantment.sharpness, Enchantment.power ) );
		addType( new EnchantmentModifier( "smite", EnumChatFormatting.DARK_RED, 0x00CC2222, 0x00888888, Enchantment.smite ) );
		addType( new EnchantmentModifier( "baneOfArthropods", EnumChatFormatting.DARK_PURPLE, 0x00AA00AA, 0x00888888, Enchantment.baneOfArthropods ) );
		addType( new MultiEnchantmentModifier( "knockback", EnumChatFormatting.DARK_GREEN, 0x0000CC00, 0x00228844, Enchantment.knockback, Enchantment.punch ) );
		addType( new MultiEnchantmentModifier( "fire", EnumChatFormatting.GOLD, 0x00CC6600, 0x00884400, Enchantment.fireAspect, Enchantment.flame ) );
		
		addType( new EnchantmentModifier( "infinity", EnumChatFormatting.DARK_BLUE, 0x003333AA, 0x00000044, Enchantment.infinity ) );

		addType( new EnchantmentModifier( "protection", EnumChatFormatting.RED, 0x00FF0000, 0x00FFFF00, Enchantment.protection ) );
		addType( new EnchantmentModifier( "blastProtection", EnumChatFormatting.DARK_GREEN, 0x0000FF00, 0x00FFFF00, Enchantment.blastProtection ) );
		addType( new EnchantmentModifier( "fireProtection", EnumChatFormatting.GOLD, 0x00CC8800, 0x00FFFF00, Enchantment.fireProtection ) );
		addType( new EnchantmentModifier( "projectileProtection", EnumChatFormatting.GOLD, 0x00FF00FF, 0x00FFFF00, Enchantment.projectileProtection ) );
		addType( new EnchantmentModifier( "featherFalling", EnumChatFormatting.GRAY, 0x00999900, 0x00FFFFFFFF, Enchantment.featherFalling ) );
		addType( new EnchantmentModifier( "thorns", EnumChatFormatting.DARK_GRAY, 0x00009900, 0x00444444,Enchantment.thorns ) );
		addType( new EnchantmentModifier( "respiration", EnumChatFormatting.AQUA, 0x0000AAFF, 0x000000FF, Enchantment.respiration ) );
		addType( new EnchantmentModifier( "aquaAffinity", EnumChatFormatting.BLUE, 0x000000FF, 0x0000FFFF, Enchantment.aquaAffinity ) );

		addType( new ExtraModifier( "extra1", 0x008CE7E3, 0x0000ADA0 ) );
		addType( new ExtraModifier( "extra2", 0x00D2D200, 0x0088A4A4 ) );
		
		addType( new CheaperRepairModifier() );
		addType( new PersistanceModifier() );
		addType( new LifeStealModifier() );
		addType( new SelfRepairModifier() );

		addType( new ItemAttributeModifier( "maxHealth", EnumEnchantmentType.armor_head, EnumChatFormatting.DARK_RED, 0x00FF0000, 0x00FFFF00, 10, SharedMonsterAttributes.maxHealth, 0, 2, "CE3F55D3-655C-4F38-A597-9C13A33DB5CF" ) );
		addType( new ItemAttributeModifier( "knockbackResist", EnumEnchantmentType.armor_torso, EnumChatFormatting.DARK_GREEN, 0x0000FF00, 0x00880088, 2, SharedMonsterAttributes.knockbackResistance, 0, 0.33, "CB3F55D3-655C-6F38-A597-9C13A33DB5CF" ) );
		addType( new WalkSpeedModifier() );
		addType( new WalkSlopeModifier() );
		addType( new BackpackModifier( true ) );
		addType( new BackpackModifier( false ) );
		addType( new WaterWalkModifier() );
		
		addType( new PortableJukeboxModifier() );
		addType( new InvisibleModifiersModifier() );
	}
}
