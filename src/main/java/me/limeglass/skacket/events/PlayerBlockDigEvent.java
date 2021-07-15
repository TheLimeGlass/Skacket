package me.limeglass.skacket.events;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockEvent;
import org.eclipse.jdt.annotation.NonNull;

import com.comphenix.protocol.wrappers.EnumWrappers.Direction;
import com.comphenix.protocol.wrappers.EnumWrappers.PlayerDigType;

/**
 * Called when a sign is changed by a player.
 * <p>
 * If a Sign Change event is cancelled, the sign will not be changed.
 * @author cloned from Bukkit
 */
public class PlayerBlockDigEvent extends BlockEvent implements Cancellable {

	private static final HandlerList handlers = new HandlerList();
	private final Direction direction;
	private final PlayerDigType type;
	private boolean cancel = false;
	private final Player player;

	public PlayerBlockDigEvent(Block block, Player player, PlayerDigType type, Direction direction) {
		super(block);
		this.direction = direction;
		this.player = player;
		this.type = type;
	}

	public PlayerDigType getDigType() {
		return type;
	}

	public Direction getDirection() {
		return direction;
	}

	public Player getPlayer() {
		return player;
	}

	@Override
	public boolean isCancelled() {
		return cancel;
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.cancel = cancel;
	}

	@NonNull
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	@NonNull
	public static HandlerList getHandlerList() {
		return handlers;
	}

}
