package me.limeglass.skacket.elements.effects;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

import com.comphenix.protocol.wrappers.EnumWrappers.WorldBorderAction;

import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import me.limeglass.skacket.wrappers.WrapperPlayServerWorldBorder;

public class ExprClientBorderCenter extends SimplePropertyExpression<Player, Location> {

	static {
		register(ExprClientBorderCenter.class, Location.class, "client [side] [world] border center [location]", "players");
	}

	@Override
	public Class<? extends Location> getReturnType() {
		return Location.class;
	}

	@Override
	@Nullable
	public Location convert(Player player) {
		WrapperPlayServerWorldBorder packet = new WrapperPlayServerWorldBorder();
		packet.setAction(WorldBorderAction.SET_CENTER);
		packet.receivePacket(player);
		return new Location(player.getWorld(), packet.getCenterX(), 0, packet.getCenterZ());
	}

	@Override
	protected String getPropertyName() {
		return "client border center";
	}

	@Override
	public Class<?>[] acceptChange(ChangeMode mode) {
		return (mode == ChangeMode.SET) ? CollectionUtils.array(Location.class) : null;
	}

	@Override
	public void change(Event event, Object[] delta, ChangeMode mode) {
		Player[] players = getExpr().getAll(event);
		if (players == null)
			return;
		Location location = (Location) delta[0];
		WrapperPlayServerWorldBorder packet = new WrapperPlayServerWorldBorder();
		packet.setAction(WorldBorderAction.SET_CENTER);
		packet.setCenterX(location.getX());
		packet.setCenterZ(location.getZ());
		for (Player player : players)
			packet.sendPacket(player);
	}

}
