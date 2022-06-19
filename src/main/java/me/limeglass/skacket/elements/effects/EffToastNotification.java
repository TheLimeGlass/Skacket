package me.limeglass.skacket.elements.effects;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.eclipse.jdt.annotation.Nullable;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import eu.endercentral.crazy_advancements.advancement.AdvancementDisplay.AdvancementFrame;
import eu.endercentral.crazy_advancements.advancement.ToastNotification;

public class EffToastNotification extends Effect {

	static {
		Skript.registerEffect(EffToastNotification.class, "send %players% [advancement] toast [notification] %string% with [icon] %itemstack% and frame %advancementframe%");
	}

	private Expression<AdvancementFrame> frame;
	private Expression<String> description;
	private Expression<ItemStack> icon;
	private Expression<Player> players;

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		players = (Expression<Player>) exprs[0];
		description = (Expression<String>) exprs[1];
		icon = (Expression<ItemStack>) exprs[2];
		frame = (Expression<AdvancementFrame>) exprs[3];
		return true;
	}

	@Override
	protected void execute(Event event) {
		ToastNotification notification = new ToastNotification(icon.getSingle(event), description.getSingle(event), frame.getSingle(event));
		for (Player player : players.getArray(event))
			notification.send(player);
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		if (event == null || debug)
			return "toast notification";
		return "toast notification to " + players.toString(event, debug) + " description " + description.toString(event, debug);
	}

}
