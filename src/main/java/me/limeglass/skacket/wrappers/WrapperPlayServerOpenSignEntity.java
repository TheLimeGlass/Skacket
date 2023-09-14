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
	 * If the sign is the front side of the sign.
	 * 
	 * @return true if the editing side is the front.
	 */
	public boolean isFrontSide() {
		return handle.getBooleans().read(0);
	}

	/**
	 * Set if the editor is the front side of the sign or back.
	 * 
	 * @param value - true to be the front side, false to be the backside.
	 */
	public void setFrontSide(boolean value) {
		handle.getBooleans().write(0, value);
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
