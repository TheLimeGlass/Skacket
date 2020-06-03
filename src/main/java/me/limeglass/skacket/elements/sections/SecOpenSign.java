package me.limeglass.skacket.elements.sections;

import java.util.Arrays;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.SignChangeEvent;
import org.eclipse.jdt.annotation.Nullable;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.variables.Variables;
import ch.njol.util.Kleenean;
import me.limeglass.skacket.Skacket;
import me.limeglass.skacket.sections.LazySection;

public class SecOpenSign extends LazySection {

	static {
		Skript.registerCondition(SecOpenSign.class, "open sign [gui] to %players% [with [(text|lines)] %-strings%]");
	}

	private Expression<Player> players;
	private Expression<String> lines;

	@SuppressWarnings("unchecked")
	@Override
	protected boolean initalize(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		players = (Expression<Player>) expressions[0];
		lines = (Expression<String>) expressions[1];
		if (hasSection())
			loadSection("sign update", false, SignChangeEvent.class);
		return true;
	}

	@Override
	protected void execute(Event event) {
		String[] text = lines != null ? lines.getArray(event) : new String[] {};
		Skacket.getInstance().getSignManager()
				.open(event, text, players.getArray(event))
				.onUpdate(update -> {
					if (!hasSection())
						return;
					// Copy local variables into the event.
					Object localVariables = Variables.removeLocals(event);
					Variables.setLocalVariables(update, localVariables);
					Variables.setLocalVariables(event, localVariables);
					runSection(update);
				});
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		if (event == null)
			return "open sign";
		String text = lines != null ? " with lines " + Arrays.toString(lines.getArray(event)) : "";
		return "open sign to " + players.toString(event, debug) + text;
	}

}
