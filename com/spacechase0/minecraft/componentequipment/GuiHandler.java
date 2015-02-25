package com.spacechase0.minecraft.componentequipment;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.spacechase0.minecraft.componentequipment.client.gui.BackpackGui;
import com.spacechase0.minecraft.componentequipment.client.gui.ComponentStationGui;
import com.spacechase0.minecraft.componentequipment.client.gui.DocumentationGui;
import com.spacechase0.minecraft.componentequipment.client.gui.JukeboxGui;
import com.spacechase0.minecraft.componentequipment.client.gui.ModificationStandGui;
import com.spacechase0.minecraft.componentequipment.client.gui.PersistiumInfuserGui;
import com.spacechase0.minecraft.componentequipment.client.gui.QuiverGui;
import com.spacechase0.minecraft.componentequipment.client.gui.ReorderModifiersGui;
import com.spacechase0.minecraft.componentequipment.inventory.BackpackContainer;
import com.spacechase0.minecraft.componentequipment.inventory.ComponentStationContainer;
import com.spacechase0.minecraft.componentequipment.inventory.JukeboxContainer;
import com.spacechase0.minecraft.componentequipment.inventory.ModificationStandContainer;
import com.spacechase0.minecraft.componentequipment.inventory.PersistiumInfuserContainer;
import com.spacechase0.minecraft.componentequipment.inventory.QuiverContainer;
import com.spacechase0.minecraft.componentequipment.inventory.ReorderModifiersContainer;
import com.spacechase0.minecraft.componentequipment.tileentity.ComponentStationTileEntity;
import com.spacechase0.minecraft.componentequipment.tileentity.ModificationStandTileEntity;
import com.spacechase0.minecraft.componentequipment.tileentity.PersistiumInfuserTileEntity;
import com.spacechase0.minecraft.componentequipment.tool.Armor;

import cpw.mods.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler
{
	@Override
	public Object getServerGuiElement( int id, EntityPlayer player, World world, int x, int y, int z )
	{
		if ( id == ComponentEquipment.QUIVER_GUI_ID )
		{
			ItemStack selected = player.inventory.mainInventory[ player.inventory.currentItem ];
			if ( selected == null || selected.getItem() != ComponentEquipment.items.quiver )
			{
				return null;
			}
			
			return new QuiverContainer( player.inventory, selected );
		}
		else if ( id == ComponentEquipment.REORDER_MODIFIERS_GUI_ID )
		{
			return new ReorderModifiersContainer( player.inventory );
		}
		else if ( id == ComponentEquipment.BACKPACK_GUI_ID )
		{
			ItemStack chestplate = player.getCurrentArmor( 2 );
			if ( chestplate == null || chestplate.getItem() != ComponentEquipment.items.chestplate || Armor.instance.getModifierLevel( chestplate, "chestBackpack" ) <= 0 )
			{
				return null;
			}
			
			return new BackpackContainer( player.inventory, chestplate );
		}
		else if ( id == ComponentEquipment.JUKEBOX_GUI_ID )
		{
			ItemStack helmet = player.getCurrentArmor( 3 );
			if ( helmet == null || helmet.getItem() != ComponentEquipment.items.helmet || Armor.instance.getModifierLevel( helmet, "portableJukebox" ) <= 0 )
			{
				return null;
			}
			
			return new JukeboxContainer( player.inventory, helmet );
		}
		
		TileEntity entity = world.getTileEntity( x, y, z );
		if ( entity instanceof PersistiumInfuserTileEntity )
		{
			return new PersistiumInfuserContainer( player.inventory, ( PersistiumInfuserTileEntity ) entity );
		}
		else if ( entity instanceof ComponentStationTileEntity )
		{
			return new ComponentStationContainer( player.inventory, ( ComponentStationTileEntity ) entity );
		}
		else if ( entity instanceof ModificationStandTileEntity )
		{
			return new ModificationStandContainer( player.inventory, ( ModificationStandTileEntity ) entity );
		}
		
		return null;
	}

	@Override
	public Object getClientGuiElement( int id, EntityPlayer player, World world, int x, int y, int z )
	{
		if ( id == ComponentEquipment.QUIVER_GUI_ID )
		{
			ItemStack selected = player.inventory.mainInventory[ player.inventory.currentItem ];
			if ( selected == null || selected.getItem() != ComponentEquipment.items.quiver )
			{
				return null;
			}
			
			return new QuiverGui( player.inventory, selected );
		}
		else if ( id == ComponentEquipment.REORDER_MODIFIERS_GUI_ID )
		{
			return new ReorderModifiersGui( player.inventory );
		}
		else if ( id == ComponentEquipment.DOCUMENTATION_GUI_ID )
		{
			return new DocumentationGui();
		}
		else if ( id == ComponentEquipment.BACKPACK_GUI_ID )
		{
			ItemStack chestplate = player.getCurrentArmor( 2 );
			if ( chestplate == null || chestplate.getItem() != ComponentEquipment.items.chestplate || Armor.instance.getModifierLevel( chestplate, "chestBackpack" ) <= 0 )
			{
				return null;
			}
			
			return new BackpackGui( player.inventory, chestplate );
		}
		else if ( id == ComponentEquipment.JUKEBOX_GUI_ID )
		{
			ItemStack helmet = player.getCurrentArmor( 3 );
			if ( helmet == null || helmet.getItem() != ComponentEquipment.items.helmet || Armor.instance.getModifierLevel( helmet, "portableJukebox" ) <= 0 )
			{
				return null;
			}
			
			return new JukeboxGui( player.inventory, helmet );
		}
		
		TileEntity entity = world.getTileEntity( x, y, z );
		if ( entity instanceof PersistiumInfuserTileEntity )
		{
			return new PersistiumInfuserGui( player.inventory, ( PersistiumInfuserTileEntity ) entity );
		}
		else if ( entity instanceof ComponentStationTileEntity )
		{
			return new ComponentStationGui( player.inventory, ( ComponentStationTileEntity ) entity );
		}
		else if ( entity instanceof ModificationStandTileEntity )
		{
			return new ModificationStandGui( player.inventory, ( ModificationStandTileEntity ) entity );
		}
		
		return null;
	}
}
