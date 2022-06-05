package me.limeglass.skacket.elements.effects;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

import com.google.common.collect.Sets;
import com.sitrica.glowing.GlowingAPI;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import me.limeglass.skacket.Skacket;

public class EffGlowing extends Effect {

	static {
		Skript.registerEffect(EffGlowing.class, "set client glowing of %livingentities% to %boolean% [(for|to) %-players%]");
	}

	private Expression<LivingEntity> entities;
	private Expression<Boolean> setting;
	private Expression<Player> players;

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		entities = (Expression<LivingEntity>) exprs[0];
		setting = (Expression<Boolean>) exprs[1];
		players = (Expression<Player>) exprs[2];
		return true;
	}

	@Override
	protected void execute(Event event) {
		Set<Player> send = new HashSet<>();
		if (players == null)
			send.addAll(Bukkit.getOnlinePlayers());
		else
			send.addAll(Sets.newHashSet(players.getArray(event)));
		boolean setting = this.setting.getSingle(event);
		GlowingAPI instance = Skacket.getInstance().getGlowingAPI();
		for (LivingEntity entity : entities.getArray(event)) {
			if (setting) {
				instance.setGlowing(entity, send.toArray(new Player[send.size()]));
			} else {
				instance.stopGlowing(entity, send.toArray(new Player[send.size()]));
			}
		}
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		if (event == null)
			return "client glowing";
		if (players == null)
			return "client glowing of " + entities.toString(event, debug) + " for all players";
		return "client glowing of " + entities.toString(event, debug) + " for " + players.toString(event, debug);
	}

}
