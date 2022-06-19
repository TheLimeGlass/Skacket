package me.limeglass.skacket.objects;

import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Player;

import com.comphenix.protocol.wrappers.EnumWrappers.WorldBorderAction;

import ch.njol.skript.Skript;
import ch.njol.skript.util.Version;
import me.limeglass.skacket.Skacket;
import me.limeglass.skacket.wrappers.OldWrapperPlayServerWorldBorder;
import me.limeglass.skacket.wrappers.WrapperPlayServerInitializeWorldBorder;
import me.limeglass.skacket.wrappers.WrapperPlayServerWorldBorderCenter;
import me.limeglass.skacket.wrappers.WrapperPlayServerWorldBorderSize;
import me.limeglass.skacket.wrappers.WrapperPlayServerWorldBorderWarningBlocks;

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

	public void sendRedScreen(long time, TimeUnit timeUnit, Player... players) {
		WrapperPlayServerWorldBorderWarningBlocks packet = new WrapperPlayServerWorldBorderWarningBlocks();
		packet.setWarningBlocks((int) newRadius);
		for (Player player : players)
			packet.sendPacket(player);
		if (timeUnit == null || time <= 0)
			return;
		Bukkit.getScheduler().runTaskLater(Skacket.getInstance(), () -> {
			WrapperPlayServerWorldBorderWarningBlocks packet2 = new WrapperPlayServerWorldBorderWarningBlocks();
			packet2.setWarningBlocks(0);
			for (Player player : players)
				packet2.sendPacket(player);
		}, timeUnit.toSeconds(time) * 20L);
	}

	public void reset(Player... players) {
		sendInitalize(players);
		setWarningDistance(0);
		WrapperPlayServerWorldBorderWarningBlocks packet = new WrapperPlayServerWorldBorderWarningBlocks();
		packet.setWarningBlocks(warningDistance);
		for (Player player : players)
			packet.sendPacket(player);
	}

	public void sendCenterSize(Player... players) {
		if (Skript.getMinecraftVersion().isSmallerThan(new Version(1, 16, 4))) {
			sendInitalize(players);
			return;
		}
		WrapperPlayServerWorldBorderSize packet = new WrapperPlayServerWorldBorderSize();
		packet.setDiameter(newRadius);
		for (Player player : players)
			packet.sendPacket(player);
		WrapperPlayServerWorldBorderCenter packet2 = new WrapperPlayServerWorldBorderCenter();
		packet2.setLocation(center);
		for (Player player : players)
			packet2.sendPacket(player);
	}

	public void sendInitalize(Player... players) {
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
		if (Skript.getMinecraftVersion().isSmallerThan(new Version(1, 16, 4))) {
			OldWrapperPlayServerWorldBorder packet = new OldWrapperPlayServerWorldBorder();
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
			return;
		}
		WrapperPlayServerInitializeWorldBorder packet = new WrapperPlayServerInitializeWorldBorder();
		if (portalTeleportBoundary > 0)
			packet.setPortalTeleportBoundary(portalTeleportBoundary);
		packet.setWarningBlocks(warningDistance);
		packet.setWarningTime(warningTime);
		if (speed > 0)
			packet.setSpeed(speed);
		packet.setX(center.getX());
		packet.setZ(center.getZ());
		if (oldRadius > 0)
			packet.setOldDiameter(oldRadius);
		if (newRadius > 0)
			packet.setNewDiameter(newRadius);
		for (Player player : players) {
			if (!player.getWorld().equals(world))
				continue;
			packet.sendPacket(player);
		}
	}

}
