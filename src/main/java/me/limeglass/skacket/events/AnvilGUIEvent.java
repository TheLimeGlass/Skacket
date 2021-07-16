package me.limeglass.skacket.events;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

import net.wesjd.anvilgui.AnvilGUI.Response;

public class AnvilGUIEvent extends PlayerEvent {

	private static final HandlerList handlers = new HandlerList();
	private Response response = Response.close();
	private final String text;

	public AnvilGUIEvent(Player player, String text) {
		super(player);
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public Response getResponse() {
		return response;
	}

	public void setResponse(Response response) {
		this.response = response;
	}

	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

}
