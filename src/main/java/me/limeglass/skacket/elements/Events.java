package me.limeglass.skacket.elements;

import java.util.Locale;

import org.bukkit.Location;
import org.bukkit.SoundCategory;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.eclipse.jdt.annotation.Nullable;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import me.limeglass.skacket.events.AnvilGUIEvent;
import me.limeglass.skacket.events.AnvilGUIEvent.Click;
import me.limeglass.skacket.events.NamedSoundEvent;
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

		Skript.registerEvent("anvil gui use", SimpleEvent.class, AnvilGUIEvent.class, "anvil gui use");
		// Anvils
		EventValues.registerEventValue(AnvilGUIEvent.class, String.class, new Getter<String, AnvilGUIEvent>() {
			@Override
			@Nullable
			public String get(AnvilGUIEvent event) {
				return event.getText();
			}
		}, 0);
		EventValues.registerEventValue(AnvilGUIEvent.class, Player.class, new Getter<Player, AnvilGUIEvent>() {
			@Override
			@Nullable
			public Player get(AnvilGUIEvent event) {
				return event.getPlayer();
			}
		}, 0);
		EventValues.registerEventValue(AnvilGUIEvent.class, Number.class, new Getter<Number, AnvilGUIEvent>() {
			@Override
			@Nullable
			public Number get(AnvilGUIEvent event) {
				return event.getClickedSlot();
			}
		}, 0);
		EventValues.registerEventValue(AnvilGUIEvent.class, Click.class, new Getter<Click, AnvilGUIEvent>() {
			@Override
			@Nullable
			public Click get(AnvilGUIEvent event) {
				return event.getClickType();
			}
		}, 0);

		Skript.registerEvent("named sound", SimpleEvent.class, NamedSoundEvent.class, "[named] sound [(trigger|play)]", "player hear[ing] sound[s]")
				.description("Called when a sound is to be played to the client.")
				.since("1.0.13");
		EventValues.registerEventValue(NamedSoundEvent.class, Player.class, new Getter<Player, NamedSoundEvent>() {
			@Nullable
			public Player get(NamedSoundEvent event) {
				return event.getPlayer();
			}
		}, 0);
		EventValues.registerEventValue(NamedSoundEvent.class, Location.class, new Getter<Location, NamedSoundEvent>() {
			@Nullable
			public Location get(NamedSoundEvent event) {
				return event.getLocation();
			}
		}, 0);
		EventValues.registerEventValue(NamedSoundEvent.class, Float.class, new Getter<Float, NamedSoundEvent>() {
			@Nullable
			public Float get(NamedSoundEvent event) {
				return event.getPitch();
			}
		}, 0);
		EventValues.registerEventValue(NamedSoundEvent.class, Long.class, new Getter<Long, NamedSoundEvent>() {
			@Nullable
			public Long get(NamedSoundEvent event) {
				return event.getSeed();
			}
		}, 0);
		EventValues.registerEventValue(NamedSoundEvent.class, String.class, new Getter<String, NamedSoundEvent>() {
			@Nullable
			public String get(NamedSoundEvent event) {
				return event.getSound().name().replaceAll("_", " ").toLowerCase(Locale.ENGLISH);
			}
		}, 0);
		EventValues.registerEventValue(NamedSoundEvent.class, SoundCategory.class, new Getter<SoundCategory, NamedSoundEvent>() {
			@Nullable
			public SoundCategory get(NamedSoundEvent event) {
				return event.getSoundCategory();
			}
		}, 0);
	}

}
