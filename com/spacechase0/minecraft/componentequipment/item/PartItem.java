package com.spacechase0.minecraft.componentequipment.item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;

import com.spacechase0.minecraft.componentequipment.ComponentEquipment;
import com.spacechase0.minecraft.componentequipment.tool.Material;
import com.spacechase0.minecraft.componentequipment.tool.Modifier;
import com.spacechase0.minecraft.componentequipment.tool.Part;
import com.spacechase0.minecraft.spacecore.util.TranslateUtils;

public class PartItem extends SimpleItem
{
	public PartItem()
	{
		super( "partItem" );
		setCreativeTab( ComponentEquipment.partsTab );
		
		//setMaxStackSize( 8 );
	}

	@Override
    public void registerIcons( IIconRegister register )
    {
		icons = new HashMap< String, Map< String, IIcon > >();

		String[] mats = Material.getTypes();
		String[] parts = Part.getTypes();
		for ( int ip = 0; ip < parts.length; ++ip )
		{
			Map< String, IIcon > partIcons = new HashMap< String, IIcon >();
			
			for ( int im = 0; im < mats.length; ++im )
			{
				String path = "parts/" + parts[ ip ] + "/" + mats[ im ];
				partIcons.put( mats[ im ], register.registerIcon( "componentequipment:" + path ) );
			}
			
			icons.put( parts[ ip ], partIcons );
		}
		badIcon = register.registerIcon( "componentequipment:badPart" );
    }
	
	@Override
    public void getSubItems( Item id, CreativeTabs tabs, List list )
    {
		String[] mats = Material.getTypes();
		String[] parts = Part.getTypes();
		for ( int ip = 0; ip < parts.length; ++ip )
		{
			String part = parts[ ip ];
			if ( part.equals( "paxelHead" ) || part.equals( "helm" ) || part.equals( "chest" ) || part.equals( "leggings" ) || part.equals( "boots" ) )
			{
				continue;
			}
			
			for ( int im = 0; im < mats.length; ++im )
			{
				NBTTagCompound tag = new NBTTagCompound();
				tag.setString( "Part", part );
				tag.setString( "Material", mats[ im ] );

				ItemStack stack = new ItemStack( this );
				stack.setTagCompound( tag );
				list.add( stack );
			}
		}
    }
	
	@Override
    public IIcon getIcon( ItemStack stack, int pass )
    {
		NBTTagCompound tag = stack.getTagCompound();
		if ( tag == null )
		{
			//System.out.println( "Bad NBT tag" );
			return badIcon;
		}
		
		String part = tag.getString( "Part" );
		String mat = tag.getString( "Material" );
		
		if ( icons.get( part ) == null )
		{
			//System.out.println( "NO icon category for " + part );
			return badIcon;
		}
		return icons.get( part ).get( mat );
    }
	
	@Override
	public void addInformation( ItemStack stack, EntityPlayer player, List list, boolean par4 )
	{
		if ( stack == null || stack.getTagCompound() == null )
		{
			return;
		}
		NBTTagCompound tag = stack.getTagCompound();

		String part = tag.getString( "Part" );
		String mat = tag.getString( "Material" );
		
		list.add( TranslateUtils.translate( "item.part.tooltip.part", TranslateUtils.translate( "componentequipment:part." + part ) ) );
		list.add( TranslateUtils.translate( "item.part.tooltip.material", Material.getData( mat ).getFormat() + TranslateUtils.translate( "componentequipment:material." + mat ) ) );
	}
	
	@Override
    public String getUnlocalizedName( ItemStack stack )
    {
		NBTTagCompound tag = stack.getTagCompound();
		String part = tag.getString( "Part" );
		
        return "item.part";//part + "Part";
    }
	
	public static ItemStack[] getAllStacks()
	{
		return getAllStacks( "", "" );
	}
	
	public static ItemStack[] getAllStacksOfType( String type )
	{
		return getAllStacks( type, "" );
	}
	
	public static ItemStack[] getAllStacksOfMaterial( String mat )
	{
		return getAllStacks( "", mat );
	}
	
	private static ItemStack[] getAllStacks( String wantType, String wantMat )
	{
		List< ItemStack > stacks = new ArrayList< ItemStack >();

		for ( String type : Part.getTypes() )
		{
			for ( String mat : Material.getTypes() )
			{
				ItemStack stack = new ItemStack( ComponentEquipment.items.part );
				NBTTagCompound tag = new NBTTagCompound();
				tag.setString( "Part", type );
				tag.setString( "Material", mat );
				stack.setTagCompound( tag );
				
				if ( wantType.equals( "" ) && wantMat.equals( "" ) )
				{
					stacks.add( stack );
				}
				else if ( wantType.equals( type ) && wantMat.equals( "" ) )
				{
					stacks.add( stack );
				}
				else if ( wantType.equals( "" ) && wantMat.equals( mat ) )
				{
					stacks.add( stack );
				}
			}
		}
		
		return stacks.toArray( new ItemStack[ 0 ] );
	}
	
	private Map< String, Map< String, IIcon > > icons;
	private IIcon badIcon;
}
