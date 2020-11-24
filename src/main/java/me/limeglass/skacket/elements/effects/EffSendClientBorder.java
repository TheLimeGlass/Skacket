package me.limeglass.skacket.elements.effects;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import me.limeglass.skacket.objects.ClientWorldBorder;

public class EffSendClientBorder extends Effect {

	static {
		Skript.registerEffect(EffSendClientBorder.class, "send client [side] [world] border %clientworldborder% to %players%", "set client [side] [world] border of %players% to %clientworldborder%");
	}

	private Expression<ClientWorldBorder> border;
	private Expression<Player> players;

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		players = (Expression<Player>) exprs[0 ^ matchedPattern];
		border = (Expression<ClientWorldBorder>) exprs[matchedPattern];
		return true;
	}

	@Override
	protected void execute(Event event) {
		if (players == null || border == null)
			return;
		Player[] players = this.players.getArray(event);
		border.getSingle(event).send(players);
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		if (event == null)
			return "client border send";
		return "client border send to " + players.toString(event, debug);
	}

}
