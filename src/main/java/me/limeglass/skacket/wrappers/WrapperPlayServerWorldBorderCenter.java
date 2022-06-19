package me.limeglass.skacket.wrappers;

import org.bukkit.Location;
import org.bukkit.World;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;

public class WrapperPlayServerWorldBorderCenter extends AbstractPacket {

	public static final PacketType TYPE = PacketType.Play.Server.SET_BORDER_CENTER;

	public WrapperPlayServerWorldBorderCenter() {
		super(new PacketContainer(TYPE), TYPE);
		handle.getModifier().writeDefaults();
	}

	public WrapperPlayServerWorldBorderCenter(PacketContainer packet) {
		super(packet, TYPE);
	}

	public Location getLocation(World world) {
		return new Location(world, getX(), 0, getZ());
	}

	public void setLocation(Location location) {
		setX(location.getX());
		setZ(location.getZ());
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

}
