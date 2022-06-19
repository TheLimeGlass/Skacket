package me.limeglass.skacket.wrappers;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;

public class WrapperPlayServerInitializeWorldBorder extends AbstractPacket {

	public static final PacketType TYPE = PacketType.Play.Server.INITIALIZE_BORDER;

	public WrapperPlayServerInitializeWorldBorder() {
		super(new PacketContainer(TYPE), TYPE);
		handle.getModifier().writeDefaults();
	}

	public WrapperPlayServerInitializeWorldBorder(PacketContainer packet) {
		super(packet, TYPE);
	}

	public double getX() {
		return handle.getDoubles().read(0);
	}

	public void setX(double value) {
		handle.getDoubles().write(0, value);
	}

	public double getZ() {
		return handle.getDoubles().read(1);
	}

	public void setZ(double value) {
		handle.getDoubles().write(1, value);
	}

	public double getOldDiameter() {
		return handle.getDoubles().read(2);
	}

	public void setOldDiameter(double value) {
		handle.getDoubles().write(2, value);
	}

	public double getNewDiameter() {
		return handle.getDoubles().read(3);
	}

	public void setNewDiameter(double value) {
		handle.getDoubles().write(3, value);
	}

	public long getSpeed() {
		return handle.getLongs().read(0);
	}

	public void setSpeed(long value) {
		handle.getLongs().write(0, value);
	}

	public int getPortalTeleportBoundary() {
		return handle.getIntegers().read(0);
	}

	public void setPortalTeleportBoundary(int value) {
		handle.getIntegers().write(0, value);
	}

	public int getWarningBlocks() {
		return handle.getIntegers().read(1);
	}

	public void setWarningBlocks(int value) {
		handle.getIntegers().write(1, value);
	}

	public int getWarningTime() {
		return handle.getIntegers().read(2);
	}

	public void setWarningTime(int value) {
		handle.getIntegers().write(2, value);
	}

}
