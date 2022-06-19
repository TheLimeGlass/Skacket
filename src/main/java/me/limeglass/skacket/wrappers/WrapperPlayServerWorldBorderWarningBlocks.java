package me.limeglass.skacket.wrappers;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;

public class WrapperPlayServerWorldBorderWarningBlocks extends AbstractPacket {

	public static final PacketType TYPE = PacketType.Play.Server.SET_BORDER_WARNING_DISTANCE;

	public WrapperPlayServerWorldBorderWarningBlocks() {
		super(new PacketContainer(TYPE), TYPE);
		handle.getModifier().writeDefaults();
	}

	public WrapperPlayServerWorldBorderWarningBlocks(PacketContainer packet) {
		super(packet, TYPE);
	}

	public int getWarningBlocks() {
		return handle.getIntegers().read(0);
	}

	public void setWarningBlocks(int value) {
		handle.getIntegers().write(0, value);
	}

}
