package me.limeglass.skacket.elements.expressions;

import org.bukkit.event.Event;

import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import me.limeglass.skacket.objects.ClientWorldBorder;

public class ExprClientBorderSpeed extends SimplePropertyExpression<ClientWorldBorder, Number> {

	static {
		register(ExprClientBorderSpeed.class, Number.class, "client [side] [world] border speed", "clientworldborders");
	}

	@Override
	public Class<? extends Number> getReturnType() {
		return Number.class;
	}

	@Override
	public Number convert(ClientWorldBorder border) {
		return border.getSpeed();
	}

	@Override
	protected String getPropertyName() {
		return "client border speed";
	}

	@Override
	public Class<?>[] acceptChange(ChangeMode mode) {
		return (mode == ChangeMode.ADD || mode == ChangeMode.REMOVE || mode == ChangeMode.SET) ? CollectionUtils.array(Number.class) : null;
	}

	@Override
	public void change(Event event, Object[] delta, ChangeMode mode) {
		if (getExpr() == null)
			return;
		ClientWorldBorder[] borders = getExpr().getAll(event);
		if (borders == null)
			return;
		int speed = ((Number) delta[0]).intValue();
		switch (mode) {
			case ADD:
				for (ClientWorldBorder border : borders) {
					int existing = convert(border).intValue();
					border.setSpeed(existing + speed);
				}
				break;
			case REMOVE:
				for (ClientWorldBorder border : borders) {
					int existing = convert(border).intValue();
					if (existing - speed < 0)
						continue;
					border.setSpeed(existing - speed);
				}
				break;
			case SET:
				for (ClientWorldBorder border : borders)
					border.setSpeed(speed);
				break;
			default:
				break;
		}
	}

}
