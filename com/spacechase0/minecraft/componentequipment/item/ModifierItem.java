package com.spacechase0.minecraft.componentequipment.item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;

import com.spacechase0.minecraft.componentequipment.ComponentEquipment;
import com.spacechase0.minecraft.componentequipment.tool.Arrow;
import com.spacechase0.minecraft.componentequipment.tool.Modifier;
import com.spacechase0.minecraft.spacecore.util.TranslateUtils;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ModifierItem extends SimpleItem
{
	public ModifierItem()
	{
		super( "modifier" );
		
		setCreativeTab( ComponentEquipment.partsTab );
		setMaxStackSize( 1 );
	}

	@Override
    @SideOnly(Side.CLIENT)
    public void registerIcons( IIconRegister register )
    {
		itemIcon = register.registerIcon( "componentequipment:modifierEssence_back" );
		colorIcon = register.registerIcon( "componentequipment:modifierEssence_front" );

		blankIcon = register.registerIcon( "componentequipment:blank" );
    }
	
	@Override
    public void getSubItems( Item id, CreativeTabs tabs, List list )
    {
		String[] mods = Modifier.getTypes();
		for ( String mod : mods )
		{
			for ( int il = 1; il <= Modifier.getModifier( mod ).getMaxLevel(); ++il )
			{
				list.add( getStackFor( mod, il ) );
			}
		}
    }
	
	@Override
    public IIcon getIcon( ItemStack stack, int pass )
    {
		if ( stack.getTagCompound() != null )
		{
			NBTTagCompound tag = stack.getTagCompound();
			if ( tag.getString( "Modifier" ).equals( "invisibleModifiers" ) )
			{
				if ( pass == 0 ) return blankIcon;
			}
		}
		
		if ( pass == 0 )
		{
			return itemIcon;
		}
		else
		{
			return colorIcon;
		}
    }
	
	@Override
    public int getColorFromItemStack( ItemStack stack, int pass )
    {
		NBTTagCompound tag = stack.getTagCompound();
		Modifier mod = Modifier.getModifier( tag.getString( "Modifier" ) );
		if ( mod == null )
		{
			return 0x00000000;
		}
		
        return mod.getIconColor( tag.getInteger( "Level" ), pass );
    }
	
	@Override
    public boolean requiresMultipleRenderPasses()
    {
        return true;
    }

	@Override
    public int getRenderPasses( int metadata )
    {
    	return 2;
    }
	
	@Override
    public String getItemStackDisplayName( ItemStack stack )
	{
		ItemStack dummy = getDummyFor( stack );
		Modifier mod = Modifier.getModifier( stack.getTagCompound().getString( "Modifier" ) );
		if ( mod == null )
		{
			return TranslateUtils.translate( getUnlocalizedName() + ".name", stack.getTagCompound().getString( "Modifier" ) + "?" );
		}
		
		return TranslateUtils.translate( getUnlocalizedName() + ".name", mod.getName( dummy ) );
	}
	
	@Override
	public void addInformation( ItemStack stack, EntityPlayer player, List list, boolean par4 )
	{
		ItemStack dummy = getDummyFor( stack );
		Modifier mod = Modifier.getModifier( stack.getTagCompound().getString( "Modifier" ) );
		if ( mod == null )
		{
			list.add( "Modifier \"" + stack.getTagCompound().getString( "Modifier" ) + "\"?" );
			list.add( "Level " + stack.getTagCompound().getInteger( "Level" ) );
			return;
		}
		
		mod.addInformation( dummy, list );
	}

	@Override
	public boolean hasEffect( ItemStack stack )
	{
		return false; //true;
	}
	
	@Override
    public WeightedRandomChestContent getChestGenBase( ChestGenHooks chest, Random rnd, WeightedRandomChestContent original )
    {
		Modifier mod = Modifier.getModifier( Modifier.getTypes()[ rnd.nextInt( Modifier.getTypes().length ) ] ); 
		int level = 1 + rnd.nextInt( mod.getMaxLevel() );
		
		NBTTagCompound tag = new NBTTagCompound();
		tag.setString( "Modifier", mod.type );
		tag.setInteger( "Level", level );
		
		ItemStack loot = new ItemStack( this );
		loot.setTagCompound( tag );
		original.theItemId = loot;
		
		//System.out.println( "Did generation " + chest.category + " " + mod.type + " " + level );
		
        return original;
    }
	
	private ItemStack getDummyFor( ItemStack stack )
	{
		NBTTagCompound tag = stack.getTagCompound();
		String mod = tag.getString( "Modifier" );
		int level = tag.getInteger( "Level" );
		
		ItemStack dummy = getDummyFor( mod, level );
		if ( tag.hasKey( "Data" ) )
		{
			for ( Object keyObj : tag.getCompoundTag( "Data" ).func_150296_c() )
			{
				String key = ( String ) keyObj;
				NBTBase nbt = ( NBTBase ) tag.getCompoundTag( "Data" ).getTag( key );
				dummy.getTagCompound().setTag( key, nbt );
			}
		}
		
		return dummy;
	}
	
	private ItemStack getDummyFor( String mod, int level )
	{
		NBTTagCompound dmod = new NBTTagCompound();
		dmod.setString( "Type", mod );
		dmod.setInteger( "Level", level );
		
		NBTTagList dmods = new NBTTagList();
		dmods.appendTag( dmod );
		
		NBTTagCompound dtag = new NBTTagCompound();
		dtag.setString( "handle", "paper" );
		dtag.setString( "head", "paper" );
		dtag.setString( "binding", "paper" );
		dtag.setTag( "Modifiers", dmods );
		
		ItemStack dummy = new ItemStack( ComponentEquipment.items.axe );
		dummy.setTagCompound( dtag );
		
		return dummy;
	}

	public static ItemStack getStackFor( String mod, int level )
	{
		NBTTagCompound tag = new NBTTagCompound();
		tag.setString( "Modifier", mod );
		tag.setInteger( "Level", level );
		
		ItemStack stack = new ItemStack( ComponentEquipment.items.modifierEssence );
		stack.setTagCompound( tag );
		return stack;
	}
	
	public static ItemStack[] getAllStacks()
	{
		List< ItemStack > stacks = new ArrayList< ItemStack >();
		
		for ( String modifier : Modifier.getTypes() )
		{
			Modifier mod = Modifier.getModifier( modifier );
			for ( int il = 1; il <= mod.getMaxLevel(); ++il )
			{
				ItemStack stack = new ItemStack( ComponentEquipment.items.modifierEssence );
				NBTTagCompound tag = new NBTTagCompound();
				tag.setString( "Modifier", modifier );
				tag.setInteger( "Level", il );
				stack.setTagCompound( tag );
				
				stacks.add( stack );
			}
		}
		
		return stacks.toArray( new ItemStack[ 0 ] );
	}
	
	private IIcon colorIcon;
	private IIcon blankIcon;
}
