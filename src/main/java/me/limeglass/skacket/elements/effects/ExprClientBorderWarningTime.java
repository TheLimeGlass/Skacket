package me.limeglass.skacket.elements.effects;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import com.comphenix.protocol.wrappers.EnumWrappers.WorldBorderAction;

import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.skript.util.Timespan;
import ch.njol.util.coll.CollectionUtils;
import me.limeglass.skacket.wrappers.WrapperPlayServerWorldBorder;

public class ExprClientBorderWarningTime extends SimplePropertyExpression<Player, Timespan> {

	static {
		register(ExprClientBorderCenter.class, Location.class, "client [side] [world] border warning [time]", "players");
	}

	@Override
	public Class<? extends Timespan> getReturnType() {
		return Timespan.class;
	}

	@Override
	public Timespan convert(Player player) {
		WrapperPlayServerWorldBorder packet = new WrapperPlayServerWorldBorder();
		packet.setAction(WorldBorderAction.SET_WARNING_TIME);
		packet.receivePacket(player);
		return new Timespan((packet.getWarningTime() * 1000));
	}

	@Override
	protected String getPropertyName() {
		return "client border warning time";
	}

	@Override
	public Class<?>[] acceptChange(ChangeMode mode) {
		return (mode == ChangeMode.SET) ? CollectionUtils.array(Timespan.class) : null;
	}

	@Override
	public void change(Event event, Object[] delta, ChangeMode mode) {
		Player[] players = getExpr().getAll(event);
		if (players == null)
			return;
		Timespan time = (Timespan) delta[0];
		switch (mode) {
			case ADD:
				for (Player player : players) {
					Timespan existing = convert(player);
					WrapperPlayServerWorldBorder packet = new WrapperPlayServerWorldBorder();
					packet.setAction(WorldBorderAction.SET_WARNING_TIME);
					packet.setWarningTime((int) (existing.getMilliSeconds() + time.getMilliSeconds()));
					packet.sendPacket(player);
				}
				break;
			case REMOVE:
				for (Player player : players) {
					Timespan existing = convert(player);
					WrapperPlayServerWorldBorder packet = new WrapperPlayServerWorldBorder();
					packet.setAction(WorldBorderAction.SET_WARNING_TIME);
					if (existing.getMilliSeconds() - time.getMilliSeconds() < 0)
						continue;
					packet.setWarningTime((int) (existing.getMilliSeconds() - time.getMilliSeconds()));
					packet.sendPacket(player);
				}
				break;
			case DELETE:
			case RESET:
				for (Player player : players) {
					WrapperPlayServerWorldBorder packet = new WrapperPlayServerWorldBorder();
					packet.setWarningTime(player.getWorld().getWorldBorder().getWarningTime());
					packet.setAction(WorldBorderAction.SET_WARNING_TIME);
					packet.sendPacket(player);
				}
				break;
			case SET:
				WrapperPlayServerWorldBorder packet = new WrapperPlayServerWorldBorder();
				packet.setAction(WorldBorderAction.SET_WARNING_TIME);
				packet.setWarningTime((int)time.getMilliSeconds());
				for (Player player : players)
					packet.sendPacket(player);
				break;
			case REMOVE_ALL:
			default:
				break;
		}
	}

}
