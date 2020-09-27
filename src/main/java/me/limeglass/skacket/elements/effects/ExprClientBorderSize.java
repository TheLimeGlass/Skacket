package me.limeglass.skacket.elements.effects;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

import com.comphenix.protocol.wrappers.EnumWrappers.WorldBorderAction;

import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import me.limeglass.skacket.wrappers.WrapperPlayServerWorldBorder;

public class ExprClientBorderSize extends SimplePropertyExpression<Player, Number> {

	static {
		register(ExprClientBorderSize.class, Number.class, "client [side] [world] border (radius|size)", "players");
	}

	@Override
	public Class<? extends Number> getReturnType() {
		return Number.class;
	}

	@Override
	@Nullable
	public Number convert(Player player) {
		WrapperPlayServerWorldBorder packet = new WrapperPlayServerWorldBorder();
		packet.setAction(WorldBorderAction.SET_SIZE);
		packet.receivePacket(player);
		return packet.getRadius();
	}

	@Override
	protected String getPropertyName() {
		return "client border radius";
	}

	@Override
	public Class<?>[] acceptChange(ChangeMode mode) {
		return mode != ChangeMode.REMOVE_ALL ? CollectionUtils.array(Number.class) : null;
	}

	@Override
	public void change(Event event, Object[] delta, ChangeMode mode) {
		Player[] players = getExpr().getAll(event);
		if (players == null)
			return;
		double radius = (double) delta[0];
		switch (mode) {
			case ADD:
				for (Player player : players) {
					double existing = convert(player).doubleValue();
					WrapperPlayServerWorldBorder packet = new WrapperPlayServerWorldBorder();
					packet.setAction(WorldBorderAction.SET_SIZE);
					packet.setRadius(existing + radius);
					packet.sendPacket(player);
				}
				break;
			case REMOVE:
				for (Player player : players) {
					double existing = convert(player).doubleValue();
					WrapperPlayServerWorldBorder packet = new WrapperPlayServerWorldBorder();
					packet.setAction(WorldBorderAction.SET_SIZE);
					if (existing - radius < 0)
						existing = radius;
					packet.setRadius(existing - radius);
					packet.sendPacket(player);
				}
				break;
			case DELETE:
			case RESET:
				for (Player player : players) {
					WrapperPlayServerWorldBorder packet = new WrapperPlayServerWorldBorder();
					packet.setRadius(player.getWorld().getWorldBorder().getSize());
					packet.setAction(WorldBorderAction.SET_SIZE);
					packet.sendPacket(player);
				}
				break;
			case SET:
				WrapperPlayServerWorldBorder packet = new WrapperPlayServerWorldBorder();
				packet.setAction(WorldBorderAction.SET_SIZE);
				packet.setRadius(radius);
				for (Player player : players)
					packet.sendPacket(player);
				break;
			case REMOVE_ALL:
			default:
				break;
		}
	}

}
