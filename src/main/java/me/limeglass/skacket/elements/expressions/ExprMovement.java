package me.limeglass.skacket.elements.expressions;

import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.parser.ParserInstance;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.skript.log.ErrorQuality;
import ch.njol.util.Kleenean;
import me.limeglass.skacket.events.SteerVehicleEvent;
import me.limeglass.skacket.events.SteerVehicleEvent.Movement;

public class ExprMovement extends SimpleExpression<Movement> {

	static {
		Skript.registerExpression(ExprMovement.class, Movement.class, ExpressionType.SIMPLE, "[the] movements");
	}

	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		if (!ParserInstance.get().isCurrentEvent(SteerVehicleEvent.class)) {
			Skript.error("Cannot use 'movements' outside of the steer vehicle event", ErrorQuality.SEMANTIC_ERROR);
			return false;
		}
		return true;
	}

	@Override
	@Nullable
	protected Movement[] get(Event event) {
		return ((SteerVehicleEvent)event).getMovements();
	}

	@Override
	public boolean isSingle() {
		return false;
	}

	@Override
	public Class<? extends Movement> getReturnType() {
		return Movement.class;
	}

	@Override
	public String toString(@Nullable Event e, boolean debug) {
		return "movements";
	}

}
