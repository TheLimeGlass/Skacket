package me.limeglass.skacket.elements.effects;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import com.comphenix.protocol.wrappers.EnumWrappers.WorldBorderAction;

import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import me.limeglass.skacket.wrappers.WrapperPlayServerWorldBorder;

public class ExprClientBorderDamageAmount extends SimplePropertyExpression<Player, Number> {

	static {
		register(ExprClientBorderSize.class, Number.class, "client [side] [world] border damage [amount]", "players");
	}

	@Override
	public Class<? extends Number> getReturnType() {
		return Number.class;
	}

	@Override
	public Number convert(Player player) {
		WrapperPlayServerWorldBorder packet = new WrapperPlayServerWorldBorder();
		packet.setAction(WorldBorderAction.SET_WARNING_BLOCKS);
		packet.receivePacket(player);
		return packet.getWarningDistance();
	}

	@Override
	protected String getPropertyName() {
		return "client border warning distance";
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
					packet.setAction(WorldBorderAction.SET_WARNING_BLOCKS);
					packet.setWarningDistance((int) (existing + radius));
					packet.sendPacket(player);
				}
				break;
			case REMOVE:
				for (Player player : players) {
					double existing = convert(player).doubleValue();
					WrapperPlayServerWorldBorder packet = new WrapperPlayServerWorldBorder();
					packet.setAction(WorldBorderAction.SET_WARNING_BLOCKS);
					if (existing - radius < 0)
						existing = radius;
					packet.setWarningDistance((int) (existing - radius));
					packet.sendPacket(player);
				}
				break;
			case DELETE:
			case RESET:
				for (Player player : players) {
					WrapperPlayServerWorldBorder packet = new WrapperPlayServerWorldBorder();
					packet.setRadius(player.getWorld().getWorldBorder().getWarningDistance());
					packet.setAction(WorldBorderAction.SET_WARNING_BLOCKS);
					packet.sendPacket(player);
				}
				break;
			case SET:
				WrapperPlayServerWorldBorder packet = new WrapperPlayServerWorldBorder();
				packet.setAction(WorldBorderAction.SET_WARNING_BLOCKS);
				packet.setWarningDistance((int) radius);
				for (Player player : players)
					packet.sendPacket(player);
				break;
			case REMOVE_ALL:
			default:
				break;
		}
	}

}
	
//	private Expression<Player> player;
//	@Override
//	public Class<? extends Number> getReturnType() {
//		return Number.class;
//	}
//	@Override
//	public boolean isSingle() {
//		return true;
//	}
//	@SuppressWarnings("unchecked")
//	@Override
//	public boolean init(Expression<?>[] e, int matchedPattern, Kleenean isDelayed, ParseResult parser) {
//		player = (Expression<Player>) e[0];
//		return true;
//	}
//	@Override
//	public String toString(@Nullable Event e, boolean paramBoolean) {
//		return "[client [side]] [world] border damage [amount] (for|of) %player%";
//	}
//	@Override
//	@Nullable
//	protected Number[] get(Event e) {
//		if (player != null) {
//			return new Number[]{ClientBorderManager.getDamageAmount(player.getSingle(e))};
//		}
//		return null;
//	}
//	@Override
//	public void change(Event e, Object[] delta, Changer.ChangeMode mode){
//		if (player != null) {
//			Number amountNow = ClientBorderManager.getDamageAmount(player.getSingle(e));
//			if (amountNow == null) {
//				amountNow = 0.2D;
//			}
//			if (mode == ChangeMode.SET) {
//				Number amount = (Number)delta[0];
//				ClientBorderManager.setDamageAmount(player.getSingle(e), amount.doubleValue());
//			} else if (mode == ChangeMode.ADD) {
//				Number amount = (Number)delta[0];
//				ClientBorderManager.setDamageAmount(player.getSingle(e), amountNow.doubleValue() + amount.doubleValue());
//			} else if (mode == ChangeMode.REMOVE) {
//				Number amount = (Number)delta[0];
//				ClientBorderManager.setDamageAmount(player.getSingle(e), amountNow.doubleValue() - amount.doubleValue());
//			} else if (mode == ChangeMode.RESET) {
//				ClientBorderManager.setDamageAmount(player.getSingle(e), 0.2D); //Same number Minecraft uses when creating borders
//			}
//		}
//	}
//	@Override
//	public Class<?>[] acceptChange(final Changer.ChangeMode mode) {
//		if (mode == ChangeMode.SET || mode == ChangeMode.ADD || mode == ChangeMode.REMOVE || mode == ChangeMode.RESET) {
//			return CollectionUtils.array(Number.class);
//		}
//		return null;
//	}
