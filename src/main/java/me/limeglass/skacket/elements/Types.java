package me.limeglass.skacket.elements;

import java.util.Locale;

import org.eclipse.jdt.annotation.Nullable;

import com.comphenix.protocol.wrappers.EnumWrappers.ItemSlot;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.EnumSerializer;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.expressions.base.EventValueExpression;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import ch.njol.skript.util.EnumUtils;
import me.limeglass.skacket.events.SteerVehicleEvent.Movement;

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
	}

}
