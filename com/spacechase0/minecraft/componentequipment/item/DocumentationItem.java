package com.spacechase0.minecraft.componentequipment.item;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.spacechase0.minecraft.componentequipment.ComponentEquipment;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class DocumentationItem extends Item
{
	public DocumentationItem()
	{
		setUnlocalizedName( "componentequipment:docBook" );
		setCreativeTab( ComponentEquipment.partsTab );
	}

    public ItemStack onItemRightClick( ItemStack stack, World world, EntityPlayer player )
    {
    	player.openGui( ComponentEquipment.instance, ComponentEquipment.DOCUMENTATION_GUI_ID, world, 0, 0, 0 );
        return stack;
    }

	@Override
    @SideOnly(Side.CLIENT)
    public void registerIcons( IIconRegister register )
    {
        itemIcon = register.registerIcon( "minecraft:book_enchanted" );
    }
}
