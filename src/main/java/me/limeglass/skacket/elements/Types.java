package me.limeglass.skacket.elements;

import java.util.Locale;

import org.eclipse.jdt.annotation.Nullable;

import com.comphenix.protocol.wrappers.EnumWrappers.ItemSlot;
import com.comphenix.protocol.wrappers.EnumWrappers.PlayerDigType;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.EnumSerializer;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.expressions.base.EventValueExpression;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import ch.njol.skript.util.EnumUtils;
import me.limeglass.skacket.events.AnvilGUIEvent.Click;
import me.limeglass.skacket.events.SteerVehicleEvent.Movement;
import me.limeglass.skacket.objects.ClientWorldBorder;

public class Types {

	static {
		EnumUtils<Movement> movements = new EnumUtils<>(Movement.class, "movement");
		Classes.registerClass(new ClassInfo<>(Movement.class, "movement")
				.user("movements?")
				.name("Steer Movements")
				.usage(movements.getAllNames())
				.description("Movements define direction in the steer vehicle event.")
				.defaultExpression(new EventValueExpression<>(Movement.class))
				.parser(new Parser<Movement>() {

					@Override
					@Nullable
					public Movement parse(String input, ParseContext context) {
						return movements.parse(input);
					}

					@Override
					public boolean canParse(ParseContext context) {
						return true;
					}

					@Override
					public String toString(Movement movement, int flags) {
						return movements.toString(movement, flags);
					}

					@Override
					public String toVariableNameString(Movement movement) {
						return movement.name().toLowerCase(Locale.ENGLISH);
					}

					@Override
					public String getVariableNamePattern() {
						return "\\S+";
					}

				})
				.serializer(new EnumSerializer<>(Movement.class)));
		EnumUtils<ItemSlot> slots = new EnumUtils<>(ItemSlot.class, "itemslot");
		Classes.registerClass(new ClassInfo<>(ItemSlot.class, "itemslot")
				.user("itemslots?")
				.name("Item Slots Armour")
				.usage(slots.getAllNames())
				.defaultExpression(new EventValueExpression<>(ItemSlot.class))
				.parser(new Parser<ItemSlot>() {

					@Override
					@Nullable
					public ItemSlot parse(String input, ParseContext context) {
						return slots.parse(input);
					}

					@Override
					public boolean canParse(ParseContext context) {
						return true;
					}

					@Override
					public String toString(ItemSlot slot, int flags) {
						return slots.toString(slot, flags);
					}

					@Override
					public String toVariableNameString(ItemSlot slot) {
						return slot.name().toLowerCase(Locale.ENGLISH);
					}

					@Override
					public String getVariableNamePattern() {
						return "\\S+";
					}

				})
				.serializer(new EnumSerializer<>(ItemSlot.class)));
		Classes.registerClass(new ClassInfo<>(ClientWorldBorder.class, "clientworldborder")
				.user("clientworldborders?")
				.name("Client World Border")
				.defaultExpression(new EventValueExpression<>(ClientWorldBorder.class))
				.parser(new Parser<ClientWorldBorder>() {

					@Override
					public boolean canParse(ParseContext context) {
						return false;
					}

					@Override
					public String toString(ClientWorldBorder border, int flags) {
						return border.getWorld().getName();
					}

					@Override
					public String toVariableNameString(ClientWorldBorder border) {
						return border.getWorld().getName().toLowerCase(Locale.ENGLISH);
					}

					@Override
					public String getVariableNamePattern() {
						return "\\S+";
					}

				}));
		EnumUtils<PlayerDigType> digs = new EnumUtils<>(PlayerDigType.class, "playerdigtype");
		Classes.registerClass(new ClassInfo<>(PlayerDigType.class, "playerdigtype")
				.user("(block)? ?dig ?types?")
				.name("Player Block Dig Type")
				.usage(digs.getAllNames())
				.description("The different types that trigger a player block dig event.")
				.defaultExpression(new EventValueExpression<>(PlayerDigType.class))
				.parser(new Parser<PlayerDigType>() {

					@Override
					@Nullable
					public PlayerDigType parse(String input, ParseContext context) {
						return digs.parse(input);
					}

					@Override
					public boolean canParse(ParseContext context) {
						return true;
					}

					@Override
					public String toString(PlayerDigType type, int flags) {
						return digs.toString(type, flags);
					}

					@Override
					public String toVariableNameString(PlayerDigType type) {
						return type.name().toLowerCase(Locale.ENGLISH);
					}

					@Override
					public String getVariableNamePattern() {
						return "\\S+";
					}

				})
				.serializer(new EnumSerializer<>(PlayerDigType.class)));
		EnumUtils<Click> clicks = new EnumUtils<>(Click.class, "anvilclick");
		Classes.registerClass(new ClassInfo<>(Click.class, "anvilclick")
				.user("anvil ?click( type)?")
				.name("Anvil Click Type")
				.usage(clicks.getAllNames())
				.defaultExpression(new EventValueExpression<>(Click.class))
				.parser(new Parser<Click>() {

					@Override
					@Nullable
					public Click parse(String input, ParseContext context) {
						return clicks.parse(input);
					}

					@Override
					public boolean canParse(ParseContext context) {
						return true;
					}

					@Override
					public String toString(Click type, int flags) {
						return clicks.toString(type, flags);
					}

					@Override
					public String toVariableNameString(Click type) {
						return type.name().toLowerCase(Locale.ENGLISH);
					}

					@Override
					public String getVariableNamePattern() {
						return "\\S+";
					}

				})
				.serializer(new EnumSerializer<>(Click.class)));
	}

}
