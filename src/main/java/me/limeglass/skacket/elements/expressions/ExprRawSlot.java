package me.limeglass.skacket.elements.expressions;

import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.eclipse.jdt.annotation.Nullable;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.parser.ParserInstance;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.skript.log.ErrorQuality;
import ch.njol.util.Kleenean;

public class ExprRawSlot extends SimpleExpression<Number> {

	static {
		Skript.registerExpression(ExprRawSlot.class, Number.class, ExpressionType.SIMPLE, "[the] [clicked] raw slot");
	}

	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		if (!ParserInstance.get().isCurrentEvent(InventoryClickEvent.class)) {
			Skript.error("Cannot use 'raw slow' outside of the inventory click event", ErrorQuality.SEMANTIC_ERROR);
			return false;
		}
		return true;
	}

	@Override
	@Nullable
	protected Number[] get(Event event) {
		return new Number[] {((InventoryClickEvent)event).getRawSlot()};
	}

	@Override
	public boolean isSingle() {
		return true;
	}

	@Override
	public Class<? extends Number> getReturnType() {
		return Number.class;
	}

	@Override
	public String toString(@Nullable Event e, boolean debug) {
		return "raw slot";
	}

}
