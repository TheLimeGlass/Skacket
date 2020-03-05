package me.limeglass.skacket.elements.events;

import java.util.Locale;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import ch.njol.util.Checker;
import me.limeglass.skacket.events.SteerVehicleEvent;
import me.limeglass.skacket.events.SteerVehicleEvent.Movement;

public class EvtSteerVehicle extends SkriptEvent {

	static {
		Skript.registerEvent("steer vehicle", EvtSteerVehicle.class, SteerVehicleEvent.class, "(steering [vehicle] [%movements%]|vehicle steer[ing])")
				.description("Called when a player attempts to move the entity they're riding.")
				.examples("on steering vehicle left or right:");
		EventValues.registerEventValue(SteerVehicleEvent.class, Player.class, new Getter<Player, SteerVehicleEvent>() {
			@Override
			@Nullable
			public Player get(SteerVehicleEvent event) {
				return event.getPlayer();
			}
		}, 0);
	}

	@Nullable
	private Literal<Movement> movements;

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Literal<?>[] args, int matchedPattern, ParseResult parseResult) {
		movements = (Literal<Movement>) args[0];
		return true;
	}

	@Override
	public boolean check(Event event) {
		if (movements == null)
			return true;
		return movements.check(event, new Checker<Movement>() {
			@Override
			public boolean check(Movement check) {
				for (Movement movement : ((SteerVehicleEvent) event).getMovements()) {
					if (movement == check)
						return true;
				}
				return false;
			}
		});
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return "on steering vehicle" + (movements != null ? " " + movements.toString().toLowerCase(Locale.ENGLISH) : "");
	}

}
