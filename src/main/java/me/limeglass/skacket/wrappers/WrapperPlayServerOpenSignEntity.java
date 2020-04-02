package me.limeglass.skacket.wrappers;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.BlockPosition;

public class WrapperPlayServerOpenSignEntity extends AbstractPacket {

	public static final PacketType TYPE = PacketType.Play.Server.OPEN_SIGN_EDITOR;

	public WrapperPlayServerOpenSignEntity() {
		super(new PacketContainer(TYPE), TYPE);
		handle.getModifier().writeDefaults();
	}

	public WrapperPlayServerOpenSignEntity(PacketContainer packet) {
		super(packet, TYPE);
	}

	/**
	 * Retrieve Location.
	 * <p>
	 * Notes: block coordinates
	 * @return The current Location
	 */
	public BlockPosition getLocation() {
		return handle.getBlockPositionModifier().read(0);
	}

	/**
	 * Set Location.
	 * @param value - new value.
	 */
	public void setLocation(BlockPosition value) {
		handle.getBlockPositionModifier().write(0, value);
	}

}
