package me.limeglass.skacket.wrappers;

import java.util.List;

import org.bukkit.ChatColor;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.utility.MinecraftReflection;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.nbt.NbtCompound;

public class WrapperPlayServerUpdateSign extends AbstractPacket {

	@SuppressWarnings("deprecation")
	public static final PacketType TYPE = PacketType.Play.Server.UPDATE_SIGN;
	// when this gets removed, use the below type, this is what ProtocolLib does anyways with the above type.
	//public static final PacketType TYPE = MinecraftReflection.signUpdateExists() ? PacketType.Play.Server.UPDATE_SIGN : PacketType.Play.Server.TILE_ENTITY_DATA;

	public WrapperPlayServerUpdateSign() {
		super(new PacketContainer(TYPE), TYPE);
		handle.getModifier().writeDefaults();
	}

	public WrapperPlayServerUpdateSign(PacketContainer packet) {
		super(packet, TYPE);
	}

	/**
	 * Retrieve Location.
	 * <p>
	 * Notes: block Coordinates
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

	/**
	 * Retrieve this sign's lines of text represented by a chat component array.
	 * @return The current lines
	 */
	public WrappedChatComponent[] getLines() {
		return handle.getChatComponentArrays().read(0);
	}

	/**
	 * Set this sign's lines of text.
	 * @param value - Lines, must be 4 elements long
	 */
	public void setLines(List<String> list) {
		if (list == null)
			throw new IllegalArgumentException("list cannot be null!");
		if (list.isEmpty())
			throw new IllegalArgumentException("list cannot be empty!");
		if (list.size() > 4)
			throw new IllegalArgumentException("list must have 4 or less elements!");
		if (MinecraftReflection.signUpdateExists()) {
			WrappedChatComponent[] lines = new WrappedChatComponent[4];
			String[] text = list.toArray(new String[list.size()]);
			lines[0] = WrappedChatComponent.fromText(text[0]);
			if (text.length >= 2)
				lines[1] = WrappedChatComponent.fromText(text[1]);
			else
				lines[1] = WrappedChatComponent.fromText("");
			if (text.length >= 3)
				lines[2] = WrappedChatComponent.fromText(text[2]);
			else
				lines[2] = WrappedChatComponent.fromText("");
			if (text.length == 4)
				lines[3] = WrappedChatComponent.fromText(text[3]);
			else
				lines[3] = WrappedChatComponent.fromText("");
			handle.getChatComponentArrays().write(0, lines);
			return;
		}

		int ACTION_INDEX = 9; //ID found at https://wiki.vg/Protocol#Block_Entity_Data it may change at some point Mojang.
		String NBT_FORMAT = "{\"text\":\"%s\"}"; // NBT format currently known for 1.15, may change at some point Mojang.
		String NBT_BLOCK_ID = "minecraft:sign";

		NbtCompound signNBT = (NbtCompound) handle.getNbtModifier().read(0);

		for (int line = 0; line < list.size(); line++) {
			signNBT.put("Text" + (line + 1), String.format(NBT_FORMAT, ChatColor.translateAlternateColorCodes('&', list.get(line))));
		}

		BlockPosition position = getLocation();
		signNBT.put("x", position.getX());
		signNBT.put("y", position.getY());
		signNBT.put("z", position.getZ());
		signNBT.put("id", NBT_BLOCK_ID);

		handle.getIntegers().write(0, ACTION_INDEX);
		handle.getNbtModifier().write(0, signNBT);
	}

}
