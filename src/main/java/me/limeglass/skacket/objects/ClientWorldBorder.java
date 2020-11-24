package me.limeglass.skacket.objects;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Player;

import com.comphenix.protocol.wrappers.EnumWrappers.WorldBorderAction;

import me.limeglass.skacket.wrappers.WrapperPlayServerWorldBorder;

public class ClientWorldBorder {

	private int warningTime, warningDistance, portalTeleportBoundary;
	private double oldRadius, newRadius;
	private final World world;
	private Location center;
	private long speed;

	public ClientWorldBorder(World world) {
		this.world = world;
		WorldBorder border = world.getWorldBorder();
		warningDistance = border.getWarningDistance();
		warningTime = border.getWarningTime();
		oldRadius = border.getSize();
		newRadius = border.getSize();
		center = border.getCenter();
	}

	public void setPortalTeleportBoundary(int portalTeleportBoundary) {
		this.portalTeleportBoundary = portalTeleportBoundary;
	}

	public int getPortalTeleportBoundary() {
		return portalTeleportBoundary;
	}

	public void setWarningDistance(int warningDistance) {
		this.warningDistance = warningDistance;
	}

	public int getWarningDistance() {
		return warningDistance;
	}

	public void setWarningTime(int warningTime) {
		this.warningTime = warningTime;
	}

	public int getWarningTime() {
		return warningTime;
	}

	public void setCenter(Location center) {
		this.center = center;
	}

	public Location getCenter() {
		return center;
	}

	public void setOldRadius(double oldRadius) {
		this.oldRadius = oldRadius;
	}

	public double getOldRadius() {
		return oldRadius;
	}

	public void setRadius(double radius) {
		this.newRadius = radius;
	}

	public double getRadius() {
		return newRadius;
	}

	public void setSpeed(long speed) {
		this.speed = speed;
	}

	public long getSpeed() {
		return speed;
	}

	public World getWorld() {
		return world;
	}

	public void send(Player... players) {
		WorldBorder existing = world.getWorldBorder();
		if (center == null)
			center = existing.getCenter();
		if (warningDistance <= 0)
			warningDistance = existing.getWarningDistance();
		if (warningTime <= 0)
			warningTime = existing.getWarningTime();
		if (warningTime <= 0)
			warningTime = existing.getWarningTime();
		if (newRadius <= 0)
			newRadius = existing.getSize();
		WrapperPlayServerWorldBorder packet = new WrapperPlayServerWorldBorder();
		if (portalTeleportBoundary > 0)
			packet.setPortalTeleportBoundary(portalTeleportBoundary);
		packet.setAction(WorldBorderAction.INITIALIZE);
		packet.setWarningDistance(warningDistance);
		packet.setWarningTime(warningTime);
		packet.setCenterX(center.getX());
		packet.setCenterZ(center.getZ());
		if (oldRadius > 0)
			packet.setOldRadius(oldRadius);
		packet.setRadius(newRadius);
		if (speed > 0)
			packet.setSpeed(speed);
		for (Player player : players) {
			if (!player.getWorld().equals(world))
				continue;
			packet.sendPacket(player);
		}
	}

}
