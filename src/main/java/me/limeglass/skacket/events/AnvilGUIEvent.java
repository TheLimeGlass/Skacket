package me.limeglass.skacket.events;

import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;
import org.eclipse.jdt.annotation.Nullable;

import net.wesjd.anvilgui.AnvilGUI;
import net.wesjd.anvilgui.AnvilGUI.StateSnapshot;

public class AnvilGUIEvent extends PlayerEvent {

	private static final HandlerList handlers = new HandlerList();
	private final StateSnapshot snapshot;
	private final Click type;
	private final int slot;

	public AnvilGUIEvent(int slot, StateSnapshot snapshot) {
		super(snapshot.getPlayer());
		this.type = Click.match(slot);
		this.snapshot = snapshot;
		this.slot = slot;
	}

	public enum Click {

		LEFT(AnvilGUI.Slot.INPUT_LEFT),
		RIGHT(AnvilGUI.Slot.INPUT_RIGHT),
		COMPLETE(AnvilGUI.Slot.OUTPUT);

		private final int slot;

		Click(int slot) {
			this.slot = slot;
		}

		@Nullable
		public static Click match(int slot) {
			for (Click type : Click.values()) {
				if (type.slot == slot)
					return type;
			}
			return null;
		}

	}

	@Nullable
	public String getText() {
		return snapshot.getText();
	}

	@Nullable
	public Click getClickType() {
		return type;
	}

	public ItemStack getLeftItem() {
		return snapshot.getLeftItem();
	}

	public ItemStack getRightItem() {
		return snapshot.getRightItem();
	}

	public ItemStack getOutputItem() {
		return snapshot.getOutputItem();
	}

	public int getClickedSlot() {
		return slot;
	}

	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

}
