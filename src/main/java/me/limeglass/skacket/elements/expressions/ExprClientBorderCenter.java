package me.limeglass.skacket.elements.expressions;

import org.bukkit.Location;
import org.bukkit.event.Event;

import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import me.limeglass.skacket.objects.ClientWorldBorder;

public class ExprClientBorderCenter extends SimplePropertyExpression<ClientWorldBorder, Location> {

	static {
		register(ExprClientBorderCenter.class, Location.class, "client [side] [world] border center [location]", "clientworldborders");
	}

	@Override
	public Class<? extends Location> getReturnType() {
		return Location.class;
	}

	@Override
	public Location convert(ClientWorldBorder border) {
		return border.getCenter();
	}

	@Override
	protected String getPropertyName() {
		return "client border center";
	}

	@Override
	public Class<?>[] acceptChange(ChangeMode mode) {
		return (mode == ChangeMode.SET || mode == ChangeMode.RESET) ? CollectionUtils.array(Location.class) : null;
	}

	@Override
	public void change(Event event, Object[] delta, ChangeMode mode) {
		if (getExpr() == null)
			return;
		ClientWorldBorder[] borders = getExpr().getAll(event);
		if (borders == null)
			return;
		if (mode == ChangeMode.RESET) {
			for (ClientWorldBorder border : borders)
				border.setCenter(border.getWorld().getWorldBorder().getCenter());
			return;
		}
		Location location = (Location) delta[0];
		for (ClientWorldBorder border : borders)
			border.setCenter(location);
	}

}
