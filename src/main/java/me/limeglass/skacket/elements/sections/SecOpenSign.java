package me.limeglass.skacket.elements.sections;

import java.util.Arrays;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.SignChangeEvent;
import org.eclipse.jdt.annotation.Nullable;

import ch.njol.skript.Skript;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.lang.EffectSection;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.Trigger;
import ch.njol.skript.lang.TriggerItem;
import ch.njol.skript.variables.Variables;
import ch.njol.util.Kleenean;
import me.limeglass.skacket.Skacket;

public class SecOpenSign extends EffectSection {

	static {
		Skript.registerSection(SecOpenSign.class, "open [a] sign [gui] to %players% [with [(text|lines)] %-strings%]");
	}

	private Expression<Player> players;

	@Nullable
	private Expression<String> lines;

	@Nullable
	private TriggerItem actualNext;

	@Nullable
	private Trigger trigger;

	@Override
	@SuppressWarnings("unchecked")
	public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, ParseResult parseResult, @Nullable SectionNode sectionNode, @Nullable List<TriggerItem> triggerItems) {
		players = (Expression<Player>) expressions[0];
		lines = (Expression<String>) expressions[1];
		if (hasSection())
			trigger = loadCode(sectionNode, "sign update", SignChangeEvent.class);
		return true;
	}

	@Override
	protected TriggerItem walk(Event event) {
		String[] text = lines != null ? lines.getArray(event) : new String[] {};
		Object localVariables = Variables.copyLocalVariables(event);
		Skacket.getInstance().getSignManager()
				.open(event, text, players.getArray(event))
				.onUpdate(update -> {
					if (!hasSection())
						return;
					// Copy local variables into the event.
					if (localVariables != null)
						Variables.setLocalVariables(update, localVariables);
					trigger.execute(update);
				});
		debug(event, false);
		return actualNext;
	}

	@Override
	public SecOpenSign setNext(@Nullable TriggerItem next) {
		actualNext = next;
		return this;
	}

	@Nullable
	public TriggerItem getActualNext() {
		return actualNext;
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		if (event == null)
			return "open sign";
		String text = lines != null ? " with lines " + Arrays.toString(lines.getArray(event)) : "";
		return "open sign to " + players.toString(event, debug) + text;
	}

}
