package com.spacechase0.minecraft.componentequipment.item;

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
import com.spacechase0.minecraft.componentequipment.tool.Arrow;
import com.spacechase0.minecraft.spacecore.util.TranslateUtils;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ArrowItem extends SimpleItem
{
	public ArrowItem()
	{
		super( "arrow" );
		
		setCreativeTab( ComponentEquipment.toolsTab );
	}

	@Override
    @SideOnly(Side.CLIENT)
    public void registerIcons( IIconRegister register )
    {
		headIcons = new HashMap< String, IIcon >();
		String[] heads = Arrow.getTypes();
		for ( int ih = 0; ih < heads.length; ++ih )
		{
			headIcons.put( heads[ ih ], register.registerIcon( "componentequipment:arrow/head/" + heads[ ih ] ) );
		}

		stickIcon = register.registerIcon( "componentequipment:arrow/stick/wood" );
		featherIcon = register.registerIcon( "componentequipment:arrow/feather" );
		
		blankIcon = register.registerIcon( "componentequipment:blank" );
		badIcon = register.registerIcon( "componentequipment:badTool" );
    }
	
	@Override
    public void getSubItems( Item id, CreativeTabs tabs, List list )
    {
		String[] heads = Arrow.getTypes();
		for ( int ih = 0; ih < heads.length; ++ih )
		{
			NBTTagCompound tag = new NBTTagCompound();
			tag.setString( "Head", heads[ ih ] );
			tag.setBoolean( "Feather", true );
			
			ItemStack stack = new ItemStack( this );
			stack.setTagCompound( tag );
			list.add( stack );
		}
		for ( int ih = 0; ih < heads.length; ++ih )
		{
			NBTTagCompound tag = new NBTTagCompound();
			tag.setString( "Head", heads[ ih ] );
			tag.setBoolean( "Feather", false );
			
			ItemStack stack = new ItemStack( this );
			stack.setTagCompound( tag );
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
		
		if ( pass == 0 )
		{
			return stickIcon;
		}
		else if ( pass == 1 )
		{
			return headIcons.get( tag.getString( "Head" ) );
		}
		else if ( pass == 2 && tag.getBoolean( "Feather" ) )
		{
			return featherIcon;
		}
		
		return blankIcon;
    }
	
	@Override
    public void addInformation( ItemStack stack, EntityPlayer player, List list, boolean par4)
	{
		if ( stack == null || stack.getTagCompound() == null )
		{
			list.add( "INVALID ARROW: Delete immediately." );
			return;
		}
		
		NBTTagCompound tag = stack.getTagCompound();
		
		if ( tag.getBoolean( "Feather" ) )
		{
			list.add( TranslateUtils.translate( "item.arrow.feathered" ) );
		}
	}
	
	@Override
    public boolean requiresMultipleRenderPasses()
    {
        return true;
    }

	@Override
    public int getRenderPasses( int metadata )
    {
    	return 3;
    }
	
	@Override
    public String getUnlocalizedName( ItemStack stack )
    {
		if ( stack == null || stack.getTagCompound() == null )
		{
			return "item.badArrow";
		}
		
		String mat = getHeadMaterial( stack );
		mat = mat.substring( 0, 1 ).toUpperCase() + mat.substring( 1 );
		
		return "item.arrow" + mat;
    }
	
	public static String getHeadMaterial( ItemStack stack )
	{
		if ( stack == null || stack.getTagCompound() == null )
		{
			return null;
		}
		
		NBTTagCompound tag = stack.getTagCompound();
		
		return tag.getString( "Head" );
	}
	
	public static boolean getFeathered( ItemStack stack )
	{
		if ( stack == null || stack.getTagCompound() == null )
		{
			return false;
		}
		
		NBTTagCompound tag = stack.getTagCompound();
		
		return tag.getBoolean( "Feather" );
	}
	
	private Map< String, IIcon > headIcons;
	private IIcon stickIcon;
	private IIcon featherIcon;
	private IIcon blankIcon;
	private IIcon badIcon;
}
