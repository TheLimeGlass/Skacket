package me.limeglass.skacket.elements.effects;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.eclipse.jdt.annotation.Nullable;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.utility.MinecraftVersion;
import com.comphenix.protocol.wrappers.EnumWrappers.ItemSlot;
import com.comphenix.protocol.wrappers.Pair;
import com.google.common.collect.Sets;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import me.limeglass.skacket.wrappers.WrapperPlayServerEntityEquipment;

public class EffArmourChange extends Effect {

	static {
		Skript.registerEffect(EffArmourChange.class, "make %livingentities% appear to be wearing %itemstacks% in slot %itemslots% [(for|to) %-players%]", "set armo[u]r of %livingentities% to %itemstacks% in slot %itemslots% [(for|to) %-players%]");
	}

	private Expression<LivingEntity> entities;

	@Nullable
	private Expression<Player> players;

	private Expression<ItemStack> items;

	private Expression<ItemSlot> slots;

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		entities = (Expression<LivingEntity>) exprs[0];
		items = (Expression<ItemStack>) exprs[1];
		slots = (Expression<ItemSlot>) exprs[2];
		players = (Expression<Player>) exprs[3];
		return true;
	}

	@Override
	protected void execute(Event event) {
		Set<Player> receivers = new HashSet<>();
		if (players == null)
			receivers.addAll(Bukkit.getOnlinePlayers());
		else
			receivers.addAll(Sets.newHashSet(players.getArray(event)));
		List<ItemStack> items = this.items.stream(event).collect(Collectors.toList());
		List<ItemSlot> slots = this.slots.stream(event).collect(Collectors.toList());
		if (MinecraftVersion.atOrAbove(MinecraftVersion.BEE_UPDATE)) {
			List<Pair<ItemSlot, ItemStack>> pairs = IntStream.range(0, Math.min(slots.size(), items.size()))
				    .boxed()
				    .collect(Collectors.toMap(slots::get, items::get))
				    .entrySet().stream()
				    .map(entry -> new Pair<>(entry.getKey(), entry.getValue()))
				    .collect(Collectors.toList());
			for (LivingEntity entity : entities.getArray(event)) {
				PacketContainer container = new PacketContainer(PacketType.Play.Server.ENTITY_EQUIPMENT);
				container.getIntegers().write(0, entity.getEntityId());
				container.getSlotStackPairLists().write(0, pairs);
				for (Player receiver : receivers) {
					try {
						ProtocolLibrary.getProtocolManager().sendServerPacket(receiver, container);
					} catch (InvocationTargetException e) {
						throw new RuntimeException("Cannot send packet.", e);
					}
				}
			}
		} else {
			if (slots.size() <= 0 || items.size() <= 0)
				return;
			for (LivingEntity entity : entities.getArray(event)) {
				WrapperPlayServerEntityEquipment packet = new WrapperPlayServerEntityEquipment();
				packet.setEntityID(entity.getEntityId());
				packet.setSlot(slots.get(0));
				packet.setItem(items.get(0));
				for (Player receiver : receivers)
					packet.sendPacket(receiver);
			}
		}
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		if (event == null)
			return "client side armour";
		return "client side armour to " + entities.toString(event, debug) + " armour " + items.toString(event, debug);
	}

}
