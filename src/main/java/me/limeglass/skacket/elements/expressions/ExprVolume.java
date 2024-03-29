package me.limeglass.skacket.elements.expressions;

import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.expressions.base.EventValueExpression;
import ch.njol.skript.lang.ExpressionType;
import me.limeglass.skacket.events.NamedSoundEvent;

public class ExprVolume extends EventValueExpression<Float> {

	static {
		Skript.registerExpression(ExprVolume.class, Float.class, ExpressionType.SIMPLE, "[the] volume");
	}

	public ExprVolume() {
		super(Float.class);
	}

	@Nullable
	private EventValueExpression<Float> volume;
	
	@Override
	@Nullable
	public Class<?>[] acceptChange(ChangeMode mode) {
		if (mode == ChangeMode.RESET || mode == ChangeMode.DELETE || mode == ChangeMode.REMOVE_ALL)
			return null;
		volume = new EventValueExpression<>(Float.class);
		if (volume.init())
			return new Class[] {Float.class};
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
