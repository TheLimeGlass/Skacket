package me.limeglass.skacket.elements.events;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
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
import me.limeglass.skacket.events.NamedSoundEvent;

public class EvtNamedSound extends SkriptEvent {

	static {
		Skript.registerEvent("named sound", EvtNamedSound.class, NamedSoundEvent.class, "[named] sound [%sounds%] [(trigger|play)]", "player hear[ing] sound [%sounds%]")
				.description("Called when a sound is to be played to the client.");
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
		EventValues.registerEventValue(NamedSoundEvent.class, Sound.class, new Getter<Sound, NamedSoundEvent>() {
			@Nullable
			public Sound get(NamedSoundEvent event) {
				return event.getSound();
			}
		}, 0);
		EventValues.registerEventValue(NamedSoundEvent.class, SoundCategory.class, new Getter<SoundCategory, NamedSoundEvent>() {
			@Nullable
			public SoundCategory get(NamedSoundEvent event) {
				return event.getSoundCategory();
			}
		}, 0);
	}

	@Nullable
	private Literal<Sound> sounds;

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Literal<?>[] args, int matchedPattern, ParseResult parseResult) {
		sounds = (Literal<Sound>) args[0];
		return true;
	}

	@Override
	public boolean check(Event event) {
		if (sounds == null)
			return true;
		return sounds.check(event, new Checker<Sound>() {
			@Override
			public boolean check(Sound sound) {
				return ((NamedSoundEvent) event).getSound() == sound;
			}
		});
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return "on sound" + (sounds != null && event != null ? " " + sounds.toString(event, debug) : "");
	}

}
