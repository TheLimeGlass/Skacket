//package me.limeglass.skacket.elements.sections;
//
//import java.util.Arrays;
//import java.util.List;
//
//import org.bukkit.entity.Player;
//import org.bukkit.event.Event;
//import org.bukkit.event.inventory.InventoryClickEvent;
//import org.bukkit.inventory.ItemStack;
//import org.eclipse.jdt.annotation.Nullable;
//
//import ch.njol.skript.Skript;
//import ch.njol.skript.aliases.ItemType;
//import ch.njol.skript.config.SectionNode;
//import ch.njol.skript.lang.Expression;
//import ch.njol.skript.lang.Section;
//import ch.njol.skript.lang.SkriptParser.ParseResult;
//import ch.njol.skript.variables.Variables;
//import ch.njol.skript.lang.TriggerItem;
//import ch.njol.util.Kleenean;
//import me.limeglass.skacket.objects.AnvilMenu;
//
//public class SecOpenAnvil extends Section {
//
//	static {
//		//Skript.registerSection(SecOpenAnvil.class, "open [an] anvil [gui] (named|with title) %string% to %players% with [items] %itemtypes%");
//	}
//
//	private Expression<Player> players;
//	private Expression<ItemType> items;
//	private Expression<String> title;
//
//	@SuppressWarnings("unchecked")
//	@Override
//	public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, ParseResult parseResult, SectionNode sectionNode, List<TriggerItem> triggerItems) {
//		title = (Expression<String>) expressions[0];
//		players = (Expression<Player>) expressions[1];
//		items = (Expression<ItemType>) expressions[2];
//		this.loadCode(sectionNode);
//		super.setNext(this);
//		if (hasSection())
//			loadSection("anvil", false, InventoryClickEvent.class);
//		return true;
//	}
//
//	@Override
//	protected @Nullable TriggerItem walk(Event event) {
//		ItemStack[] items = Arrays.stream(this.items.getArray(event))
//				.map(itemtype -> itemtype.getRandom())
//				.toArray(ItemStack[]::new);
//		for (Player player : players.getArray(event)) {
//			new AnvilMenu(title.getSingle(event), items, player, e -> {
//				if (!hasSection())
//					return;
//				// Copy local variables into the event.
//				Object localVariables = Variables.removeLocals(event);
//				Variables.setLocalVariables(e, localVariables);
//				Variables.setLocalVariables(event, localVariables);
//				runSection(e);
//			});
//		}
//		return null;
//	}
//
//	@Override
//	public String toString(@Nullable Event event, boolean debug) {
//		if (event == null)
//			return "open anvil";
//		String items = this.items != null ? " with items " + Arrays.toString(this.items.getArray(event)) : "";
//		return "open anvil to " + players.toString(event, debug) + items;
//	}
//
//}
