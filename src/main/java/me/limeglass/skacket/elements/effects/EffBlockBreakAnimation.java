package me.limeglass.skacket.elements.effects;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

import com.comphenix.protocol.wrappers.BlockPosition;
import com.google.common.collect.Sets;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Name;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import me.limeglass.skacket.wrappers.WrapperPlayServerBlockBreakAnimation;

@Name("Block Break Animation")
@Description("Sends a block break animation at a stage to players. 0–9 are the displayable destroy stage any other number like -1 will reset it.")
public class EffBlockBreakAnimation extends Effect {

	static {
		Skript.registerEffect(EffBlockBreakAnimation.class, "show [%-players%] [a] block break animation at %locations% with stage %number%",
				// Below are RandomSk syntax clones.
				"show [a] block break [animation] stage %number% at %locations% [for %-players%]",
				"show stacking block break [animation] stage %number% at %locations% [for %-players%]");
	}

	private Expression<Location> locations;

	@Nullable
	private Expression<Player> players;

	private Expression<Number> stage;

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		if (matchedPattern == 0) {
			players = (Expression<Player>) exprs[0];
			locations = (Expression<Location>) exprs[1];
			stage = (Expression<Number>) exprs[2];
		} else {
			stage = (Expression<Number>) exprs[0];
			locations = (Expression<Location>) exprs[1];
			players = (Expression<Player>) exprs[2];
		}
		return true;
	}

	@Override
	protected void execute(Event event) {
		Set<Player> receivers = new HashSet<>();
		if (players == null)
			receivers.addAll(Bukkit.getOnlinePlayers());
		else
			receivers.addAll(Sets.newHashSet(players.getArray(event)));
		int stage = this.stage.getSingle(event).intValue();
		for (Location location : locations.getArray(event)) {
			for (Player receiver : receivers) {
				WrapperPlayServerBlockBreakAnimation packet = new WrapperPlayServerBlockBreakAnimation();
				packet.setLocation(new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ()));
				packet.setEntityID(receiver.getEntityId());
				packet.setDestroyStage(stage);
				packet.sendPacket(receiver);
			}
		}
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		if (event == null || debug)
			return "block break animation";
		return "block break animation to " + players.toString(event, debug) + " at stage " + stage.toString(event, debug) + " at " + locations.toString(event, debug);
	}

}
