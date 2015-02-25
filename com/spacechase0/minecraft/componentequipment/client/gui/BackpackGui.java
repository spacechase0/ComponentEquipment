package com.spacechase0.minecraft.componentequipment.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import com.spacechase0.minecraft.componentequipment.inventory.BackpackContainer;
import com.spacechase0.minecraft.componentequipment.item.ArmorItem;
import com.spacechase0.minecraft.componentequipment.tool.Modifier;
import com.spacechase0.minecraft.componentequipment.tool.modifier.BackpackModifier;
import com.spacechase0.minecraft.spacecore.util.ClientUtils;
import com.spacechase0.minecraft.spacecore.util.TranslateUtils;

public class BackpackGui extends GuiContainer
{
	public BackpackGui( InventoryPlayer player, ItemStack theChestplate )
	{
		super( new BackpackContainer( player, theChestplate ) );
		chestplate = theChestplate;

		ArmorItem armor = ( ArmorItem ) chestplate.getItem();
		
		Object[] obj = inventorySlots.inventorySlots.toArray();
		Slot slotF = ( Slot ) obj[ 0 ];
		Slot slotL = ( Slot ) obj[ obj.length - 1 ];
		
		if ( armor.armor.getModifierLevel( chestplate, "chestBackpack" ) == 4 ) xSize += 18 * 1.5 + 8;
		ySize = slotL.yDisplayPosition + 24;
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer( int param1, int param2 )
	{
		ArmorItem armor = ( ArmorItem ) chestplate.getItem();
		
		if ( armor.armor.getModifierLevel( chestplate, "chestBackpack" ) == 1 )
		{
			ClientUtils.drawString( TranslateUtils.translate( "container.backpack" ), 8, 6, 4210752 );
			ClientUtils.drawString( TranslateUtils.translate( "container.inventory" ), 8, ySize - 96 + 2, 4210752 );
		}
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer( float f, int i, int j )
	{
		ArmorItem armor = ( ArmorItem ) chestplate.getItem();
		BackpackModifier mod = ( BackpackModifier ) Modifier.getModifier( "chestBackpack" );
		
        GL11.glColor4f( 1.0F, 1.0F, 1.0F, 1.0F );
        ClientUtils.bindTexture( mod.getGuiTexture( armor.armor.getModifierLevel( chestplate, "chestBackpack" ) ) );
        int x = ( width - xSize ) / 2;
        int y = ( height - ySize ) / 2;
        this.drawTexturedModalRect( x, y, 0, 0, xSize, ySize );
	}
	
	private final ItemStack chestplate;
}
