package me.limeglass.skacket.elements.events;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

import com.comphenix.protocol.wrappers.EnumWrappers.PlayerDigType;

import ch.njol.skript.Skript;
import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Direction;
import ch.njol.skript.util.Getter;
import ch.njol.util.Checker;
import me.limeglass.skacket.events.PlayerBlockDigEvent;

public class EvtPlayerDig extends SkriptEvent {

	static {
		Skript.registerEvent("player dig", EvtPlayerDig.class, PlayerBlockDigEvent.class, "[player] [(1¦start|2¦stop|3¦abandon)] [block] (min(e|ing)|dig[ging]) [(for|of) %itemtypes%]", "player release (use item|(right mouse button|right[(-| )]click))")
				.description("Called when a sound is to be played to the client.");
		EventValues.registerEventValue(PlayerBlockDigEvent.class, PlayerDigType.class, new Getter<PlayerDigType, PlayerBlockDigEvent>() {
			@Nullable
			public PlayerDigType get(PlayerBlockDigEvent event) {
				return event.getDigType();
			}
		}, 0);
		EventValues.registerEventValue(PlayerBlockDigEvent.class, Block.class, new Getter<Block, PlayerBlockDigEvent>() {
			@Nullable
			public Block get(PlayerBlockDigEvent event) {
				return event.getBlock();
			}
		}, 0);
		EventValues.registerEventValue(PlayerBlockDigEvent.class, Player.class, new Getter<Player, PlayerBlockDigEvent>() {
			@Nullable
			public Player get(PlayerBlockDigEvent event) {
				return event.getPlayer();
			}
		}, 0);
		EventValues.registerEventValue(PlayerBlockDigEvent.class, Direction.class, new Getter<Direction, PlayerBlockDigEvent>() {
			@Nullable
			public Direction get(PlayerBlockDigEvent event) {
				com.comphenix.protocol.wrappers.EnumWrappers.Direction direction = event.getDirection();
				if (direction == null)
					return null;
				BlockFace face = BlockFace.valueOf(direction.name());
				return new Direction(new double[] {face.getModX(), face.getModY(), face.getModZ()});
			}
		}, 0);
	}

	@Nullable
	private Literal<ItemType> items;
	private boolean release;
	private int type;

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Literal<?>[] args, int matchedPattern, ParseResult parseResult) {
		items = (Literal<ItemType>) args[0];
		release = matchedPattern == 1;
		type = matchedPattern;
		return true;
	}

	@Override
	public boolean check(Event event) {
		PlayerBlockDigEvent digEvent = (PlayerBlockDigEvent) event;
		if (release)
			return digEvent.getDigType() == PlayerDigType.RELEASE_USE_ITEM;
		if (digEvent.getDigType() != PlayerDigType.START_DESTROY_BLOCK && type == 1)
			return false;
		if (digEvent.getDigType() != PlayerDigType.STOP_DESTROY_BLOCK && type == 2)
			return false;
		if (digEvent.getDigType() != PlayerDigType.ABORT_DESTROY_BLOCK && type == 3)
			return false;
		Material material = digEvent.getBlock().getType();
		return items.check(event, new Checker<ItemType>() {
			@Override
			public boolean check(ItemType item) {
				return material == item.getMaterial();
			}
		});
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		if (event == null || debug)
			return "on player block dig";
		return "on player block dig " + (items != null && event != null ? " " + items.toString(event, debug) : "");
	}

}
