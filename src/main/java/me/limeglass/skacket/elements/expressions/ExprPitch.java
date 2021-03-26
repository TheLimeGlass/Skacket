package me.limeglass.skacket.elements.expressions;

import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.expressions.base.EventValueExpression;
import ch.njol.skript.lang.ExpressionType;
import me.limeglass.skacket.events.NamedSoundEvent;

public class ExprPitch extends EventValueExpression<Number> {

	static {
		Skript.registerExpression(ExprPitch.class, Number.class, ExpressionType.SIMPLE, "[the] pitch");
	}

	public ExprPitch() {
		super(Number.class);
	}

	@Nullable
	private EventValueExpression<Number> pitch;
	
	@Override
	@Nullable
	public Class<?>[] acceptChange(ChangeMode mode) {
		if (mode == ChangeMode.RESET || mode == ChangeMode.DELETE || mode == ChangeMode.REMOVE_ALL)
			return null;
		pitch = new EventValueExpression<>(Number.class);
		if (pitch.init())
			return new Class[] {Number.class};
		pitch = null;
		return null;
	}

	@Override
	public void change(Event e, @Nullable Object[] delta, ChangeMode mode) {
		Number pitch = delta == null ? null : (Number) delta[0];
		Number existing = this.pitch != null ? this.pitch.getSingle(e) : null;
		if (existing == null)
			return;
		float f = pitch.floatValue();
		float ex = existing.floatValue();
		NamedSoundEvent event = (NamedSoundEvent) e;
		switch (mode) {
			case SET:
				event.setPitch(f);
				break;
			case ADD:
				event.setPitch(ex + f);
				break;
			case REMOVE:
				event.setPitch(ex - f);
				break;
			case REMOVE_ALL:
			case DELETE:
			case RESET:
				assert false;
		}
	}

}
