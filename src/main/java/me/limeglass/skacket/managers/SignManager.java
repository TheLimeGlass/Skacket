package me.limeglass.skacket.managers;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.TileState;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.sign.Side;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.util.Consumer;
import org.jetbrains.annotations.Nullable;

import com.comphenix.protocol.wrappers.BlockPosition;
import com.google.common.collect.Sets;

import ch.njol.skript.Skript;
import ch.njol.skript.util.chat.BungeeConverter;
import ch.njol.skript.util.chat.ChatMessages;
import me.limeglass.skacket.Skacket;
import me.limeglass.skacket.wrappers.WrapperPlayServerOpenSignEntity;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.bungeecord.BungeeComponentSerializer;

public class SignManager implements Listener {

	private static final boolean PAPER_SIGNS = Skript.methodExists(Player.class, "sendBlockUpdate", Location.class, TileState.class);
	private static final boolean oldAF = !Skript.methodExists(Block.class, "getBlockData");
	private final Set<SignEditor> signs = new HashSet<>();

	@Nullable
	private static BungeeComponentSerializer serializer;

	static {
		if (Skript.classExists("net.kyori.adventure.text.Component"))
			serializer = BungeeComponentSerializer.get();
	}

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

	public SignEditor open(Event event, String[] lines, Player... players) {
		Set<Player> set = Sets.newHashSet(players);
		// Remove duplicate entries, a player can't have two signs open.
		for (Iterator<SignEditor> iterator = signs.iterator(); iterator.hasNext();)
			iterator.next().getPlayers().removeAll(set);
		SignEditor signEditor = new SignEditor(event, lines, set);
		// Open packet GUI
		for (Player player : players) {
			Location location = player.getLocation();
			location = location.add(0, 2, 0);
			if (oldAF) {
				signEditor.setPreviousBlockDataOld(player, location.getBlock().getType());
			} else
				signEditor.setPreviousBlockData(player, location.getBlock().getBlockData());
			BlockData data = materialAttempt("SIGN_POST", "OAK_SIGN").createBlockData();
			player.sendBlockChange(location, data);
			if (lines != null && lines.length <= 4 && lines.length > 0) {
				if (serializer != null) {
					List<Component> components = Arrays.stream(lines)
							.map(text -> BungeeConverter.convert(ChatMessages.parseToArray(text)))
							.map(serializer::deserialize)
							.collect(Collectors.toList());
					Sign sign = (Sign) data.createBlockState();
					for (int line = 0; line < components.size(); line ++)
						sign.getSide(Side.FRONT).line(line, components.get(line));
					if (PAPER_SIGNS) {
						player.sendBlockUpdate(location, sign); // PAPER DOESN'T WORK BY THE WAY
					} else {
						player.sendSignChange(location, components);
					}
				} else {
					player.sendSignChange(location, lines);
				}
			}
			WrapperPlayServerOpenSignEntity packet = new WrapperPlayServerOpenSignEntity();
			packet.setLocation(new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ()));
			packet.setFrontSide(true);
			packet.sendPacket(player);
		}
		signs.add(signEditor);
		return signEditor;
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
		private Map<Player, Material> oldShit = new HashMap<>();
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

		public void setPreviousBlockDataOld(Player player, Material data) {
			oldShit.put(player, data);
		}

		public Optional<BlockData> getPreviousBlockData(Player player) {
			return Optional.ofNullable(previousBlockData.get(player));
		}

		public Optional<Material> getPreviousBlockDataOld(Player player) {
			return Optional.ofNullable(oldShit.get(player));
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
