package me.limeglass.skacket.elements.expressions;

import org.bukkit.event.Event;

import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import me.limeglass.skacket.objects.ClientWorldBorder;

public class ExprClientBorderWarningDistance extends SimplePropertyExpression<ClientWorldBorder, Number> {

	static {
		register(ExprClientBorderWarningDistance.class, Number.class, "client [side] [world] border warning [block] distance", "clientworldborders");
	}

	@Override
	public Class<? extends Number> getReturnType() {
		return Number.class;
	}

	@Override
	public Number convert(ClientWorldBorder border) {
		return border.getWarningDistance();
	}

	@Override
	protected String getPropertyName() {
		return "client border warning distance";
	}

	@Override
	public Class<?>[] acceptChange(ChangeMode mode) {
		return (mode != ChangeMode.REMOVE_ALL) ? CollectionUtils.array(Number.class) : null;
	}

	@Override
	public void change(Event event, Object[] delta, ChangeMode mode) {
		if (getExpr() == null)
			return;
		ClientWorldBorder[] borders = getExpr().getAll(event);
		if (borders == null)
			return;
		int distance = ((Number) delta[0]).intValue();
		switch (mode) {
			case ADD:
				for (ClientWorldBorder border : borders) {
					int existing = convert(border).intValue();
					border.setWarningDistance(existing + distance);
				}
				break;
			case REMOVE:
				for (ClientWorldBorder border : borders) {
					int existing = convert(border).intValue();
					if (existing - distance < 0)
						continue;
					border.setWarningDistance(existing - distance);
				}
				break;
			case DELETE:
			case RESET:
				for (ClientWorldBorder border : borders)
					border.setWarningDistance(border.getWorld().getWorldBorder().getWarningDistance());
				break;
			case SET:
				for (ClientWorldBorder border : borders)
					border.setWarningDistance(distance);
				break;
			default:
				break;
		}
	}

}
