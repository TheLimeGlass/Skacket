package me.limeglass.skacket.elements.expressions;

import org.bukkit.World;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import me.limeglass.skacket.objects.ClientWorldBorder;

public class ExprClientWorldBorder extends SimpleExpression<ClientWorldBorder> {

	static {
		Skript.registerExpression(ExprClientWorldBorder.class, ClientWorldBorder.class, ExpressionType.SIMPLE, "new client [side] [world] border (with|for) %worlds%");
	}

	private Expression<World> worlds;

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		worlds = (Expression<World>) exprs[0];
		return true;
	}

	@Override
	@Nullable
	protected ClientWorldBorder[] get(Event event) {
		return CollectionUtils.array(new ClientWorldBorder(worlds.getSingle(event)));
	}

	@Override
	public boolean isSingle() {
		return worlds.isSingle();
	}

	@Override
	public Class<? extends ClientWorldBorder> getReturnType() {
		return ClientWorldBorder.class;
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		if (debug || worlds == null)
			return "new client world border";
		return "new client world border for worlds: " + worlds.toString(event, debug);
	}

}
