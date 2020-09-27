package me.limeglass.skacket.elements.effects;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.eclipse.jdt.annotation.Nullable;

import com.comphenix.protocol.wrappers.EnumWrappers.ItemSlot;
import com.google.common.collect.Sets;

import ch.njol.skript.Skript;
import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import me.limeglass.skacket.wrappers.WrapperPlayServerEntityEquipment;

public class EffArmourChange extends Effect {

	static {
		Skript.registerEffect(EffArmourChange.class, "make %livingentities% appear to be wearing %itemtype% in slot %itemslot% [(for|to) %-players%]", "set armo[u]r of %livingentities% to %itemtype% in slot %itemslot% [(for|to) %-players%]");
	}

	private Expression<LivingEntity> entities;

	@Nullable
	private Expression<Player> players;

	private Expression<ItemType> item;

	private Expression<ItemSlot> slot;

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		entities = (Expression<LivingEntity>) exprs[0];
		item = (Expression<ItemType>) exprs[1];
		slot = (Expression<ItemSlot>) exprs[2];
		players = (Expression<Player>) exprs[3];
		return true;
	}

	@Override
	protected void execute(Event event) {
		Set<Player> send = new HashSet<>();
		if (players == null)
			send.addAll(Bukkit.getOnlinePlayers());
		else
			send.addAll(Sets.newHashSet(players.getArray(event)));
		ItemSlot slot = this.slot.getSingle(event);
		for (LivingEntity entity : entities.getArray(event))
			setArmour(entity, slot, item.getSingle(event).getRandom(), send.toArray(new Player[send.size()]));
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		if (event == null)
			return "client side armour";
		return "client side armour to " + entities.toString(event, debug) + " armour " + item.toString(event, debug);
	}

	private static void setArmour(LivingEntity entity, ItemSlot slot, ItemStack item, Player... players) {
		/*1.16 support when ProtocolLib gets updated.
		 * 
		if (MinecraftVersion.atOrAbove(MinecraftVersion.BEE_UPDATE)) {
			PacketContainer container = new PacketContainer(PacketType.Play.Server.ENTITY_EQUIPMENT);
			// Write the entity ID too.

			List<Pair<EnumWrappers.ItemSlot, ItemStack>> data = new ArrayList<>();
			// Example addition, please change this syntax to support multiple ItemSlots in future.
			data.add(new Pair<>(EnumWrappers.ItemSlot.CHEST, new ItemStack(Material.NETHERITE_CHESTPLATE)));
			data.add(new Pair<>(EnumWrappers.ItemSlot.LEGS, new ItemStack(Material.GOLDEN_LEGGINGS)));

			container.getSlotStackPairLists().write(0, data);
			return;
		}*/
		WrapperPlayServerEntityEquipment packet = new WrapperPlayServerEntityEquipment();
		packet.setEntityID(entity.getEntityId());
		packet.setSlot(slot);
		packet.setItem(item);
		for (Player player : players)
			packet.sendPacket(player);
	}

}
