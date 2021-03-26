package me.limeglass.skacket.elements.expressions;

import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.expressions.base.EventValueExpression;
import ch.njol.skript.lang.ExpressionType;
import me.limeglass.skacket.events.NamedSoundEvent;

public class ExprSoundLocation extends EventValueExpression<Number> {

	static {
		Skript.registerExpression(ExprSoundLocation.class, Number.class, ExpressionType.SIMPLE, "[the] sound location");
	}

	public ExprSoundLocation() {
		super(Number.class);
	}

	@Nullable
	private EventValueExpression<Number> volume;

	@Override
	@Nullable
	public Class<?>[] acceptChange(ChangeMode mode) {
		if (mode == ChangeMode.RESET || mode == ChangeMode.DELETE || mode == ChangeMode.REMOVE_ALL)
			return null;
		volume = new EventValueExpression<>(Number.class);
		if (volume.init())
			return new Class[] {Number.class};
		volume = null;
		return null;
	}

	@Override
	public void change(Event e, @Nullable Object[] delta, ChangeMode mode) {
		Number volume = delta == null ? null : (Number) delta[0];
		Number existing = this.volume != null ? this.volume.getSingle(e) : null;
		if (existing == null)
			return;
		float f = volume.floatValue();
		float ex = existing.floatValue();
		NamedSoundEvent event = (NamedSoundEvent) e;
		switch (mode) {
			case SET:
				event.setVolume(f);
				break;
			case ADD:
				event.setVolume(ex + f);
				break;
			case REMOVE:
				event.setVolume(ex - f);
				break;
			case REMOVE_ALL:
			case DELETE:
			case RESET:
				assert false;
		}
	}

}
