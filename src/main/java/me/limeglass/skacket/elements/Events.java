package me.limeglass.skacket.elements;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.eclipse.jdt.annotation.Nullable;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import me.limeglass.skacket.events.ServerSignChangeEvent;

public class Events {

	static {
		Skript.registerEvent("server sign change", SimpleEvent.class, ServerSignChangeEvent.class, "server [sending] sign change")
				.description("Called when the server sends a sign change event to a player.", 
						"Useful for sending lines on a sign to exact players, or permission based to see text on a sign.")
				.examples("on server sending sign change:", 
						"\tplayer doesn't have permission \"vip.sign.see\"",
						"\tlocation of block is {signs::vipEvent}",
						"\tcancel event");
		EventValues.registerEventValue(ServerSignChangeEvent.class, Player.class, new Getter<Player, ServerSignChangeEvent>() {
			@Override
			@Nullable
			public Player get(ServerSignChangeEvent event) {
				return event.getPlayer();
			}
		}, 0);
		EventValues.registerEventValue(ServerSignChangeEvent.class, Block.class, new Getter<Block, ServerSignChangeEvent>() {
			@Override
			@Nullable
			public Block get(ServerSignChangeEvent event) {
				return event.getBlock();
			}
		}, 0);
	}

}
