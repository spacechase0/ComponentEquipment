package com.spacechase0.minecraft.componentequipment.client.tick;

import java.util.EnumSet;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;

import com.spacechase0.minecraft.componentequipment.ComponentEquipment;
import com.spacechase0.minecraft.componentequipment.item.BowItem;
import com.spacechase0.minecraft.componentequipment.item.QuiverItem;
import com.spacechase0.minecraft.componentequipment.network.SelectedArrowPacket;
import com.spacechase0.minecraft.spacecore.client.gui.Controls;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;

public class ArrowSlotSelector
{
	public ArrowSlotSelector( KeyBinding[] theKeys )
	{
		keys = theKeys;
		for ( int i = 0; i < keys.length; ++i )
		{
			Controls.addKeyBinding( keys[ i ] );
		}
	}

    @SubscribeEvent
    public void keyDown( InputEvent.KeyInputEvent event )
    {
    	Minecraft mc = FMLClientHandler.instance().getClient();
    	EntityPlayer player = mc.thePlayer;
    	if ( player == null || mc.currentScreen != null || !mc.inGameHasFocus )
    	{
    		return;
    	}
    	else if ( player.getCurrentEquippedItem() == null || !( player.getCurrentEquippedItem().getItem() instanceof BowItem ) )
    	{
    		return;
    	}
    	
    	int selected = QuiverItem.getSelected( player );
    	
    	for ( KeyBinding kb : keys )
    	{
	    	if ( kb.getKeyDescription().equals( "key.ce_quiverUp" ) && kb.isPressed() )
	    	{
	    		++selected;
	    	}
	    	else if ( kb.getKeyDescription().equals( "key.ce_quiverDown" ) && kb.isPressed() )
	    	{
	    		--selected;
	    	}
    	}
    	
    	for ( ; selected < 0; selected += QuiverItem.SELECTION_SIZE );
    	for ( ; selected >= QuiverItem.SELECTION_SIZE; selected -= QuiverItem.SELECTION_SIZE );
    	
    	ComponentEquipment.network.sendToServer( new SelectedArrowPacket( selected ) );
    }
    
    private KeyBinding[] keys;
}