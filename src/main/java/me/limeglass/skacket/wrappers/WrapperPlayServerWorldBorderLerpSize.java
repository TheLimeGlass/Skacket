package me.limeglass.skacket.wrappers;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;

public class WrapperPlayServerWorldBorderLerpSize extends AbstractPacket {

	public static final PacketType TYPE = PacketType.Play.Server.SET_BORDER_LERP_SIZE;

	public WrapperPlayServerWorldBorderLerpSize() {
		super(new PacketContainer(TYPE), TYPE);
		handle.getModifier().writeDefaults();
	}

	public WrapperPlayServerWorldBorderLerpSize(PacketContainer packet) {
		super(packet, TYPE);
	}

	public double getOldDiameter() {
		return handle.getDoubles().read(0);
	}

	public void setOldDiameter(double value) {
		handle.getDoubles().write(0, value);
	}

	public double getNewDiameter() {
		return handle.getDoubles().read(1);
	}

	public void setNewDiameter(double value) {
		handle.getDoubles().write(1, value);
	}

	public double getSpeed() {
		return handle.getLongs().read(0);
	}

	public void setSpeed(long value) {
		handle.getLongs().write(0, value);
	}

}
