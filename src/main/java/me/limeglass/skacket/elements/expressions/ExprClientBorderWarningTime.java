package me.limeglass.skacket.elements.expressions;

import org.bukkit.event.Event;

import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.skript.util.Timespan;
import ch.njol.util.coll.CollectionUtils;
import me.limeglass.skacket.objects.ClientWorldBorder;

public class ExprClientBorderWarningTime extends SimplePropertyExpression<ClientWorldBorder, Timespan> {

	static {
		register(ExprClientBorderWarningTime.class, Timespan.class, "client [side] [world] border warning [time]", "clientworldborders");
	}

	@Override
	public Class<? extends Timespan> getReturnType() {
		return Timespan.class;
	}

	@Override
	public Timespan convert(ClientWorldBorder border) {
		return new Timespan(border.getWarningTime() * 1000);
	}

	@Override
	protected String getPropertyName() {
		return "client border warning time";
	}

	@Override
	public Class<?>[] acceptChange(ChangeMode mode) {
		return (mode != ChangeMode.REMOVE_ALL) ? CollectionUtils.array(Timespan.class) : null;
	}

	@Override
	public void change(Event event, Object[] delta, ChangeMode mode) {
		if (getExpr() == null)
			return;
		ClientWorldBorder[] borders = getExpr().getAll(event);
		if (borders == null)
			return;
		Timespan time = (Timespan) delta[0];
		switch (mode) {
			case ADD:
				for (ClientWorldBorder border : borders) {
					Timespan existing = convert(border);
					border.setWarningTime((int) (existing.getMilliSeconds() + time.getMilliSeconds()));
				}
				break;
			case REMOVE:
				for (ClientWorldBorder border : borders) {
					Timespan existing = convert(border);
					if (existing.getMilliSeconds() - time.getMilliSeconds() < 0)
						continue;
					border.setWarningTime((int) (existing.getMilliSeconds() - time.getMilliSeconds()));
				}
				break;
			case DELETE:
			case RESET:
				for (ClientWorldBorder border : borders)
					border.setWarningTime(border.getWorld().getWorldBorder().getWarningTime());
				break;
			case SET:
				for (ClientWorldBorder border : borders)
					border.setWarningTime((int)time.getMilliSeconds());
				break;
			default:
				break;
		}
	}

}
