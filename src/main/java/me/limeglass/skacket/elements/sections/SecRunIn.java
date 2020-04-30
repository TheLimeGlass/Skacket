package me.limeglass.skacket.elements.sections;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.util.Timespan;
import ch.njol.util.Kleenean;
import me.limeglass.skacket.sections.Section;

public class SecRunIn extends Section {

	static {
		Skript.registerCondition(SecRunIn.class, "run [(code|section)] in %timespan%");
	}

	private Expression<Timespan> timespan;

	@SuppressWarnings("unchecked")
	@Override
	protected boolean initalize(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		timespan = (Expression<Timespan>) expressions[0];
		if (!hasSection())
			Skript.error("The run code in timespan needs to have a section.");
		loadSection(false);
		return true;
	}

	@Override
	protected void execute(Event event) {
		Bukkit.getScheduler().runTaskLater(Skript.getInstance(), () -> runSection(event), timespan.getSingle(event).getTicks_i());
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		if (event == null)
			return "run code later";
		return "run code in " + timespan.toString(event, debug);
	}

}
