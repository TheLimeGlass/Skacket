package me.limeglass.skacket.wrappers;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.EnumWrappers.Direction;
import com.comphenix.protocol.wrappers.EnumWrappers.PlayerDigType;

public class WrapperPlayClientBlockDig extends AbstractPacket {

	public static final PacketType TYPE = PacketType.Play.Client.BLOCK_DIG;

	public WrapperPlayClientBlockDig() {
		super(new PacketContainer(TYPE), TYPE);
		handle.getModifier().writeDefaults();
	}

	public WrapperPlayClientBlockDig(PacketContainer packet) {
		super(packet, TYPE);
	}

	/**
	 * Retrieve Location.
	 * <p>
	 * Notes: block Coordinates
	 * 
	 * @return The current Location
	 */
	public BlockPosition getLocation() {
		return handle.getBlockPositionModifier().read(0);
	}

	/**
	 * Set Location.
	 * 
	 * @param value - new value.
	 */
	public void setLocation(BlockPosition value) {
		handle.getBlockPositionModifier().write(0, value);
	}

	/**
	 * Retrieve the block dig state.
	 * 
	 * @return The current block dig state.
	 */
	public PlayerDigType getDigType() {
		return handle.getPlayerDigTypes().readSafely(0);
	}

	/**
	 * Set the block dig state.
	 * 
	 * @param value - new value.
	 */
	public void setDigType(PlayerDigType type) {
		handle.getPlayerDigTypes().write(0, type);
	}

	/**
	 * Rertrieve the block face direction it was hit from.
	 * 
	 * @return The current direction the block was hit from.
	 */
	public Direction getDirection() {
		return handle.getDirections().readSafely(0);
	}

}
