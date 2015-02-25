package com.spacechase0.minecraft.componentequipment.client.tick;

import java.util.EnumSet;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;

import com.spacechase0.minecraft.componentequipment.ComponentEquipment;
import com.spacechase0.minecraft.componentequipment.network.OpenGuiPacket;
import com.spacechase0.minecraft.spacecore.client.gui.Controls;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;

public class ReorderOpener
{
	public ReorderOpener( KeyBinding theKey )
	{
		key = theKey;
		Controls.addKeyBinding( key );
	}

	@SubscribeEvent
    public void keyDown( InputEvent.KeyInputEvent event )
    {
    	Minecraft mc = FMLClientHandler.instance().getClient();
    	EntityPlayer player = mc.thePlayer;
    	if ( player == null || mc.currentScreen != null || !mc.inGameHasFocus || !key.isPressed() )
    	{
    		return;
    	}

    	ComponentEquipment.network.sendToServer( new OpenGuiPacket( ComponentEquipment.REORDER_MODIFIERS_GUI_ID ) );
    }

	private KeyBinding key;
}