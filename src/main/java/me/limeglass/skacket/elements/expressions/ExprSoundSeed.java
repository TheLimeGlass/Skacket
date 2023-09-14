package me.limeglass.skacket.elements.expressions;

import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.EventValueExpression;
import ch.njol.skript.lang.ExpressionType;
import me.limeglass.skacket.events.NamedSoundEvent;

@Name("Sound Seed")
@Description({
	"Sound seed is the random variation that some sounds have.",
	"This allows you to set the seed to always be the same sound"
})
@Since("1.1.0")
public class ExprSoundSeed extends EventValueExpression<Long> {

	static {
		Skript.registerExpression(ExprSoundSeed.class, Long.class, ExpressionType.SIMPLE, "[the] sound seed");
	}

	public ExprSoundSeed() {
		super(Long.class, true);
	}

	@Nullable
	private EventValueExpression<Long> seed;
	
	@Override
	@Nullable
	public Class<?>[] acceptChange(ChangeMode mode) {
		if (mode == ChangeMode.RESET || mode == ChangeMode.DELETE || mode == ChangeMode.REMOVE_ALL)
			return null;
		seed = new EventValueExpression<>(Long.class);
		if (seed.init())
			return new Class[] {Number.class};
		return null;
	}

	@Override
	public void change(Event e, @Nullable Object[] delta, ChangeMode mode) {
		Number seed = delta == null ? null : (Number) delta[0];
		Number existing = this.seed != null ? this.seed.getSingle(e) : null;
		if (existing == null)
			return;
		long s = seed.longValue();
		long ex = existing.longValue();
		NamedSoundEvent event = (NamedSoundEvent) e;
		switch (mode) {
			case SET:
				event.setSeed(s);
				break;
			case ADD:
				event.setSeed(ex + s);
				break;
			case REMOVE:
				event.setSeed(ex - s);
				break;
			case REMOVE_ALL:
			case DELETE:
			case RESET:
				assert false;
		}
	}

}
