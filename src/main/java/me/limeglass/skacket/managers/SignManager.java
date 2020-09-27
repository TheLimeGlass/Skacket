package me.limeglass.skacket.managers;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.util.Consumer;

import com.comphenix.protocol.wrappers.BlockPosition;
import com.google.common.collect.Sets;

import me.limeglass.skacket.Skacket;
import me.limeglass.skacket.wrappers.WrapperPlayServerOpenSignEntity;

public class SignManager implements Listener {

	private final Set<SignEditor> signs = new HashSet<>();

	public SignManager(Skacket instance) {
		Bukkit.getPluginManager().registerEvents(this, instance);
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		getSignFor(player).ifPresent(sign -> {
			Set<Player> players = sign.getPlayers();
			players.remove(player);
			if (players.isEmpty())
				signs.remove(sign);
		});
	}

	public SignEditor open(Event event, String[] text, Player... players) {
		Set<Player> set = Sets.newHashSet(players);
		// Remove duplicate entries, a player can't have two signs open.
		for (Iterator<SignEditor> iterator = signs.iterator(); iterator.hasNext();)
			iterator.next().getPlayers().removeAll(set);
		SignEditor sign = new SignEditor(event, text, set);
		// Open packet GUI
		for (Player player : players) {
			Location location = player.getLocation();
			location.setY(255);
			sign.setPreviousBlockData(player, location.getBlock().getBlockData());
			player.sendBlockChange(location, materialAttempt("SIGN_POST", "OAK_SIGN").createBlockData());
			if (text != null && text.length <= 4 && text.length > 0)
				player.sendSignChange(location, text);
	        WrapperPlayServerOpenSignEntity packet = new WrapperPlayServerOpenSignEntity();
	        packet.setLocation(new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ()));
	        packet.sendPacket(player);
		}
		signs.add(sign);
		return sign;
	}

	private Material materialAttempt(String attempt, String fallback) {
		Material material = null;
		try {
			material = Material.valueOf(attempt.toUpperCase());
		} catch (Exception e) {
			try {
				material = Material.valueOf(fallback);
			} catch (Exception e1) {}
		}
		if (material == null)
			material = Material.CHEST;
		return material;
	}

	public Optional<SignEditor> getSignFor(Player player) {
		return signs.stream().filter(sign -> sign.getPlayers().contains(player)).findFirst();
	}

	public Optional<SignEditor> getSignFor(Event event) {
		return signs.stream().filter(sign -> sign.getEvent().equals(event)).findFirst();
	}

	public Set<SignEditor> getSigns() {
		return signs;
	}

	public class SignEditor {

		private Map<Player, BlockData> previousBlockData = new HashMap<>();
		private final Set<Player> players = new HashSet<>();
		private Consumer<SignChangeEvent> consumer;
		private final String[] lines;
		private final Event event;

		public SignEditor(Event event, String[] lines, Collection<Player> players) {
			this.players.addAll(players);
			this.lines = lines;
			this.event = event;
		}

		public SignEditor onUpdate(Consumer<SignChangeEvent> consumer) {
			this.consumer = consumer;
			return this;
		}

		public void setPreviousBlockData(Player player, BlockData data) {
			previousBlockData.put(player, data);
		}

		public Optional<BlockData> getPreviousBlockData(Player player) {
			return Optional.ofNullable(previousBlockData.get(player));
		}

		public void accept(SignChangeEvent event) {
			consumer.accept(event);
			players.remove(event.getPlayer());
			if (players.isEmpty())
				signs.remove(this);
		}

		public Set<Player> getPlayers() {
			return players;
		}

		public String[] getLines() {
			return lines;
		}

		public Event getEvent() {
			return event;
		}

	}

}
