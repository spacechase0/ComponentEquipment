package com.spacechase0.minecraft.componentequipment.network;

public class PacketCodec extends com.spacechase0.minecraft.spacecore.network.PacketCodec
{
	public PacketCodec()
	{
		addPacket( new ActivateInfuserPacket() );
		addPacket( new ActivateModificationPacket() );
		addPacket( new OpenGuiPacket() );
		addPacket( new ReorderModifiersPacket() );
		addPacket( new SelectedArrowPacket() );
	}
	
	protected static final byte ID_ACTIVATE_INFUSER = 0;
	protected static final byte ID_ACTIVATE_MODIFICATION = 1;
	protected static final byte ID_OPEN_GUI = 2;
	protected static final byte ID_REORDER_MODIFIERS = 3;
	protected static final byte ID_SELECTED_ARROW = 4;
}
