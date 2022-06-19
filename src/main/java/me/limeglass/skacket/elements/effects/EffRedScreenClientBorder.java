package me.limeglass.skacket.elements.effects;

import java.util.concurrent.TimeUnit;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Name;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.util.Timespan;
import ch.njol.util.Kleenean;
import me.limeglass.skacket.objects.ClientWorldBorder;

@Name("Red Screen Effect")
@Description("Send the border warning red screen effect to someone. Time is required to reset it, otherwise it's forever.")
public class EffRedScreenClientBorder extends Effect {

	static {
		Skript.registerEffect(EffRedScreenClientBorder.class, "send red [world] [border] screen [effect] to %players% [for %-timespan%]");
	}

	private Expression<Timespan> timespan;
	private Expression<Player> players;

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		players = (Expression<Player>) exprs[0];
		timespan = (Expression<Timespan>) exprs[1];
		return true;
	}

	@Override
	protected void execute(Event event) {
		if (players == null)
			return;
		Player[] players = this.players.getArray(event);
		if (players == null)
			return;
		Timespan timespan = (this.timespan == null) ? null : this.timespan.getSingle(event);
		for (Player player : players) {
			ClientWorldBorder border = new ClientWorldBorder(player.getWorld());
			if (timespan == null)
				border.sendRedScreen(0, null, players);
			else
				border.sendRedScreen(timespan.getMilliSeconds(), TimeUnit.MILLISECONDS, players);
		}
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		if (event == null || debug)
			return "client border set";
		return "client border set to " + players.toString(event, debug);
	}

}
