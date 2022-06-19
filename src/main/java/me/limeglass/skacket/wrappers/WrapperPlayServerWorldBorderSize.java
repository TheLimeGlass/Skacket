package me.limeglass.skacket.wrappers;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;

public class WrapperPlayServerWorldBorderSize extends AbstractPacket {

	public static final PacketType TYPE = PacketType.Play.Server.SET_BORDER_SIZE;

	public WrapperPlayServerWorldBorderSize() {
		super(new PacketContainer(TYPE), TYPE);
		handle.getModifier().writeDefaults();
	}

	public WrapperPlayServerWorldBorderSize(PacketContainer packet) {
		super(packet, TYPE);
	}

	public double getDiameter() {
		return handle.getDoubles().read(0);
	}

	public void setDiameter(double value) {
		handle.getDoubles().write(0, value);
	}

}
