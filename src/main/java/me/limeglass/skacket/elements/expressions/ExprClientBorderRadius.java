package me.limeglass.skacket.elements.expressions;

import org.bukkit.event.Event;

import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import me.limeglass.skacket.objects.ClientWorldBorder;

public class ExprClientBorderRadius extends SimplePropertyExpression<ClientWorldBorder, Number> {

	static {
		register(ExprClientBorderRadius.class, Number.class, "client [side] [world] border radius", "clientworldborders");
	}

	@Override
	public Class<? extends Number> getReturnType() {
		return Number.class;
	}

	@Override
	public Number convert(ClientWorldBorder border) {
		return border.getRadius();
	}

	@Override
	protected String getPropertyName() {
		return "client border radius";
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
		int radius = ((Number) delta[0]).intValue();
		switch (mode) {
			case ADD:
				for (ClientWorldBorder border : borders) {
					int existing = convert(border).intValue();
					border.setRadius(existing + radius);
				}
				break;
			case REMOVE:
				for (ClientWorldBorder border : borders) {
					int existing = convert(border).intValue();
					if (existing - radius < 0)
						continue;
					border.setRadius(existing - radius);
				}
				break;
			case SET:
				for (ClientWorldBorder border : borders)
					border.setRadius(radius);
				break;
			default:
				break;
		}
	}

}
