package me.limeglass.skacket.events;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.eclipse.jdt.annotation.Nullable;

public class AnvilGUIEvent extends PlayerEvent {

	private static final HandlerList handlers = new HandlerList();
	private final String text;
	private final Click click;

	public AnvilGUIEvent(Player player, @Nullable String text, Click click) {
		super(player);
		this.text = text;
		this.click = click;
	}

	public enum Click {
		LEFT, RIGHT, COMPLETE;
	}

	@Nullable
	public String getText() {
		return text;
	}

	public Click getClickType() {
		return click;
	}

	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

}
