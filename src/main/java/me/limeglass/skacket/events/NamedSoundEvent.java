package me.limeglass.skacket.events;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class NamedSoundEvent extends Event implements Cancellable {

	private static final HandlerList handlers = new HandlerList();
	private SoundCategory category;
	private final Player player;
	private Location location;
	private boolean cancelled;
	private float volume;
	private Sound sound;
	private float pitch;

	public NamedSoundEvent(Player player, Sound sound, SoundCategory category, Location location, float volume, float pitch) {
		this.category = category;
		this.location = location;
		this.volume = volume;
		this.player = player;
		this.sound = sound;
		this.pitch = pitch;
	}

	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	public Player getPlayer() {
		return player;
	}

	public Location getLocation() {
		return location;
	}

	public Sound getSound() {
		return sound;
	}

	public void setSound(Sound sound) {
		this.sound = sound;
	}

	public float getVolume() {
		return volume;
	}

	public void setVolume(float volume) {
		this.volume = volume;
	}

	public float getPitch() {
		return pitch;
	}

	public void setPitch(float pitch) {
		this.pitch = pitch;
	}

	public SoundCategory getSoundCategory() {
		return category;
	}

	public void setSoundCategory(SoundCategory category) {
		this.category = category;
	}

	public boolean isCancelled() {
		return this.cancelled;
	}

	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

}
