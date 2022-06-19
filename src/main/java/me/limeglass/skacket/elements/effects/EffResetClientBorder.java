package me.limeglass.skacket.elements.effects;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

import ch.njol.skript.doc.NoDoc;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import me.limeglass.skacket.objects.ClientWorldBorder;

@NoDoc
public class EffResetClientBorder extends Effect {

	static {
		//Skript.registerEffect(EffResetClientBorder.class, "reset client [side] [world] border of %players%");
	}

	private Expression<Player> players;

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		players = (Expression<Player>) exprs[0];
		return true;
	}

	@Override
	protected void execute(Event event) {
		if (players == null)
			return;
		Player[] players = this.players.getArray(event);
		if (players == null)
			return;
		for (Player player : players) {
			ClientWorldBorder border = new ClientWorldBorder(player.getWorld());
			border.reset(players);
		}
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		if (event == null)
			return "client border reset";
		return "client border reset to " + players.toString(event, debug);
	}

}
