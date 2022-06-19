package me.limeglass.skacket.elements.effects;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import me.limeglass.skacket.objects.ClientWorldBorder;

public class EffSetClientBorder extends Effect {

	static {
		Skript.registerEffect(EffSetClientBorder.class, "set client [side] [world] border of %players% to [(size|radius|diameter)] %number% and [center [at] [location]] %location%");
	}

	private Expression<Location> center;
	private Expression<Player> players;
	private Expression<Number> size;

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		players = (Expression<Player>) exprs[0];
		size = (Expression<Number>) exprs[1];
		center = (Expression<Location>) exprs[2];
		return true;
	}

	@Override
	protected void execute(Event event) {
		if (players == null || size == null || center == null)
			return;
		Player[] players = this.players.getArray(event);
		Location center = this.center.getSingle(event);
		Number size = this.size.getSingle(event);
		if (center == null || size == null || players == null)
			return;
		double radius = size.doubleValue();
		for (Player player : players) {
			ClientWorldBorder border = new ClientWorldBorder(player.getWorld());
			border.setRadius(radius);
			border.setCenter(center);
			border.sendCenterSize(players);
		}
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		if (event == null || debug)
			return "client border set";
		return "client border set to " + players.toString(event, debug);
	}

}
