package me.limeglass.skacket.objects;

import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.limeglass.skacket.Skacket;
import net.md_5.bungee.api.ChatColor;
import net.wesjd.anvilgui.version.VersionMatcher;
import net.wesjd.anvilgui.version.VersionWrapper;

/**
 * An anvil gui, used for gathering a user's input.
 * Heavily optimized and made to allow for itemstack customization.
 * @author Wesley Smith and LimeGlass
 */
public class AnvilMenu {

	private static VersionWrapper WRAPPER = new VersionMatcher().match();
	private final Consumer<InventoryClickEvent> consumer;
	private final ListenUp listener = new ListenUp();
	private final Inventory inventory;
	private final int containerId;
	private final Player holder;
	private boolean open;

	public AnvilMenu(String title, ItemStack[] items, Player holder, Consumer<InventoryClickEvent> consumer) {

		this.consumer = consumer;
		this.holder = holder;

		WRAPPER.handleInventoryCloseEvent(holder);
		WRAPPER.setActiveContainerDefault(holder);

		Bukkit.getPluginManager().registerEvents(listener, Skacket.getInstance());

		Object container = WRAPPER.newContainerAnvil(holder, ChatColor.translateAlternateColorCodes('&', title));

		inventory = WRAPPER.toBukkitInventory(container);
		for (int i = 0; i < items.length; i++) {
			inventory.setItem(i, items[i]);
			if (i >= 2)
				break;
		}

		containerId = WRAPPER.getNextContainerId(holder, container);
		WRAPPER.sendPacketOpenWindow(holder, containerId, ChatColor.translateAlternateColorCodes('&', title));
		WRAPPER.setActiveContainer(holder, container);
		WRAPPER.setActiveContainerId(container, containerId);
		WRAPPER.addActiveContainerSlotListener(container, holder);
		
		open = true;
	}

	private class ListenUp implements Listener {

		@EventHandler
		public void onInventoryClick(InventoryClickEvent event) {
			if (!event.getInventory().equals(inventory))
				return;
            consumer.accept(event);
		}

		@EventHandler
		public void onInventoryClose(InventoryCloseEvent event) {
			if (event.getInventory().equals(inventory))
				closeInventory();
		}

	}

	public void closeInventory() {
		if (!open)
			return;
		open = false;

		WRAPPER.handleInventoryCloseEvent(holder);
		WRAPPER.setActiveContainerDefault(holder);
		WRAPPER.sendPacketCloseWindow(holder, containerId);
		HandlerList.unregisterAll(listener);
	}

	public Inventory getInventory() {
		return inventory;
	}

}
