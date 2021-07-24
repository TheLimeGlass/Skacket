package me.limeglass.skacket.elements.conditions;

import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

import ch.njol.skript.Skript;
import ch.njol.skript.expressions.base.EventValueExpression;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import me.limeglass.skacket.events.AnvilGUIEvent.Click;

public class CondAnvilClick extends Condition {

	static {
		Skript.registerCondition(CondAnvilClick.class, "[the] anvil click[ed] [type] (was|is)(0¦|1¦n('|o)t) %anvilclicks%");
	}

	private Expression<Click> click, expected;

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		click = new EventValueExpression<>(Click.class);
		expected = (Expression<Click>) exprs[0];
		setNegated(parseResult.mark == 1);
		return ((EventValueExpression<Click>) click).init();
	}

	@Override
	public boolean check(Event event) {
		Click click = this.click.getSingle(event);
		if (click == null)
			return false;
		return expected.check(event,
				other -> click == other,
				isNegated());
	}

	@Override
	public String toString(@Nullable Event event, final boolean debug) {
		if (event == null)
			return "anvil click";
		return "anvil click was" + (isNegated() ? " not " : "") + expected.toString(event, debug);
	}

}
