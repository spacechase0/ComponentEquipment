package com.spacechase0.minecraft.componentequipment.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import com.spacechase0.minecraft.componentequipment.ComponentEquipment;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class IngotBlockItem extends ItemBlock
{
    public IngotBlockItem( Block block )
    {
    	super( block );
        this.setMaxDamage( 0 );
        this.setHasSubtypes( true );
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage( int damage )
    {
        return ComponentEquipment.blocks.ingot.getIcon( 0, damage );
    }

    public int getMetadata( int data )
    {
        return data;
    }

    public String getUnlocalizedName( ItemStack stack )
    {
    	return ComponentEquipment.blocks.ingot.getUnlocalizedName( stack );
    }
}
