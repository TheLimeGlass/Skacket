package me.limeglass.skacket.elements.expressions;

import org.bukkit.event.Event;

import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import me.limeglass.skacket.objects.ClientWorldBorder;

public class ExprClientBorderPortalTeleportBoundary extends SimplePropertyExpression<ClientWorldBorder, Number> {

	static {
		register(ExprClientBorderPortalTeleportBoundary.class, Number.class, "client [side] [world] border [portal] teleport boundary", "clientworldborders");
	}

	@Override
	public Class<? extends Number> getReturnType() {
		return Number.class;
	}

	@Override
	public Number convert(ClientWorldBorder border) {
		return border.getPortalTeleportBoundary();
	}

	@Override
	protected String getPropertyName() {
		return "client border teleport boundary";
	}

	@Override
	public Class<?>[] acceptChange(ChangeMode mode) {
		return (mode != ChangeMode.REMOVE_ALL && mode != ChangeMode.RESET && mode != ChangeMode.DELETE) ? CollectionUtils.array(Number.class) : null;
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
					border.setPortalTeleportBoundary(existing + distance);
				}
				break;
			case REMOVE:
				for (ClientWorldBorder border : borders) {
					int existing = convert(border).intValue();
					if (existing - distance < 0)
						continue;
					border.setPortalTeleportBoundary(existing - distance);
				}
				break;
			case SET:
				for (ClientWorldBorder border : borders)
					border.setPortalTeleportBoundary(distance);
				break;
			default:
				break;
		}
	}

}
