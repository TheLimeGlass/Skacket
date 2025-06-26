package me.limeglass.skacket.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class SteerVehicleEvent extends Event implements Cancellable {

	private static final HandlerList handlers = new HandlerList();
	private final Movement[] movements;
	private final Player player;
	private boolean cancelled;

	public enum Movement {
		LEFT,
		RIGHT,
		FORWARDS,
		BACKWARDS,
		JUMP,
		SNEAK,
		SPRINT;
	}

	public SteerVehicleEvent(Player player, Movement... movements) {
		super(true);
		this.movements = movements;
		this.player = player;
	}

	public Player getPlayer() {
		return player;
	}

	public Movement[] getMovements() {
		return movements;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

}
