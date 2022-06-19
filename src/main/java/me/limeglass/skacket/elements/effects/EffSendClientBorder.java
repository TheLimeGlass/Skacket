package me.limeglass.skacket.elements.effects;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Name;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import me.limeglass.skacket.objects.ClientWorldBorder;

@Name("Send Client Border")
@Description("Send a border to a player. This effect is for 1.16.3 and lower. 1.16.4 and up use the set border effect.")
public class EffSendClientBorder extends Effect {

	static {
		Skript.registerEffect(EffSendClientBorder.class, "send client [side] [world] border %clientworldborder% to %players%", "set client [side] [world] border of %players% to %clientworldborder%");
	}

	private Expression<ClientWorldBorder> border;
	private Expression<Player> players;

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		players = (Expression<Player>) exprs[1 - matchedPattern];
		border = (Expression<ClientWorldBorder>) exprs[matchedPattern];
		return true;
	}

	@Override
	protected void execute(Event event) {
		if (players == null || border == null)
			return;
		Player[] players = this.players.getArray(event);
		ClientWorldBorder border = this.border.getSingle(event);
		if (border == null || players == null)
			return;
		border.sendInitalize(players);
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		if (event == null || debug)
			return "client border send";
		return "client border send to " + players.toString(event, debug);
	}

}
